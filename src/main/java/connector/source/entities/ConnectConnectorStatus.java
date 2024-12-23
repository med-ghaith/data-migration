package connector.source.entities;

import javax.json.bind.annotation.JsonbProperty;

public class ConnectConnectorStatus {
    @JsonbProperty("state")
    public ConnectorStatus.State status;

    @JsonbProperty("worker_id")
    public String workerId;

    @Override
    public String toString() {
        return "ConnectConnectorStatus{" +
                "status=" + status +
                ", workerId='" + workerId + '\'' +
                '}';
    }
}
