package com.contact.converters;

import com.contact.dao.Accessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;

public class ModelConverter implements GenericConverter {

    private static final String ID_FIELD = "id";

    private final Class<?> clazz;

    public ModelConverter(Class clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> types = new HashSet<>();
        types.add(new ConvertiblePair(String.class, this.clazz));
        types.add(new ConvertiblePair(this.clazz, String.class));
        return types;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String.class.equals(sourceType.getType())) {
            if (StringUtils.isBlank((String) source)) {
                return null;
            }
            Long id = Long.parseLong((String) source);

            return Accessor.findOne(this.clazz, id);
        } else if (this.clazz.equals(sourceType.getType())) {
            try {
                if (null == source) {
                    return "";
                }
                return FieldUtils.readField(source, ID_FIELD, true).toString();
            } catch (IllegalAccessException e) {
            }
        }
        throw new IllegalArgumentException(source + " cannot be converted into a suitable type!");
    }
}
