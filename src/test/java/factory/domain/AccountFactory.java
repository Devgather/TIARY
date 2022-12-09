package factory.domain;

import config.factory.FactoryPreset;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class AccountFactory {
    public static Account createDefaultAccount(final Profile profile) {
        return create(profile, FactoryPreset.EMAIL, FactoryPreset.PASSWORD);
    }

    public static Account create(final Profile profile, final String email, final String password) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        final Account account = Account.builder()
                .profile(profile)
                .email(email)
                .password((password == null) ? (null) : (passwordEncoder.encode(password)))
                .build();

        account.createUuid();

        return account;
    }
}