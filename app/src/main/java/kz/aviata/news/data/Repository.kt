package kz.aviata.news.data

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.aviata.news.data.database.Dao
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.everything.paging.EverythingDataSourceFactory
import kz.aviata.news.ui.topheadlines.paging.TopHeadLinesDataSourceFactory
import kz.aviata.news.utils.PagingListing
import kz.aviata.news.utils.PagingUtil

class Repository(
    private val topHeadLinesFactory: TopHeadLinesDataSourceFactory,
    private val everythingDataSourceFactory: EverythingDataSourceFactory,
    private val dao: Dao
) {

    fun getTopHeadlines() : PagingListing<ArticlesItem> {
        val topHeadlinesPagedList = LivePagedListBuilder(
            topHeadLinesFactory,
            PagingUtil.listConfig
        ).build()
        return PagingListing(
            pagedList = topHeadlinesPagedList,
            networkState = Transformations.switchMap(topHeadLinesFactory.source) {
                it.statusLiveData
            },
            error = Transformations.switchMap(topHeadLinesFactory.source) {
                it.errorLiveData
            },
            retry = {
                topHeadLinesFactory.source.value?.retryFailedQuery()
            },
            refresh = {
                topHeadLinesFactory.source.value?.invalidate()
            }
        )
    }

    fun getEverything(): PagingListing<ArticlesItem>{
        val topHeadlinesPagedList = LivePagedListBuilder(
            everythingDataSourceFactory,
            PagingUtil.listConfig
        ).build()

        return PagingListing(
            pagedList = topHeadlinesPagedList,
            networkState = Transformations.switchMap(everythingDataSourceFactory.source) {
                it.statusLiveData
            },
            error = Transformations.switchMap(everythingDataSourceFactory.source) {
                it.errorLiveData
            },
            retry = {
                everythingDataSourceFactory.source.value?.retryFailedQuery()
            },
            refresh = {
                everythingDataSourceFactory.source.value!!.invalidate()
            }
        )
    }

    suspend fun insertArticle(article: ArticlesItem) = withContext(Dispatchers.IO){
        dao.insert(article)
    }

    suspend fun delete(article: ArticlesItem) = withContext(Dispatchers.IO){
        dao.delete(article)
    }

    fun getArticles() = dao.getAllArticles()
}