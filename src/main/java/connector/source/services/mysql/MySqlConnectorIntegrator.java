package connector.source.services.mysql;


import connector.source.entities.AdditionalPropertyMetadata;
import connector.source.entities.ConnectorProperty;
import connector.source.services.ConnectorIntegratorBase;
import io.debezium.config.Field;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.storage.kafka.history.KafkaSchemaHistory;
import org.apache.kafka.connect.source.SourceConnector;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MySqlConnectorIntegrator extends ConnectorIntegratorBase {


    private static final Map<String, AdditionalPropertyMetadata> MYSQL_PROPERTIES;
    static {
        Map<String, AdditionalPropertyMetadata> additionalMetadata = new LinkedHashMap<>();
        // Connection properties
        additionalMetadata.put(MySqlConnectorConfig.TOPIC_PREFIX.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.SERVER_ID.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.HOSTNAME.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.PORT.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.USER.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.PASSWORD.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(KafkaSchemaHistory.BOOTSTRAP_SERVERS.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(KafkaSchemaHistory.TOPIC.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(MySqlConnectorConfig.JDBC_DRIVER.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));

        // Connection properties - advanced section incl SSL subcategory
        additionalMetadata.put(MySqlConnectorConfig.SSL_MODE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL, enumArrayToList(MySqlConnectorConfig.SecureConnectionMode.values())));
        additionalMetadata.put(MySqlConnectorConfig.SSL_KEYSTORE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(MySqlConnectorConfig.SSL_KEYSTORE_PASSWORD.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(MySqlConnectorConfig.SSL_TRUSTSTORE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(MySqlConnectorConfig.SSL_TRUSTSTORE_PASSWORD.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(MySqlConnectorConfig.SERVER_ID_OFFSET.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));
        additionalMetadata.put(MySqlConnectorConfig.CONNECTION_TIMEOUT_MS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));
        additionalMetadata.put(MySqlConnectorConfig.KEEP_ALIVE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));
        additionalMetadata.put(MySqlConnectorConfig.KEEP_ALIVE_INTERVAL_MS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));
        additionalMetadata.put(MySqlConnectorConfig.ON_CONNECT_STATEMENTS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));

        // Filter properties
        additionalMetadata.put(MySqlConnectorConfig.DATABASE_INCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.DATABASE_EXCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.TABLE_INCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.TABLE_EXCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.COLUMN_INCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.COLUMN_EXCLUDE_LIST.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));
        additionalMetadata.put(MySqlConnectorConfig.TABLES_IGNORE_BUILTIN.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.FILTERS));

        MYSQL_PROPERTIES = Collections.unmodifiableMap(additionalMetadata);
    }

    @Override
    protected ConnectorDescriptor getConnectorDescriptor() {
        return new ConnectorDescriptor("mysql", "MySQL", true);
    }

    @Override
    protected SourceConnector getConnector() {
        return new MySqlConnector();
    }

    @Override
    public Map<String, AdditionalPropertyMetadata> allPropertiesWithAdditionalMetadata() {
        return MYSQL_PROPERTIES;
    }

    @Override
    public Field.Set getAllConnectorFields() {
        return MySqlConnectorConfig.ALL_FIELDS;
    }
}