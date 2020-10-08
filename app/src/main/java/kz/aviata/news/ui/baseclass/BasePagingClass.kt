package kz.aviata.news.ui.baseclass

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kz.aviata.news.di.CoroutineProvider
import kz.aviata.news.ui.Status
import kz.aviata.news.ui.UiCaller
import kz.aviata.news.ui.UiCallerImpl
import kz.aviata.news.ui.UiProvider
import kz.aviata.news.utils.EventWrapper
import kz.aviata.news.utils.ResourceString
import org.koin.core.KoinComponent

abstract class BasePagingClass<Value>(
    protected val coroutineProvider: CoroutineProvider = CoroutineProvider(),
    protected val coroutineJob: Job = SupervisorJob(),
    protected val scope: CoroutineScope = CoroutineScope(coroutineJob + coroutineProvider.IO),

    private val _statusLiveData: MutableLiveData<Status> = MutableLiveData(),
    private val _errorLiveData: MutableLiveData<EventWrapper<ResourceString>> = MutableLiveData(),

    private var retryQuery: (() -> Any)? = null,

    protected val uiCaller: UiCaller = UiCallerImpl(
        scope,
        coroutineProvider,
        _statusLiveData,
        _errorLiveData
    )

) : PageKeyedDataSource<Int, Value>(), UiProvider by uiCaller, KoinComponent {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Value>
    ) {
        retryQuery = { loadInitial(params, callback) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        retryQuery = { loadAfter(params, callback) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {}

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }

    override fun invalidate() {
        super.invalidate()
        coroutineJob.cancel()
    }
}