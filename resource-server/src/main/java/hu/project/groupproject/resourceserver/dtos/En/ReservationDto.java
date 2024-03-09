package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;

public record ReservationDto(String userId, String orgId, String preferredName, String email, 
        Long phone, Timestamp startDate,Timestamp endDate) {}
