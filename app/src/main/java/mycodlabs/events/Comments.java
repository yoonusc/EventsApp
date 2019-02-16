package mycodlabs.events;

/**
 * Created by yoonus on 5/18/2018.
 */

public class Comments {

    int id;
    int userId;
    int eventid;
    String userName;
    String comment;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public Comments(int id, int userId, int eventid, String userName, String comment,String time) {
        this.id = id;
        this.userId = userId;
        this.eventid = eventid;
        this.userName = userName;
        this.comment = comment;
        this.time=time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
