package common.config.url;

public enum ViewUrl {
    INDEX("/");

    private final String url;

    ViewUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return url;
    }
}