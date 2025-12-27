package queue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/task_queue";
    private static final String USER = "queue_user";
    private static final String PASSWORD = "queue_pass";

    public void save(Task task) {
        String sql = "insert into tasks (id, type, payload, status, attempts, created_at) "
                + "values (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, task.getUuid());
            ps.setString(2, task.getType());
            ps.setString(3, task.getPayload());
            ps.setString(4, task.getStatus().name());
            ps.setInt(5, 0);
            ps.setTimestamp(6, Timestamp.from(task.getCreatedAt()));

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
                Task task = new Task(rs.getString("type"), rs.getString("payload"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return tasks;
    }
}