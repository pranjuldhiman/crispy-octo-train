package com.rs.roundupclasses.youtube;

import com.rs.roundupclasses.models.CommentListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("get/params.html")
    Call<CommentListResponse> getComment(@Field("userid") String searchText, @Field("videoid") String videoid);

    @FormUrlEncoded
    @POST("user/comment.html")
    Call<CommentListResponse> sendComment(@Field("userid") String searchText, @Field("videoid") String videoid,@Field("comment") String comment);
    @FormUrlEncoded
    @POST("user/like.html")
    Call<CommentListResponse> likedislike(@Field("userid") String searchText, @Field("videoid") String videoid,@Field("likestatus") int likestatus );


}
