package com.gigantdevs.todolist.models;

public class TodoItem {
    private String text;
    private String creator;
    private String lastModificator;
    private String date;
    private boolean done;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastModificator() {
        return lastModificator;
    }

    public void setLastModificator(String lastModificator) {
        this.lastModificator = lastModificator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
