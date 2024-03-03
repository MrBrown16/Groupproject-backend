package hu.project.groupproject.resourceserver.dtos.En.votes;

import java.util.List;

import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPartial;

public record VoteDtoPublic(String id, String description, List<VoteOptionDtoPartial> options, String postId) {}
