package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public boolean checkEmailDuplication(final String email) {
        return accountRepository.findByEmail(email).isPresent();
    }
}