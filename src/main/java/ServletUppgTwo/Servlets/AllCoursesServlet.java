package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/course")
public class AllCoursesServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Skapa en anslutning till databasen
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");

            // Hämta data från databasen och skapa tabellen
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Namn</th><th>YHP</th><th>Beskrivning</th></tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getInt("YHP") + "</td>");
                out.println("<td>" + rs.getString("description") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            out.println("<a href=\"index.html\">Hem</a>");
            out.println("<a href=\"addcourse\">Lägg till kurser</a>");
            out.println("<a href=\"course\">Kurser</a>");
            out.println("<a href=\"addstudent\">Lägg till student");
            out.println("<a href=\"students\">Studenter");
            out.println("<a href=\"assigncourse\">Tillge kurs");
            out.println("<a href=\"studentcourse\">Studenters kurser");

            rs.close();
            stmt.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                out.println("Error: " + e.getMessage());
            }
        }
    }
}