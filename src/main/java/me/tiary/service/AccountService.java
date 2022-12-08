package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountCreationResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.VerificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    private final ProfileRepository profileRepository;

    private final VerificationRepository verificationRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public boolean checkEmailDuplication(final String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public AccountCreationResponseDto register(final AccountCreationRequestDto requestDto) {
        if (checkEmailDuplication(requestDto.getEmail())) {
            throw new AccountException(AccountStatus.EXISTING_EMAIL);
        }

        final Verification verification = verificationRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new AccountException(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION));

        if (!verification.getState()) {
            throw new AccountException(AccountStatus.UNVERIFIED_EMAIL);
        }

        final Profile profile = profileRepository.findByUuidLeftJoinFetchAccount(requestDto.getProfileUuid())
                .orElseThrow(() -> new AccountException(AccountStatus.NOT_EXISTING_PROFILE_UUID));

        if (profile.getAccount() != null) {
            throw new AccountException(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE);
        }

        final Account account = Account.builder()
                .profile(profile)
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        final Account result = accountRepository.save(account);

        return modelMapper.map(result, AccountCreationResponseDto.class);
    }
}