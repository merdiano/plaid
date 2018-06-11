package io.plaidapp.data.api.orientnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by merdan on 6/10/18.
 */

public class User implements Parcelable {

    public final long id;
    public final String name;
    public final String nickname;
    public final String url;
    public final String first_name;
    public final String last_name;
    public final String description;

    public User(long id, String name, String nickname, String url, String first_name, String last_name, String description) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.url = url;
        this.first_name = first_name;
        this.last_name = last_name;
        this.description = description;
    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        nickname = in.readString();
        url = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(url);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(description);
    }
    public String fullname(){
        return first_name+" "+last_name;
    }
}
