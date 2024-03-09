package hu.project.groupproject.resourceserver.dtos.En.posts.in;

import org.springframework.lang.NonNull;

public record PostDtoCreate(@NonNull String userId, String orgId, @NonNull String content, String voteDescription, String[] optionTexts) {}

