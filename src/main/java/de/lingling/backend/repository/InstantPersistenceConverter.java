package de.lingling.backend.repository;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class InstantPersistenceConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final Instant entityValue) {
        return Timestamp.from(entityValue);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp databaseValue) {
        return databaseValue.toInstant();
    }
}
