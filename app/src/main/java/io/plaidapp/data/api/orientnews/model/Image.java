package io.plaidapp.data.api.orientnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by merdan on 6/10/18.
 */

public class Image implements Parcelable {
    public String url;
    public int width;
    public int height;

    public Image(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    protected Image(Parcel in) {
        url = in.readString();
        width = in.readInt();
        height = in.readInt();
    }
    @SuppressWarnings("unused")
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(width);
        dest.writeInt(height);
    }
}
