package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile extends Timestamp {
    public static final int NICKNAME_MAX_LENGTH = 20;

    public static final String BASIC_PICTURE = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String uuid;

    @Column(length = NICKNAME_MAX_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String picture;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<OAuth> oAuths = new ArrayList<>();

    @PrePersist
    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    void setAccount(final Account account) {
        this.account = account;
    }
}