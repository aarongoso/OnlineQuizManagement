/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.onlinequizmanagement.service;

import com.mycompany.onlinequizmanagement.model.Quiz;
import com.mycompany.onlinequizmanagement.model.Question;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aaron
 */
// Service class for managing quizzes and their questions
// Includes methods for CRUD operations and validation logic
public class QuizService {

    private static List<Quiz> quizzes = new ArrayList<>(); // List of quizzes
    private static int nextId = 1; // Counter for generating unique quiz IDs

    // Create a new quiz
    public Quiz createQuiz(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        Quiz quiz = new Quiz(nextId++, title);
        quizzes.add(quiz);
        return quiz;
    }

    // Retrieve a quiz by ID
    public Quiz getQuizById(int id) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId() == id) {
                return quiz;
            }
        }
        return null; // Return null if no matching quiz is found
    }

    // Retrieve all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizzes;
    }

    // Add a question to a quiz
    public void addQuestionToQuiz(int quizId, Question question) {
        Quiz quiz = getQuizById(quizId);
        if (quiz != null) {
            quiz.addQuestion(question);
        }
    }

    // Delete a quiz by ID
    public boolean deleteQuiz(int quizId) {
        Quiz quiz = getQuizById(quizId);
        if (quiz != null) {
            quizzes.remove(quiz);
            return true;
        }
        return false;
    }

    // Validate a questions fields
    public boolean validateQuestion(Question question) {
        return question.getQuestionText() != null && !question.getQuestionText().isEmpty()
                && question.getChoices() != null && question.getChoices().length > 0
                && question.getCorrectAnswer() != null && !question.getCorrectAnswer().isEmpty();
    }

    // Calculate the score for submitted answers
    public int calculateScore(Quiz quiz, List<String> submittedAnswers) {
        if (quiz.getQuestions().size() != submittedAnswers.size()) {
            return -1; // Return -1 if the number of answers doesnt match the questions
        }
        int score = 0;
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (quiz.getQuestions().get(i).getCorrectAnswer().equals(submittedAnswers.get(i))) {
                score++;
            }
        }
        return score;
    }
}
