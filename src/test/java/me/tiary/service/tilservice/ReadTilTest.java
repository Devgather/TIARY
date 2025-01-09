package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilReadResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.TilRepository;
import me.tiary.service.TilService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findJoinFetchProfileByUuid(tilUuid);

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTil(tilUuid));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] til does exist")
    void successIfTilDoesExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findJoinFetchProfileByUuid(tilUuid);

        final Parser markdownParser = Parser.builder().build();
        final HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
        final Node document = markdownParser.parse(til.getContent());

        // When
        final TilReadResponseDto result = tilService.readTil(tilUuid);

        // Then
        assertThat(result.getTitle()).isEqualTo(til.getTitle());
        assertThat(result.getContent()).isEqualTo(htmlRenderer.render(document));
        assertThat(result.getMarkdown()).isEqualTo(til.getContent());
        assertThat(result.getAuthor()).isEqualTo(til.getProfile().getNickname());
    }
}