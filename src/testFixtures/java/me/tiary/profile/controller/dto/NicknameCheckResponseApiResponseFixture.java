package me.tiary.profile.controller.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.tiary.common.dto.ApiResponse;
import me.tiary.support.util.json.ObjectMapperHelper;

public final class NicknameCheckResponseApiResponseFixture {

    private static final ObjectMapper objectMapper = ObjectMapperHelper.objectMapper();

    private static final TypeReference<ApiResponse<NicknameCheckResponse>> typeReference = new TypeReference<>() {
    };

    public static ApiResponse<NicknameCheckResponse> from(final String json) throws Exception {
        return objectMapper.readValue(json, typeReference);
    }

}
