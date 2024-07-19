package com.example.firstaid.model;

public class Choice {
    private final int textId; //text on button
    private final int nextPage; //which page to go to when clicking button

    public Choice(int textId, int nextPage) {
        this.textId = textId;
        this.nextPage = nextPage;
    }

    public int getTextId() {
        return textId;
    }

    public int getNextPage() {
        return nextPage;
    }
}
