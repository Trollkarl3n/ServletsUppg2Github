package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet (urlPatterns = "/addcourse")
public class AddCourseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Lägg till ny kurs</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Lägg till ny kurs</h2>");
        out.println("<form action=\"AddCourseServlet\" method=\"post\">");
        out.println("<label for=\"name\">Kursnamn:</label>");
        out.println("<input type=\"text\" id=\"name\" name=\"name\" required><br>");
        out.println("<label for=\"yhp\">YHP:</label>");
        out.println("<input type=\"number\" id=\"yhp\" name=\"yhp\" required><br>");
        out.println("<label for=\"description\">Beskrivning:</label><br>");
        out.println("<textarea id=\"description\" name=\"description\" required></textarea><br>");
        out.println("<input type=\"submit\" value=\"Lägg till kurs\">");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Hämta data från formuläret
        String name = request.getParameter("name");
        int yhp = Integer.parseInt(request.getParameter("yhp"));
        String description = request.getParameter("description");

        Connection conn = null;
        try {
            // Anslut till databasen
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");

            // Skapa SQL-fråga för att lägga till kursen
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO courses (name, YHP, description) VALUES (?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setInt(2, yhp);
            pstmt.setString(3, description);

            // Utför SQL-frågan för att lägga till kursen
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                out.println("<p>Kursen har lagts till framgångsrikt.</p>");
            } else {
                out.println("<p>Det uppstod ett fel. Kursen kunde inte läggas till.</p>");
            }

            // Stäng PreparedStatement
            pstmt.close();
        } catch (Exception e) {
            out.println("<p>Fel: " + e.getMessage() + "</p>");
        } finally {
            try {
                // Stäng anslutningen till databasen
                if (conn != null) conn.close();
            } catch (SQLException e) {
                out.println("<p>Fel: " + e.getMessage() + "</p>");
            }
        }
    }
}