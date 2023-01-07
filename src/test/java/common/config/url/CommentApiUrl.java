package common.config.url;

public enum CommentApiUrl {
    WRITE_COMMENT("");

    public static final String COMMON_URL = "/api/comment";

    private final String url;

    CommentApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}
