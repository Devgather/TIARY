package me.tiary.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.tiary.domain.common.Timestamp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Verification extends Timestamp {
    public static final int CODE_MAX_LENGTH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @ColumnDefault("(uuid())")
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = CODE_MAX_LENGTH, nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean state;
}