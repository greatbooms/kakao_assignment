package com.eric.kakaopay.repository;

import com.eric.kakaopay.entity.RoomUserInfo;
import com.eric.kakaopay.entity.SpreadInfo;
import com.eric.kakaopay.entity.SpreadReceiveInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SpreadReceiveInfoRepository extends JpaRepository<SpreadReceiveInfo, Integer> {
    Boolean existsByRoomUserInfoKeyAndSpreadInfoKey(RoomUserInfo roomUserInfoKey, SpreadInfo spreadInfoKey);
    SpreadReceiveInfo findFirstBySpreadInfoKeyAndRoomUserInfoKeyIsNullAndRegDateIsGreaterThanEqualOrderBySpreadReceiveInfoKeyAsc(SpreadInfo spreadInfoKey, Timestamp ts);
    @Query("select a from SpreadReceiveInfo a left join fetch a.roomUserInfoKey b join fetch a.spreadInfoKey c where c.token = ?1 and c.spreadTime >= ?2")
    List<SpreadReceiveInfo> findDataByToken(String token, Timestamp ts);
}
