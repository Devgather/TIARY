package me.tiary.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "profile_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public Account(final Long id, final Profile profile, final String email, final String password) {
        setProfile(profile);

        this.id = id;
        this.email = email;
        this.password = password;
    }

    void setProfile(final Profile profile) {
        if (this.profile != null) {
            this.profile.setAccount(null);
        }

        this.profile = profile;

        if (profile != null) {
            profile.setAccount(this);
        }
    }
}