package org.mockserver.serialization.serializers.collections;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.mockserver.model.KeyMatchStyle;
import org.mockserver.model.KeyToMultiValue;
import org.mockserver.model.KeysToMultiValues;
import org.mockserver.model.NottableString;

import java.io.IOException;
import java.util.Collection;

import static org.mockserver.model.NottableString.serialiseNottableString;

/**
 * @author jamesdbloom
 */
public abstract class KeysToMultiValuesSerializer<T extends KeysToMultiValues<? extends KeyToMultiValue, T>> extends StdSerializer<T> {

    KeysToMultiValuesSerializer(Class<T> valueClass) {
        super(valueClass);
    }

    @Override
    public void serialize(T collection, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (collection.getKeyMatchStyle() != null && collection.getKeyMatchStyle() != KeyMatchStyle.SUB_SET) {
            jgen.writeObjectField("keyMatchStyle", collection.getKeyMatchStyle());
        }
        for (NottableString key : collection.keySet()) {
            jgen.writeFieldName(serialiseNottableString(key));
            Collection<NottableString> values = collection.getValues(key);
            jgen.writeStartArray(values.size());
            for (NottableString nottableString : values) {
                jgen.writeObject(nottableString);
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

}
