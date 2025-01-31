package com.contractors.app.domain.utils

import android.content.Context
import android.util.Log
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.runtime.Error

//fun getAddressFromCoordinates(
//    point: Point,
//    onSuccess: (String) -> Unit,
//    onError: (String) -> Unit
//) {
//    val searchManager: SearchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//    val searchOptions = SearchOptions().apply {
//        searchTypes = SearchType.GEO.value
//        resultPageSize = 32
//    }
//    val searchSessionListener = object : Session.SearchListener {
//        override fun onSearchResponse(p0: Response) {
//            val geoObject = p0.collection.children.firstOrNull()?.obj
//
//            if (geoObject != null) {
//                val streetOrObjectName = geoObject.name
//                val fullDescription = geoObject.descriptionText?.split(", ")?.reversed()?.joinToString(", ")
//
//                val fullAddress = listOfNotNull(
//                    fullDescription,
//                    streetOrObjectName
//                ).joinToString(", ")
//                onSuccess(fullAddress)
//            } else {
//                onError("Адрес не найден")
//            }
//        }
//
//        override fun onSearchError(p0: Error) {
//            onError(p0.toString())
//        }
//    }
//
//    searchManager.submit(
//        point,
//        0,
//        searchOptions,
//        searchSessionListener
//    )
//}