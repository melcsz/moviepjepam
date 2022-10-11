package com.example.moviepj.service;

import com.example.moviepj.csv.Parser;
import com.example.moviepj.exception.CSVImportException;
import com.example.moviepj.exception.PasswordsDoNotMatch;
import com.example.moviepj.exception.RoleNotFound;
import com.example.moviepj.persistance.entity.*;
import com.example.moviepj.persistance.entity.status.UserStatus;
import com.example.moviepj.persistance.repository.RoleRepository;
import com.example.moviepj.persistance.repository.UserRepository;
import com.example.moviepj.service.criteria.SearchCriteria;
import com.example.moviepj.service.dto.UserDto;
import com.example.moviepj.service.model.QueryResponseWrapper;
import com.example.moviepj.service.model.Role;
import com.example.moviepj.service.model.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;
    private final Parser parser;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final MailService mailService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder bcryptEncoder, Parser parser, RoleRepository roleRepository, PasswordEncoder encoder, MailService mailService) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.parser = parser;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.mailService = mailService;
    }

    public void save(MultipartFile file) {
        try {
            System.out.printf(">>>>>>>>>>>>>Starting the CSV import %s%n", new Date());
            List<UserEntity> users = parser.csvToUserEntity(file.getInputStream());
            List<String> existingUser = userRepository.getAllUserEmails();
            users.removeIf(user -> existingUser.contains(user.getEmail()));
            Map<String, CityEntity> addresses = users.stream()
                    .map(UserEntity::getAddress)
                    .map(AddressEntity::getCity)
                    .collect(Collectors.toMap(CityEntity::getName, x -> x, (a1, a2) -> a1));
            System.out.println(addresses.size());

            for (UserEntity user : users) {
                user.getAddress().setCity(addresses.get(user.getAddress().getCity().getName()));

            }
            userRepository.saveAll(users);
            System.out.printf(">>>>>>>>>>>>>Ending the CSV import %s%n", new Date());
        } catch (IOException e) {
            throw new CSVImportException("fail to store csv data: " + e.getMessage());
        }
    }

    public QueryResponseWrapper<UserWrapper> getUsers(SearchCriteria criteria) {
        Page<UserWrapper> content = userRepository.findAllWithPagination(criteria.createPage());

        return new QueryResponseWrapper<>(content.getTotalElements(), content.getContent());
    }


    public String registerUser(UserDto signUpRequest) throws Exception {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new Exception("user already exists with this email");
        }

        UserEntity user = new UserEntity(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(Role.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFound("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase(Locale.ROOT)) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByName(Role.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFound("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        RoleEntity modRole = roleRepository.findByName(Role.ROLE_MODERATOR)
                                .orElseThrow(() -> new RoleNotFound("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(Role.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFound("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setStatus(UserStatus.NOT_ACTIVATED);
        mailService.sendVerificationEmail(signUpRequest);
        userRepository.save(user);
        return ("Email sent");
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updatePassword(String oldPass, String newPass) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        String password;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
            password = ((UserDetails) principal).getPassword();
        } else {
            email = principal.toString();
            password = principal.toString();
        }
        UserEntity user = userRepository.getByEmail(email);
        if (bcryptEncoder.matches(oldPass, password)) {
            newPass = bcryptEncoder.encode(newPass);
            user.setPassword(newPass);
        } else {
            throw new PasswordsDoNotMatch("passwords do not match");
        }

    }

    public UserEntity getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Transactional
    public void verifyEmail(UserEntity user) {
        user.setStatus(UserStatus.ACTIVATED);
    }
}