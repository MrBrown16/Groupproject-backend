package hu.project.groupproject.resourceserver.dtos.En;

public record ItemDtoPublicWithImages(String itemId, String userId, String name, String description, String condition, String location, String email, Long phone, Long price, String[] images) {}

