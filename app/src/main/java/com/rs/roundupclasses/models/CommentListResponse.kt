package com.rs.roundupclasses.models

data class CommentListResponse(
    val comments: List<Comment>,
    val likecount: String = "",
    val unlikecount: String = "",
    val commentcount: String = "",
    val like_status: Int = 0,
    val message: String = "",
    val status: Int = 0
)