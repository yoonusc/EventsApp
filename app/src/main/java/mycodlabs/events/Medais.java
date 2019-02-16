package mycodlabs.events;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naman on 13-04-2017.
 */

public class Medais implements Parcelable {


    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int event_id;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    String type;
    String path;

    public String getType() {
        return type;
    }

    public Medais(int id,int event_id, String type, String path) {
        this.type = type;
        this.path = path;
        this.event_id=event_id;
        this.id=id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.event_id);
        dest.writeString(this.type);
        dest.writeString(this.path);
        dest.writeInt(this.id);
    }

    protected Medais(Parcel in) {
        this.event_id = in.readInt();
        this.type = in.readString();
        this.path = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Medais> CREATOR = new Parcelable.Creator<Medais>() {
        @Override
        public Medais createFromParcel(Parcel source) {
            return new Medais(source);
        }

        @Override
        public Medais[] newArray(int size) {
            return new Medais[size];
        }
    };
}
