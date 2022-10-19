package com.example.moviepj.controller;

import com.example.moviepj.csv.ResponseMessage;
import com.example.moviepj.payload.request.ReviewRequest;
import com.example.moviepj.persistance.entity.ReviewEntity;
import com.example.moviepj.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/ratings")
@Controller
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/import-csv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            ratingService.save(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/ratings/import-csv")
                    .path(file.getName())
                    .toUriString();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, fileDownloadUri));
        } catch (Exception e) {
            message = e.getMessage() + " !";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, ""));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewRequest review) throws Exception {
        return ratingService.addReview(review);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    public List<ReviewEntity> getReviews() throws Exception {
        return ratingService.getReviews();
    }
}
