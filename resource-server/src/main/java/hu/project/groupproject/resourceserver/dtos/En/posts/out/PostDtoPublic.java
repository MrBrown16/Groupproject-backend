package hu.project.groupproject.resourceserver.dtos.En.posts.out;

public record PostDtoPublic(String id, String userId, String userName, String orgId, String orgname, String content, Long likes, Long dislikes, String voteId) {}
