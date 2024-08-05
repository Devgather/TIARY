package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
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
    private final TilRepository tilRepository;

    private final TilTagRepository tilTagRepository;

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
}