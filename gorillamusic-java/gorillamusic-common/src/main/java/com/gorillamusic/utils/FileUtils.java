package com.gorillamusic.utils;

import com.gorillamusic.entity.config.AppConfig;
import com.gorillamusic.entity.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Date：2026/1/3  12:04
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Component("fileUtils")
@Slf4j
public class FileUtils {

    @Resource
    private AppConfig appConfig;

    public String copyAvatar(String userId) {
        try {
            int randomNumber = (int) (Math.random() * 20 + 1);
            //文件夹
            String avatarFolderPath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR;
            File folder = new File(avatarFolderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //文件名
            String avatarPath = Constants.FILE_FOLDER_AVATAR + userId + randomNumber + Constants.AVATAR_SUFFIX;
            File avatarFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + avatarPath);
            ClassPathResource resource = new ClassPathResource(String.format(Constants.DEFAULT_AVATAR_PATH, randomNumber));
            org.apache.commons.io.FileUtils.copyToFile(resource.getInputStream(), avatarFile);
            return  avatarPath;
        }catch (Exception e){
            log.error("拷贝头像失败");
            return null;
        }

    }
}
