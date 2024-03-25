package hu.project.groupproject.resourceserver.dtos.En.posts.in;


public record PostDtoCreate( String userId, String orgId,  String content, String voteDescription, String[] optionTexts) {}

