//package com.contractors.app.ui.elements
//
//import android.view.ViewGroup
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.contractors.app.R
//import com.contractors.app.api.Post
//import com.yandex.mapkit.geometry.Point
//import com.yandex.mapkit.map.CameraPosition
//import com.yandex.mapkit.map.IconStyle
//import com.yandex.mapkit.mapview.MapView
//import com.yandex.runtime.image.ImageProvider
//
//@Composable
//fun YandexMapView(post: Post, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val point = Point(post.latitude.toDouble(), post.longitude.toDouble())
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            MapView(context).apply {
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//            }
//        },
//        update = { mapView ->
//            val map = mapView.mapWindow.map
//
//            map.move(
//                CameraPosition(
//                    point,
//                    14.0f,
//                    150.0f,
//                    30.0f
//                )
//            )
//
//            val imageProvider = ImageProvider.fromResource(context,  R.drawable.adress  )
//            val placeMark = map.mapObjects.addPlacemark(point).apply {
//                isVisible = true
//                setIcon(imageProvider)
//            }
//            placeMark.setIconStyle(
//                IconStyle().apply {
//                    scale = 0.07f
//                }
//            )
//        }
//    )
//}