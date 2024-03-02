package hu.project.groupproject.resourceserver.dtos.Hu;

import java.sql.Timestamp;

public record ReservationDtoHu(String userId, String orgId, String nev, String email, Integer telefonszam, Timestamp startDate,Timestamp endDate) {}
// "userId":string
// "orgId":string
// "nev": string
// "email": string
// "telefonszam":number|null
// "startDate": string //yyyy-MM-dd hh:mm
// "endDate": string //yyyy-MM-dd hh:mm