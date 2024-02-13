package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AssignCourseServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Öppna HTML-formuläret för att associera en student med en kurs
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Assign Course</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Assign Course to Student</h2>");
        out.println("<form action=\"AssignCourseServlet\" method=\"post\">");

        // Hämta och visa alla studenter från databasen i en dropdown-lista
        out.println("<label for=\"student\">Välj student:</label>");
        out.println("<select name=\"student\" id=\"student\">");

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                out.println("<option value=\"" + rs.getInt("id") + "\">" + rs.getString("Fname") + " " + rs.getString("Lname") + "</option>");
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            out.println("<option disabled selected>Inga studenter tillgängliga</option>");
            out.println("<p>Fel: " + e.getMessage() + "</p>");
        }
        out.println("</select><br>");

        // Hämta och visa alla kurser från databasen i en dropdown-lista
        out.println("<label for=\"course\">Välj kurs:</label>");
        out.println("<select name=\"course\" id=\"course\">");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");
            while (rs.next()) {
                out.println("<option value=\"" + rs.getInt("id") + "\">" + rs.getString("name") + "</option>");
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            out.println("<option disabled selected>Inga kurser tillgängliga</option>");
            out.println("<p>Fel: " + e.getMessage() + "</p>");
        }
        out.println("</select><br>");

        // Lägg till en knapp för att skicka formuläret
        out.println("<input type=\"submit\" value=\"Assign Course\">");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hantera postdata för att associera student med kurs och lägg till det i databasen
        int studentId = Integer.parseInt(request.getParameter("student"));
        int courseId = Integer.parseInt(request.getParameter("course"));

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO attendance (student_id, course_id) VALUES (?, ?)");
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
            pstmt.close();

            // Skicka om användaren tillbaka till samma sida (doGet-metoden) efter lyckad insättning
            response.sendRedirect("AssignCourseServlet");
        } catch (SQLException | ClassNotFoundException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<p>Fel: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorera
            }
        }
    }
}