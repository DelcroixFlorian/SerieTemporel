package com.SerieTemporel.modele;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ConvertListeSerie implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> liste_droit) {
        StringBuilder string_base = new StringBuilder("");
        if (liste_droit != null) {
            for (String droit : liste_droit) {
                int index = liste_droit.indexOf(droit);
                string_base.append(index).append(":").append(droit).append(SPLIT_CHAR);
            }
        }
        return string_base.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        String[] bd_split = string == null ? new String[0] : string.split(SPLIT_CHAR);
        List<String> entity = new ArrayList<String>();

        for (String elt : bd_split) {
            if (!elt.isEmpty() && !elt.isBlank()) {
                String[] ref = elt.split(":");
                int index = Integer.getInteger(ref[0]);
                String droit = ref[1];
                entity.add(index, droit);
            }
        }

        return entity;
    }

}

