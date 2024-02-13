package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet(urlPatterns = "/assigncourse")
public class AssignCourseServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Bygg upp HTML-strukturen med inbäddade CSS-stilar
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<!DOCTYPE html>");
        htmlResponse.append("<html>");
        htmlResponse.append("<head>");
        htmlResponse.append("<title>Assign Course</title>");

        // CSS-stilar inbäddade i HTML-filen
        htmlResponse.append("<style>");
        htmlResponse.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f2f2f2; }");
        htmlResponse.append("h2 { color: #333; }");
        htmlResponse.append("form { width: 50%; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.1); }");
        htmlResponse.append("label { display: block; margin-bottom: 10px; }");
        htmlResponse.append("select, input[type=\"submit\"] { width: 100%; padding: 10px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 5px; }");
        htmlResponse.append("input[type=\"submit\"] { background-color: #4CAF50; color: white; cursor: pointer; }");
        htmlResponse.append("input[type=\"submit\"]:hover { background-color: #45a049; }");
        htmlResponse.append("</style>");

        htmlResponse.append("</head>");
        htmlResponse.append("<body>");

        htmlResponse.append("<h2>Assign Course to Student</h2>");
        htmlResponse.append("<form method=\"post\">");

        // Hämta och visa alla studenter från databasen i en dropdown-lista
        htmlResponse.append("<label for=\"student\">Välj student:</label>");
        htmlResponse.append("<select name=\"student\" id=\"student\">");

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                htmlResponse.append("<option value=\"" + rs.getInt("id") + "\">" + rs.getString("Fname") + " " + rs.getString("Lname") + "</option>");
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            // Hantera eventuella fel och visa meddelande
            htmlResponse.append("<option disabled selected>Inga studenter tillgängliga</option>");
            htmlResponse.append("<p class=\"error\">Fel: " + e.getMessage() + "</p>");
        }
        htmlResponse.append("</select><br>");

        // Hämta och visa alla kurser från databasen i en dropdown-lista
        htmlResponse.append("<label for=\"course\">Välj kurs:</label>");
        htmlResponse.append("<select name=\"course\" id=\"course\">");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");
            while (rs.next()) {
                htmlResponse.append("<option value=\"" + rs.getInt("id") + "\">" + rs.getString("name") + "</option>");
            }
            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            // Hantera eventuella fel och visa meddelande
            htmlResponse.append("<option disabled selected>Inga kurser tillgängliga</option>");
            htmlResponse.append("<p class=\"error\">Fel: " + e.getMessage() + "</p>");
        }
        htmlResponse.append("</select><br>");

        // Lägg till en knapp för att skicka formuläret
        htmlResponse.append("<input type=\"submit\" value=\"Assign Course\">");
        htmlResponse.append("</form>");

        htmlResponse.append("</body>");
        htmlResponse.append("</html>");

        // Skriv ut HTML-strukturen till responsen
        out.println(htmlResponse.toString());
    }

    @Override
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
            response.sendRedirect("index.html");
        } catch (SQLException | ClassNotFoundException e) {
            // Hantera eventuella fel och visa meddelande
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<p class=\"error\">Fel: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignorera
            }
        }
    }
}