package com.minlish.app.data.remote

import java.io.IOException

class AppNetworkException(
    val httpCode: Int,
    val errorCode: String?,
    val serverMessage: String
) : IOException(serverMessage)
