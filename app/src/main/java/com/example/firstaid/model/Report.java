package com.example.firstaid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Observable;

public class Report extends Observable implements Parcelable {
    private final String start;
    private String finish;
    private String location;

    private final ArrayList<String> question;
    private final ArrayList<String> answer;
    private final ArrayList<String> qStart;
    private final ArrayList<String> qFinish;

    public Report(String start, String location) {
        this.start = start;
        this.location = location;
        finish = "Unfinished";
        question = new ArrayList<>();
        answer = new ArrayList<>();
        qStart = new ArrayList<>();
        qFinish = new ArrayList<>();
    }

    public void addQuestionData(String question, String answer, String start, String finish){
        this.question.add(question);
        this.answer.add(answer);
        this.qStart.add(start);
        this.qFinish.add(finish);
        update();
    }

    private void update() {
        setChanged();
        notifyObservers();
    }

    public String getStart() {
        return start;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public ArrayList<String> getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public ArrayList<String> getqStart() {
        return qStart;
    }

    public ArrayList<String> getqFinish() {
        return qFinish;
    }

    private Report(Parcel in) {
        start = in.readString();
        finish = in.readString();
        location = in.readString();
        question = in.createStringArrayList();
        answer = in.createStringArrayList();
        qStart = in.createStringArrayList();
        qFinish = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(start);
        dest.writeString(finish);
        dest.writeString(location);
        dest.writeStringList(question);
        dest.writeStringList(answer);
        dest.writeStringList(qStart);
        dest.writeStringList(qFinish);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
