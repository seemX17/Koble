package com.seemran.koble.Models;

/**
 * Created by Seemran on 7/11/2017.
 */

public class User {
    // Now in the screenshot  there are four things per row
   // There were 3 strings
    public String name,message,time,image;

    public User() {  //constructor
    }
//getter and setter
    public String getName() {  return name;  }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
