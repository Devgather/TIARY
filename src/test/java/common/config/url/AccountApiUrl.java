package common.config.url;

public enum AccountApiUrl {
    EMAIL_EXISTENCE_CHECK("/email/"),
    REGISTER(""),
    VERIFICATION_MAIL_DELIVERY("/verification/"),
    EMAIL_VERIFICATION("/verification"),
    LOGIN("/login"),
    LOGOUT("/logout");

    public static final String COMMON_URL = "/api/account";

    private final String url;

    AccountApiUrl(final String url) {
        this.url = url;
    }

    public String getEntireUrl() {
        return COMMON_URL + url;
    }
}