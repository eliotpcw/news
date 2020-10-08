package kz.aviata.news.data.network

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.coroutineScope
import kz.aviata.news.R
import kz.aviata.news.utils.FormatResourceString
import kz.aviata.news.utils.IdResourceString
import kz.aviata.news.utils.ResourceString
import kz.aviata.news.utils.TextResourceString
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection.*
import java.net.SocketTimeoutException

interface CoroutineCaller {
    suspend fun <T> apiCall(result: suspend () -> T): RequestResult<T>
}

interface ApiCallerInterface : CoroutineCaller

object ApiCaller : ApiCallerInterface {

    override suspend fun <T> apiCall(result: suspend () -> T): RequestResult<T> = try {
        coroutineScope {
            RequestResult.Success(
                result.invoke()
            )
        }
    } catch (e: Exception) {
        handleException(e)
    }

    private fun <T> handleException(e: Exception): RequestResult<T> = when (e) {
        is JsonSyntaxException -> {
            RequestResult.Error(IdResourceString(R.string.request_json_error))
        }
        is ConnectException -> {
            RequestResult.Error(IdResourceString(R.string.request_connection_error))
        }
        is SocketTimeoutException -> {
            RequestResult.Error(IdResourceString(R.string.request_timeout))
        }
        is HttpException -> when (e.code()) {
            HTTP_NOT_FOUND, HTTP_BAD_REQUEST, HTTP_FORBIDDEN -> {
                RequestResult.Error(
                    TextResourceString(e.response()?.errorBody()?.string()),
                    e.code()
                )
            }
            HTTP_INTERNAL_ERROR -> {
                RequestResult.Error(
                    IdResourceString(R.string.request_http_error_500),
                    e.code()
                )
            }
            else -> {
                RequestResult.Error(
                    ServerError.print(
                        e.response()?.errorBody()?.string(),
                        FormatResourceString(R.string.request_http_error_format, e.code())
                    ), e.code()
                )
            }
        }
        else -> {
            RequestResult.Error(
                FormatResourceString(
                    R.string.request_error,
                    e::class.java.simpleName,
                    e.localizedMessage
                )
            )
        }
    }
}

sealed class RequestResult<out T : Any?> {
    data class Success<out T : Any?>(val result: T) : RequestResult<T>()
    data class Error(
        val error: ResourceString,
        val code: Int = 0
    ) : RequestResult<Nothing>()
}

@Keep
data class ServerError(
    val timestamp: String?,
    val status: Int,
    val error: String?,
    val message: String?,
    val path: String?
) {

    fun print(default: ResourceString): ResourceString {
        return TextResourceString(message ?: error ?: return default)
    }

    companion object {

        @Throws(JsonSyntaxException::class)
        fun from(response: String?): ServerError {
            return Gson().fromJson(response, ServerError::class.java)
        }

        fun print(response: String?, default: ResourceString) = try {
            from(response).print(default)
        } catch (e: Exception) {
            default
        }

        fun checkCondition(response: String,
                           condition: ServerError.() -> Boolean
        ) = try {
            from(response).condition()
        } catch (e: Exception) {
            false
        }
    }
}