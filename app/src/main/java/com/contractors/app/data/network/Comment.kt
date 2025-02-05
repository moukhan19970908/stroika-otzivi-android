package com.contractors.app.data.network

data class Comment(
    val id: Int = 0,
    val text: String = "",
    val blog_id: Int = 0,
    val user_id: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    val user: CommentUser = CommentUser()
)