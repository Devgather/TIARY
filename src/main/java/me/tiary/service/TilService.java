package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import org.modelmapper.ModelMapper;
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
        final List<TilTag> tilTags = new ArrayList<>();

        final Profile profile = profileRepository.findByUuid(profileUuid)
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_PROFILE));

        final Til til = tilRepository.save(
                Til.builder()
                        .profile(profile)
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .build()
        );

        for (final String tagName : requestDto.getTags()) {

            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder()
                                    .name(tagName)
                                    .build())
                    );

            final TilTag tilTag = TilTag.builder()
                    .tag(tag)
                    .til(til)
                    .build();
            tilTags.add(tilTag);
        }

        tilTagRepository.saveAll(tilTags);

        return modelMapper.map(til, TilWritingResponseDto.class);
    }
}