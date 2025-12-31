package queue;

public class SleepTaskHandler implements TaskHandler {
    @Override
    public void handle(Task task) throws Exception {
        System.out.println("Executing task: " + task.getUuid());
        Thread.sleep(3000);
        System.out.println("Task done: " + task.getUuid());
    }
}