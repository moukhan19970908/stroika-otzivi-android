//package com.contractors.app.data.network
//
//import com.contractors.app.presentation.ui.model.Post
//
//data class Blogs(
//    val current_page: Int = 0,
//    val data: List<Blog> = emptyList(),
//    val first_page_url: String = "",
//    val from: Int = 0,
//    val last_page: Int = 0,
//    val last_page_url: String = "",
//    val links: List<Link> = emptyList(),
//    val next_page_url: String? = null,
//    val path: String = "",
//    val per_page: Int = 0,
//    val prev_page_url: String? = null,
//    val to: Int = 0,
//    val total: Int = 0
//)
//
//data class Blog(
//    val id: Int = 0,
//    val title: String = "",
//    val image: String = "",
//    val body: String = "",
//    val created_at: String = "",
//    val comments: List<Comment> = emptyList()
//)
//
//data class CommentUser(
//    val id: Int = 0,
//    val fio: String? = "",
//    val sure_name: String = "",
//    val name: String = "",
//    val last_name: String = "",
//    val user_type_id: Int = 0,
//    val experience: String = "",
//    val email: String = "",
//    val phone: String = "",
//    val email_verified_at: String? = null,
//    val avatar: String? = "",
//    val created_at: String = "",
//    val updated_at: String = ""
//)
//
//data class RegistrationInfo(
//    val sure_name: String = "",
//    val name: String = "",
//    val last_name: String = "",
//    val special: String = "",
//    val experience: String = "",
//    val fio: String = "",
//    val email: String = "",
//    val phone: String = "",
//    val user_type_id: String = "",
//    val password: String = "",
//    val passwordRepeat: String = "",
//    val condition: Boolean = false,
//    val condition1: Boolean = false,
//    val condition2: Boolean = false,
//)
//
//data class RegistrationInfoMasterDTO(
//    val email: String = "",
//    val phone: String = "",
//    val user_type_id: String = "",
//    val password: String = "",
//    val experience: String = "",
//    val name: String = "",
//    val surname: String = "",
//    val middle_name: String = "",
//    val specialist_id: String = "1",
//)
//
//data class CreatePostInfo(
//    val title: String = "",
//    val description: String = "",
//    val latitude: Double = 0.0,
//    val longitude: Double = 0.0,
//    val address: String = "",
//    val gallery: Array<Int> = arrayOf(1),
//    val customer_name: String = "",
//    val type_id: Int = 1,
//)
//
//data class MasterCommentInfo(
//    val role: String = "",
//    val type_work: String = "",
//    val name_client: String = "",
//    val phone_client: String = "",
//    val experience: String = "",
//    val recommendations: String = "",
//    val emotion_rating: Int = 0,
//    val payment_rating: Int = 0,
//    val quality_rating: Int = 0,
//    val delivery_rating: Int = 0,
//    val honesty_rating: Int = 0,
//    val post_id: Int = 0,
//    val gallery: Array<Int> = arrayOf(),
//)
//
//data class RealtorCommentInfo(
//    val advantage: String = "",
//    val disadvantage: String = "",
//    val rating: Int = 0,
//    val post_id: Int = 0,
//    val gallery: Array<Int> = arrayOf(),
//)
//
//data class UpdateProfileInfo(
//    val fio: String = "",
//    val phone: String = "",
//    val email: String = "",
//    val user_type_id: Int = 0,
//)
//
//data class PostResponse(
//    val success: String = "",
//    val data: String = "",
//)
//
//data class SearchResult(
//    val success: Boolean = false,
//    val data: PostData = PostData()
//)
//
//data class CodeResponse(
//    val success: String = "",
//    val token: String = ""
//)
//
//data class ResetPasswordResponse(
//    val success: String = "",
//)
//
//data class PostData(
//    val current_page: Int = 1,
//    val data: List<Post> = emptyList(),
//    val first_page_url: String = "",
//    val from: Int = 0,
//    val last_page: Int = 1,
//    val last_page_url: String = "",
//    val links: List<Link> = emptyList(),
//    val next_page_url: String? = null,
//    val path: String = "",
//    val per_page: Int = 5,
//    val prev_page_url: String? = null,
//    val to: Int = 0,
//    val total: Int = 0,
//)
//
//interface OwnComment {
//    val id: Int
//    val created_at: String
//}
//
//data class MasterComment(
//    override val id: Int = 0,
//    val role: String = "",
//    val type_work: String = "",
//    val name_client: String = "",
//    val phone_client: String = "",
//    val experience: String = "",
//    val recommendations: String = "",
//    val emotion_rating: Int = 0,
//    val payment_rating: Int = 0,
//    val quality_rating: Int = 0,
//    val delivery_rating: Int = 0,
//    val honesty_rating: Int = 0,
//    val user_id: Int = 0,
//    val post_id: Int = 0,
//    override val created_at: String = "",
//    val updated_at: String = "",
//    val user: CommentUser = CommentUser()
//) : OwnComment
//
//data class RealtorComment(
//    override val id: Int = 0,
//    val advantage: String = "",
//    val disadvantage: String = "",
//    val rating: Int = 0,
//    val user_id: Int = 0,
//    val post_id: Int = 0,
//    override val created_at: String = "",
//    val updated_at: String = "",
//    val user: CommentUser = CommentUser()
//) : OwnComment
//
//data class Image(
//    val id: Int = 0,
//    val image_path: String = "",
//    val post_id: Int = 0,
//    val type: String = "",
//    val created_at: String = "",
//    val updated_at: String = ""
//)
//
//data class Link(
//    val url: String? = null,
//    val label: String = "",
//    val active: Boolean = false
//)
//
//
//data class UserResponse(
//    val user: User = User(),
//    val comment_count: Int = 0,
//    val emotion_rating: String = "",
//    val payment_rating: String = "",
//    val quality_rating: String = "",
//    val delivery_rating: String = "",
//    val honesty_rating: String = "",
//)
//
//data class User(
//    val id: String = "",
//    val name: String = "",
//    val middle_name: String = "",
//    val surname: String = "",
//    val user_type_id: Int = 0,
//    val experience: String = "",
//    val specialist: String = "",
//    val email: String = "",
//    val phone: String = "",
//    val email_verified_at: String = "",
//    val avatar: String? = "",
//    val created_at: String = "",
//    val updated_at: String = "",
//)
//
//data class OtherComment(
//    val id: Int = 0,
//    val role: String = "",
//    val type_work: String = "",
//    val name_client: String = "",
//    val phone_client: String = "",
//    val experience: String = "",
//    val recommendations: String = "",
//    val emotion_rating: Int = 0,
//    val payment_rating: Int = 0,
//    val quality_rating: Int = 0,
//    val delivery_rating: Int = 0,
//    val honesty_rating: Int = 0,
//    val user_id: Int = 0,
//    val post_id: Int = 0,
//    val created_at: String = "",
//    val updated_at: String = "",
//    val post: OtherPost = OtherPost()
//)
//
//data class OtherPost(
//    val id: Int = 0,
//    val title: String = "",
//    val description: String = "",
//    val user_id: Int = 0,
//    val latitude: String = "",
//    val longitude: String = "",
//    val status: String = "",
//    val address: String = "",
//    val rating: String = "",
//    val created_at: String = "",
//    val updated_at: String = ""
//)
//
//data class ErrorResponse(
//    val message: String,
//    val errors: Map<String, List<String>>
//)
//
//data class OwlImage(
//    val id: Int = 1,
//    val image_path: String = "",
//    val post_id: Int = 1,
//    val type: String = "",
//    val created_at: String = "",
//    val updated_at: String = ""
//)