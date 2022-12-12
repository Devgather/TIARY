package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Verification extends Timestamp {
    public static final int CODE_MAX_LENGTH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = CODE_MAX_LENGTH, nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean state;

    @PrePersist
    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}