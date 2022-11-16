package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Profile extends Timestamp {
    public static final int NICKNAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = NICKNAME_MAX_LENGTH, nullable = false, unique = true)
    private String nickname;
}