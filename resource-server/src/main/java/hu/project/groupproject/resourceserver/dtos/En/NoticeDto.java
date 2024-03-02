package hu.project.groupproject.resourceserver.dtos.En;

import java.time.Instant;

public record NoticeDto(String userId,String type, String urgency, String description, String location, Integer phone, Instant date) {}
// "userid":string
// "tipus": string
// "sulyossag": string
// "leiras": string
// "helyszin": string
// "telefonszam":number|null
// "date": string