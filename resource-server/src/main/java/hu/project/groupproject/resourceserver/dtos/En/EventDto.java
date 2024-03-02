package hu.project.groupproject.resourceserver.dtos.En;

import java.util.List;
import java.sql.Timestamp;

public record EventDto(String name, String description, String location, Long userId, Long orgId, List<Integer> publicPhones, List<String> publicEmails, Timestamp startDate, Timestamp endDate) {}
// "nev": string //rendezvény neve
// "leiras": string
// "helyszin": string
// "userid": string
// "orgid": string
// "startDate": string //yyyy-MM-dd hh:mm
// "endDate": string //yyyy-MM-dd hh:mm
// "telefonszam":Array<number> //csak itt kell array legyen mert lehet több elérhetőséget megadni
// "email": Array<string> //csak itt kell array legyen mert lehet több elérhetőséget megadni