package com.example.servlet;

import com.example.util.DBUtil;
import com.example.util.JWTUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String storedHash = DBUtil.getPasswordHash(username);
            if (storedHash != null && BCrypt.checkpw(password, storedHash)){
                String token = JWTUtil.generateToken(username);
                res.setHeader("Authorization", "Bearer" + token);
                res.setContentType("application/json");
                res.getWriter().write("{\"message\": \"Login succesful\"}");
            }
            else {
                res.setStatus(401);
                res.getWriter().write("{\"error\": \"Invalid credentials\"}");
            }
        } catch (SQLException e){
            res.setStatus(500);
            res.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}
