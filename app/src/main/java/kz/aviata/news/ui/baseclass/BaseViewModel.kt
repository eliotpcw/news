package kz.aviata.news.ui.baseclass

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kz.aviata.news.di.CoroutineProvider

open class BaseViewModel (
    protected val coroutineProvider: CoroutineProvider = CoroutineProvider(),
    protected val coroutineJob: Job = SupervisorJob(),
    protected val scope: CoroutineScope = CoroutineScope(coroutineJob + coroutineProvider.IO)
): ViewModel(){
    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}