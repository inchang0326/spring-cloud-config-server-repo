package com.example.exam.biz.ext.repo.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Mapper
public interface ExtMapper {
    HashMap<String, Object> selectHistoryForLock(@Param("map") HashMap<String, Object> inputMap);
    void insertHistory(@Param("map") HashMap<String, Object> inputMap);
    void updateHistory(@Param("map") HashMap<String, Object> inputMap);
}
