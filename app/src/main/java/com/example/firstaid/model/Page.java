package com.example.firstaid.model;

public class Page {
    private final int textId;
    private Choice choice1;
    private Choice choice2;
    private boolean singleButton = false;


    public Page(int textId) {
        this.textId = textId;
        singleButton = true;
    }

    public Page(int textId, Choice choice2) {
        this.textId = textId;
        this.choice2 = choice2;
        singleButton = true;
    }

    public Page(int textId, Choice choice1, Choice choice2) {
        this.textId = textId;
        this.choice1 = choice1;
        this.choice2 = choice2;
    }

    public int getTextId() {
        return textId;
    }

    public Choice getChoice1() {
        return choice1;
    }

    public Choice getChoice2() {
        return choice2;
    }

    public boolean isSingleButton() {
        return singleButton;
    }

}
