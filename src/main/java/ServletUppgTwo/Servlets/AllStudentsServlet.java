package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/students")
public class AllStudentsServlet extends HttpServlet {
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Förnamn</th><th>Efternamn</th><th>Stad</th><th>Hobby</th></tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("Fname") + "</td>");
                out.println("<td>" + rs.getString("Lname") + "</td>");
                out.println("<td>" + rs.getString("city") + "</td>");
                out.println("<td>" + rs.getString("hobby") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // Lägg till länkar till andra Servlets och startsidan
            out.println("<a href=\"index.html\">Till startsidan</a>");
            out.println("<br>");
            out.println("<a href=\"studentcourse\">Visa alla studenter och kurser</a>");
            out.println("<br>");
            out.println("<a href=\"course\">Visa alla kurser</a>");
            out.println("<br>");
            out.println("<a href=\"addstudent\">Lägg till student");
            out.println("<br>");
            out.println("<a href=\"addcourse\">Lägg till kurs");

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