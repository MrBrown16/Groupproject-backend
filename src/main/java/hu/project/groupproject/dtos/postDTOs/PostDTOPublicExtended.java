package hu.project.groupproject.dtos.postDTOs;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;

public record PostDTOPublicExtended(PostDTOPublic post, VoteDTOPublic vote) {}
