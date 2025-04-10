package com.example.task;

import com.example.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FilmStatusTask {

    @Autowired
    private FilmService filmService;

    /**
     * 每天凌晨1点执行一次
     * 自动更新电影状态
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateFilmStatus() {
        filmService.autoUpdateFilmStatus();
    }
} 