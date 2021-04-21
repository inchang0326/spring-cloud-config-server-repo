package com.example.external.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {

    @GetMapping("/success")
    public String getYN() {
        String result = "Y";
        return result;
    }

    @GetMapping("/error")
    public String getYN2() throws Exception {
        String result = "error";
        int value = 1/0;
        return result;
    }

    @GetMapping("/nothing")
    public void getYN3() {
        try {
            Thread.sleep(600000);
        } catch (Exception e) {

        }
    }

    @GetMapping("/delayed")
    public String getYN4() {
        String result = "";
        try {
            result = "Y";
            Thread.sleep(30000);
        } catch (Exception e) {

        }
        return result;
    }
}