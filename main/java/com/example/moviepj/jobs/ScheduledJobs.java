package com.example.moviepj.jobs;

import com.example.moviepj.service.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ScheduledJobs {

    private final FileService fileService;

    public ScheduledJobs(FileService fileService) {
        this.fileService = fileService;
    }

     //@Scheduled(fixedDelay = 30000)
    public void downloadMovieImages() {
        System.out.printf(">>>>Download Image Job has been started %s%n", new Date());
        fileService.download();
        System.out.printf(">>>>>Download Image Job has been finished %s%n", new Date());
    }
    //@Scheduled(fixedDelay = 30000)
    public void checkCrashedCases(){
        System.out.printf(">>>>Checking Crashed Job has started %s%n", new Date());
        fileService.checkCrashedCases();
        System.out.printf(">>>>>Checking Crashed Job has ended %s%n", new Date());
    }


}
