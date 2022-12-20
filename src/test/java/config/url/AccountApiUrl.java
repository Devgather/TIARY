package config.url;

public enum AccountApiUrl {
    EMAIL_DUPLICATION_CHECK("/email/"),
    REGISTER(""),
    LOGIN("/login");

    public static final String COMMON_URL = "/api/account";

    private final String url;

    AccountApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}