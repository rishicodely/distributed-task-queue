package queue;

public class Main {
    public static void main(String[] args) {

        TaskRepository repo = new TaskRepository();

        Task task = new Task("SLEEP", "{ \"duration\": 3000 }");
        repo.save(task);

        System.out.println("Task saved: " + task.getUuid());
    }
}
