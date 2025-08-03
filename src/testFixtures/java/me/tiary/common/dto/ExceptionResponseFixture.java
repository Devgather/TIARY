package me.tiary.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.tiary.support.util.json.ObjectMapperHelper;

public final class ExceptionResponseFixture {

    private static final ObjectMapper objectMapper = ObjectMapperHelper.objectMapper();

    public static ExceptionResponse from(final String json) throws Exception {
        return objectMapper.readValue(json, ExceptionResponse.class);
    }

}
