package edu.virginia.sde.reviews;

public class Review {
    String userName;
    int rating;
    String comment;
    String time;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public String getSubject(){
        return course.getSubject();
    }
    public int getNumber(){
        return course.getNumber();
    }
    public String getTitle(){
        return course.getTitle();
    }

    Course course;

    public Review(String userName, int rating, String comment, String time, Course course){
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.time = time;
        this.course = course;
    }

    public Review(String userName, int rating, String time, Course course){
        this(userName, rating, "", time, course);
    }
}
