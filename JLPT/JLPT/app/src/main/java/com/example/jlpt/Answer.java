package com.example.jlpt;

public class Answer {
    private String content;
    private Boolean isCorrect;

    public Answer(String content, Boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }


    public boolean isCorrect() {
        return isCorrect;
    }
}
