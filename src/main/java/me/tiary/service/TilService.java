package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.*;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
import me.tiary.vo.til.TilStreakVo;
import me.tiary.vo.til.TilVo;
import me.tiary.vo.til.TilWithProfileVo;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TilService {
    private final TilRepository tilRepository;

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    public boolean checkUuidExistence(final String uuid) {
        return tilRepository.findByUuid(uuid).isPresent();
    }

    public String searchAuthorUsingUuid(final String uuid) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(uuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_TIL));

        return til.getProfile().getNickname();
    }

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
                .markdown(til.getContent())
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

        final int totalPages = tilPage.getTotalPages();

        final List<TilVo> tils = new ArrayList<>();

        for (final Til til : tilContent) {
            final TilVo tilVo = modelMapper.map(til, TilVo.class);

            tils.add(tilVo);
        }

        return TilListReadResponseDto.builder()
                .tils(tils)
                .totalPages(totalPages)
                .build();
    }

    public RecentTilListReadResponseDto readRecentTilList(final Pageable pageable) {
        final Page<Til> tilPage = tilRepository.findAll(pageable);

        final List<Til> tilContent = tilPage.getContent();

        final List<TilWithProfileVo> tils = new ArrayList<>();

        for (final Til til : tilContent) {
            final TilWithProfileVo tilWithProfileVo = TilWithProfileVo.builder()
                    .uuid(til.getUuid())
                    .nickname(til.getProfile().getNickname())
                    .picture(til.getProfile().getPicture())
                    .title(til.getTitle())
                    .content(til.getContent())
                    .build();

            tils.add(tilWithProfileVo);
        }

        return RecentTilListReadResponseDto.builder()
                .tils(tils)
                .build();
    }

    @Transactional
    public TilEditResponseDto updateTil(final String profileUuid, final String tilUuid, final TilEditRequestDto requestDto) {
        final Til til = tilRepository.findByUuid(tilUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TilException(TilStatus.NOT_AUTHORIZED_MEMBER);
        }

        til.update(requestDto.getTitle(), requestDto.getContent());

        return modelMapper.map(til, TilEditResponseDto.class);
    }

    @Transactional
    public TilDeletionResponseDto deleteTil(final String profileUuid, final String tilUuid) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(tilUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TilException(TilStatus.NOT_AUTHORIZED_MEMBER);
        }

        tilRepository.deleteByUuid(tilUuid);

        return modelMapper.map(til, TilDeletionResponseDto.class);
    }

    public TilStreakReadResponseDto readTilStreak(final String nickname, final LocalDate startDate, final LocalDate endDate) {
        if (profileRepository.findByNickname(nickname).isEmpty()) {
            throw new TilException(TilStatus.NOT_EXISTING_PROFILE);
        }

        final List<Til> tils = tilRepository.findAllByProfileNicknameAndCreatedDateBetween(nickname, startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));

        final Map<LocalDate, Integer> tilNumbers = new TreeMap<>();

        for (final Til til : tils) {
            final LocalDate date = til.getCreatedDate().toLocalDate();

            if (tilNumbers.containsKey(date)) {
                tilNumbers.replace(date, tilNumbers.get(date) + 1);
            } else {
                tilNumbers.put(date, 1);
            }
        }

        final List<TilStreakVo> streaks = new ArrayList<>();

        for (Map.Entry<LocalDate, Integer> entry : tilNumbers.entrySet()) {
            final TilStreakVo tilStreakVo = TilStreakVo.builder()
                    .date(entry.getKey())
                    .tilNumber(entry.getValue())
                    .build();

            streaks.add(tilStreakVo);
        }

        return TilStreakReadResponseDto.builder()
                .streaks(streaks)
                .build();
    }
}