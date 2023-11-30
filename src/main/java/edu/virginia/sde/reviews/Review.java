package edu.virginia.sde.reviews;

public class Review {
    String userName;
    int rate;
    String comment;
    String time;
    Course course;

    public Review(String userName, int rate, String comment, String time, Course course){
        this.userName = userName;
        this.rate = rate;
        this.comment = comment;
        this.time = time;
        this.course = course;
    }

    public Review(String userName, int rate, String time, Course course){
        this(userName, rate, "", time, course);
    }
}
