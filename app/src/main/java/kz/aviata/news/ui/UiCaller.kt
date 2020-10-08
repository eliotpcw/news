package kz.aviata.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kz.aviata.news.di.CoroutineProvider
import kz.aviata.news.utils.EventWrapper
import kz.aviata.news.utils.ResourceString
import kz.aviata.news.utils.TextResourceString

interface UiProvider {
    val statusLiveData: LiveData<Status>
    val errorLiveData: LiveData<EventWrapper<ResourceString>>
}

interface UiCaller : UiProvider {
    override val statusLiveData: MutableLiveData<Status>

    fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        statusLD: MutableLiveData<Status>? = statusLiveData,
        resultBlock: (suspend (T) -> Unit)? = null
    )

    fun set(status: Status, statusLD: MutableLiveData<Status>? = statusLiveData)

    fun setError(error: ResourceString)
}

class UiCallerImpl(
    private val scope: CoroutineScope,
    private val scopeProvider: CoroutineProvider,
    _statusLiveData: MutableLiveData<Status>,
    _errorLiveData: MutableLiveData<EventWrapper<ResourceString>>
) : UiCaller {
    override val statusLiveData: MutableLiveData<Status> = _statusLiveData
    override val errorLiveData: MutableLiveData<EventWrapper<ResourceString>> = _errorLiveData

    override fun <T> makeRequest(
        call: suspend CoroutineScope.() -> T,
        statusLD: MutableLiveData<Status>?,
        resultBlock: (suspend (T) -> Unit)?
    ) {
        scope.launch(scopeProvider.Main) {
            set(Status.SHOW_LOADING, statusLD)
            try {
                val result = withContext(scopeProvider.IO, call)
                resultBlock?.invoke(result)
            } catch (e: Exception) {
                if (e !is CancellationException) setError(TextResourceString(e.message.orEmpty()))
            }
            set(Status.HIDE_LOADING, statusLD)
        }
    }

    /**
     * Чтобы не терять прогрессбар на нескольких запросах
     */
    private var requestCounter = 0

    /**
     * Выставляем статус
     * по дефолту выставлен [statusLiveData]
     * можно подставить свою лайвдату или [null]
     */
    override fun set(status: Status, statusLD: MutableLiveData<Status>?) {
        statusLD ?: return
        if (statusLD === statusLiveData) {
            when (status) {
                Status.SHOW_LOADING -> {
                    requestCounter++
                }
                Status.HIDE_LOADING -> {
                    requestCounter--
                    if (requestCounter > 0) return
                    requestCounter = 0
                }
            }
        }
        scope.launch(scopeProvider.Main) {
            statusLD.value = status
        }
    }

    override fun setError(error: ResourceString) {
        scope.launch(scopeProvider.Main) {
            errorLiveData.value = EventWrapper(error)
        }
    }
}