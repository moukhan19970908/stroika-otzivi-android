package com.contractors.app.data.repository

import android.util.Log
import com.contractors.app.data.database.ContractorsDatabase
import com.contractors.app.data.database.toDBO
import com.contractors.app.data.network.CreatePostInfo
import com.contractors.app.data.network.MasterCommentInfo
import com.contractors.app.data.network.RealtorCommentInfo
import com.contractors.app.data.network.RegistrationInfo
import com.contractors.app.data.network.ResetPasswordResponse
import com.contractors.app.data.network.UpdateProfileInfo
import com.contractors.app.domain.AppDispatchers
import com.contractors.app.domain.utils.BASE_URL
import com.contractors.app.domain.utils.errorParser
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.model.toPost
import com.contractors.app.presentation.ui.screen.sendCode.SendParam
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiClient: HttpClient,
    private val database: ContractorsDatabase,
) {

    fun resetPassword(number: String, callback: (Int, String) -> Unit) {
        val map = mapOf(
            "phone" to number
        )
        requestPost(
            "${BASE_URL}api/resetPassword",
            map,
            callback = callback
        )
    }
    fun registration(info: RegistrationInfo, callback: (Int, String) -> Unit) {
        val body =
            if (info.user_type_id != "1") {
                mutableMapOf(
                    "email" to info.email,
                    "phone" to info.phone,
                    "user_type_id" to info.user_type_id,
                    "password" to info.password,
                    "name" to info.name,
                    "surname" to info.sure_name,
                    "middle_name" to info.last_name,
                )
            } else {
                mutableMapOf(
                    "email" to info.email,
                    "phone" to info.phone,
                    "user_type_id" to info.user_type_id,
                    "password" to info.password,
                    "experience" to info.experience,
                    "name" to info.name,
                    "surname" to info.sure_name,
                    "middle_name" to info.last_name,
                    "specialist_id" to "1",
                )
            }

        requestPost(
            "${BASE_URL}api/registration",
            body,
            callback = callback
        )
    }

    fun verify(phone: String, code: String, callback: (Int, String) -> Unit) {
        requestPost(
            "${BASE_URL}api/verify",
            mapOf(
                "phone" to phone,
                "code" to code,
            ),
            callback = callback
        )
    }

    fun createPost(info: CreatePostInfo, token: String, callback: (Int, String) -> Unit) {
        val param = mapOf(
            "title" to info.title,
            "description" to info.description,
            "latitude" to info.latitude,
            "longitude" to info.longitude,
            "address" to info.address,
            "gallery" to info.gallery.toList(),
            "customer_name" to info.customer_name,
            "type_id" to info.type_id,
        )
        requestPost(
            "${BASE_URL}api/createPost",
            param,
            bearer = "Bearer $token",
            callback = callback
        )
    }

    fun addMasterComment(info: MasterCommentInfo, token: String, callback: (Int, String) -> Unit) {
        requestPost(
            "${BASE_URL}api/addMasterComment",
            mapOf(
                "role" to info.role,
                "type_work" to info.type_work,
                "name_client" to info.name_client,
                "phone_client" to info.phone_client,
                "experience" to info.experience,
                "recommendations" to info.recommendations,
                "emotion_rating" to info.emotion_rating,
                "payment_rating" to info.payment_rating,
                "quality_rating" to info.quality_rating,
                "delivery_rating" to info.delivery_rating,
                "honesty_rating" to info.honesty_rating,
                "post_id" to info.post_id,
                "gallery" to info.gallery,
            ),
            bearer = "Bearer $token",
            callback = callback
        )
    }

    fun addRealtorComment(
        info: RealtorCommentInfo,
        token: String,
        callback: (Int, String) -> Unit
    ) {
        requestPost(
            "${BASE_URL}api/addRieltorComment",
            mapOf(
                "advantage" to info.advantage,
                "disadvantage" to info.disadvantage,
                "rating" to info.rating,
                "post_id" to info.post_id,
                "gallery" to info.gallery
            ),
            bearer = "Bearer $token",
            callback = callback
        )
    }
//    fun verifyCodeForPassword(param: RegistrationInfo, callback: (Int, String) -> Unit) {
//        mutableMapOf(
//            "phone" to param.phone,
//            "code" to param.code,
//        )
//        requestPost(
//            "${BASE_URL}api/verifyCodeForPassword",
//            callback = callback,
//        )
//    }

    fun sendCodeForRecover(sendParam: SendParam, callback: (result:  String) -> Unit) {
        requestPost(
            "${BASE_URL}api/resetPassword",
            mapOf(
                "phone" to sendParam.number
            ),
            callback = { code, result ->
                if (code == 200) {
                    val gson = Gson()
                    val resp = gson.fromJson(result, ResetPasswordResponse::class.java)
                    resp!!
                } else {
                    errorParser(result)
                }
            }
        )
    }

    fun auth(email: String, password: String, callback: (Int, String) -> Unit) {
        requestPost(
            "${BASE_URL}api/login",
            mapOf(
                "email" to email,
                "password" to password
            ),
            callback = callback
        )
    }


    fun changePassword(
        cPassword: String,
        nPassword: String,
        token: String,
        callback: (Int, String) -> Unit
    ) {
        requestPost(
            "${BASE_URL}api/changePassword",
            mapOf(
                "current_password" to cPassword,
                "new_password" to nPassword
            ),
            bearer = "Bearer $token",
            callback = callback
        )
    }

    fun addCommentToBlog(
        text: String,
        blogId: String,
        token: String,
        callback: (Int, String) -> Unit
    ) {
        requestPost(
            "${BASE_URL}api/addCommentToBlog",
            mapOf(
                "text" to text,
                "blog_id" to blogId
            ),
            bearer = "Bearer $token",
            callback = callback
        )
    }

    fun updateProfile(info: UpdateProfileInfo, token: String, callback: (Int, String) -> Unit) {
        requestPost(
            "${BASE_URL}api/updateProfile",
            mapOf(
                "fio" to info.fio,
                "phone" to info.phone,
                "email" to info.email,
                "user_type_id" to info.user_type_id
            ),
            bearer = "Bearer $token",
            callback = callback
        )
    }

    fun getUserTypes(callback: (Int, String) -> Unit) {
        requestGet("${BASE_URL}api/getUserTypes", callback = callback)
    }

    fun getProfileByToken(token: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/profile",
            callback = callback,
            bearer = "Bearer $token"
        )
    }

    fun uploadCommentImage(info: RegistrationInfo, token: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/uploadComment",
            callback = callback,
            bearer = "Bearer $token"
        )
    }

    fun getPosts(callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getPosts",
            callback = callback,
            bearer = ""
        )
    }

    fun getTopPosts(callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/topPosts",
            callback = callback,
            bearer = ""
        )
    }

    fun getNearestPosts(latitude: Double, longitude: Double, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getNearestPosts?latitude=${latitude}&longitude=${longitude}",
            callback = callback,
            bearer = ""
        )
    }

    fun getPostById(id: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getPostById/${id}",
            callback = callback,
        )
    }

    fun getBlogs(token: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getBlogs",
            callback = callback,
            bearer = "Bearer $token"
        )
    }


    fun getBlogById(token: String, id: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getBlogById/${id}",
            callback = callback,
            bearer = "Bearer $token"
        )
    }

    fun getProfileById(id: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getProfile/${id}",
            callback = callback,
        )
    }

    fun getUserComments(id: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getUserComments/${id}",
            callback = callback,
        )
    }

    fun getSpecializations(callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getSpecializations",
            callback = callback,
        )
    }

    fun getOwnPosts(token: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getOwnPosts",
            callback = callback,
            bearer = "Bearer $token",
        )
    }

    fun search(text: String, callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/search?query=${text}",
            callback = callback,
        )
    }

    fun uploadAvatar(file: File, token: String, callback: (Int, String) -> Unit) {
        uploadImage(
            "${BASE_URL}api/uploadAvatar",
            "Bearer $token",
            file,
            "avatar",
            callback
        )
    }

    fun uploadCommentImage(file: File, token: String, callback: (Int, String) -> Unit) {
        uploadImage(
            "${BASE_URL}api/uploadComment",
            "Bearer $token",
            file,
            "file",
            callback
        )
    }

    suspend fun addFavoritePost(post: Post) = withContext(AppDispatchers.IO) {
        database.favoriteDao().insertFavoritePost(post.toDBO())
    }

    suspend fun loadAllFavoritesPost(): List<Post> = withContext(AppDispatchers.IO) {
        return@withContext database.favoriteDao().getListFavoritesPostDBO()
            .map { postDBO -> postDBO.toPost() }
    }

    suspend fun loadFourFavoritesPost(): List<Post> = withContext(AppDispatchers.IO) {
        return@withContext database.favoriteDao().getFourPostFavoritesDBO()
            .map { postDBO -> postDBO.toPost() }
    }

    suspend fun insertFavoritePost(vararg post: Post) = withContext(AppDispatchers.IO) {
        post.forEach { item ->
            database.favoriteDao().insertFavoritePost(item.toDBO())
        }
    }

    suspend fun removeFavoritePostById(idServer: Int) = withContext(AppDispatchers.IO) {
        database.favoriteDao().deleteFavoritePostById(idServer = idServer)
    }


    private fun uploadImage(
        url: String,
        bearer: String,
        file: File,
        key: String,
        callback: (Int, String) -> Unit
    ) {
        CoroutineScope(AppDispatchers.IO).launch {
            try {
                val response: HttpResponse = apiClient.post(url) {
                    contentType(ContentType.MultiPart.FormData)
                    header("Authorization", bearer)
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(key, file.readBytes(), Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        ContentType.Image.JPEG.toString()
                                    )
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"${file.name}\""
                                    )
                                })
                            }
                        )
                    )
                }
                withContext(AppDispatchers.Main) {
                    callback(response.status.value, response.bodyAsText())
                }
            } catch (e: Exception) {
                Log.e("Upload Exception", e.toString())
            }
        }
    }

    private fun requestGet(
        url: String,
        callback: (Int, String) -> Unit,
        body: Map<String, String> = mapOf(),
        bearer: String = "",
    ) {
        CoroutineScope(AppDispatchers.IO).launch {
            try {
                val response: HttpResponse = apiClient.get(url) {
                    contentType(ContentType.Application.Json)
                    header("Authorization", bearer)
                    setBody(
                        Gson().toJson(body).toString()
                    )
                }
                withContext(AppDispatchers.Main) {
                    callback(response.status.value, response.bodyAsText())
                }
            } catch (e: Exception) {
                Log.e("request", e.toString())
            }
        }
    }

    private fun requestPost(
        url: String,
        body: Map<String, Any> = mapOf(),
        callback: (Int, String) -> Unit,
        bearer: String = "",
    ) {
        CoroutineScope(AppDispatchers.IO).launch {
            try {
                val response: HttpResponse = apiClient.post(url) {
                    contentType(ContentType.Application.Json)
                    header("Authorization", bearer)
                    setBody(
                        Gson().toJson(body).toString()
                    )
                }
                withContext(AppDispatchers.Main) {
                    callback(response.status.value, response.bodyAsText())
                }
            } catch (e: Exception) {
                Log.e("request", e.toString())
            }
        }
    }

    fun getObjectTypes(callback: (Int, String) -> Unit) {
        requestGet(
            "${BASE_URL}api/getObjectTypes",
            callback = callback,
        )
    }


}