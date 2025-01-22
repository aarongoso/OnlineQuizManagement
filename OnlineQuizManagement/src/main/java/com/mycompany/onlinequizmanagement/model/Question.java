/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlinequizmanagement.model;

import java.util.Arrays;

/**
 *
 * @author aaron
 */
// Model class representing a single quiz question
// Includes the question text, multiple-choice options, and the correct answer
public class Question {

    private String questionText;
    private String[] choices; // Array of choices for the question
    private String correctAnswer;
    
    // Default constructor (required for deserialization)
    public Question() {
    }

    // Constructor for initializing question details
    public Question(String questionText, String[] choices, String correctAnswer) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    // Getters and setters for accessing and modifying question details
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Question{"
                + "questionText='" + questionText + '\''
                + ", choices=" + Arrays.toString(choices)
                + ", correctAnswer='" + correctAnswer + '\''
                + '}';
    }
}
