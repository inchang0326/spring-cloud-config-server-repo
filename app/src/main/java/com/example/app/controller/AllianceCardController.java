package com.example.app.controller;

import com.example.app.service.AllianceCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class AllianceCardController {

    @Autowired
    private AllianceCardService allianceCardService;

    // 카드 리스트 조회
    @RequestMapping("/cardList")
    public ModelAndView cardList() {
        ModelAndView mnv = new ModelAndView("card_list");
        mnv.addObject("AllianceCards", allianceCardService.getAlliacneCardList());

        // 계속 계정계 호출하지 않도록 캐싱 고려

        return mnv;
    }

    // 카드 상세 조회
    @RequestMapping(value = "/cardDetail", method = RequestMethod.POST)
    public ModelAndView cardDetail(@RequestParam(value = "allianceCode", required = true) String allianceCode
                                , @RequestParam(value = "cardGubunCode", required = true) String cardGubunCode) {
        ModelAndView mnv = new ModelAndView("card_detail");

        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("allianceCode", allianceCode);
        inputMap.put("cardGubunCode", cardGubunCode);

        mnv.addObject("AllianceCard", allianceCardService.getCardDetail(inputMap));
        
        // 카드 정보를 캐싱해둔다면, 이것 또한 불필요한 호출
        
        return mnv;
    }

    // 약관동의
    @RequestMapping(value = "/cardText")
    public ModelAndView cardText(@RequestParam(value = "allianceCode", required = true) String allianceCode
                                , @RequestParam(value = "cardGubunCode", required = true) String cardGubunCode
                                , @RequestParam(value = "cardTypeCode", required = true) String cardTypeCode
                                , @RequestParam(value = "pubTransYn", required = true) String pubTransYn
                                , @RequestParam(value = "cardnm", required = true) String cardnm) throws Exception {
        ModelAndView mnv = new ModelAndView("card_text");

        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("allianceCode", allianceCode);
        inputMap.put("cardGubunCode", cardGubunCode);
        inputMap.put("cardTypeCode", cardTypeCode.trim());
        inputMap.put("pubTransYn", pubTransYn);
        inputMap.put("cardnm", cardnm);

        // 보유 카드 조회
        boolean hasCard = allianceCardService.hasCard(inputMap);
        if(hasCard) throw new Exception("이미 보유한 카드가 있습니다.");

        mnv.addObject("AllianceCardInfo", inputMap);
        return mnv;
    }

    // 신청 정보 입력
    @RequestMapping(value = "/cardApply")
    public ModelAndView cardApply(@RequestParam(value = "allianceCode", required = true) String allianceCode
                                , @RequestParam(value = "cardGubunCode", required = true) String cardGubunCode
                                , @RequestParam(value = "cardTypeCode", required = true) String cardTypeCode
                                , @RequestParam(value = "pubTransYn", required = true) String pubTransYn
                                , @RequestParam(value = "cardnm", required = true) String cardnm
                                , @RequestParam(value = "termsOfServiceYn", required = true) String termsOfServiceYn
                                , @RequestParam(value = "required", required = true) String required
                                , @RequestParam(value = "marketingYn", required = false) String marketingYn
                                , @RequestParam(value = "instRateReduceYn", required = true) String instRateReduceYn
                                , @RequestParam(value = "revolvingYn", required = true) String revolvingYn
                                , @RequestParam(value = "extNo", required = false) String extNo) {
        ModelAndView mnv = new ModelAndView("card_apply");

        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("allianceCode", allianceCode);
        inputMap.put("cardGubunCode", cardGubunCode);
        inputMap.put("cardTypeCode", cardTypeCode);
        inputMap.put("pubTransYn", pubTransYn);
        inputMap.put("cardnm", cardnm);
        inputMap.put("termsOfServiceYn", termsOfServiceYn.equals("on") ? "Y" : "N");
        inputMap.put("required", required.equals("on") ? "Y" : "N");
        inputMap.put("marketingYn", marketingYn.equals("on")  ? "Y" : "N");
        inputMap.put("instRateReduceYn", instRateReduceYn.equals("on") ? "Y" : "N");
        inputMap.put("revolvingYn", revolvingYn.equals("on") ? "Y" : "N");
        inputMap.put("extNo", extNo);

        // 카드신청(약관동의)
        allianceCardService.cardApply(inputMap);

        // 계좌조회
        inputMap.put("account", (String) allianceCardService.getAccount());

        mnv.addObject("AllianceCardApplyInfo", inputMap);
        return mnv;
    }
}
