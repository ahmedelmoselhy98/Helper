package com.chefshub.utils.ext

import ERROR_API
import android.os.RemoteException
import android.util.AndroidException
import java.io.IOException
import java.net.*


fun Throwable.handleException() =
    if (this is AndroidException || this is RemoteException || this is BindException || this is PortUnreachableException || this is SocketTimeoutException || this is UnknownServiceException || this is UnknownHostException || this is IOException || this is ConnectException || this is NoRouteToHostException) {
        Throwable(ERROR_API.CONNECTION_ERROR)
    } else {
        this
    }