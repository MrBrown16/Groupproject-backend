package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;

public record ReservationDto(String userId, String orgId, String preferredName, String email, Integer phone, Timestamp startDate,Timestamp endDate) {}
// "userId":string
// "orgId":string
// "nev": string
// "email": string
// "telefonszam":number|null
// "startDate": string //yyyy-MM-dd hh:mm
// "endDate": string //yyyy-MM-dd hh:mm