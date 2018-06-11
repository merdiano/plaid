package io.plaidapp.data.api.orientnews.model;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.Date;

import io.plaidapp.data.PlaidItem;
import io.plaidapp.util.DribbbleUtils;

/**
 * Created by merdan on 5/14/18.
 */

public class News extends PlaidItem implements Parcelable{
    public final Date date;
    public final String content;
    public final String excerpt;
    public final String thumbnail;
    public final User author;
    // todo move this into a decorator
    public boolean hasFadedIn = false;
    public final Images thumbnail_images;
    //public final boolean animated;
    public Spanned parsedDescription;
    public News(long id, String title, String url, Date created_at, String content,String excerpt,
                String thumbnail,User author,Images thumbnail_images) {
        super(id, title, url);
        this.date = created_at;
        this.content = content;
        this.excerpt = excerpt;
        this.thumbnail = thumbnail;
        this.author = author;
        this.thumbnail_images = thumbnail_images;

    }

    protected News(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        long tmpCreated_at = in.readLong();
        date = tmpCreated_at != -1 ? new Date(tmpCreated_at) : null;
        content = in.readString();
        excerpt = in.readString();
        thumbnail = in.readString();
        hasFadedIn = in.readByte() != 0x00;
        author =  (User)in.readValue(User.class.getClassLoader());
        thumbnail_images = (Images) in.readValue(Images.class.getClassLoader());

    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeString(content);
        dest.writeString(excerpt);
        dest.writeString(thumbnail);
        dest.writeByte((byte) (hasFadedIn ? 0x01 : 0x00));
        dest.writeValue(author);
        dest.writeValue(thumbnail_images);

    }
    public Spanned getParsedDescription(ColorStateList linkTextColor,
                                        @ColorInt int linkHighlightColor) {
        if (parsedDescription == null && !TextUtils.isEmpty(excerpt)) {
            parsedDescription = DribbbleUtils.parseDribbbleHtml(excerpt, linkTextColor,
                    linkHighlightColor);
        }
        return parsedDescription;
    }
}
