package com.example.servlet;

import com.example.util.DBUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            boolean created = DBUtil.createUser(username,hashed);
            res.setContentType("application/json");
            res.getWriter().write("{\"success\": " + created + "}");
        } catch (SQLException e) {
            res.setStatus(500);
            res.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}
