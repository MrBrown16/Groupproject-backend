package hu.project.groupproject.resourceserver.dtos.En.posts.in;

import org.springframework.lang.NonNull;

public record PostDtoUpdate( String id, @NonNull PostDtoCreate post) {}
