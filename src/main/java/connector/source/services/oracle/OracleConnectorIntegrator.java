package connector.source.services.oracle;

import connector.source.entities.AdditionalPropertyMetadata;
import connector.source.entities.ConnectorProperty;
import connector.source.services.ConnectorIntegratorBase;
import io.debezium.config.Field;
import io.debezium.connector.oracle.OracleConnector;
import io.debezium.connector.oracle.OracleConnectorConfig;
import org.apache.kafka.connect.source.SourceConnector;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class OracleConnectorIntegrator extends ConnectorIntegratorBase {

    private static final Map<String, AdditionalPropertyMetadata> ORACLE_PROPERTIES;

    static {
        Map<String, AdditionalPropertyMetadata> additionalMetadata = new LinkedHashMap<>();

        // Define Oracle connection properties
        additionalMetadata.put(OracleConnectorConfig.HOSTNAME.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(OracleConnectorConfig.PORT.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(OracleConnectorConfig.USER.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(OracleConnectorConfig.PASSWORD.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));

        additionalMetadata.put(OracleConnectorConfig.CONNECTOR_ADAPTER.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));


        ORACLE_PROPERTIES = Collections.unmodifiableMap(additionalMetadata);
    }

    @Override
    public Field.Set getAllConnectorFields() {
        return OracleConnectorConfig.ALL_FIELDS;
    }

    @Override
    protected ConnectorDescriptor getConnectorDescriptor() {
        return new ConnectorDescriptor("oracle", "Oracle", true);
    }

    @Override
    public Map<String, AdditionalPropertyMetadata> allPropertiesWithAdditionalMetadata() {
        return ORACLE_PROPERTIES;
    }

    @Override
    protected SourceConnector getConnector() {
        return new OracleConnector();
    }
}