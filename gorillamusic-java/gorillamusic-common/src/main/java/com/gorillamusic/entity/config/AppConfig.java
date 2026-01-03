package com.gorillamusic.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Date：2026/1/3  12:14
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Configuration
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;

    public String getProjectFolder() {
        return projectFolder;
    }
}
