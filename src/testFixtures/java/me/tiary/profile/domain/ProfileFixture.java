package me.tiary.profile.domain;

public final class ProfileFixture {

    public static final String DEFAULT_NICKNAME = "test";

    public static final String DEFAULT_PICTURE_URL = "https://storage.tiary.me/common/profile/picture.png";

    public static Profile create() {
        return of(DEFAULT_NICKNAME, DEFAULT_PICTURE_URL);
    }

    public static Profile of(final String nickname, final String pictureUrl) {
        return Profile.builder()
                .nickname(nickname)
                .pictureUrl(pictureUrl)
                .build();
    }

}
