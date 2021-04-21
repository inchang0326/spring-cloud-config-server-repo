package com.example.exam.biz.card.repo.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface AllianceCardMapper {

    List<HashMap<String, Object>> selectAllianceCardList(@Param("today") String today);
    HashMap<String, Object> selectAllianceCard(@Param("map") HashMap<String, Object> inputMap);
    String hasCard(@Param("map") HashMap<String, Object> inputMap);
    String selectCardApplyHistory(@Param("map") HashMap<String, Object> inputMap);
    HashMap<String, Object> selectCardApplyHistoryForLock(@Param("map") HashMap<String, Object> inputMap);
    void insertCardApplyHistory(@Param("map") HashMap<String, Object> inputMap);
    void updateCardApplyHistory(@Param("map") HashMap<String, Object> inputMap);
}
