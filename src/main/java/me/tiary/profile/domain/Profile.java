package me.tiary.profile.domain;

import jakarta.persistence.*;
import lombok.*;
import me.tiary.common.domain.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Profile extends Timestamp {

    public static final int MAX_NICKNAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = MAX_NICKNAME_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String pictureUrl;

}
