package hu.project.groupproject.resourceserver.dtos.Hu;

import java.sql.Timestamp;

public record NoticeDtoHu(String userId,String tipus, String sulyossag, String leiras, String helyszin, Long telefonszam, Timestamp date) {}
// "userid":string
// "tipus": string
// "sulyossag": string
// "leiras": string
// "helyszin": string
// "telefonszam":number|null
// "date": string