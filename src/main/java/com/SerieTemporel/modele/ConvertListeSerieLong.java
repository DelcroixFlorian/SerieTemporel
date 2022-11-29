package com.SerieTemporel.modele;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ConvertListeSerieLong implements AttributeConverter<List<Long>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Long> liste_ref_user) {
        StringBuilder string_base = new StringBuilder("");
        if (liste_ref_user != null) {
            for (Long id_user : liste_ref_user) {
                string_base.append(id_user).append(SPLIT_CHAR);
            }
        }
        return string_base.toString();
    }

    @Override
    public List<Long> convertToEntityAttribute(String string) {
        String[] bd_split = string == null ? new String[0] : string.split(SPLIT_CHAR);
        List<Long> entity = new ArrayList<>();

        for (String elt : bd_split) {
            if (!elt.isEmpty() && !elt.isBlank()) {
                entity.add(Long.decode(elt));
            }
        }

        return entity;
    }

}

