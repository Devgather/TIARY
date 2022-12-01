package me.tiary.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ExceptionResponse {
    private final List<String> messages;
}