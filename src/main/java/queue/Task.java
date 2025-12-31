package queue;

import java.util.UUID;
import java.security.Timestamp;
import java.time.Instant;

public class Task {
    private UUID id;
    private String type;
    private String payload;
    private TaskStatus status;
    private int attempts;
    private Instant createdAt;

    public Task(String type, String payload) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.payload = payload;
        this.status = TaskStatus.PENDING;
        this.attempts = 0;
        this.createdAt = Instant.now();
    }

    public Task(
            UUID id,
            String type,
            String payload,
            TaskStatus status,
            int attempts,
            Instant createdAt) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.status = status;
        this.attempts = attempts;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
