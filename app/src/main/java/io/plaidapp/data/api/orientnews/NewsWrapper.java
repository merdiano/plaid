package io.plaidapp.data.api.orientnews;

import java.util.List;

import io.plaidapp.data.api.orientnews.model.News;

/**
 * Created by merdan on 6/9/18.
 */

public class NewsWrapper {
    public int count;
    public int total_count;
    public int pages;
    public List<News> posts;
}
