package me.tiary.domain.tiltag;

import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[TilTag] createId")
class CreateIdTest {
    @Test
    @DisplayName("[Success] til is null and tag is null")
    void successIfTilIsNullAndTagIsNull() {
        // Given
        final TilTag tilTag = TilTag.builder()
                .til(null)
                .tag(null)
                .build();

        // When
        tilTag.createId();

        // Then
        assertThat(tilTag.getId().getTilId()).isNull();
        assertThat(tilTag.getId().getTagId()).isNull();
    }

    @Test
    @DisplayName("[Success] til is not null and tag is null")
    void successIfTilIsNotNullAndTagIsNull() {
        // Given
        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        final TilTag tilTag = TilTag.builder()
                .til(til)
                .tag(null)
                .build();

        // When
        tilTag.createId();

        // Then
        assertThat(tilTag.getId().getTilId()).isEqualTo(til.getId());
        assertThat(tilTag.getId().getTagId()).isNull();
    }

    @Test
    @DisplayName("[Success] til is null and tag is not null")
    void successIfTilIsNullAndTagIsNotNull() {
        // Given
        final Tag tag = TagFactory.createDefaultTag();

        final TilTag tilTag = TilTag.builder()
                .til(null)
                .tag(tag)
                .build();

        // When
        tilTag.createId();

        // Then
        assertThat(tilTag.getId().getTilId()).isNull();
        assertThat(tilTag.getId().getTagId()).isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("[Success] til is not null and tag is not null")
    void successIfTilIsNotNullAndTagIsNotNull() {
        // Given
        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        final Tag tag = TagFactory.createDefaultTag();

        final TilTag tilTag = TilTag.builder()
                .til(til)
                .tag(tag)
                .build();

        // When
        tilTag.createId();

        // Then
        assertThat(tilTag.getId().getTilId()).isEqualTo(til.getId());
        assertThat(tilTag.getId().getTagId()).isEqualTo(tag.getId());
    }
}