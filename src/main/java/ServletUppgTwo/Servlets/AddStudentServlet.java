package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/addstudent")
public class AddStudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Bygg upp HTML-strukturen med inbäddade CSS-stilar
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<!DOCTYPE html>");
        htmlResponse.append("<html lang=\"en\">");
        htmlResponse.append("<head>");
        htmlResponse.append("<meta charset=\"UTF-8\">");
        htmlResponse.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlResponse.append("<title>Lägg till ny student</title>");

        // CSS-stilar inbäddade i HTML-filen
        htmlResponse.append("<style>");
        htmlResponse.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f2f2f2; }");
        htmlResponse.append("h2 { text-align: center; margin-top: 20px; }");
        htmlResponse.append("form { max-width: 400px; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        htmlResponse.append("label { display: block; margin-bottom: 10px; }");
        htmlResponse.append("input[type=\"text\"] { width: 100%; padding: 8px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ccc; }");
        htmlResponse.append("input[type=\"submit\"] { width: 100%; padding: 10px; background-color: #4CAF50; color: white; border: none; border-radius: 5px; cursor: pointer; }");
        htmlResponse.append("input[type=\"submit\"]:hover { background-color: #45a049; }");
        htmlResponse.append("</style>");

        htmlResponse.append("</head>");
        htmlResponse.append("<body>");

        htmlResponse.append("<h2>Lägg till ny student</h2>");
        htmlResponse.append("<form method=\"post\">");
        htmlResponse.append("<label for=\"fname\">Förnamn:</label>");
        htmlResponse.append("<input type=\"text\" id=\"fname\" name=\"fname\" required><br>");
        htmlResponse.append("<label for=\"lname\">Efternamn:</label>");
        htmlResponse.append("<input type=\"text\" id=\"lname\" name=\"lname\" required><br>");
        htmlResponse.append("<input type=\"submit\" value=\"Lägg till student\">");
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
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");

        Connection conn = null;
        try {
            // Anslut till databasen
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");

            // Skapa SQL-fråga för att lägga till studenten
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (Fname, Lname) VALUES (?, ?)");
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.executeUpdate();

            response.sendRedirect("index.html"); // Skicka tillbaka till startsidan efter lyckad insättning
        } catch (Exception e) {
            // Hantera eventuella fel och visa meddelande
            out.println("Fel: " + e.getMessage());
        } finally {
            try {
                // Stäng anslutningen till databasen
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Hantera eventuella fel och visa meddelande
                out.println("Fel: " + e.getMessage());
            }
        }
    }
}