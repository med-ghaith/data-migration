package connector.source.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ConnectionValidationResult {
    public Status status;
    public List<PropertyValidationResult> propertyValidationResults;
    public List<GenericValidationResult> genericValidationResults;

    public ConnectionValidationResult(Status status, List<PropertyValidationResult> propertyValidationResults, List<GenericValidationResult> genericValidationResults) {
        this.status = status;
        this.propertyValidationResults = propertyValidationResults;
        this.genericValidationResults = genericValidationResults;
    }

    public static ConnectionValidationResult valid() {
        return new ConnectionValidationResult(Status.VALID, Collections.emptyList(), Collections.emptyList());
    }

    public static ConnectionValidationResult invalid(List<PropertyValidationResult> propertyValidationResults) {
        return invalid(propertyValidationResults, Collections.emptyList());
    }

    public static ConnectionValidationResult invalid(List<PropertyValidationResult> propertyValidationResults, List<GenericValidationResult> genericValidationResults) {
        return new ConnectionValidationResult(Status.INVALID, propertyValidationResults, genericValidationResults);
    }

    public static enum Status {
        VALID, INVALID;
    }
}
