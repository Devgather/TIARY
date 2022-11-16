package me.tiary.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Profile extends Timestamp {
    public static final int NICKNAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = NICKNAME_MAX_LENGTH, nullable = false, unique = true)
    private String nickname;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Account account;

    void setAccount(final Account account) {
        this.account = account;
    }
}