package ServletUppgTwo.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet (urlPatterns = "/addstudent")
public class AddStudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Lägg till ny student</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Lägg till ny student</h2>");
        out.println("<form action=\"AddStudentServlet\" method=\"post\">");
        out.println("<label for=\"fname\">Förnamn:</label>");
        out.println("<input type=\"text\" id=\"fname\" name=\"fname\" required><br>");
        out.println("<label for=\"lname\">Efternamn:</label>");
        out.println("<input type=\"text\" id=\"lname\" name=\"lname\" required><br>");
        out.println("<input type=\"submit\" value=\"Lägg till student\">");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testgritacademy", "User", "");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (Fname, Lname) VALUES (?, ?)");
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.executeUpdate();

            response.sendRedirect("index.html"); // Skicka tillbaka till startsidan efter lyckad insättning


        } catch (Exception e) {
            out.println("Fel: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                out.println("Fel: " + e.getMessage());
            }
        }
    }
}