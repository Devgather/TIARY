package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Profile extends Timestamp {
    public static final int NICKNAME_MAX_LENGTH = 20;

    public static final String BASIC_PICTURE = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @ColumnDefault("(uuid())")
    private String uuid;

    @Column(length = NICKNAME_MAX_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String picture;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Account account;

    void setAccount(final Account account) {
        this.account = account;
    }
}