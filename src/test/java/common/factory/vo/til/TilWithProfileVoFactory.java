package common.factory.vo.til;

import common.config.factory.FactoryPreset;
import me.tiary.vo.til.TilWithProfileVo;

import java.util.UUID;

public final class TilWithProfileVoFactory {
    public static TilWithProfileVo createDefaultTilWithProfileVo() {
        return create(UUID.randomUUID().toString(),
                FactoryPreset.NICKNAME,
                FactoryPreset.PICTURE,
                FactoryPreset.TITLE,
                FactoryPreset.CONTENT);
    }

    public static TilWithProfileVo create(final String uuid,
                                          final String nickname,
                                          final String picture,
                                          final String title,
                                          final String content) {
        return TilWithProfileVo.builder()
                .uuid(uuid)
                .nickname(nickname)
                .picture(picture)
                .title(title)
                .content(content)
                .build();
    }
}
