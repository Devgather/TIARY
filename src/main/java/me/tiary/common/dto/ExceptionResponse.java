package me.tiary.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(List<String> messages, LocalDateTime timestamp) {
}
