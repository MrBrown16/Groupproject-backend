package hu.project.groupproject.dtos.postDTOs.out;

import java.util.List;

import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.entities.MyVote;

//not for use Only testing
public record PostDTOPrivate(Long id, MyUser user, MyOrg myOrg, String content, String url, List<String> imagePath, Long likes, Long dislikes, MyVote myVote) {}
// public record PostDTOPrivate(String id, MyUser user, MyOrg myOrg, String content, String url, String likes, String dislikes, MyVote myVote) {}
