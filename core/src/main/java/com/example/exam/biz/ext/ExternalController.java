package com.example.exam.biz.ext;

import com.example.exam.biz.ext.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/ext")
public class ExternalController {

    @Autowired
    ExternalService externalService;

    @GetMapping
    public DeferredResult<String> getResult() throws InterruptedException, ExecutionException, TimeoutException {
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("extNo", "03");
        return externalService.fep(inputMap);
    }
}
