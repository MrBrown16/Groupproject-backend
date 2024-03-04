package hu.project.groupproject.resourceserver.dtos.En;

public record ItemDto(String userId, String name, String description, String state, String location, Long phone) {}
// "userId":string
// "nev": string
// "leiras": string
// "allapot": string
// "helyszin": string
// "telefonszam": number|null