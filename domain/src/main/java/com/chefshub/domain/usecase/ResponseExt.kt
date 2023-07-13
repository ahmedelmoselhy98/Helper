package com.chefshub.domain.usecase

import ERROR_API
import android.os.RemoteException
import android.util.AndroidException
import android.util.Log
import com.chefshub.data.entity.EndPointModel
import com.chefshub.data.entity.user.AuthMeta
import com.chefshub.data.entity.user.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.transform
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.*

val TAG = "ResponseExt"
inline fun <T, M, R> Flow<Response<EndPointModel<T, M>>>.transformResponseData(
    isLogin: Boolean = false,
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform {
        val errorBody = it.errorBody()
        val body = it.body()

        when {

            it.isSuccessful && body != null -> {

                val data = body.data!!
                Log.e("tester", "transformResponseData: ${data}" )
                if (isLogin && data is UserModel) {
                    data.token = (it.body()?.meta as AuthMeta?)?.token
                }
                onSuccess(data)
            }
            it.code() == 400 && errorBody == null -> throw Throwable(ERROR_API.BAD_REQUEST)
            (it.code() == 400 || it.code() == 403 || it.code() == 422) && errorBody != null -> throw Throwable(
                errorBody.handleError()
            )
            it.code() == 401 -> throw Throwable(ERROR_API.UNAUTHRIZED)
            it.code() == 404 -> throw Throwable(ERROR_API.NOT_FOUND)
            it.code() == 403 -> throw Throwable(ERROR_API.UPDATE_APP)
            it.code() == 500 -> throw Throwable(ERROR_API.SERVER_ERROR)
            it.code() == 503 -> throw Throwable(ERROR_API.MAINTENANCE)
            else -> {
                Log.e(TAG, it.code().toString() + "," + errorBody.toString())
                throw Throwable().handleException()
            }
        }
        Log.e(TAG, "transformResponseData: code ${it.code()} error body ${errorBody?.string()}")

    }
}


/*
inline fun <T, R> Flow<Response<T>>.transformResponse(
    crossinline onSuccess: suspend FlowCollector<R>.(T) -> Unit
): Flow<R> {
    return transform {
        val errorBody = it.errorBody()
        val body = it.body()
        when {

            it.isSuccessful && body != null -> onSuccess(body!!)
            it.code() == 400 && errorBody == null -> throw Throwable(ERROR_API.BAD_REQUEST)
            (it.code() == 400 || it.code() == 403 || it.code() == 422) && errorBody != null -> throw Throwable(
                errorBody.handleError()
            )
            it.code() == 401 -> throw Throwable(ERROR_API.UNAUTHRIZED)
            it.code() == 404 -> throw Throwable(ERROR_API.NOT_FOUND)
            it.code() == 500 -> throw Throwable(ERROR_API.SERVER_ERROR)
            it.code() == 503 -> throw Throwable(ERROR_API.MAINTENANCE)
            else -> {
                throw Throwable().handleException()
            }
        }
    }
}
*/

fun Throwable.handleException(): Throwable {
    Log.e("exception", this.javaClass.canonicalName + "," + this.javaClass.name)
    return if (this is AndroidException || this is RemoteException || this is BindException || this is PortUnreachableException || this is SocketTimeoutException || this is UnknownServiceException || this is UnknownHostException || this is IOException || this is ConnectException || this is NoRouteToHostException) {
        Throwable(ERROR_API.CONNECTION_ERROR)
    } else {
        this
    }
}

fun ResponseBody.handleError(): String {
//{
//  "error": {
//    "message": "Invalid Data Passed!",
//    "status_code": 403,
//    "code": null
//  },
//  "success": false
//}
    val json = JSONObject(this.string())
    if (json.has("error") && json.get("error") is JSONObject) {
        val error = json.getJSONObject("error").getString("message")
        return error
    }

    Log.d("error-body", "handleError: $json")
    //{"data":{"errors":[{"key":"phone","value":"The selected Phone is invalid."}]}}
    return if (json.has("data") && json.getJSONObject("data")
            .has("errors")
    ) {
        val errors = json.getJSONObject("data").getJSONArray("errors").getJSONObject(0)
        errors.getString("value")
    } else if (json.has("data") && json.getJSONObject("data")
            .has("message")
    ) {
        val errors = json.getJSONObject("data")
        errors.getString("message")
    } else if (json.has("errors")) {
        val errors = json.getJSONArray("errors").getJSONObject(0)
        val errorMessage = errors.getString("value")
        val key = errors.getString("key")
        errorMessage
    } else if (json.has("error")) {
        val jsonError = json.getString("error")
        //val errorMessage = jsonError.getString("error")
        jsonError
    } else if (json.has("message")) {
        val jsonError = json.getString("message")
        //val errorMessage = jsonError.getString("error")
        jsonError
    } else {
        val jsonError = json.getJSONArray("error").getJSONObject(0)
        val errorMessage = jsonError.getString("value")
        errorMessage

    }
}
