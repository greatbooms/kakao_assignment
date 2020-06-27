package com.eric.kakaopay.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "spread_info")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpreadInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spread_info_key", nullable = false)
    private Integer spreadInfoKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_user_info_key", nullable = false)
    private RoomUserInfo roomUserInfoKey;
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;
    @Column(name = "total_user_count", nullable = false)
    private Integer totalUserCount;
    @Column(name = "spread_time", nullable = false)
    private Timestamp spreadTime;
    @Column(name = "reg_date", nullable = false)
    private Timestamp regDate;

//    @Builder
//    public SpreadInfo(RoomUserInfo roomUserInfoKey, String token, long totalAmount, int totalUserCount) {
//        this.roomUserInfoKey = roomUserInfoKey;
//        this.token = token;
//        this.totalAmount = totalAmount;
//        this.totalUserCount = totalUserCount;
//        this.spreadTime = new Timestamp(System.currentTimeMillis());
//        this.regDate = new Timestamp(System.currentTimeMillis());
//    }

    @Builder
    public SpreadInfo(RoomUserInfo roomUserInfoKey, String token, long totalAmount, int totalUserCount , Timestamp spreadTime, Timestamp regDate) {
        this.roomUserInfoKey = roomUserInfoKey;
        this.token = token;
        this.totalAmount = totalAmount;
        this.totalUserCount = totalUserCount;
        if(spreadTime == null) {
            spreadTime = new Timestamp(System.currentTimeMillis());
        }
        this.spreadTime = spreadTime;
        if(regDate == null) {
            regDate = new Timestamp(System.currentTimeMillis());
        }
        this.regDate = regDate;
    }
}
