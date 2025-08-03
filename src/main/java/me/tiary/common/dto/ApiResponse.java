package me.tiary.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApiResponse<T>(T data, List<String> messages, LocalDateTime timestamp) {
}
