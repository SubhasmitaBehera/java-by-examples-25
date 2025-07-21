package com.example.servlet;

import com.example.dtos.SignupDto;
import com.example.util.DBUtil;
import com.google.gson.Gson;
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
        SignupDto signupDto = new Gson().fromJson(req.getReader(), SignupDto.class);
        String username = signupDto.getUsername();
        String password = signupDto.getPassword();

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
