package com.contractors.app.presentation.ui.screen.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.contractors.app.data.network.Blog
import com.contractors.app.data.network.Blogs
import com.contractors.app.data.network.CreatePostInfo
import com.contractors.app.data.network.MasterComment
import com.contractors.app.data.network.MasterCommentInfo
import com.contractors.app.data.network.OtherComment
import com.contractors.app.data.network.model.OwnPostsDTO
import com.contractors.app.data.network.OwnComment
import com.contractors.app.data.network.model.PostDTO
import com.contractors.app.data.network.SearchResult
import com.contractors.app.data.network.RealtorCommentInfo
import com.contractors.app.data.network.UserResponse
import com.contractors.app.data.network.model.ObjectTypesObjDTO
import com.contractors.app.data.network.model.SpecializationsObjDTO
import com.contractors.app.data.network.model.toObjectTypes
import com.contractors.app.data.network.model.toSpecialization
import com.contractors.app.data.repository.AppRepository
import com.contractors.app.domain.AppDispatchers
import com.contractors.app.presentation.ui.model.Post
import com.contractors.app.presentation.ui.model.toPost
import com.contractors.app.presentation.ui.screen.changeData.ChangeDataType
import com.contractors.app.domain.utils.errorParser
import com.contractors.app.domain.utils.jsonToMap
import com.contractors.app.presentation.ui.model.SearchParam
import com.contractors.app.presentation.ui.screen.reg.objectTypeList
import com.contractors.app.presentation.ui.screen.reg.professionList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


data class DataState(
    val changeDataType: ChangeDataType = ChangeDataType.Email,
    val selectedPost: Post = PostDTO().toPost(isFavorite = false),
    val comment: OwnComment = MasterComment(),
    val commentList: List<OwnComment> = listOf(),
    val posts: SearchResult = SearchResult(),
    val favoritePosts: List<Post> = emptyList(),
    val topPosts: SearchResult = SearchResult(),
    val nearestPosts: SearchResult = SearchResult(),
    val isSearch: Boolean = false,
    val searchPosts: SearchResult = SearchResult(),
    val ownPosts: OwnPostsDTO = OwnPostsDTO(),
    val blogs: Blogs = Blogs(),
    val blog: Blog = Blog(),
    val otherUserId: String = "",
    val otherUser: UserResponse = UserResponse(),
    val otherCommentList: List<OtherComment> = emptyList(),
    val selectedImages: List<File> = emptyList(),
    val listType: ListType = ListType.Interesting,
    val searchParam: SearchParam = SearchParam(),
    val specializations: List<String> = professionList,
    val objectTypes: List<String> = objectTypeList,
)

sealed interface DataAction {
    data class SetSearchParam(val param: SearchParam) : DataAction
    data class GetOwnPosts(val token: String) : DataAction
    data class SetDataType(val changeDataType: ChangeDataType) : DataAction
    data class SetCommentList(val commentList: List<OwnComment>) : DataAction
    data class SetShowAllType(val listType: ListType) : DataAction
    data class Search(val text: String) : DataAction
    data class GetAvatarById(val id: String, val callback: (String?) -> Unit) : DataAction
    data class AddRealtorComment(
        val realtorComment: RealtorCommentInfo,
        val token: String,
        val callback: (Boolean, String) -> Unit
    ) : DataAction

    data class AddMasterComment(
        val masterCommentInfo: MasterCommentInfo,
        val token: String,
        val callback: (Boolean, String) -> Unit
    ) : DataAction

    data class AddCommentToBlog(
        val blogId: String,
        val text: String,
        val token: String,
        val callback: (Boolean) -> Unit
    ) : DataAction

    data class SetProfileById(val id: String) : DataAction
    data class GetProfileById(val id: String) : DataAction
    data class GetUserComments(val id: String) : DataAction
    data class GetBlogById(val token: String, val id: String) : DataAction
    data class GetBlogs(val token: String) : DataAction
    data class GetPosts(val point: Point) : DataAction
    data object GetSpecializations : DataAction
    data class UploadImage(
        val files: List<File>,
        val token: String,
        val callback: (List<Int>) -> Unit
    ) : DataAction

    data class CreatePost(
        val info: CreatePostInfo,
        val token: String,
        val callback: (Boolean, String) -> Unit
    ) : DataAction

    data class SetPost(val post: PostDTO) : DataAction
    data class SetComment(val comment: OwnComment) : DataAction
    data object GetFavoriteFourPosts : DataAction
    data class AddPostToFavorite(val post: Post) : DataAction
    data object GetFavoriteAllPosts : DataAction
    data class RemoveFavoritePost(val post: Post) : DataAction
    data class ResetPassword(val number: String) : DataAction
    class GetObjectTypes: DataAction

}

