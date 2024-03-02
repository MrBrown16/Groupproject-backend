package hu.project.groupproject.resourceserver.dtos.Hu;

import java.util.Set;

public record UserInfoDtoHu(String userId, String email, String nev, Integer telefonszam, Set<String> roles,String helyszin, Set<String> orgs) {}
// "userId":string
// "email":string
// "nev": string
// "telefonszam": number|null
// "role": string[]
// "helyszin": string;
// "orgs":string[]