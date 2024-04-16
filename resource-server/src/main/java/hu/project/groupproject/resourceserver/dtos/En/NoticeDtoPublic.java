package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;

import hu.project.groupproject.resourceserver.enums.NoticeTypes;

public record NoticeDtoPublic(String noticeId, String userId,NoticeTypes type, String urgency, String description, String location, Long phone, Timestamp date) {}

