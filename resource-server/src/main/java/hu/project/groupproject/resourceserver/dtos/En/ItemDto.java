package hu.project.groupproject.resourceserver.dtos.En;

public record ItemDto(String userId, String name, String description, String condition, String location, Long phone, Long price) {}
// "userId":string
// "nev": string
// "leiras": string
// "allapot": string
// "helyszin": string
// "telefonszam": number|null