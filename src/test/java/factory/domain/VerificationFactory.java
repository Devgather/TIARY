package factory.domain;

import config.factory.FactoryPreset;
import me.tiary.domain.Verification;
import utility.StringUtility;

public final class VerificationFactory {
    public static Verification createUnverifiedVerification() {
        return create(
                FactoryPreset.EMAIL,
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                false
        );
    }

    public static Verification createVerifiedVerification() {
        return create(
                FactoryPreset.EMAIL,
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                true
        );
    }

    public static Verification create(final String email, final String code, final Boolean state) {
        final Verification verification = Verification.builder()
                .email(email)
                .code(code)
                .state(state)
                .build();

        verification.createUuid();

        return verification;
    }
}