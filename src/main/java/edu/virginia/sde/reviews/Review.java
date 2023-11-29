package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    String accountName;
    int rate;
    String comment;
    String time;
    Course course;

    public Review(String accountName, int rate, String comment, String time, Course course){
        this.accountName = accountName;
        this.rate = rate;
        this.comment = comment;
        this.time = time;
        this.course = course;
    }

    public Review(String accountName, int rate, String time, Course course){
        this(accountName, rate, "", time, course);
    }
}
