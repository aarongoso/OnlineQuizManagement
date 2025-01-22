/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.onlinequizmanagement.resources;

import com.mycompany.onlinequizmanagement.model.Question;
import com.mycompany.onlinequizmanagement.model.Quiz;
import com.mycompany.onlinequizmanagement.service.QuizService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aaron
 */
// Resource class to manage quiz-related endpoints
// Handles creating, updating, retrieving, and deleting quizzes
@Path("/quizzes")
public class QuizResource {

    // Service instance to handle business logic
    private QuizService quizService = new QuizService();

    // Retrieve all quizzes (Admin only)
    // Returns a list of all quizzes, including their questions and answers
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllQuizzes() {
        // Retrieve all quizzes from the service
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return Response.ok(quizzes).build();
    }

    // Create a new quiz (Admin only)
    // Validates the input and returns a success or error message
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuiz(Quiz quiz) {
        if (quiz.getTitle() == null || quiz.getTitle().trim().isEmpty()) {
            // Return error if quiz title is invalid
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Quiz title cannot be null or empty\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Create quiz using the service and return success message
        Quiz createdQuiz = quizService.createQuiz(quiz.getTitle());
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Quiz created successfully\", \"quizId\": " + createdQuiz.getId() + "}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    // Retrieve a single quiz by ID (Admin only)
    // Includes questions and answers in the response
    @GET
    @Path("/{quizId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuiz(@PathParam("quizId") int quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz == null) {
            // Return 404 error if quiz not found
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Return the quiz details
        return Response.ok(quiz, MediaType.APPLICATION_JSON).build();
    }

    // Add a question to an existing quiz (Admin only)
    // Validates the input and returns a success or error message
    @POST
    @Path("/{quizId}/questions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addQuestion(@PathParam("quizId") int quizId, Question question) {
        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz == null) {
            // Return 404 error if quiz not found
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Validate the question
        boolean isValid = quizService.validateQuestion(question);
        if (!isValid) {
            // Return 400 error for invalid question data
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid question data\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Add the question to the quiz
        quizService.addQuestionToQuiz(quizId, question);
        return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Question added successfully\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    // Retrieve all available quizzes for students (without]answers)
    @GET
    @Path("/student-quizzes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableQuizzesForStudents() {
        // Retrieve all quizzes
        List<Quiz> quizzes = quizService.getAllQuizzes();

        // Check if the list is empty and return a meaningful error
        if (quizzes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"No quizzes available\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Prepare a list for student-friendly quiz details
        List<String[]> availableQuizzes = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            String[] quizDetails = {"Quiz ID: " + quiz.getId(), "Title: " + quiz.getTitle()};
            availableQuizzes.add(quizDetails);
        }

        // Return the list of quizzes
        return Response.ok(availableQuizzes, MediaType.APPLICATION_JSON).build();
    }

    // Retrieve a specific quiz for students (without answers)
    @GET
    @Path("/{quizId}/student-view")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuizForStudent(@PathParam("quizId") int quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        // Check if the quiz exists
        if (quiz == null) {
            // Return a 404 Not Found error
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Build a student friendly view of the quiz
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{ \"id\": ").append(quiz.getId())
                .append(", \"title\": \"").append(quiz.getTitle()).append("\", ")
                .append("\"questions\": [");

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            jsonResponse.append("{ \"questionText\": \"").append(question.getQuestionText()).append("\", ")
                    .append("\"choices\": ").append(java.util.Arrays.toString(question.getChoices())).append(" }");

            if (i < quiz.getQuestions().size() - 1) {
                jsonResponse.append(", ");
            }
        }

        jsonResponse.append("] }");
        return Response.ok(jsonResponse.toString(), MediaType.APPLICATION_JSON).build();

    }

    // Delete a quiz by ID (Admin only)
    // Returns a success or error message if the quiz does not exist
    @DELETE
    @Path("/{quizId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteQuiz(@PathParam("quizId") int quizId) {
        boolean isDeleted = quizService.deleteQuiz(quizId);
        if (!isDeleted) {
            // Return 404 error if quiz not found
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Return success message
        return Response.ok("{\"message\": \"Quiz deleted successfully\"}", MediaType.APPLICATION_JSON).build();
    }

    // Update an existing quiz (Admin only)
    // Validates the input and updates the quiz details
    @PUT
    @Path("/{quizId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuiz(@PathParam("quizId") int quizId, Quiz updatedQuiz) {
        Quiz existingQuiz = quizService.getQuizById(quizId);
        if (existingQuiz == null) {
            // Return 404 error if quiz not found
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (updatedQuiz.getTitle() == null || updatedQuiz.getTitle().isEmpty()) {
            // Return 400 error for invalid title
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Quiz title cannot be empty\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        // Update the quiz title
        existingQuiz.setTitle(updatedQuiz.getTitle());
        return Response.ok("{\"message\": \"Quiz updated successfully\"}", MediaType.APPLICATION_JSON).build();
    }

    // Submit answers for a quiz and receive feedback (Student functionality)
    @POST
    @Path("/{quizId}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitAnswers(@PathParam("quizId") int quizId, List<String> submittedAnswers) {
        Quiz quiz = quizService.getQuizById(quizId);

        // Check if quiz exists
        if (quiz == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Quiz not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Validate the answers and calculate the score
        int totalQuestions = quiz.getQuestions().size();
        int score = quizService.calculateScore(quiz, submittedAnswers);

        // Check if the number of submitted answers matches the number of questions
        if (score == -1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Number of answers does not match the number of questions\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Calculate the percentage score
        double percentageScore = (double) score / totalQuestions * 100;

        // Build the detailed feedback response
        String responseJson = "{"
                + "\"message\": \"Quiz submitted successfully\", "
                + "\"score\": " + score + ", "
                + "\"totalQuestions\": " + totalQuestions + ", "
                + "\"percentage\": " + percentageScore + "}";
        return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
    }
}
