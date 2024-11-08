package connector.source.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ConnectConnectorConfigResponse {
    private String name;
    private Map<String, String> config;



    public ConnectConnectorConfigResponse(String name) {
        this.name = name;
    }

    public ConnectConnectorConfigResponse(String name, Map<String, ?> config) {
        this.name = name;
        setConfig(config);
    }
    public void setConfig(Map<String, ?> config) {
        this.config = config.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }

}
