package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    String accountName;
    int rate;
    String comment;
    Timestamp timestamp;
    Course course;

    public Review(String accountName, int rate, String comment, Timestamp timestamp, Course course){
        this.accountName = accountName;
        this.rate = rate;
        this.comment = comment;
        this.timestamp = timestamp;
        this.course = course;
    }

    public Review(String accountName, int rate, Timestamp timestamp, Course course){
        this(accountName, rate, "", timestamp, course);
    }
}
