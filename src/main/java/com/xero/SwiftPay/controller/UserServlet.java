package com.xero.SwiftPay.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xero.SwiftPay.dao.UserDao;
import com.xero.SwiftPay.model.User;

public class UserServlet extends HttpServlet {

    private UserDao userDAO;

    @Override
    public void init() {
        userDAO = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("login")) {
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else if (action.equals("register")) {
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        } else if (action.equals("profile")) {
            String username = (String) request.getSession().getAttribute("username");
            User user = userDAO.getUser(username);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("register")) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Create new user and save to DB
            userDAO.registerUser(new User(username, email, password));

            request.setAttribute("message", "Registration successful!");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

        } else if (action.equals("login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (userDAO.authenticate(username, password)) {
                request.getSession().setAttribute("username", username);
                response.sendRedirect("user?action=profile");
            } else {
                request.setAttribute("error", "Invalid credentials!");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }

        } else if (action.equals("logout")) {
            request.getSession().invalidate();
            response.sendRedirect("user?action=login");
        }
    }
}
    