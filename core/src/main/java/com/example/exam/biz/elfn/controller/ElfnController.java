package com.example.exam.biz.elfn.controller;

import com.example.exam.biz.elfn.service.ElfnService;
import com.example.exam.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("card")
public class ElfnController {
    @Autowired
    private ElfnService elfnService;

    @GetMapping()
    public List<HashMap<String, Object>> getAllianceCardList() throws Exception {
        List<HashMap<String, Object>> retList = null;
        try {
            retList = elfnService.getAllianceCardList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return retList;
    }

    @PostMapping
    public HashMap<String, Object> getAllianceCard(@RequestBody HashMap<String, Object> inputMap) throws Exception {
        HashMap<String, Object> retMap = null;
        try {
            retMap = elfnService.getAllianceCard(inputMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return retMap;
    }

    @PostMapping("/hasCard")
    public HashMap<String, Object> hasCard(@RequestBody HashMap<String, Object> inputMap) throws Exception {
        HashMap<String, Object> retMap = null;
        try {
            retMap = elfnService.hasCard(inputMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return retMap;
    }

    @PostMapping("/apply")
    public void cardApply(@RequestBody HashMap<String, Object> inputMap) throws Exception {
        try {
            elfnService.cardApply(inputMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/account")
    public String getAccount(@RequestBody HashMap<String, Object> inputMap) throws Exception {
        String acc = "";
        try {
            acc = elfnService.getAccount(inputMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // 암호화
        return acc;
    }
}