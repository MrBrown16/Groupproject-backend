package hu.project.groupproject.dtos.postDTOs.in;

import org.springframework.lang.NonNull;

// import io.micrometer.common.lang.NonNull;

public record PostDTOUpdate(@NonNull Long id, @NonNull PostDTOCreate post) {}
