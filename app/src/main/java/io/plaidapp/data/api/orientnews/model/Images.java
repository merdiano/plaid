package io.plaidapp.data.api.orientnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by merdan on 6/10/18.
 */

public class Images implements Parcelable {
    public Image large;
    public Image thumbnail;
    public Image medium;
    public Image medium_large;
    public static final int[] NORMAL_IMAGE_SIZE = new int[] { 400, 300 };
    public static final int[] TWO_X_IMAGE_SIZE = new int[] { 800, 600 };

    public Images(Image large, Image thumbnail, Image medium, Image medium_large) {
        this.large = large;
        this.thumbnail = thumbnail;
        this.medium = medium;
        this.medium_large = medium_large;
    }

    protected Images(Parcel in) {
        large = (Image) in.readValue(Image.class.getClassLoader());
        thumbnail = (Image)in.readValue(Image.class.getClassLoader());
        medium = (Image)in.readValue(Image.class.getClassLoader());
        medium_large = (Image)in.readValue(Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(large);
        dest.writeValue(thumbnail);
        dest.writeValue(medium);
        dest.writeValue(medium_large);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
