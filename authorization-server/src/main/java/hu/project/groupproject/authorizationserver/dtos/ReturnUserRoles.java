package hu.project.groupproject.authorizationserver.dtos;

import java.util.Set;

public record ReturnUserRoles(String userName, String userId, Set<String> roles) {}
