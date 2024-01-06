package hu.project.groupproject.dtos.postDTOs;

import java.util.List;

import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.entities.MyVote;

public record PostDTOPrivate(Long id, MyUser user, MyOrg myOrg, String content, String url, List<String> imagePath, Long likes, Long dislikes, MyVote myVote) {}
