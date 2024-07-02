package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.TilReadResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TilService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] readTil")
class ReadTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TilTagRepository tilTagRepository;

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTil(tilUuid));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] til does exist and tags do not exist")
    void successIfTilDoesExistAndTagsDoNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        final Parser markdownParser = Parser.builder().build();
        final HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
        final Node document = markdownParser.parse(til.getContent());

        doReturn(new ArrayList<>())
                .when(tilTagRepository)
                .findAllByTilUuid(tilUuid);

        // When
        final TilReadResponseDto result = tilService.readTil(tilUuid);

        // Then
        assertThat(result.getTitle()).isEqualTo(til.getTitle());
        assertThat(result.getContent()).isEqualTo(htmlRenderer.render(document));
        assertThat(result.getMarkdown()).isEqualTo(til.getContent());
        assertThat(result.getTags()).isEmpty();
        assertThat(result.getAuthor()).isEqualTo(til.getProfile().getNickname());
    }

    @Test
    @DisplayName("[Success] til does exist and tags do exist")
    void successIfTilDoesExistAndTagsDoExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        final Parser markdownParser = Parser.builder().build();
        final HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
        final Node document = markdownParser.parse(til.getContent());

        final List<TilTag> tilTags = List.of(
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(0))),
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(1)))
        );

        doReturn(tilTags)
                .when(tilTagRepository)
                .findAllByTilUuid(tilUuid);

        // When
        final TilReadResponseDto result = tilService.readTil(tilUuid);

        // Then
        assertThat(result.getTitle()).isEqualTo(til.getTitle());
        assertThat(result.getContent()).isEqualTo(htmlRenderer.render(document));
        assertThat(result.getMarkdown()).isEqualTo(til.getContent());
        assertThat(result.getTags()).hasSize(2);
        assertThat(result.getTags().get(0)).isEqualTo(tilTags.get(0).getTag().getName());
        assertThat(result.getTags().get(1)).isEqualTo(tilTags.get(1).getTag().getName());
        assertThat(result.getAuthor()).isEqualTo(til.getProfile().getNickname());
    }
}