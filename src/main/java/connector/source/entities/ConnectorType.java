package connector.source.entities;

import java.util.List;

public class ConnectorType extends ConnectorDefinition {
    public List<ConnectorProperty> properties;

    public ConnectorType() {
    }

    public ConnectorType(String id, String className, String displayName, String version, boolean enabled, List<ConnectorProperty> properties) {
        super(id, className, displayName, version, enabled);
        this.properties = properties;
    }
}
