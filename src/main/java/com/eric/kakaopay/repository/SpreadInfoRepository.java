package com.eric.kakaopay.repository;

import com.eric.kakaopay.dto.SpreadListDto;
import com.eric.kakaopay.entity.SpreadInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpreadInfoRepository extends JpaRepository<SpreadInfo, Integer> {
    Boolean existsByToken(String token);

    @Query("select a from SpreadInfo a join fetch a.roomUserInfoKey where a.token = ?1")
    SpreadInfo findByTokenJoinFetch(String token);

    @Query(value =
            "SELECT " +
                    "NEW com.eric.kakaopay.dto.SpreadListDto(si.roomUserInfoKey.roomUserInfoKey, rui.roomId, rui.userId, si.spreadInfoKey, si.token, si.totalAmount, si.totalUserCount, function('date_format', si.spreadTime, '%Y-%m-%d %T'))" +
                    "FROM SpreadInfo as si left join si.roomUserInfoKey as rui " +
                    "WHERE rui.roomId = ?1"

    )
    List<SpreadListDto> findSpreadInfoUsingRoomId(String roomId);

}
