package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.tag.TagListEditRequestDto;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.dto.tag.TagListWritingRequestDto;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    private final TilRepository tilRepository;

    private final TilTagRepository tilTagRepository;

    private final ProfileRepository profileRepository;

    @Transactional
    public void writeTagList(final String profileUuid, final String tilUuid, final TagListWritingRequestDto requestDto) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(tilUuid)
                .orElseThrow(() -> new TagException(TagStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TagException(TagStatus.NOT_AUTHORIZED_MEMBER);
        }

        final List<TilTag> tilTags = new ArrayList<>();

        for (final String tagName : requestDto.getTags()) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder()
                                    .name(tagName)
                                    .build()
                    ));

            final TilTag tilTag = TilTag.builder()
                    .til(til)
                    .tag(tag)
                    .build();

            tilTags.add(tilTag);
        }

        tilTagRepository.saveAll(tilTags);
    }

    public TagListReadResponseDto readTagList(final String tilUuid) {
        final Optional<Til> til = tilRepository.findByUuid(tilUuid);

        if (til.isEmpty()) {
            throw new TagException(TagStatus.NOT_EXISTING_TIL);
        }

        final List<TilTag> tilTags = tilTagRepository.findAllByTilUuidJoinFetchTag(tilUuid);
        final List<String> tags = new ArrayList<>();

        for (final TilTag tilTag : tilTags) {
            tags.add(tilTag.getTag().getName());
        }

        return TagListReadResponseDto.builder()
                .tags(tags)
                .build();
    }

    public TagListReadResponseDto readTagListByProfile(final String nickname) {
        if (profileRepository.findByNickname(nickname).isEmpty()) {
            throw new TagException(TagStatus.NOT_EXISTING_PROFILE);
        }

        final List<TilTag> tilTags = tilTagRepository.findAllByTilProfileNicknameJoinFetchTag(nickname);
        final List<String> tags = new ArrayList<>();

        for (final TilTag tilTag : tilTags) {
            tags.add(tilTag.getTag().getName());
        }

        return TagListReadResponseDto.builder()
                .tags(tags)
                .build();
    }

    @Transactional
    public void updateTagList(final String profileUuid, final String tilUuid, final TagListEditRequestDto requestDto) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(tilUuid)
                .orElseThrow(() -> new TagException(TagStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TagException(TagStatus.NOT_AUTHORIZED_MEMBER);
        }

        tilTagRepository.deleteAllByTilUuid(tilUuid);

        final List<TilTag> tilTags = new ArrayList<>();

        for (final String tagName : requestDto.getTags()) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder()
                                    .name(tagName)
                                    .build()
                    ));

            final TilTag tilTag = TilTag.builder()
                    .til(til)
                    .tag(tag)
                    .build();

            tilTags.add(tilTag);
        }

        tilTagRepository.saveAll(tilTags);
    }

    @Transactional
    public void deleteTagList(final String profileUuid, final String tilUuid) {
        final Til til = tilRepository.findByUuidJoinFetchProfile(tilUuid)
                .orElseThrow(() -> new TagException(TagStatus.NOT_EXISTING_TIL));

        if (!til.getProfile().getUuid().equals(profileUuid)) {
            throw new TagException(TagStatus.NOT_AUTHORIZED_MEMBER);
        }

        tilTagRepository.deleteAllByTilUuid(tilUuid);
    }
}