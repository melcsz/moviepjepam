package com.example.moviepj.service;

import com.example.moviepj.persistance.entity.FileEntity;
import com.example.moviepj.persistance.entity.status.FileStatus;
import com.example.moviepj.persistance.repository.FileRepository;
import com.example.moviepj.persistance.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileService {

    private final String THUMBNAIL_PATH = "C:\\Users\\Asus\\movieThumbnails\\";
    private final String PATH = "C:\\Users\\Asus\\movieimages\\";
    private final String THUMBNAIL_EXTENSION = "mini" + ".jpg";
    private final String EXTENSION = ".jpg";

    private final MovieRepository movieRepository;
    private final FileRepository fileRepository;

    @Autowired
    public FileService(MovieRepository movieRepository, FileRepository fileRepository) {
        this.movieRepository = movieRepository;
        this.fileRepository = fileRepository;
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void download() {
        List<String> notUploadedUrls = movieRepository.findAllNotUploadedUrls();
        for (int i = 0; i < notUploadedUrls.size(); i += 100) {
            executeImageDownloadTask(notUploadedUrls.subList(i, Math.min((i + 100), notUploadedUrls.size())));
        }
    }

    @Transactional
    public void executeImageDownloadTask(List<String> urls) {
        CompletableFuture.supplyAsync(() -> {
            for (String url : urls) {
                FileEntity fileEntity = new FileEntity();
                try {

                    final String uuid = UUID.randomUUID().toString().replace("-", "");
                    fileEntity = new FileEntity(PATH, THUMBNAIL_PATH, uuid, url,
                            "jpg", System.currentTimeMillis(), movieRepository.getByUrlL(url), FileStatus.IN_PROGRESS);
                    BufferedImage image = resize(new URL(url), new Dimension(50, 50));
                    fileEntity = fileRepository.save(fileEntity);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    Files.copy(is, Paths.get(THUMBNAIL_PATH + fileEntity.getName() + THUMBNAIL_EXTENSION));
                    InputStream in = new URL(url).openStream();
                    Files.copy(in, Paths.get(PATH + fileEntity.getName() + EXTENSION));
                    fileEntity.setFileStatus(FileStatus.FINISHED);
                    fileRepository.save(fileEntity);
                } catch (Exception ignored) {
                    fileEntity.setFileStatus(FileStatus.FAILED);
                }
            }
            return true;
        }, executorService);
    }

    @Transactional
    public void checkCrashedCases() {
        List<FileEntity> allInProgress = fileRepository.getAllInProgress();
        for (FileEntity entity : allInProgress) {
            if (System.currentTimeMillis() - entity.getCreatedTimeInMillis() > 60000) {
                entity.setFileStatus(FileStatus.FAILED);
            }
        }
    }

    public BufferedImage resize(final URL url, final Dimension size) throws IOException {
        final BufferedImage image = ImageIO.read(url);
        final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = resized.createGraphics();
        g.drawImage(image, 0, 0, size.width, size.height, null);
        g.dispose();
        return resized;
    }

    public FileEntity getThumbnailByID(Long id) {
        return fileRepository.getFileEntityById(id);
    }

    public FileEntity getPath(Long id){
        return fileRepository.getPath(id);
    }
}
