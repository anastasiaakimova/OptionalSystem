import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        int status = 0;
        int courseName;
        String studentName;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Драйвер подключен");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/optional",
                    "root", "123456");
            System.out.println("Соединение установлено.");
            st = con.createStatement();

            System.out.println("Вас приветствует система факультатив!");

            showMenu();

            while (status != 5) {
                status = scanner.nextInt();
                switch (status) {
                    case 1:
                        System.out.println("Просмотр доступных курсов и ведущих преподавателей:" + "\n");
                        rs = st.executeQuery("SELECT course.id, courseName, nameOfTeacher FROM course \n" +
                                "INNER JOIN teacher ON teacher.id = course.id");
                        while (rs.next()) {
                            System.out.println("\t" + rs.getInt("course.id") + "\t"
                                    + rs.getString("courseName")
                                    + "\t" + rs.getString("nameOfTeacher"));
                        }
                        showMenu();
                        break;
                    case 2:
                        System.out.println("Просмотр записанных студентов на курс:" + "\n");
                        rs = st.executeQuery("SELECT courseName, studentName FROM course \n" +
                                "INNER JOIN student ON student.id = course.id");
                        while (rs.next()) {
                            System.out.println("\t" + rs.getString("courseName")
                                    + "\t" + rs.getString("studentName"));
                        }
                        // add update
                        showMenu();
                        break;
                    case 3:
                        System.out.println("Просмотр рейтинга студентов " + "\n");
                        rs = st.executeQuery("SELECT studentName, assessment FROM student \n" +
                                "INNER JOIN archive ON student.id = id_student");
                        while (rs.next()) {
                            System.out.println("\t" + rs.getString("studentName")
                                    + "\t" + rs.getInt("assessment"));
                        }
                        showMenu();
                        break;
                    case 4:
                        System.out.println("Запись на курс:");
                        System.out.println("Введите фамилию и инициалы");
                        studentName = scanner2.nextLine();
                        // choice of course numbers
                        System.out.println("Возможные курсы:");
                        rs = st.executeQuery("SELECT course.id, courseName, nameOfTeacher FROM course \n" +
                                "INNER JOIN teacher ON teacher.id = course.id");
                        while (rs.next()) {
                            System.out.println("\t" + rs.getInt("course.id") + "\t"
                                    + rs.getString("courseName")
                                    + "\t" + rs.getString("nameOfTeacher"));
                        }
                        System.out.println("Введите номер курса:");
                        courseName = scanner.nextInt();
                        String query = "INSERT INTO student (studentName,id_cource) \n" +
                                " VALUES (' " + studentName + "', '" + courseName + "')";
                        st.executeUpdate(query);
                        System.out.println("Вы записаны на курс!");
                        showMenu();
                        break;
                    case 5:
                        System.out.println("До новых встреч!");
                        break;
                    default:
                        System.out.println("Не верный ввод!");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
                scanner.close();
                scanner2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMenu() {
        System.out.println("Вы можете:" + "\n" +
                "1 - Просмотр доступных курсов и ведущих преподавателей" + "\n" +
                "2 - Просмотр записанных студентов на курс" + "\n" +
                "3 - Просмотр рейтинга студентов " + "\n" +
                "4 - Запись на курс  " + "\n" +
                "5 - Выйти" + "\n");
    }
}
