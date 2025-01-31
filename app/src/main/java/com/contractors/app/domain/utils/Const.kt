package com.contractors.app.domain.utils

import com.contractors.app.BuildConfig

enum class Const {
    Contractors,
    ImageDescription,
    AuthToken,
}

val BASE_URL = BuildConfig.BASE_URL
val YANDEX_TOKEN = BuildConfig.YANDEX_TOKEN
val SEND_CODE_DELAY = 60