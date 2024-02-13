package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/studentcourse")
public class StudentCourseServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Bygg upp HTML-strukturen med CSS direkt i HTML-filen
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<!DOCTYPE html>");
        htmlResponse.append("<html lang=\"en\">");
        htmlResponse.append("<head>");
        htmlResponse.append("<meta charset=\"UTF-8\">");
        htmlResponse.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlResponse.append("<title>Studenters Kurser</title>");

        // CSS-stilar inbäddade i HTML-filen
        htmlResponse.append("<style>");
        htmlResponse.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f2f2f2; }");
        htmlResponse.append("table { width: 80%; margin: 20px auto; border-collapse: collapse; }");
        htmlResponse.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        htmlResponse.append("th { background-color: #f2f2f2; }");
        htmlResponse.append("a { display: block; margin-bottom: 5px; padding: 10px; background-color: #4CAF50; color: white; text-align: center; text-decoration: none; }");
        htmlResponse.append("a:hover { background-color: #45a049; }");
        htmlResponse.append("</style>");

        htmlResponse.append("</head>");
        htmlResponse.append("<body>");

        // Navigationsmeny
        htmlResponse.append("<nav class=\"navtop\">");
        htmlResponse.append("<a href=\"index.html\">Hem</a>");
        htmlResponse.append("<a href=\"addcourse\">Lägg till kurser</a>");
        htmlResponse.append("<a href=\"course\">Kurser</a>");
        htmlResponse.append("<a href=\"addstudent\">Lägg till student</a>");
        htmlResponse.append("<a href=\"students\">Studenter</a>");
        htmlResponse.append("<a href=\"assigncourse\">Tillge kurs</a>");
        htmlResponse.append("<a href=\"studentcourse\">Studenters kurser</a>");
        htmlResponse.append("</nav>");

        // Skapa en anslutning till databasen
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");

            // Hämta data från databasen och skapa tabellen
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT students.Fname, students.Lname, courses.name FROM students " +
                    "INNER JOIN attendance ON students.id = attendance.student_id " +
                    "INNER JOIN courses ON attendance.course_id = courses.id");

            // Skriv ut tabellrubriker
            htmlResponse.append("<table>");
            htmlResponse.append("<tr><th>Förnamn</th><th>Efternamn</th><th>Kurs</th></tr>");

            // Loopa genom resultatsättet och skriv ut data i tabellen
            while (rs.next()) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>").append(rs.getString("Fname")).append("</td>");
                htmlResponse.append("<td>").append(rs.getString("Lname")).append("</td>");
                htmlResponse.append("<td>").append(rs.getString("name")).append("</td>");
                htmlResponse.append("</tr>");
            }
            htmlResponse.append("</table>");

            rs.close();
            stmt.close();
        } catch (Exception e) {
            // Visa felmeddelande om något går fel
            htmlResponse.append("<p>Error: ").append(e.getMessage()).append("</p>");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Visa felmeddelande om anslutningen inte kunde stängas
                htmlResponse.append("<p>Error: ").append(e.getMessage()).append("</p>");
            }
        }

        htmlResponse.append("</body>");
        htmlResponse.append("</html>");

        // Skriv ut HTML-strukturen till responsen
        out.println(htmlResponse.toString());
    }
}
