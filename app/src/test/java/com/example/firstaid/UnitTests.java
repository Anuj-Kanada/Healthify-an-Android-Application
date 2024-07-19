package com.example.firstaid;

import com.example.firstaid.model.Page;
import com.example.firstaid.model.Protocol;
import com.example.firstaid.model.Report;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UnitTests {
    @Test
    public void report_isCreated() {
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);

        assertEquals("Test Location", report.getLocation());
    }

    @Test
    public void startTime_isStored() {
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);

        assertEquals(currentTime.toString(), report.getStart());
    }

    @Test
    public void question_isStored(){
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);


        String question = "Name?";
        String answer = "Georgia";
        String start = "10:55";
        String finish = "10:56";

        report.addQuestionData(question, answer, start, finish);

        assertEquals("Name?", report.getQuestion().get(0));
    }

    @Test
    public void answer_isStored(){
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);


        String question = "Name?";
        String answer = "Georgia";
        String start = "10:55";
        String finish = "10:56";

        report.addQuestionData(question, answer, start, finish);

        assertEquals("Georgia", report.getAnswer().get(0));
    }

    @Test
    public void start_isStored(){
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);


        String question = "Name?";
        String answer = "Georgia";
        String start = "10:55";
        String finish = "10:56";

        report.addQuestionData(question, answer, start, finish);

        assertEquals("10:55", report.getqStart().get(0));
    }

    @Test
    public void finish_isStored(){
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);


        String question = "Name?";
        String answer = "Georgia";
        String start = "10:55";
        String finish = "10:56";

        report.addQuestionData(question, answer, start, finish);

        assertEquals("10:56", report.getqFinish().get(0));
    }

    @Test
    public void multipleData_isStored(){
        Date currentTime = Calendar.getInstance().getTime();
        String location = "Test Location";
        Report report = new Report(currentTime.toString(), location);


        String question = "Name?";
        String answer = "Georgia";
        String start = "10:55";
        String finish = "10:56";
        report.addQuestionData(question, answer, start, finish);

        question = "Name?";
        answer = "Connor";
        start = "10:57";
        finish = "10:58";
        report.addQuestionData(question, answer, start, finish);

        assertEquals("Georgia, Connor", report.getAnswer().get(0) + ", " + report.getAnswer().get(1));
    }

    @Test
    public void firstPage_isReturned() {
        Protocol protocol = new Protocol();
        Page page = protocol.getPage(0);

        assertEquals(2, page.getChoice1().getNextPage());
    }

    @Test
    public void singleButton_isTrue() {
        Protocol protocol = new Protocol();
        Page page = protocol.getPage(3);

        assertTrue(page.isSingleButton());
    }

    @Test
    public void singleButton_isFalse() {
        Protocol protocol = new Protocol();
        Page page = protocol.getPage(0);

        assertTrue(!page.isSingleButton());
    }
}
