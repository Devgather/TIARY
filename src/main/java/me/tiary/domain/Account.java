package me.tiary.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;
import java.util.UUID;

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

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public Account(final Long id, final Profile profile, final String uuid, final String email, final String password) {
        setProfile(profile);

        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.password = password;
    }

    @PrePersist
    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
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