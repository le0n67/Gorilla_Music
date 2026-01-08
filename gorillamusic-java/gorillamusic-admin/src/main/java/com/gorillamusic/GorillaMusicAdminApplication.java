package com.gorillamusic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Date：2026/1/2  10:15
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@SpringBootApplication(scanBasePackages = {"com.gorillamusic"})
@MapperScan(basePackages = {"com.gorillamusic.mappers"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class GorillaMusicAdminApplication {
    public static void main(String[] args) {
        System.out.println("===========================MusicAdmin start===========================");
        SpringApplication.run(GorillaMusicAdminApplication.class, args);
    }
}