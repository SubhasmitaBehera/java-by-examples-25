package com.example.servlet;

import com.example.util.JWTUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/protected")
public class ProtectedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String authHeader = req.getHeader("Auth-Token");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            System.out.println("1");
            String token = authHeader.substring(7);
            if (JWTUtil.validateToken(token)){
                System.out.println("2");
                String username = JWTUtil.getUsername(token);
                res.getWriter().write("Welcome to protected endpoint, " +username + "!");
                return;
            }
        }
        res.setStatus(401);
        res.getWriter().write("Unauthorized: invalid or missing token.");
    }
}
