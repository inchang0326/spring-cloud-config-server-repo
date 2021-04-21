package com.example.exam.biz.log.repo.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Mapper
public interface LogMapper {

    void logging(@Param("map") HashMap<String, Object> inputMap);
}
