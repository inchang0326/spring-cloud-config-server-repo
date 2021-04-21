package com.example.exam.biz.elfn.service;

import com.example.exam.biz.card.service.CardService;
import com.example.exam.biz.dep.service.DepService;
import com.example.exam.biz.user.service.UserService;
import com.example.exam.common.code.EnumResultCode;
import com.example.exam.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
public class ElfnService {
    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepService depService;

    public List<HashMap<String, Object>> getAllianceCardList() throws ServiceException {
        List<HashMap<String, Object>> retList = null;
        try {
            String today = LocalDate.now().toString().replaceAll("-", "");
            retList = cardService.getAllianceCardList(today);
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
            String today = LocalDate.now().toString().replaceAll("-", "");
            inputMap.put("today", today);

            retMap = cardService.getAllianceCard(inputMap);
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
        return retMap;
    }

    public HashMap<String, Object> hasCard(HashMap<String, Object> inputMap) throws ServiceException {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            String appCstno = (String) inputMap.get("cstno");
            String examCstno = (String) userService.getUser(inputMap).get("CSTNO");

            if((appCstno != null && examCstno != null) && appCstno.equals(examCstno)) {
                String hasCard = cardService.hasCard(inputMap);

                if(Integer.parseInt(hasCard) > 0) {
                    retMap.put("hasCard", "Y");
                } else {
                    retMap.put("hasCard", "N");
                }
            } else {
                throw new ServiceException(EnumResultCode.R11113.toString(), EnumResultCode.R11113.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }

        return retMap;
    }

    public void cardApply(HashMap<String, Object> inputMap) throws ServiceException {
        try {
            String appCstno = (String) inputMap.get("cstno");
            String examCstno = (String) userService.getUser(inputMap).get("CSTNO");

            if((appCstno != null && examCstno != null) && appCstno.equals(examCstno)) {
                cardService.cardApply(inputMap);
            } else {
                throw new ServiceException(EnumResultCode.R11113.toString(), EnumResultCode.R11113.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
    }

    public String getAccount(HashMap<String, Object> inputMap) throws ServiceException {
        String acc = "";
        try {
            String appCstno = (String) inputMap.get("cstno");
            String examCstno = (String) userService.getUser(inputMap).get("CSTNO");

            if((appCstno != null && examCstno != null) && appCstno.equals(examCstno)) {
                acc = depService.getAccount(appCstno);
            } else {
                throw new ServiceException(EnumResultCode.R11113.toString(), EnumResultCode.R11113.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }
        return acc;
    }
}
