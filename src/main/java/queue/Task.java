package queue;

import java.util.UUID;
import java.time.Instant;

public class Task {
    private UUID id;
    private String type;
    private String payload;
    private TaskStatus status;
    private int attempts;
    private int maxAttempts;
    private Instant createdAt;
    private Instant nextRunAt;

    public Task(String type, String payload) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.payload = payload;
        this.status = TaskStatus.PENDING;
        this.attempts = 0;
        this.maxAttempts = 3;
        this.createdAt = Instant.now();
        this.nextRunAt = Instant.now();
    }

    public Task(
            UUID id,
            String type,
            String payload,
            TaskStatus status,
            int attempts,
            int maxAttempts,
            Instant createdAt,
            Instant nextRunAt) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.status = status;
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.createdAt = createdAt;
        this.nextRunAt = nextRunAt;
    }

    public UUID getUuid() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
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

    public Instant getNextRunAt() {
        return nextRunAt;
    }
}
