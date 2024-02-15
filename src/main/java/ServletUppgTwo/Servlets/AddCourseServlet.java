package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/addcourse")
public class AddCourseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Bygg upp HTML-strukturen med inbäddade CSS-stilar
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<!DOCTYPE html>");
        htmlResponse.append("<html>");
        htmlResponse.append("<head>");
        htmlResponse.append("<title>Lägg till ny kurs</title>");

        // CSS-stilar inbäddade i HTML-filen
        htmlResponse.append("<style>");
        htmlResponse.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f2f2f2; }");
        htmlResponse.append("h2 { text-align: center; margin-top: 20px; }");
        htmlResponse.append("form { width: 50%; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.1); }");
        htmlResponse.append("label { display: block; margin-bottom: 10px; }");
        htmlResponse.append("input[type=\"text\"], input[type=\"number\"], textarea, input[type=\"submit\"] { width: 100%; padding: 10px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 5px; }");
        htmlResponse.append("input[type=\"submit\"] { background-color: #4CAF50; color: white; cursor: pointer; }");
        htmlResponse.append("input[type=\"submit\"]:hover { background-color: #45a049; }");
        htmlResponse.append("</style>");

        htmlResponse.append("</head>");
        htmlResponse.append("<body>");

        htmlResponse.append("<h2>Lägg till ny kurs</h2>");
        htmlResponse.append("<form method=\"post\">");
        htmlResponse.append("<label for=\"name\">Kursnamn:</label>");
        htmlResponse.append("<input type=\"text\" id=\"name\" name=\"name\" required><br>");
        htmlResponse.append("<label for=\"yhp\">YHP:</label>");
        htmlResponse.append("<input type=\"number\" id=\"yhp\" name=\"yhp\" required><br>");
        htmlResponse.append("<label for=\"description\">Beskrivning:</label><br>");
        htmlResponse.append("<textarea id=\"description\" name=\"description\" required></textarea><br>");
        htmlResponse.append("<input type=\"submit\" value=\"Lägg till kurs\">");
        htmlResponse.append("</form>");

        htmlResponse.append("</body>");
        htmlResponse.append("</html>");

        // Skriv ut HTML-strukturen till responsen
        out.println(htmlResponse.toString());
    }

    @Override
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

            response.sendRedirect("index.html");
            // Stäng PreparedStatement
            pstmt.close();
        } catch (Exception e) {
            // Hantera eventuella fel och visa meddelande
            out.println("<p>Fel: " + e.getMessage() + "</p>");
        } finally {
            try {
                // Stäng anslutningen till databasen
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Hantera eventuella fel och visa meddelande
                out.println("<p>Fel: " + e.getMessage() + "</p>");
            }
        }
    }
}