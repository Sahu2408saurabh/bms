package com.bloodbank.bms.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        String errorTitle = "Oops! Something went wrong";
        String errorMessage = "We encountered an unexpected error. Please try again.";
        String errorCode = "UNKNOWN_ERROR";
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorCode = String.valueOf(statusCode);
            
            // Set appropriate messages based on status code
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle = "Page Not Found";
                errorMessage = "The page you're looking for doesn't exist or has been moved.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle = "Access Denied";
                errorMessage = "You don't have permission to access this page.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorTitle = "Server Error";
                errorMessage = "Something went wrong on our server. We're working to fix it.";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                errorTitle = "Bad Request";
                errorMessage = "The request could not be understood by the server.";
            }
        }

        // Add data to model
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("requestedURI", uri);
        model.addAttribute("title", "Error - Blood Bank");

        return "error-page";
    }

    public String getErrorPath() {
        return "/error";
    }
}