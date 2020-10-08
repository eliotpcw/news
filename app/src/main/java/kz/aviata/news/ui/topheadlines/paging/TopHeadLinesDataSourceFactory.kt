package kz.aviata.news.ui.topheadlines.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.data.network.IApi

class TopHeadLinesDataSourceFactory(
    private val api: IApi
) : DataSource.Factory<Int, ArticlesItem>() {

    val source = MutableLiveData<TopHeadLinesDataSource>()

    override fun create(): DataSource<Int, ArticlesItem> {
        val headLinesDS = TopHeadLinesDataSource(api)
        source.postValue(headLinesDS)
        return headLinesDS
    }
}