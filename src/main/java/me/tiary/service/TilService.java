package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.*;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.vo.til.TilVo;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TilService {
    private final TilRepository tilRepository;

    private final TagRepository tagRepository;

    private final TilTagRepository tilTagRepository;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public TilWritingResponseDto writeTil(final String profileUuid, final TilWritingRequestDto requestDto) {
        final Profile profile = profileRepository.findByUuid(profileUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_PROFILE));

        final Til til = tilRepository.save(
                Til.builder()
                        .profile(profile)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .build()
        );

        final List<TilTag> tilTags = new ArrayList<>();

        for (final String tagName : requestDto.getTags()) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(
                                    Tag.builder()
                                            .name(tagName)
                                            .build()
                            )
                    );

            final TilTag tilTag = TilTag.builder()
                    .til(til)
                    .tag(tag)
                    .build();

            tilTags.add(tilTag);
        }

        tilTagRepository.saveAll(tilTags);

        return modelMapper.map(til, TilWritingResponseDto.class);
    }

    public TilReadResponseDto readTil(final String tilUuid) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(tilUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_TIL));

        final Parser markdownParser = Parser.builder().build();
        final HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
        final Node document = markdownParser.parse(til.getContent());

        return TilReadResponseDto.builder()
                .title(til.getTitle())
                .content(htmlRenderer.render(document))
                .author(til.getProfile().getNickname())
                .createdDate(til.getCreatedDate())
                .build();
    }

    public TilListReadResponseDto readTilList(final String nickname, final Pageable pageable) {
        if (profileRepository.findByNickname(nickname).isEmpty()) {
            throw new TilException(TilStatus.NOT_EXISTING_PROFILE);
        }

        final Page<Til> tilPage = tilRepository.findByProfileNickname(nickname, pageable);

        final List<Til> tilContent = tilPage.getContent();

        final List<TilVo> tils = new ArrayList<>();

        for (final Til til : tilContent) {
            final TilVo tilVo = modelMapper.map(til, TilVo.class);

            tils.add(tilVo);
        }

        return TilListReadResponseDto.builder()
                .tils(tils)
                .build();
    }

    public TilEditResponseDto updateTil(final String profileUuid, final String tilUuid, final TilEditRequestDto requestDto) {
        final Til til = tilRepository.findByUuid(tilUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TilException(TilStatus.NOT_AUTHORIZED_MEMBER);
        }

        til.update(requestDto.getTitle(), requestDto.getContent());

        tilTagRepository.deleteAllByTilUuid(tilUuid);

        final List<TilTag> tilTags = new ArrayList<>();

        for (final String tagName : requestDto.getTags()) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(
                                    Tag.builder()
                                            .name(tagName)
                                            .build()
                            )
                    );

            final TilTag tilTag = TilTag.builder()
                    .til(til)
                    .tag(tag)
                    .build();

            tilTags.add(tilTag);
        }

        tilTagRepository.saveAll(tilTags);

        return modelMapper.map(til, TilEditResponseDto.class);
    }
}