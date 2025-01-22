package com.mycompany.onlinequizmanagement.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aaron
 */
// Model class representing a quiz
// Includes the quiz ID, title, and a list of associated questions


public class Quiz {
    
    public Quiz() {
    // Default constructor
}

    private int id;  // Unique identifier for the quiz
    private String title;
    private List<Question> questions; // List of questions in the quiz

    // Constructor to initialize quiz details
    public Quiz(int id, String title) {
        this.id = id;
        this.title = title;
        this.questions = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
}
