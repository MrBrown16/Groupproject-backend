package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;
import java.util.List;

public record EventDtoPublic(String eventId, String name, String description, String location, String userId, String orgId, List<Long> publicPhones, List<String> publicEmails, Timestamp startDate, Timestamp endDate) {}
