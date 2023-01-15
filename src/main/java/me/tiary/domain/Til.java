package me.tiary.domain;

import lombok.*;
import me.tiary.domain.common.Timestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "til")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Til extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "profile_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Column(columnDefinition = "char(36)", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @OneToMany(mappedBy = "til", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Til(final Long id,
               final Profile profile,
               final String uuid,
               final String title,
               final String content,
               final List<Comment> comments) {
        setProfile(profile);

        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.content = content;
        this.comments = (comments == null) ? (new ArrayList<>()) : (comments);
    }

    @PrePersist
    private void prePersist() {
        createUuid();
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void update(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    void setProfile(final Profile profile) {
        if (this.profile != null) {
            this.profile.getTils().remove(this);
        }

        this.profile = profile;

        if (profile != null) {
            profile.getTils().add(this);
        }
    }
}