package com.eric.kakaopay.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "room_user_info", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"room_id", "user_id"},
                name = "uk_room_user_info_group_1"
        )})
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_user_info_key", nullable = false)
    private Integer roomUserInfoKey;
    @Column(name = "room_id", nullable = false)
    private String roomId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Column(name = "reg_date", nullable = false)
    private Timestamp regDate;

    @Builder
    public RoomUserInfo(String roomId, int userId) {
        this.roomId = roomId;
        this.userId = userId;
        this.regDate = new Timestamp(System.currentTimeMillis());
    }
}

