package hu.project.groupproject.resourceserver.dtos.En;

import java.sql.Timestamp;

import hu.project.groupproject.resourceserver.enums.NoticeTypes;

public record NoticeDto(String userId,NoticeTypes type, String urgency, String description, String location, Long phone, Timestamp date) {}
// "userid":string
// "tipus": string
// "sulyossag": string
// "leiras": string
// "helyszin": string
// "telefonszam":number|null
// "date": string