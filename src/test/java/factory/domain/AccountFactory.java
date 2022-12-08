package factory.domain;

import me.tiary.domain.Account;
import me.tiary.domain.Profile;

public final class AccountFactory {
    public static Account createDefaultAccount(final Profile profile) {
        return create(profile, "test@example.com", "test");
    }

    public static Account create(final Profile profile, final String email, final String password) {
        final Account account = Account.builder()
                .profile(profile)
                .email(email)
                .password(password)
                .build();

        account.createUuid();

        return account;
    }
}