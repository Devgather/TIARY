package me.tiary.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class Timestamp {
    @Column(columnDefinition = "datetime", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(columnDefinition = "datetime", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    protected Timestamp() {
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
}