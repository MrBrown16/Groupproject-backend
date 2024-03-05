package hu.project.groupproject.resourceserver.dtos.En.posts.out;

public record PostDtoPublicWithImages(String id, String userId, String userName, String orgId, String orgname, String content, String[] images, Long likes, Long dislikes, String voteId) {}
