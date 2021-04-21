package com.example.app.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class AllianceCardService {

    public List<HashMap<String, Object>> getAlliacneCardList() {
        String baseUrl = "http://localhost:8081/card";

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON); // json
        HttpEntity httpEntity = new HttpEntity(null, header);

        ResponseEntity<String> responseEntity = send(httpEntity, baseUrl, HttpMethod.GET);

        Gson gson = new Gson();
        Type type = new TypeToken<List<HashMap<String, Object>>>() {}.getType();
        List<HashMap<String, Object>> retList = gson.fromJson(responseEntity.getBody().toString(), type);

        return retList;
    }

    public HashMap<String, Object> getCardDetail(HashMap<String, Object> inputMap) {
        String baseUrl = "http://localhost:8081/card";

        HttpEntity<?> httpEntity = getPostData(inputMap);
        ResponseEntity<String> responseEntity = send(httpEntity, baseUrl, HttpMethod.POST);

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
        HashMap<String, Object> retMap = gson.fromJson(responseEntity.getBody().toString(), type);

        return retMap;
    }

    public boolean hasCard(HashMap<String, Object> inputMap) {
        String baseUrl = "http://localhost:8081/card/hasCard";
        inputMap.put("cstno", "0000011199017867");

        HttpEntity<?> httpEntity = getPostData(inputMap);
        ResponseEntity<String> responseEntity = send(httpEntity, baseUrl, HttpMethod.POST);

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
        HashMap<String, Object> retMap = gson.fromJson(responseEntity.getBody().toString(), type);

        boolean result = false;
        if("Y".equals((String) retMap.get("hasCard"))) result = true;
        return result;
    }

    public void cardApply(HashMap<String, Object> inputMap) {
        String baseUrl = "http://localhost:8081/card/apply";
        inputMap.put("cstno", "0000011199017867");

        HttpEntity<?> httpEntity = getPostData(inputMap);
        send(httpEntity, baseUrl, HttpMethod.POST);
    }

    public String getAccount() {
        String baseUrl = "http://localhost:8081/card/account";
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("cstno", "0000011199017867");

        HttpEntity<?> httpEntity = getPostData(inputMap);
        ResponseEntity<String> responseEntity = send(httpEntity, baseUrl, HttpMethod.POST);
        return responseEntity.getBody();
    }

    private HttpEntity<?> getPostData(HashMap<String, Object> inputMap) {
        Map params = inputMap;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //JSON 변환
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<?> httpEntity = new HttpEntity<>(params, headers);
        return httpEntity;
    }

    private ResponseEntity<String> send(HttpEntity<?> httpEntity, String baseUrl, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl, httpMethod, httpEntity, String.class);
        return responseEntity;
    }
}
