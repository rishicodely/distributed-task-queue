package queue;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("SLEEP", "{\"Duration\": 3000}");
        System.out.println("Task ID: " + task.getUuid());
        System.out.println("Status: " + task.getStatus());
    }
}
