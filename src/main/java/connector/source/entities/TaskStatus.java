package connector.source.entities;

import java.util.List;

public class TaskStatus {
    public final ConnectorStatus.State taskStatus;
    public List<String> errors;

    public TaskStatus(ConnectorStatus.State taskStatus, List<String> errors) {
        this.taskStatus = taskStatus;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "taskStatus='" + taskStatus + '\'' +
                ", errors=" + errors +
                '}';
    }
}
