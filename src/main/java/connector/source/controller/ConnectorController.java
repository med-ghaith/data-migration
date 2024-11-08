package connector.source.controller;


import connector.source.entities.*;
import connector.source.services.ConnectorIntegrator;
import connector.source.services.ConnectorIntegratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class ConnectorController {

    ConnectorIntegratorService connectorIntegratorService;


    KafkaConnectClient kafkaConnectClient;


    private Map<String, String> convertPropertiesToStrings(Map<String, ?> properties) {
        return properties.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }

    @GetMapping("/connector-types")
    public List<ConnectorDefinition> getConnectorTypes() {
        return connectorIntegratorService.getIntegrators().values()
                .stream()
                .map(ConnectorIntegrator::getConnectorDefinition)
                .collect(Collectors.toList());
    }

    @PostMapping("/connector-types/{id}/validation/connection")
    public ResponseEntity<ConnectionValidationResult> validateConnectionProperties(@PathVariable("id") String connectorTypeId, @RequestBody Map<String, String> properties) {
        ConnectorIntegrator integrator = connectorIntegratorService.getIntegrators().get(connectorTypeId);

        ConnectionValidationResult validationResult = integrator.validateConnection(convertPropertiesToStrings(properties));

        return ResponseEntity.ok(validationResult);
    }

    @PostMapping("/connector-types/{id}/validation/properties")
    public ResponseEntity<PropertiesValidationResult> validateConnectorProperties(@PathVariable("id") String connectorTypeId, @RequestBody Map<String, String> properties) {
        ConnectorIntegrator integrator = connectorIntegratorService.getIntegrators().get(connectorTypeId);


        PropertiesValidationResult validationResult = integrator.validateProperties(convertPropertiesToStrings(properties));

        return ResponseEntity.ok(validationResult);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createConnector(@RequestBody ConnectConnectorConfigResponse kafkaConnectConfig) {
        try {
            String result = kafkaConnectClient.createConnector(kafkaConnectConfig);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create/{connector-type-id}")
    public ResponseEntity<String> createConnectorWithType(@RequestBody ConnectConnectorConfigResponse kafkaConnectConfig, @PathVariable("connector-type-id") String connectorTypeId) {
        ConnectorIntegrator integrator = connectorIntegratorService.getIntegrators().get(connectorTypeId);

        PropertiesValidationResult validationResult = integrator.validateProperties(kafkaConnectConfig.getConfig());
        kafkaConnectConfig.getConfig().put("connector.class", connectorIntegratorService.getIntegrators().get(connectorTypeId).getConnectorType().getClassName());
        try {
            String result = kafkaConnectClient.createConnector(kafkaConnectConfig);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/connectors")
    public ResponseEntity<List<ConnectorStatus>> listConnectors() {
        List<String> activeConnectors;

        activeConnectors = kafkaConnectClient.listConnectors();

        List<ConnectorStatus> connectorData = Collections.emptyList();
        if (!activeConnectors.isEmpty()) {
            connectorData = activeConnectors.stream().map(
                    connectorName -> {
                        var connectorInfo = kafkaConnectClient.getConnectorInfo(connectorName);
                        String connectorType = connectorInfo.getConfig().get("connector.class");
                        if (!connectorType.startsWith("io.debezium")) {
                            return null;
                        }
                        var connectorStatus = kafkaConnectClient.getConnectorStatus(connectorName);
                        var connectorState = new ConnectorStatus(connectorName);
                        connectorState.setConnectorType(connectorType);
                        connectorState.setConnectorStatus(connectorStatus.connectorStatus.status);
                        connectorStatus.taskStates.forEach(
                                taskStatus -> connectorState.setTaskState(
                                        taskStatus.id,
                                        taskStatus.status,
                                        (taskStatus.getErrorsAsList() != null
                                                ? taskStatus.getErrorsAsList()
                                                .stream().filter(s -> s.startsWith("Caused by:")).collect(Collectors.toList())
                                                : null
                                        )
                                ));
                        return connectorState;
                    }).collect(Collectors.toList());
        }



        return ResponseEntity.ok(connectorData);
    }

    @DeleteMapping("/connectors/{connector-name}")
    public ResponseEntity deleteConnector(@PathVariable("connector-name") String connectorName)  {

        return ResponseEntity.ok(kafkaConnectClient.deleteConnector(connectorName));
    }
}