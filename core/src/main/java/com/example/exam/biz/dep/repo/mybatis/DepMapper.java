package com.example.exam.biz.dep.repo.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DepMapper {

    String selectAccount(@Param("cstno") String cstno);
}
