package hu.project.groupproject.resourceserver.converters;

import org.springframework.core.convert.converter.Converter;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.Hu.EventDtoHu;

public class EventConverterEnHu implements Converter<EventDto, EventDtoHu>{

    @Override
    public EventDtoHu convert(EventDto source) {
        return new EventDtoHu(source.name(), source.description(), source.location(), source.userId(), source.orgId(), source.publicPhones(), source.publicEmails(), source.startDate(), source.endDate());
    }
    
}
