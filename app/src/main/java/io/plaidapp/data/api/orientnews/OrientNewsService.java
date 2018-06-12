package io.plaidapp.data.api.orientnews;

import java.util.List;

import io.plaidapp.data.api.EnvelopePayload;
import io.plaidapp.data.api.designernews.model.Story;
import io.plaidapp.data.api.dribbble.model.Shot;
import io.plaidapp.data.api.orientnews.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by merdan on 5/14/18.
 */

public interface OrientNewsService {
    String ENDPOINT = "http://www.orient.tm/api/core/";
    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//2018-06-09 17:17:33
    int PER_PAGE_MAX = 100;
    int PER_PAGE_DEFAULT = 30;

    /*News*/
    @GET("get_recent_posts")
    Call<NewsWrapper> getRecentPosts(@Query("page") Integer page);

    @GET("get_category_posts")
    Call<NewsWrapper> getCategoryPosts(@Query("page") Integer page,@Query("id") Integer id);

    @GET("get_post")
    Call<News> getPost(@Query("post_id") long postId);

    @EnvelopePayload("stories")
    @GET("search")
    Call<List<Story>> search(@Query("query") String query, @Query("page") Integer page);
}
