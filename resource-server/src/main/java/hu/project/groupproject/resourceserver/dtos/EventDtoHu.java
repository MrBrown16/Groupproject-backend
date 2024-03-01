package hu.project.groupproject.resourceserver.dtos;

import java.util.List;
import java.sql.Timestamp;

public record EventDtoHu(String nev, String leiras, String helyszin, Long userId, Long orgId, List<Integer> telefonszam, List<String> email, Timestamp startDate, Timestamp endDate) {}
// "nev": string
// "leiras": string
// "helyszin": string
// "userid": number
// "orgid": number
// "startDate": string
// "endDate": string
// "telefonszam":Array<number> 
// "email": Array<string> 