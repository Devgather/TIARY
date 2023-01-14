package common.factory.vo.til;

import common.config.factory.FactoryPreset;
import me.tiary.vo.til.TilVo;

import java.util.UUID;

public final class TilVoFactory {
    public static TilVo createDefaultTilVo() {
        return create(UUID.randomUUID().toString(), FactoryPreset.TITLE, FactoryPreset.CONTENT);
    }

    public static TilVo create(final String uuid, final String title, final String content) {
        return TilVo.builder()
                .uuid(uuid)
                .title(title)
                .content(content)
                .build();
    }
}