/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.onlinequizmanagement.auth;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 * @author aaron
 */
// Authorization filter for controlling access to endpoints 
// Students can view available quizzes, 
// view full quizzes and choices by id,
// and submit answers all without an API key
// Teachers/admins require an API key for their actions
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final String API_KEY = "teacher123"; // Api key for admin access

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        // Allow students to view quizzes and submit answers without authentication
        if ((path.startsWith("quizzes/") && path.endsWith("/student-view") && method.equals("GET"))
                || (path.equals("quizzes/student-quizzes") && method.equals("GET"))
                || (path.startsWith("quizzes/") && path.endsWith("/submit") && method.equals("POST"))) {
            return; // Skip authentication for student actions
        }

        // Validate API key for all other actions (Admin only)
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.equals(API_KEY)) {
            // Respond with a 403 Forbidden error for invalid or missing API key
            requestContext.abortWith(
                    jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.FORBIDDEN)
                            .entity("{\"error\": \"Access denied. Invalid or missing API key.\"}")
                            .type("application/json")
                            .build()
            );
        }
    }
}
