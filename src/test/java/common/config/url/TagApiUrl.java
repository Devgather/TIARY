package common.config.url;

public enum TagApiUrl {
    TAG_LIST_WRITING("/list/"),
    TAG_LIST_READ("/list/"),
    TAG_LIST_EDIT("/list/"),
    TAG_LIST_DELETION("/list/");

    public static final String COMMON_URL = "/api/tag";

    private final String url;

    TagApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}