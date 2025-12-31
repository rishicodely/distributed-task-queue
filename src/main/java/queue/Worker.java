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
                Task task = repository.fetchOnePending();
                if (task == null) {
                    System.out.println("No pending task found");
                    Thread.sleep(1000);
                    continue;
                }
                System.out.println("Task picked: " + task.getUuid());
                repository.updateStatus(task.getUuid(), TaskStatus.RUNNING);
                handler.handle(task);
                repository.updateStatus(task.getUuid(), TaskStatus.DONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}