package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;
import me.tiary.domain.composite.TilTagId;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "til_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TilTag extends Timestamp {
    @EmbeddedId
    private TilTagId id;

    @JoinColumn(name = "til_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tilId")
    private Til til;

    @JoinColumn(name = "tag_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    private Tag tag;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String uuid;

    @PrePersist
    private void prePersist() {
        createId();
        createUuid();
    }

    public void createId() {
        this.id = TilTagId.builder()
                .tilId((til == null) ? (null) : (til.getId()))
                .tagId((tag == null) ? (null) : (tag.getId()))
                .build();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}