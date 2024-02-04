package hu.project.groupproject.dtos;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public record UserDTOPublic(String username, Boolean enabled, Collection<GrantedAuthority> authorities){}
