package connector.source.controller;


import connector.source.entities.ConnectConnectorConfigResponse;
import connector.source.entities.ConnectConnectorStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "kafkaConnectClient", url = "${kafka.connect.uris}")
public interface KafkaConnectClient {

    @PostMapping("/connectors")
    String createConnector(@RequestBody ConnectConnectorConfigResponse configuration);

    @GetMapping("/connectors")
    List<String> listConnectors();

    @GetMapping("/connectors/{connector-name}")
    ConnectConnectorConfigResponse getConnectorInfo(@PathVariable("connector-name") String connectorName);

    @GetMapping("/connectors/{connector-name}/status")
    ConnectConnectorStatusResponse getConnectorStatus(@PathVariable("connector-name") String connectorName);

    @GetMapping("/connectors/{connector-name}/config")
    ResponseEntity getConnectorConfig(@PathVariable("connector-name") String connectorName);

    @DeleteMapping("/connectors/{connector-name}")
    ResponseEntity deleteConnector(@PathVariable("connector-name") String connectorName);

    @PutMapping("/connectors/{connector-name}/config")
    ResponseEntity updateConnectorConfig(@PathVariable("connector-name") String connectorName, @RequestBody Map<String, String> config);

    @PutMapping("/connectors/{connector-name}/pause")
    ResponseEntity pauseConnector(@PathVariable("connector-name") String connectorName);

    @PutMapping("/connectors/{connector-name}/resume")
    ResponseEntity resumeConnector(@PathVariable("connector-name") String connectorName);

    @PostMapping("/connectors/{connector-name}/restart")
    ResponseEntity restartConnector(@PathVariable("connector-name") String connectorName);

    @PostMapping("/connectors/{connector-name}/tasks/{task-number}/restart")
    ResponseEntity restartConnectorTask(@PathVariable("connector-name") String connectorName, @PathVariable("task-number") int taskNumber);


    @GetMapping("/debezium/topic-creation")
    Boolean isTopicCreationEnabled();
}
