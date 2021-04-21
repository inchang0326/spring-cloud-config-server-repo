package com.example.app.repo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;


@Repository
public interface AllianceCardMapper {
    List<HashMap<String, Object>> selectAllianceCards(@Param("today") String today);
}
