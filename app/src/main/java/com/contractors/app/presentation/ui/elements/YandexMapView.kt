package com.contractors.app.ui.elements.com.contractors.app.presentation.ui.elements

import android.graphics.PointF
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.contractors.app.R
import com.contractors.app.presentation.ui.model.Post
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun YandexMapView(posts: List<Post>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val point = Point(posts[0].latitude.toDouble(), posts[0].longitude.toDouble())
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { mapView ->
            val map = mapView.mapWindow.map

            map.move(
                CameraPosition(
                    point,
                    14.0f,
                    150.0f,
                    30.0f
                )
            )
            val imageProvider = ImageProvider.fromResource(context,  R.drawable.adress  )
            posts.forEach { post ->
                if (post.latitude != "0" && post.longitude != "0") {
                    val _point = Point(post.latitude.toDouble(), post.longitude.toDouble())
                    val placemark = map.mapObjects.addPlacemark().apply {
                        geometry = Point(59.939638, 30.339916)
                        setText(
                            "Special place",
                            TextStyle().apply {
                                size = 10f
                                placement = TextStyle.Placement.RIGHT
                                offset = 5f
                            },
                        )
                    }

                    placemark.useCompositeIcon().apply {
                        setIcon(
                            "pin",
                            imageProvider,
                            IconStyle().apply {
                                anchor = PointF(0.5f, 1.0f)
                                scale = 0.9f
                            }
                        )
                        setIcon(
                            "point",
                            imageProvider,
                            IconStyle().apply {
                                anchor = PointF(0.5f, 0.5f)
                                flat = true
                                scale = 0.05f
                            }
                        )
                    }
                }
            }
        }
    )
}