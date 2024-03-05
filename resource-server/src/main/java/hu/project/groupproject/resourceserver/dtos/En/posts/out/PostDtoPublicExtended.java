package hu.project.groupproject.resourceserver.dtos.En.posts.out;

import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPublic;

public record PostDtoPublicExtended(PostDtoPublicWithImages post, VoteDtoPublic vote) {}

