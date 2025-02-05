package com.contractors.app.domain.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import com.contractors.app.data.network.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.location.Purpose
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun jsonToMap(json: String): Map<String, String> {
    val gson = Gson()
    val typeToken = object : TypeToken<Map<String, String>>() {}.type
    return gson.fromJson(json, typeToken)
}

fun getFileFromUri(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "temp_upload_file")
    tempFile.outputStream().use { output ->
        inputStream?.copyTo(output)
    }
    return tempFile
}

fun calculateDistanceYandex(distance: Double): String {
    val distInt = distance.toInt()
    return if (distInt > 10000) {
        "больше 10км от вас"
    }
    else if (distInt < 1000) {
        "меньше 1км от вас"
    }
    else {
        "${(distance / 1000).toInt()} км от вас"
    }
}

fun initYandexLocationManager(callback: (Point) -> Unit) {
    val locationManager = MapKitFactory.getInstance().createLocationManager()
    val locationListener = object : LocationListener {
        override fun onLocationUpdated(location: Location) {
            val latitude = location.position.latitude
            val longitude = location.position.longitude
            callback(Point(latitude, longitude))
            locationManager.unsubscribe(this)
            return
        }

        override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
            // Обработка статуса (например, GPS выключен)
        }
    }

    locationManager.subscribeForLocationUpdates(0.0, 1000, 1.0, false, FilteringMode.OFF, Purpose.GENERAL, locationListener)
}

fun formatDate(input: String): String {
    try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru"))

        val dateTime = LocalDateTime.parse(input, inputFormatter)
        return dateTime.format(outputFormatter)
    } catch (e: Exception) {
        return ""
    }
}

fun userTypeToRole(type: Int): String {
    return when (type) {
        1 -> "Мастер"
        2 -> "Риелтор"
        3 -> "Заказчик"
        else -> "Мастер"
    }
}

fun formatYears(year: String): String {
    try {
        val years = year.toInt()
        val suffix = when {
            years % 10 == 1 && years % 100 != 11 -> "год"
            years % 10 in 2..4 && years % 100 !in 12..14 -> "года"
            else -> "лет"
        }
        return "$years $suffix"
    } catch (e: Exception) {
        return year
    }
}

fun openYandexMaps(latitude: Double, longitude: Double, context: Context) {
    try {
        val uri = "yandexmaps://maps?pt=$longitude,$latitude&z=14"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            val browserUri = "https://yandex.ru/maps/?pt=$longitude,$latitude&z=14"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(browserUri))
            context.startActivity(browserIntent)
        }
    } catch (_: Exception) {}

}

fun errorParser(result: String): String {
    val gson = Gson()
    val errorResponse = gson.fromJson(result, ErrorResponse::class.java)

    val displayMessage = errorResponse.message.ifEmpty {
        errorResponse.errors.values.flatten().firstOrNull() ?: "Неизвестная ошибка"
    }
    return displayMessage
}

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhone() =
    length >= 10

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

fun formatPhoneNumber(phone: String): String {
    try {
        if (phone.isEmpty()) return ""
        val digitsOnly = phone.filter { it.isDigit() }

        if (digitsOnly.length != 11 || !digitsOnly.startsWith("7") && !digitsOnly.startsWith("8")) {
            throw IllegalArgumentException("Некорректный номер телефона")
        }

        val normalized = if (digitsOnly.startsWith("8")) "7${digitsOnly.substring(1)}" else digitsOnly

        return normalized.replace(Regex("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})"), "+$1 ($2) $3-$4-$5")
    } catch (e: Exception) {
        return phone
    }
}

fun String.fullTrim() = trim().replace("\uFEFF", "")