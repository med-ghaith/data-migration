package connector.source.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorDefinition {
    public String id;
    public String className;
    public String displayName;
    public String version;
    public boolean enabled;
}
