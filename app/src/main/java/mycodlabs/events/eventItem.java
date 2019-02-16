package mycodlabs.events;

import java.util.List;

/**
 * Created by naman on 13-04-2017.
 */

public class eventItem {

    private int event_id;
    private String name;
    private String userName;
    private String updated;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    List<Medais> medais;

    public List<Medais> getMedais() {
        return medais;
    }

    public void setMedais(List<Medais> medais) {
        this.medais = medais;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String time;
    private String venue;
    private String details;
    private int usertype_id;
    private String usertype;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;
    public  int likeCount;
    public String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public eventItem(String duration,String updated,int event_id, String userName, String name, String time, String venue, String details, int usertype_id, String usertype, int creator_id, String creator, int category_id, String category) {
        this.event_id = event_id;
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.details = details;
        this.usertype_id = usertype_id;
        this.usertype = usertype;
        this.creator_id = creator_id;
        this.creator = creator;
        this.category_id = category_id;
        this.category = category;
        this.userName=userName;
        this.updated=updated;
        this.duration=duration;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getUsertype_id() {
        return usertype_id;
    }

    public void setUsertype_id(int usertype_id) {
        this.usertype_id = usertype_id;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
