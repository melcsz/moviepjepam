package com.example.moviepj.controller;

import com.example.moviepj.csv.ResponseMessage;
import com.example.moviepj.persistance.entity.UserEntity;
import com.example.moviepj.service.criteria.SearchCriteria;
import com.example.moviepj.service.UserService;
import com.example.moviepj.service.dto.UserDto;
import com.example.moviepj.service.model.QueryResponseWrapper;
import com.example.moviepj.service.model.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/import-csv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            userService.save(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/csv/download/")
                    .path(file.getName())
                    .toUriString();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fileDownloadUri));
        } catch (Exception e) {
            message = e.getMessage() + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, ""));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    public QueryResponseWrapper<UserWrapper> getUsers(SearchCriteria criteria) {
        return userService.getUsers(criteria);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    public void deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void updatePassword(@RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass) throws Exception {
        userService.updatePassword(oldPass, newPass);
    }


    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@RequestBody @ModelAttribute("user") UserDto dto) throws Exception {

        if (dto.getFirstName() == null) {
            throw new Exception("First name is required");
        }

        if (dto.getLastName() == null) {
            throw new Exception("Last name is required");
        }

        if (dto.getEmail() == null) {
            throw new Exception("Username is required");
        }

        if (dto.getPassword() == null) {
            throw new Exception("Password is required");
        } else if(!(dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{12,}$"))){
            throw new Exception("Password is weak");
        }

        String user = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/register-page")
    public String mainPage(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user",user);
        return "register";
    }

    @GetMapping("/login-page")
    public String mainPage(){
        return "login";
    }



}
