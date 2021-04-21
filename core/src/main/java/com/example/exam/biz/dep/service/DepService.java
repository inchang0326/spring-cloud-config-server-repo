package com.example.exam.biz.dep.service;

import com.example.exam.biz.dep.repo.mybatis.DepMapper;
import com.example.exam.common.code.EnumResultCode;
import com.example.exam.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepService {
    @Autowired
    private DepMapper depMapper;

    public String getAccount(String cstno) throws ServiceException {
        String acc = "";

        try {
            acc = depMapper.selectAccount(cstno);
            if(acc == null || "".equals(acc)) {
                throw new ServiceException(EnumResultCode.R11112.toString(), EnumResultCode.R11112.getMsg());
            }
        } catch(ServiceException se) {
            throw se;
        } catch(Exception e) {
            throw new ServiceException(EnumResultCode.R99999.toString(), e.getMessage());
        }

        return acc;
    }
}
