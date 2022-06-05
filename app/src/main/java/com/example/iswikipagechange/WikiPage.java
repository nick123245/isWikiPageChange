package com.example.iswikipagechange;

public class WikiPage {

    private String url;
    private String date;
    private String newDate;
    private String isChange;

    public WikiPage(String url) {
        isChange = "false";
        date = "";
        newDate = "";
        this.url = url;
    }

    public WikiPage(String url, String date, String newDate, String isChange) {
        this.url = url;
        this.date = date;
        this.newDate = newDate;
        this.isChange = isChange;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getNewDate() {
        return newDate;
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    @Override
    public String toString() {
        return "WikiPage{" +
                "url='" + url + '\'' +
                ", date='" + date + '\'' +
                ", newDate='" + newDate + '\'' +
                ", isChange=" + isChange +
                '}';
    }
}
