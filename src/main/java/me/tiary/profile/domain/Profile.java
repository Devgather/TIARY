package me.tiary.profile.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.common.domain.Timestamp;

@Entity
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Profile extends Timestamp {

    public static final int MAX_NICKNAME_LENGTH = 20;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

}
