package hu.project.groupproject.dtos.voteDTOs;

import java.util.List;

import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal;

public record VoteDTOPublic(Long id, String description, List<VoteOptionDTOInternal> options, Long postId) {}
