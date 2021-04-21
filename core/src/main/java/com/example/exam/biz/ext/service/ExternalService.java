package com.example.exam.biz.ext.service;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/*
    * 대외거래 설계방안
      1. 지연응답, 무응답 등의 이유로 Async 사용.
      2. (Async) RestTemplate으로 HTTP 요청 및 응답 정상/오류 식별
         >> Spring WebFlux .. WebClient도 좋은 방안일듯
      3. 지연응답 조치 : Long Polling( Request Timeout 설정 후 DeferredResult 사용 )
                       >> 스트리밍도 좋은 방안일듯
      4. 무응답 조치 : Request Timeout 설정. 하지만 Long Polling 되고 있으므로,
                     계속 기다릴 수 없으니 이것 또한 특정 시간 연결 해제해야 함(RestTemplate Timeout)
      5. 추가로 Timeout 발생 시, 한 번 더 요청
 */
@Service
@Log
public class ExternalService {

    @Async
    public DeferredResult<String> fep(HashMap<String, Object> inputMap) throws ExecutionException, InterruptedException, TimeoutException, AsyncRequestTimeoutException {
        // 대외제휴카드신청정보 데이터 삽입
        insertHisotry(inputMap);

        // 무응답(HTTP Connection Timeout)을 60초로 제한
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60000);
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        restTemplate.setAsyncRequestFactory(requestFactory);

        // Long Polling Timeout을 30초로 제한
        final DeferredResult<String> dfresult = new DeferredResult<>(30000L);

        // 테스트용
        String param = "";
        switch((String) inputMap.get("extNo")) {
            case "01" : { param = "success"; break;}
            case "02" : { param = "error"; break;}
            case "03" : { param = "delayed"; break;}
            case "04" : { param = "nothing"; break;}
        }

        // URL 설정
        String baseUrl = "http://localhost:8090/card/" + param;

        // Header, Body 설정
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity("parameters", requestHeaders);

        // (Async) HTTP 요청
        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.getForEntity(baseUrl, String.class);

        // HTTP 응답 콜백 처리
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) { // 성공, 실패 처리
                log.info(result.getBody());
                dfresult.setResult(result.getBody()); // 지연응답 처리

                // 대외제휴카드신청정보 락 조회
                selectHistoryForLock(inputMap);

                // 대외제휴카드신청정보 데이터 업데이트(상태코드 : 정상/실패)
                updateHisotry(inputMap);

                // 카드 요청 응답 request
                requestCard((String) inputMap.get("cardTxNo"), "01");
            }
            @Override
            public void onFailure(Throwable ex) { // 무응답(HTTP Connection Timeout) 처리
                log.info(ex.getMessage());

                // 대외제휴카드신청정보 락 조회
                selectHistoryForLock(inputMap);

                // 대외제휴카드신청정보 데이터 업데이트(상태코드 : 비정상)
                updateHisotry(inputMap);

                // 카드 요청 응답 request
                requestCard((String) inputMap.get("cardTxNo"), "04");
            }
        });

        // Long Polling Timeout 처리
        dfresult.onTimeout(new Runnable() {
            @SneakyThrows
            /*
                Runnable의 run() 메소드 안에서 발생한 예외는 호출한 쓰레드로 제대로 전파되지 않는다.
                모든 예외가 RuntimeException으로 묶여 던져지기 때문에(심지어 예외 메시지가 비어있는 경우도 있다)
                정상적인 예외 처리를 할 수 없는데, 이러한 경우 @SneakyThrows가 유용하게 사용된다.
             */
            @Override
            public void run() {
                dfresult.setErrorResult(
                        ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                .body("[Timeout]"));
                // 대외제휴카드신청정보 락 조회
                selectHistoryForLock(inputMap);

                // 대외제휴카드신청정보 데이터 업데이트(상태코드 : 오류)
                updateHisotry(inputMap);

                // 카드 요청 응답 request
                requestCard((String) inputMap.get("cardTxNo"), "03");

//                if(!(boolean) inputMap.get("repeat")) { // retry 로직
//                    fep(inputMap);
//                }
            }
        });

        return dfresult;
    }

    private HashMap<String, Object> selectHistoryForLock(HashMap<String, Object> inputMap){
        return null;
    }

    private void insertHisotry(HashMap<String, Object> inputMap) {
    }

    private void updateHisotry(HashMap<String, Object> inputMap) {
    }

    private void requestCard(String cardTxNo, String procsStcd) {
    }
}