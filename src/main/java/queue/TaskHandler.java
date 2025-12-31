package queue;

public interface TaskHandler {
    void handle(Task task) throws Exception;
}