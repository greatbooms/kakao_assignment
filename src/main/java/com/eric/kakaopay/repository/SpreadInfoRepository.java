package com.eric.kakaopay.repository;

import com.eric.kakaopay.entity.SpreadInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpreadInfoRepository extends JpaRepository<SpreadInfo, Integer> {
    Boolean existsByToken(String token);
    @Query("select a from SpreadInfo a join fetch a.roomUserInfoKey where a.token = ?1")
    SpreadInfo findByTokenJoinFetch(String token);
}
