package io.plaidapp.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;

import io.plaidapp.BuildConfig;
import io.plaidapp.data.api.orientnews.model.News;

/**
 * Created by merdan on 6/10/18.
 */

public class ShareOrientImageTask extends AsyncTask<Void, Void, File> {
    private final Activity activity;
    private final News news;

    ShareOrientImageTask(Activity activity, News news) {
        this.activity = activity;
        this.news = news;
    }
    @Override
    protected File doInBackground(Void... voids) {
        final String url = news.thumbnail_images.medium_large.url;
        try {
            return Glide
                    .with(activity)
                    .load(url)
                    .downloadOnly(800, 600)//todo width height
                    .get();
        } catch (Exception ex) {
            Log.w("SHARE", "Sharing " + url + " failed", ex);
            return null;
        }
    }
    @Override
    protected void onPostExecute(File result) {
        if (result == null) { return; }
        // glide cache uses an unfriendly & extension-less name,
        // massage it based on the original
        String fileName = news.url;
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        File renamed = new File(result.getParent(), fileName);
        result.renameTo(renamed);
        Uri uri = FileProvider.getUriForFile(activity, BuildConfig.FILES_AUTHORITY, renamed);
        ShareCompat.IntentBuilder.from(activity)
                .setText(getShareText())
                .setType(getImageMimeType(fileName))
                .setSubject(news.title)
                .setStream(uri)
                .startChooser();
    }

    private String getShareText() {
        return "“" + news.title + "” by " + news.author.fullname() + "\n" + news.url;
    }

    private String getImageMimeType(@NonNull String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }
}
