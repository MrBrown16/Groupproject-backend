package hu.project.groupproject.dtos.postDTOs.in;

import io.micrometer.common.lang.NonNull;

public record PostDTOCreate(@NonNull Long userId, @NonNull Long orgId, @NonNull String content, String voteDescription, String[] optionTexts) {}
// public record PostDTOCreate(@NonNull String userId, @NonNull String orgId, @NonNull String content, String voteDescription, String[] optionTexts) {}
