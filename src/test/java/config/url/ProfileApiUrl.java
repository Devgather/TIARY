package config.url;

public enum ProfileApiUrl {
    NICKNAME_DUPLICATION_CHECK("/nickname/"),
    PROFILE_CREATION(""),
    PROFILE_READ("/nickname/");

    public static final String COMMON_URL = "/api/profile";

    private final String url;

    ProfileApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}