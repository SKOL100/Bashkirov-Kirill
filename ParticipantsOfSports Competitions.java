import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:sports_competitions.db";

    // Метод для подключения к базе данных
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Метод для создания таблицы "competitions"
    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS competitions (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    name TEXT NOT NULL,\n"
                + "    date TEXT NOT NULL,\n"
                + "    location TEXT NOT NULL\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод для добавления новой записи о соревновании
    public void insertCompetition(String name, String date, String location) {
        String sql = "INSERT INTO competitions(name, date, location) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setString(3, location);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод для получения всех записей о соревнованиях
    public void selectAllCompetitions() {
        String sql = "SELECT id, name, date, location FROM competitions";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Вывод результатов
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("date") + "\t" +
                        rs.getString("location"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод для удаления записи о соревновании по ID
    public void deleteCompetition(int id) {
        String sql = "DELETE FROM competitions WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        // Создаем таблицу
        dbManager.createNewTable();

        // Добавляем соревнования
        dbManager.insertCompetition("Football Championship", "2024-06-07", "Moscow");
        dbManager.insertCompetition("Basketball Tournament", "2024-07-10", "Saint Petersburg");

        // Получаем и выводим все соревнования
        dbManager.selectAllCompetitions();

        // Удаляем соревнование с ID 1
        dbManager.deleteCompetition(1);

        // Получаем и выводим все соревнования после удаления
        dbManager.selectAllCompetitions();
    }
}