package common.config.url;

public enum TilApiUrl {
    TIL_WRITING(""),
    TIL_READ("/"),
    TIL_LIST_READ("/list/"),
    RECENT_TIL_LIST_READ("/list");

    public static final String COMMON_URL = "/api/til";

    private final String url;

    TilApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}