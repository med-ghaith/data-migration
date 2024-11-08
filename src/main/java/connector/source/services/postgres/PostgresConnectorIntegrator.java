package connector.source.services.postgres;


import connector.source.entities.*;
import connector.source.services.ConnectorIntegratorBase;
import io.debezium.DebeziumException;
import io.debezium.config.Configuration;
import io.debezium.config.Field;
import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.connector.postgresql.connection.PostgresConnection;
import io.debezium.relational.TableId;
import org.apache.kafka.connect.source.SourceConnector;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostgresConnectorIntegrator extends ConnectorIntegratorBase {

    private static final Map<String, AdditionalPropertyMetadata> POSTGRES_PROPERTIES;
    static {
        Map<String, AdditionalPropertyMetadata> additionalMetadata = new LinkedHashMap<>();
        // Connection properties
        additionalMetadata.put(PostgresConnectorConfig.TOPIC_PREFIX.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(PostgresConnectorConfig.HOSTNAME.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(PostgresConnectorConfig.PORT.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(PostgresConnectorConfig.USER.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(PostgresConnectorConfig.PASSWORD.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION));
        additionalMetadata.put(PostgresConnectorConfig.DATABASE_NAME.name(), new AdditionalPropertyMetadata(true, ConnectorProperty.Category.CONNECTION));

        // Connection properties - advanced section incl SSL subcategory

        additionalMetadata.put(PostgresConnectorConfig.SSL_CLIENT_CERT.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(PostgresConnectorConfig.SSL_CLIENT_KEY_PASSWORD.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(PostgresConnectorConfig.SSL_ROOT_CERT.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(PostgresConnectorConfig.SSL_CLIENT_KEY.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(PostgresConnectorConfig.SSL_SOCKET_FACTORY.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_SSL));
        additionalMetadata.put(PostgresConnectorConfig.TCP_KEEPALIVE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));
        additionalMetadata.put(PostgresConnectorConfig.ON_CONNECT_STATEMENTS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED));

        // Connection properties - advanced section: Replication properties
        additionalMetadata.put(PostgresConnectorConfig.PLUGIN_NAME.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION, enumArrayToList(PostgresConnectorConfig.LogicalDecoder.values())));
        additionalMetadata.put(PostgresConnectorConfig.SLOT_NAME.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        additionalMetadata.put(PostgresConnectorConfig.STREAM_PARAMS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        additionalMetadata.put(PostgresConnectorConfig.DROP_SLOT_ON_STOP.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        additionalMetadata.put(PostgresConnectorConfig.MAX_RETRIES.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        additionalMetadata.put(PostgresConnectorConfig.RETRY_DELAY_MS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        // additional properties added to UI Requirements document section for Replication properties:
        additionalMetadata.put(PostgresConnectorConfig.STATUS_UPDATE_INTERVAL_MS.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));
        additionalMetadata.put(PostgresConnectorConfig.XMIN_FETCH_INTERVAL.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_REPLICATION));

        // Connection properties - advanced section: publication properties
        additionalMetadata.put(PostgresConnectorConfig.PUBLICATION_NAME.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_PUBLICATION));
        additionalMetadata.put(PostgresConnectorConfig.PUBLICATION_AUTOCREATE_MODE.name(), new AdditionalPropertyMetadata(false, ConnectorProperty.Category.CONNECTION_ADVANCED_PUBLICATION, enumArrayToList(PostgresConnectorConfig.AutoCreateMode.values())));


        POSTGRES_PROPERTIES = Collections.unmodifiableMap(additionalMetadata);
    }

/*
    public FilterValidationResult validateFilters(Map<String, String> properties) {
        PropertiesValidationResult result = validateProperties(properties);
        if (result.status == PropertiesValidationResult.Status.INVALID) {
            return FilterValidationResult.invalid(result.propertyValidationResults);
        }

        PostgresConnectorConfig config = new PostgresConnectorConfig(Configuration.from(properties));

        try (PostgresConnection connection = new PostgresConnection(config.getJdbcConfig())) {
            Set<TableId> tables;
            try {
                tables = connection.readTableNames(config.databaseName(), null, null, new String[]{ "TABLE" });

                List<DataCollection> matchingTables = tables.stream()
                        .filter(tableId -> config.getTableFilters().dataCollectionFilter().isIncluded(tableId))
                        .map(tableId -> new DataCollection(tableId.schema(), tableId.table()))
                        .collect(Collectors.toList());

                return FilterValidationResult.valid(matchingTables);
            }
            catch (SQLException e) {
                throw new DebeziumException(e);
            }
        }
    }
*/

    @Override
    protected ConnectorDescriptor getConnectorDescriptor() {
        // Define the descriptor for the PostgreSQL connector
        return new ConnectorDescriptor("postgres", "PostgreSQL Connector", true);
    }

    @Override
    protected SourceConnector getConnector() {
        return new PostgresConnector();
    }
    @Override
    public Field.Set getAllConnectorFields() {
        return PostgresConnectorConfig.ALL_FIELDS;
    }

    @Override
    public Map<String, AdditionalPropertyMetadata> allPropertiesWithAdditionalMetadata() {
        return POSTGRES_PROPERTIES;
    }

}