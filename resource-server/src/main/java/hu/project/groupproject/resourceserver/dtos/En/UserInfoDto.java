package hu.project.groupproject.resourceserver.dtos.En;

import java.util.Set;

public record UserInfoDto(String userId, String email, String name, Long phone, Set<String> roles,String location, Set<String> orgs) {}
// "userId":string
// "email":string
// "nev": string
// "telefonszam": number|null
// "role": string[]
// "helyszin": string;
// "orgs":string[]