package me.tiary.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tiary.repository.VerificationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(value = "scheduler.verification-cleaner.enable", havingValue = "true")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VerificationCleaner {
    private final VerificationRepository verificationRepository;

    @Scheduled(fixedRateString = "${scheduler.verification-cleaner.interval}")
    @Transactional
    public void clean() {
        final LocalDateTime dateTime = LocalDateTime.now().minusHours(1);

        verificationRepository.deleteByLastModifiedDateLessThanEqual(dateTime);

        log.info("Verifications that was last modified 1 hour ago are deleted");
    }
}