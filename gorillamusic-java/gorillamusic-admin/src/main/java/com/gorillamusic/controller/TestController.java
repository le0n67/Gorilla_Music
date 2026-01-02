package com.gorillamusic.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date：2026/1/2  10:14
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */


@RestController
public class TestController {

    @RequestMapping("/testAdmin")
    public String testAdmin() {
        System.out.println("MusicAdmin test");
        return "MusicAdmin test";
    }
}