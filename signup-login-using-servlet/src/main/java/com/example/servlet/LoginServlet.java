package com.example.servlet;

import com.example.service.AuthService;
import com.example.util.DBUtil;
import com.example.util.JWTUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String token = authService.login(username,password);
            if (token != null){
                res.setHeader("Auth-token", token);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("token", token);
                jsonObject.addProperty("msg", "Login successful!");
                res.getWriter().write(String.valueOf(jsonObject));
            }
            else {
                res.setStatus(401);
                res.getWriter().write("Invalid credentials.");
            }
        } catch (Exception e){
            res.setStatus(500);
            res.getWriter().write("Login failed.");
        }
    }
}
