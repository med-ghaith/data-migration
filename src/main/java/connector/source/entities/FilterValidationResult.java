package connector.source.entities;

import java.util.Collections;
import java.util.List;

public class FilterValidationResult {
    public Status status;
    public List<PropertyValidationResult> propertyValidationResults;
    public List<DataCollection> matchedCollections;

    public FilterValidationResult(Status status, List<PropertyValidationResult> propertyValidationResults, List<DataCollection> matchedCollections) {
        this.status = status;
        this.propertyValidationResults = propertyValidationResults;
        this.matchedCollections = matchedCollections;
    }

    public static FilterValidationResult valid(List<DataCollection> matchedCollections) {
        return new FilterValidationResult(Status.VALID, Collections.emptyList(), matchedCollections);
    }

    public static FilterValidationResult invalid(List<PropertyValidationResult> propertyValidationResults) {
        return new FilterValidationResult(Status.INVALID, propertyValidationResults, Collections.emptyList());
    }

    public static enum Status {
        VALID, INVALID;
    }
}
