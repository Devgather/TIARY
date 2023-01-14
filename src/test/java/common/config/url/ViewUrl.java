package common.config.url;

public enum ViewUrl {
    INDEX("/"),
    LOGIN("/login"),
    REGISTER("/register"),
    PROFILE("/profile/"),
    PROFILE_EDITOR("/profile/editor"),
    TIL_EDITOR("/til/editor");

    private final String url;

    ViewUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return url;
    }
}