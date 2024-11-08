package connector.source.services;


import connector.source.entities.*;
import io.debezium.config.Field;

import java.util.Map;

public interface ConnectorIntegrator {

    ConnectorType getConnectorType();

    ConnectorDefinition getConnectorDefinition();

    Map<String, AdditionalPropertyMetadata> allPropertiesWithAdditionalMetadata();

    /**
     * Validates the set of connection-related properties.
     */
    ConnectionValidationResult validateConnection(Map<String, String> properties);

    /**
     * Returns ALL_FIELDS from a ConnectorConfig that should be validated.
     */
    Field.Set getAllConnectorFields();



    /**
     * Validates an arbitrary set of connector properties.
     */
    PropertiesValidationResult validateProperties(Map<String, String> properties);
}