package queue;

public class Worker implements Runnable {
    private final TaskRepository repository;
    private final TaskHandler handler;

    public Worker(TaskRepository repository, TaskHandler handler) {
        this.repository = repository;
        this.handler = handler;
    }

    @Override
    public void run() {
        System.out.println("Worker started..");
        while (true) {
            try {
                System.out.println("Worker pulling db..");
                Task task = repository.fetchAndMarkRunning();
                if (task == null) {
                    System.out.println("No pending task found");
                    Thread.sleep(1000);
                    continue;
                }
                System.out.println("Task picked: " + task.getUuid());
                try {
                    handler.handle(task);
                    repository.updateStatus(task.getUuid(), TaskStatus.SUCCESS);
                } catch (Exception e) {
                    repository.incrementAttempts(task.getUuid());
                    if (task.getAttempts() + 1 >= task.getMaxAttempts()) {
                        repository.updateStatus(task.getUuid(), TaskStatus.FAILED);
                    } else {
                        repository.updateStatus(task.getUuid(), TaskStatus.PENDING);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}