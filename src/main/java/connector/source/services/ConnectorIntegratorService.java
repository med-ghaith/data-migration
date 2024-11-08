package connector.source.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnectorIntegratorService {

    private final Map<String, ConnectorIntegrator> integrators = new HashMap<>();
    private final List<String> supportedConnectorClassnames = new ArrayList<>();

    private final List<ConnectorIntegrator> connectorIntegrators;

    public ConnectorIntegratorService(List<ConnectorIntegrator> connectorIntegrators) {
        this.connectorIntegrators = connectorIntegrators;
    }

    public Map<String, ConnectorIntegrator> getIntegrators() {
        return integrators;
    }

    @PostConstruct
    public void initialize() {
        for (ConnectorIntegrator integrator : connectorIntegrators) {
            integrators.put(integrator.getConnectorType().getId(), integrator);
            supportedConnectorClassnames.add(integrator.getConnectorType().getClassName());
        }
    }

    public ConnectorIntegrator getConnectorIntegrator(String connectorTypeId) {
        return integrators.get(connectorTypeId);
    }

    public List<String> getSupportedConnectorClassnames() {
        return new ArrayList<>(supportedConnectorClassnames);
    }
}