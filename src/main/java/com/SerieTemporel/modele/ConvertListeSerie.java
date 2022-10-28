package com.SerieTemporel.modele;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;

@Converter
public class ConvertListeSerie implements AttributeConverter<ArrayList<Long>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(ArrayList<Long> series) {
        StringBuilder string_base = new StringBuilder("");
        if (series != null) {
            for (Long serie : series) {
                string_base.append(serie).append(SPLIT_CHAR);
            }
        }
        return string_base.toString();
    }

    @Override
    public ArrayList<Long> convertToEntityAttribute(String string) {
        String[] bd_split = string == null ? new String[0] : string.split(SPLIT_CHAR);
        ArrayList<Long> entity = new ArrayList<Long>();

        for (String elt : bd_split) {
            if (!elt.isEmpty() && !elt.isBlank()) {
                entity.add(Long.valueOf(elt));
            }
        }

        return entity;
    }

}