@HiltViewModel
class DataViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    var state by mutableStateOf(DataState())

    fun onAction(action: DataAction) {
        when (action) {
            is DataAction.SetPost -> setPost(action.post)
            is DataAction.SetComment -> setComment(action.comment)
            is DataAction.CreatePost -> createPostInfo(action.info, action.token, action.callback)
            is DataAction.UploadImage -> uploadImage(action.files, action.token, action.callback)
            is DataAction.GetPosts -> getPosts(action.point)
            is DataAction.GetBlogById -> getBlogById(action.token, action.id)
            is DataAction.GetBlogs -> getBlogs(action.token)
            is DataAction.GetProfileById -> getProfileById(action.id)
            is DataAction.AddCommentToBlog -> addCommentToBlog(
                action.blogId,
                action.text,
                action.token,
                action.callback
            )

            is DataAction.AddMasterComment -> addMasterComment(
                action.masterCommentInfo,
                action.token,
                action.callback
            )

            is DataAction.AddRealtorComment -> addRealtorComment(
                action.realtorComment,
                action.token,
                action.callback
            )

            is DataAction.SetProfileById -> setProfileId(action.id)
            is DataAction.GetUserComments -> getUserComments(action.id)
            is DataAction.GetAvatarById -> getAvatarById(action.id, action.callback)
            is DataAction.SetShowAllType -> setShowAllType(action.listType)
            is DataAction.Search -> search(action.text)
            is DataAction.SetCommentList -> setCommentList(action.commentList)
            is DataAction.SetDataType -> setDataType(action.changeDataType)
            is DataAction.GetOwnPosts -> getOwnPosts(action.token)
            is DataAction.SetSearchParam -> setSearchParam(action.param)
            is DataAction.GetFavoriteFourPosts -> getFavoriteFourPosts()
            is DataAction.GetFavoriteAllPosts -> getFavoriteAllPosts()
            is DataAction.AddPostToFavorite -> addPostToFavorite(action.post)
            is DataAction.RemoveFavoritePost -> removePostFromFavorites(action.post)
            is DataAction.GetSpecializations -> getSpecializations()
            is DataAction.GetObjectTypes -> getObjectTypes()
            is DataAction.ResetPassword -> resetPassword(action.number)

        }
    }

    private fun getObjectTypes() {
        appRepository.getObjectTypes { _, result ->
            val gson = Gson()
            val objectTypes = gson.fromJson(result, ObjectTypesObjDTO::class.java)
            val specListServer = objectTypes.data.map { it.toObjectTypes() }.map { it.name }
            state = state.copy(objectTypes = specListServer.ifEmpty { objectTypeList })
        }

    }

    private fun resetPassword(number: String) {
        appRepository.resetPassword(number) { code, result ->
            if (code == 200) {

            }
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
        }
    }

    private fun setSearchParam(param: SearchParam) {
        appRepository.search(param.text) { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
            state = state.copy(searchPosts = posts, isSearch = true)
        }
        state = state.copy(searchParam = param)
    }

    private fun getOwnPosts(token: String) {
        appRepository.getOwnPosts(token) { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, OwnPostsDTO::class.java)
            state = state.copy(ownPosts = posts)
        }
    }

    private fun setDataType(changeDataType: ChangeDataType) {
        state = state.copy(changeDataType = changeDataType)
    }

    private fun setCommentList(commentList: List<OwnComment>) {
        state = state.copy(commentList = commentList)
    }

    private fun search(text: String) {
        appRepository.search(text) { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
            state = state.copy(searchPosts = posts, isSearch = true)
        }
    }

    private fun setShowAllType(listType: ListType) {
        state = state.copy(listType = listType)
    }

    private fun getAvatarById(id: String, callback: (String?) -> Unit) {
        appRepository.getProfileById(id) { _, result ->
            val gson = Gson()
            val user = gson.fromJson(result, UserResponse::class.java)
            callback(user.user.avatar)
        }
    }

    private fun getUserComments(id: String) {
        appRepository.getUserComments(id) { _, result ->
            val gson = Gson()
            val type = object : TypeToken<List<OtherComment>>() {}.type
            val comments: List<OtherComment> = gson.fromJson(result, type)

            state = state.copy(otherCommentList = comments)
        }
    }

    private fun getSpecializations() {
        appRepository.getSpecializations { _, result ->
            val gson = Gson()
            val specializations = gson.fromJson(result, SpecializationsObjDTO::class.java)
            val specListServer = specializations.data.map { it.toSpecialization() }.map { it.name }
            state = state.copy(specializations = specListServer.ifEmpty { professionList })
        }
    }



    private fun setProfileId(id: String) {
        state = state.copy(otherUserId = id)
    }

    private fun addRealtorComment(
        realtorCommentInfo: RealtorCommentInfo,
        token: String,
        callback: (Boolean, String) -> Unit
    ) {
        appRepository.addRealtorComment(realtorCommentInfo, token) { code, result ->
            callback(code == 200, if (code != 200) errorParser(result) else "")
        }
    }

    private fun addMasterComment(
        masterCommentInfo: MasterCommentInfo,
        token: String,
        callback: (Boolean, String) -> Unit
    ) {
        Log.e("TEST", masterCommentInfo.toString())
        appRepository.addMasterComment(masterCommentInfo, token) { code, result ->
            callback(code == 200, if (code != 200) errorParser(result) else "")
        }
    }

    private fun addCommentToBlog(
        blogId: String,
        text: String,
        token: String,
        callback: (Boolean) -> Unit
    ) {
        appRepository.addCommentToBlog(text, blogId, token) { code, _ ->
            if (code == 200) {
                getBlogById(token, state.blog.id.toString())
                callback(true)
            }
        }
    }

    private fun getProfileById(id: String) {
        appRepository.getProfileById(id) { _, result ->
            val gson = Gson()
            val user = gson.fromJson(result, UserResponse::class.java)
            state = state.copy(otherUser = user)
        }
    }

    private fun getBlogs(token: String) {
        appRepository.getBlogs(token) { _, result ->
            val gson = Gson()
            val blogs = gson.fromJson(result, Blogs::class.java)
            state = state.copy(blogs = blogs)
        }
    }

    private fun getBlogById(token: String, id: String) {
        appRepository.getBlogById(token, id) { _, result ->
            val gson = Gson()
            val blog = gson.fromJson(result, Blog::class.java)
            state = state.copy(blog = blog)
        }
    }

    private fun getPosts(point: Point) {
        appRepository.getPosts { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
            state = state.copy(posts = posts)
        }
        appRepository.getTopPosts { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
            state = state.copy(topPosts = posts)
        }
        appRepository.getNearestPosts(point.latitude, point.longitude) { _, result ->
            val gson = Gson()
            val posts = gson.fromJson(result, SearchResult::class.java)
            state = state.copy(nearestPosts = posts)
        }
    }

    private fun uploadImage(files: List<File>, token: String, callback: (List<Int>) -> Unit) {
        val listId = mutableListOf<Int>()
        state = state.copy(selectedImages = files)
        var counter = 0

        files.forEach {
            appRepository.uploadCommentImage(it, token) { _, result ->
                counter++
                val map: Map<String, String> = jsonToMap(result)
                map["image_id"]?.let { imageId ->
                    listId.add(imageId.toInt())
                }
                if (counter == files.size) {
                    callback(listId)
                }
            }
        }
    }

    private fun createPostInfo(
        info: CreatePostInfo,
        token: String,
        callback: (Boolean, String) -> Unit
    ) {
        appRepository.createPost(info, token) { code, result ->
            callback(code == 200, if (code != 200) errorParser(result) else "")
        }
    }

    private fun setComment(comment: OwnComment) {
        state = state.copy(comment = comment)
    }

    private fun setPost(post: PostDTO) {
        appRepository.getPostById(post.id.toString()) { _, result ->
            val gson = Gson()
            val newPost = gson.fromJson(result, PostDTO::class.java)
            state = state.copy(selectedPost = newPost.toPost(isFavorite = false))
        }
    }

    private fun getFavoriteFourPosts() {
        viewModelScope.launch(AppDispatchers.Default) {
            state = state.copy(favoritePosts = appRepository.loadFourFavoritesPost())
        }
    }

    private fun getFavoriteAllPosts() {
        viewModelScope.launch(AppDispatchers.Default) {
            state = state.copy(favoritePosts = appRepository.loadAllFavoritesPost())
        }
    }

    private fun addPostToFavorite(post: Post) {
        viewModelScope.launch(AppDispatchers.Default) {
            appRepository.insertFavoritePost(post.copy(isFavorite = true))

        }
    }

    private fun removePostFromFavorites(post: Post) {
        viewModelScope.launch(AppDispatchers.Default) {
            appRepository.removeFavoritePostById(idServer = post.id)
        }
    }

}