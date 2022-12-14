package common.factory.domain;

import common.config.factory.FactoryPreset;
import me.tiary.domain.Verification;
import me.tiary.utility.common.StringUtility;

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
                .code(code.toUpperCase())
                .state(state)
                .build();

        verification.createUuid();

        return verification;
    }
}