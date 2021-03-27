package demo;

import java.util.Date;

/**
 * @Author : TommyYang
 * @Time : 2019-07-31 16:58
 * @Software: IntelliJ IDEA
 * @File : Event.java
 */
public class Event {

    private String noteId;
    private String type;
    private Date time;

    public Event(String noteId, String type) {
        this.noteId = noteId;
        this.type = type;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getType() {
        return type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
