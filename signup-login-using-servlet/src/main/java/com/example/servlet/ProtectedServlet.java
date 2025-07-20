package com.example.servlet;

import com.example.util.JWTUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/protected")
public class ProtectedServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String authHeader = req.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            res.setStatus(401);
            res.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }
        String token = authHeader.substring(7);
        try {
            String username = JWTUtil.validateToken(token);
            res.setContentType("application/json");
            res.getWriter().write("{\"message\": \"Hello, " + username + "! This is protected data.\"}");
        } catch (Exception e){
            res.setStatus(401);
            res.getWriter().write("{\"error\": \"Invalid or expired token\"}");
        }
    }
}
