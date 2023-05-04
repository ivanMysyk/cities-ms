package com.test.task.kuehne.cities.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomMultipartFileSerializer extends JsonSerializer<CustomMultipartFile> {

    @Override
    public void serialize(CustomMultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("name");
        gen.writeString(value.getName());
        gen.writeFieldName("contentType");
        gen.writeString(value.getContentType());
        gen.writeFieldName("size");
        gen.writeNumber(value.getSize());
        gen.writeFieldName("data");
        gen.writeBinary(value.getBytes());
        gen.writeEndObject();
    }
}
