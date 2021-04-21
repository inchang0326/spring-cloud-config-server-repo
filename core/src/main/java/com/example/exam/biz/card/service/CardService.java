package com.example.exam.biz.card.service;

import com.example.exam.biz.card.repo.mybatis.AllianceCardMapper;
import com.example.exam.biz.ext.service.ExternalService;
import com.example.exam.common.code.EnumResultCode;
import com.example.exam.common.exception.ServiceException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private AllianceCardMapper allianceCardMapper;

    @Autowired
    private ExternalService externalService;

    public List<HashMap<String, Object>> getAllianceCardList(String today) throws ServiceException {
        List<HashMap<String, Object>> retList = null;
        try {
            retList = allianceCardMapper.selectAllianceCardList(today);

            if(retList == null) {
                throw new ServiceException(EnumResultCode.R11112.toString(), EnumResultCode.R11112.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
        return retList;
    }

    public HashMap<String, Object> getAllianceCard(HashMap<String, Object> inputMap) throws ServiceException {
        HashMap<String, Object> retMap = null;
        try {
            retMap = allianceCardMapper.selectAllianceCard(inputMap);

            if(retMap == null) {
                throw new ServiceException(EnumResultCode.R11112.toString(), EnumResultCode.R11112.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
        return retMap;
    }

    public String hasCard(HashMap<String, Object> inputMap) throws ServiceException {
        String retStr = "";
        try {
            retStr = allianceCardMapper.hasCard(inputMap);
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
        return retStr;
    }

    public void cardApply(HashMap<String, Object> inputMap) throws ServiceException {
        try {
            inputMap.put("today" , LocalDate.now().toString().replaceAll("-", ""));
            inputMap.put("cardTransNo", UUID.randomUUID().toString());
            inputMap.put("appcProcsCode", "01"); // 약관동의 씬

            String cnt = allianceCardMapper.selectCardApplyHistory(inputMap);

            if(Integer.parseInt(cnt) > 0) {
                HashMap<String, Object> reqMap = allianceCardMapper.selectCardApplyHistoryForLock(inputMap);
                allianceCardMapper.updateCardApplyHistory(reqMap);
            } else {
                allianceCardMapper.insertCardApplyHistory(inputMap);
            }

            HashMap<String, Object> reqMap = new HashMap<>();
            reqMap.put("card_tx_no", UUID.randomUUID().toString());
            reqMap.put("allianceCode", inputMap.get("allianceCode"));
            reqMap.put("cstno", inputMap.get("cstno"));
            reqMap.put("cardGubunCode", inputMap.get("cardGubunCode"));
            reqMap.put("extNo", inputMap.get("extNo")); // 테스트용 구분코드

            // 대외계 제휴카드 개설 선조회 요청
            DeferredResult<String> result = externalService.fep(reqMap);
            long beforeTime = System.currentTimeMillis();
            while(!result.hasResult()) {
                long afterTime = System.currentTimeMillis();
                if( ((afterTime - beforeTime)/1000) > 1 ) break; // 1초 대기
            }
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
    }
}
