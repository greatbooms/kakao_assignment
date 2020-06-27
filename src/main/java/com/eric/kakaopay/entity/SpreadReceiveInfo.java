package com.eric.kakaopay.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "spread_receive_info", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"room_user_info_key", "spread_info_key"},
                name = "uk_spread_receive_info_group_1"
        )})
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpreadReceiveInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spread_receive_info_key", nullable = false)
    private Integer spreadReceiveInfoKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_user_info_key")
    private RoomUserInfo roomUserInfoKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spread_info_key", nullable = false)
    private SpreadInfo spreadInfoKey;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Column(name = "receive_time")
    private Timestamp receiveTime;
    @Column(name = "reg_date", nullable = false)
    private Timestamp regDate;

//    @Builder
//    public SpreadReceiveInfo(RoomUserInfo roomUserInfoKey, SpreadInfo spreadInfoKey, long amount) {
//        this.roomUserInfoKey = roomUserInfoKey;
//        this.spreadInfoKey = spreadInfoKey;
//        this.amount = amount;
//        this.receiveTime = null;
//        this.regDate = new Timestamp(System.currentTimeMillis());
//    }

    @Builder
    public SpreadReceiveInfo(RoomUserInfo roomUserInfoKey, SpreadInfo spreadInfoKey, long amount, Timestamp regDate) {
        this.roomUserInfoKey = roomUserInfoKey;
        this.spreadInfoKey = spreadInfoKey;
        this.amount = amount;
        this.receiveTime = null;
        if(regDate == null) {
            regDate = new Timestamp(System.currentTimeMillis());
        }
        this.regDate = regDate;
    }

    public void setUpdateInfo(RoomUserInfo roomUserInfoKey) {
        this.roomUserInfoKey = roomUserInfoKey;
        this.receiveTime = new Timestamp(System.currentTimeMillis());
    }
}
