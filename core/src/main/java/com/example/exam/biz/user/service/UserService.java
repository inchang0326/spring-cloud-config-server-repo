package com.example.exam.biz.user.service;

import com.example.exam.common.code.EnumResultCode;
import com.example.exam.common.exception.ServiceException;
import com.example.exam.biz.user.repo.mybatis.UserMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Log
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public HashMap<String, Object> getUser(HashMap<String, Object> inputMap) throws ServiceException {
        HashMap<String, Object> retMap = null;

        try {
            retMap = userMapper.selectUser(inputMap);
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
}