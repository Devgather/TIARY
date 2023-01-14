package common.config.url;

public enum ViewUrl {
    INDEX("/"),
    LOGIN("/login"),
    PROFILE("/profile/"),
    PROFILE_EDITOR("/profile/editor");

    private final String url;

    ViewUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return url;
    }
}