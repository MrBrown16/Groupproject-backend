package hu.project.groupproject.dtos.postDTOs.out;

import java.util.List;

public record PostDTOPublic(Long id, Long userId, String userName, Long orgId, String orgname, String content, String url, List<String> imagePath, Long likes, Long dislikes, Long voteId) {}
