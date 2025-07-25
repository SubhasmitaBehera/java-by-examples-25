package com.example.servlet;

import com.example.dtos.SignupDto;
import com.example.service.AuthService;
import com.example.util.DBUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("username - "+username);
        System.out.println("password - "+password);
        try {
            authService.register(username,password);
            res.getWriter().write("User registered successfully!");
        } catch (Exception e) {
            res.setStatus(500);
            res.getWriter().write("Registration failed.");
        }
    }
}
