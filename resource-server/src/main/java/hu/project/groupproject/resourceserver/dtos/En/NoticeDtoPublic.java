package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;

public record NoticeDtoPublic(String noticeId, String userId,String type, String urgency, String description, String location, Long phone, Timestamp date) {}

