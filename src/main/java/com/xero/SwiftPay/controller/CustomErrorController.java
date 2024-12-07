package com.xero.SwiftPay.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Get the error code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred.";
        String errorCode = "500";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) {
                errorCode = "404";
                errorMessage = "Page not found!";
            } else if (statusCode == 500) {
                errorCode = "500";
                errorMessage = "Internal server error!";
            }
        }

        // Set the error attributes to display in the view
        request.setAttribute("errorCode", errorCode);
        request.setAttribute("errorMessage", errorMessage);

        return "error";  // Resolves to /WEB-INF/jsp/error.jsp
    }

}
