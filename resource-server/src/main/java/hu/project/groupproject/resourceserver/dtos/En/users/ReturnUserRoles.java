package hu.project.groupproject.resourceserver.dtos.En.users;

import java.util.Set;

public record ReturnUserRoles(String userName, String userId, Set<String> roles) {}

