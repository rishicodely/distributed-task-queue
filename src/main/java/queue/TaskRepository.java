package queue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

public class TaskRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/task_queue";
    private static final String USER = "queue_user";
    private static final String PASSWORD = "queue_pass";

    public void save(Task task) {
        String sql = "insert into tasks (id, type, payload, status, attempts, max_attempts, created_at, next_run_at) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, task.getUuid());
            ps.setString(2, task.getType());
            ps.setString(3, task.getPayload());
            ps.setString(4, task.getStatus().name());
            ps.setInt(5, task.getAttempts());
            ps.setInt(6, task.getMaxAttempts());
            ps.setTimestamp(7, Timestamp.from(task.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.from(task.getNextRunAt()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "select * from tasks";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getObject("id", UUID.class),
                        rs.getString("type"),
                        rs.getString("payload"),
                        TaskStatus.valueOf(rs.getString("status")),
                        rs.getInt("attempts"),
                        rs.getInt("max_attempts"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("next_run_at").toInstant());
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public Task fetchAndMarkRunning() {
        String sql = "update tasks set status = 'RUNNING' where id = ( select id from tasks where status = 'PENDING' and attempts < max_attempts and next_run_at <= now() order by next_run_at limit 1) returning *";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Task(rs.getObject("id", java.util.UUID.class),
                        rs.getString("type"), rs.getString("payload"), TaskStatus.valueOf(rs.getString("status")),
                        rs.getInt("attempts"), rs.getInt("max_attempts"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getTimestamp("next_run_at").toInstant());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementAttempts(UUID taskId) {
        String sql = "update tasks set attempts = attempts + 1 where id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, taskId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(UUID taskId, TaskStatus status) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setObject(2, taskId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void scheduleRetry(UUID taskUuid, int attempts) {
        long delaySeconds = (long) Math.pow(2, attempts);
        Instant nextRun = Instant.now().plusSeconds(delaySeconds);

        String sql = "update tasks set next_run_at = ?, status = 'PENDING' where id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.from(nextRun));
            ps.setObject(2, taskUuid);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}