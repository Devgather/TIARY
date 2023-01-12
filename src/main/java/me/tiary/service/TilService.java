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
                .orElseThrow(() -> new TilException(TilStatus.NOT_EXISTING_PROFILE_UUID));

        final Til til = Til.builder()
                .profile(profile)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        final Til result = tilRepository.save(til);

        for (final String tagEach : requestDto.getTags()) {
            final Tag tag = Tag.builder()
                    .name(tagEach)
                    .build();

            tagRepository.save(tag);

            tilTagRepository.save(TilTag.builder()
                    .til(til)
                    .tag(tag)
                    .build());
        }

        return modelMapper.map(result, TilWritingResponseDto.class);
    }
}