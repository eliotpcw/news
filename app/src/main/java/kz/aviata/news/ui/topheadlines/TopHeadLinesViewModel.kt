package kz.aviata.news.ui.topheadlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.launch
import kz.aviata.news.data.Repository
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.baseclass.BaseViewModel
import kz.aviata.news.utils.PagingListing
import java.util.*

class TopHeadLinesViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _topHeadlinesPagedList = MutableLiveData<PagingListing<ArticlesItem>>()

    private val timer: Timer = Timer()

    fun insertArticle(article: ArticlesItem) {
        scope.launch {
            repository.insertArticle(article)
        }
    }

    fun getTopHeadLines(){
        _topHeadlinesPagedList.postValue(repository.getTopHeadlines())
        timer.scheduleAtFixedRate(object :TimerTask() {
            override fun run() {
                repository.getTopHeadlines().refresh.invoke()
            }
        }, 0, 5000)
    }

    internal val topHeadLinesPagedList
            = Transformations.switchMap(_topHeadlinesPagedList) { it.pagedList }
}