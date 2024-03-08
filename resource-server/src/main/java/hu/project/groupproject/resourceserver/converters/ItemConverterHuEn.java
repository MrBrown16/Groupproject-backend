package hu.project.groupproject.resourceserver.converters;

import org.springframework.core.convert.converter.Converter;

import hu.project.groupproject.resourceserver.dtos.En.ItemDto;
import hu.project.groupproject.resourceserver.dtos.Hu.ItemDtoHu;

public class ItemConverterHuEn implements Converter<ItemDtoHu, ItemDto> {

    @Override
    public ItemDto convert(ItemDtoHu source) {
        return new ItemDto(source.userId(), source.nev(), source.leiras(), source.allapot(), source.helyszin(),source.telefonszam());
    }
    
}