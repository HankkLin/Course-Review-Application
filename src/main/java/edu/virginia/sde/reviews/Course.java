package edu.virginia.sde.reviews;

public class Course {
    public Course(String subject, int number, String title) {
        this.subject = subject;
        this.number = number;
        this.title = title;
        this.rating = 0.0;
    }
    public Course(){

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    String subject;
    int number;
    String title;
    double rating;

    @Override
    public String toString() {
        return "Subject: " + subject +
                ", Number: " + number +
                ", Title: " + title;
    }
}
