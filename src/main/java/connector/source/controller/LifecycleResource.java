package connector.source.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/connector")
@AllArgsConstructor
@CrossOrigin("*")
public class LifecycleResource {


    KafkaConnectClient kafkaConnectClient;

    @PutMapping("/{connectorname}/pause")
    public ResponseEntity pauseConnector(@PathVariable("connectorname") String connectorName) {
        return kafkaConnectClient.pauseConnector(connectorName);
    }

    @PutMapping("/{connectorname}/resume")
    public ResponseEntity<String> resumeConnector(@PathVariable("connectorname") String connectorName) {
        return kafkaConnectClient.resumeConnector(connectorName);
    }

    @PostMapping("/{connectorname}/restart")
    public ResponseEntity<String> restartConnector(@PathVariable("connectorname") String connectorName) {
        return kafkaConnectClient.restartConnector(connectorName);
    }

    @PostMapping("/{connectorname}/task/{tasknumber}/restart")
    public ResponseEntity<String> restartTask(@PathVariable("connectorname") String connectorName, @PathVariable("tasknumber") int taskNumber) {
        return kafkaConnectClient.restartConnectorTask(connectorName, taskNumber);
    }

}