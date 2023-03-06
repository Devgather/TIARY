package me.tiary.domain.account;

import me.tiary.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Account] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Account account = Account.builder().build();

        // When
        account.createUuid();

        // Then
        assertThat(account.getUuid()).isNotNull();
        assertThat(account.getUuid()).hasSize(36);
    }
}