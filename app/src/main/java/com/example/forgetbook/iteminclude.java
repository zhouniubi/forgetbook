package com.example.forgetbook;

public class iteminclude {
    private String Time, Title, Things;

 /*   public iteminclude(String time, String title, String things) {
        this.Time = time;
        this.Title = title;
        this.Things = things;
    }*/
    public void setTime(String time){
        Time = time;
    }
    public void setTitle(String title){
        Title = title;
    }
    public void setThings(String things){
        Things = things;
    }
    public String getTime() {
        return Time;
    }

    public String getTitle() {
        return Title;
    }

    public String getThings() {
        return Things;
    }
}
