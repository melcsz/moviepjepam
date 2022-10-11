package com.example.moviepj.controller;

import com.example.moviepj.persistance.entity.FileEntity;
import com.example.moviepj.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/images")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path = "/movies/thumbnail/{id}")
    public FileEntity getThumbnailFromID(@PathVariable("id") Long id){
        return fileService.getThumbnailByID(id);
    }

    @RequestMapping("/picture/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @ResponseBody
    public HttpEntity<byte[]> getArticleImage(@PathVariable Long id) throws IOException {

        FileEntity file = fileService.getPath(id);

        String IMAGE_EXTENSION = ".jpg";
        Path lPath = Paths.get(file.getPath() + "\\" + file.getName() + IMAGE_EXTENSION);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(lPath));
        byte[] image = resource.getByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);

        return new HttpEntity<>(image, headers);
    }
}
