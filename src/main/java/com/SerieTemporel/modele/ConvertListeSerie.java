package com.SerieTemporel.modele;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;

@Converter
public class ConvertListeSerie implements AttributeConverter<ArrayList<Integer>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(ArrayList<Integer> series) {
        StringBuilder string_base = new StringBuilder("");
        for (Integer serie : series) {
            string_base.append(serie).append(SPLIT_CHAR);
        }
        return string_base.toString();
    }

    @Override
    public ArrayList<Integer> convertToEntityAttribute(String string) {
        String[] bd_split = string.split(SPLIT_CHAR);
        ArrayList<Integer> entity = new ArrayList<Integer>();

        for (String elt : bd_split) {
            entity.add(Integer.valueOf(elt));
        }

        return entity;
    }

}
