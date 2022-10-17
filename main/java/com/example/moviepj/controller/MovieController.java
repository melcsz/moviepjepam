package com.example.moviepj.controller;

import com.example.moviepj.csv.ResponseMessage;
import com.example.moviepj.service.MovieService;
import com.example.moviepj.service.criteria.MovieSearchCriteria;
import com.example.moviepj.service.dto.MovieDto;
import com.example.moviepj.service.dto.UserDto;
import com.example.moviepj.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService moveService;

    @Autowired
    public MovieController(MovieService moveService) {
        this.moveService = moveService;
    }


    @PostMapping("/import-csv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            moveService.saveMovie(file);
            message = "Uploading ended successfully: " + file.getOriginalFilename();
            System.out.println(message);

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
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    public String getMoviesPaginated(MovieSearchCriteria criteria, Model model) {
        QueryResponseWrapper<MovieDto> wrapper = moveService.getMovies(criteria);
        model.addAttribute("movies", wrapper);
        model.addAttribute("criteria",criteria);
        Long totalPages = wrapper.getTotal();
        if (totalPages > 0) {
            List<Long> pageNumbers = LongStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "movies";
    }

    @GetMapping("main-page")
    public String mainPageOfWebsite(Model model){
        UserDto userDto = new UserDto();
        userDto.setEmail("");
        model.addAttribute("user", userDto);
        return "index";
    }
}
