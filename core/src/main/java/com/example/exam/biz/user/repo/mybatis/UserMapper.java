package com.example.exam.biz.user.repo.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Mapper
public interface UserMapper {
    HashMap<String, Object> selectUser(@Param("map") HashMap<String, Object> inputMap);
}
