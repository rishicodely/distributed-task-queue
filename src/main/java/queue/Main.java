package queue;

public class Main {
    public static void main(String[] args) throws Exception {

        TaskRepository repo = new TaskRepository();
        TaskHandler handler = new SleepTaskHandler();

        Worker worker = new Worker(repo, handler);
        Thread workerThread = new Thread(worker);
        workerThread.start();

        Task task = new Task("SLEEP", "{ \"duration\": 3000 }");
        repo.save(task);

        System.out.println("Task submitted: " + task.getUuid());

        workerThread.join();
    }
}
