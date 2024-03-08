package hu.project.groupproject.resourceserver.converters;

import org.springframework.core.convert.converter.Converter;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.Hu.EventDtoHu;

public class EventConverterHuEn implements Converter<EventDtoHu, EventDto>{

    @Override
    public EventDto convert(EventDtoHu source) {
        return new EventDto(source.nev(), source.leiras(), source.helyszin(), source.userId(), source.orgId(), source.telefonszam(), source.email(), source.startDate(), source.endDate());
    }
    
}
