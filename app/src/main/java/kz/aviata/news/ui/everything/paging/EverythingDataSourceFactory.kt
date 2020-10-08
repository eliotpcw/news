package kz.aviata.news.ui.everything.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.data.network.IApi

class EverythingDataSourceFactory(
    private val api: IApi
) : DataSource.Factory<Int, ArticlesItem>() {

    val source = MutableLiveData<EverythingDataSource>()

    override fun create(): DataSource<Int, ArticlesItem> {
        val everythingFactory = EverythingDataSource(api)

        source.postValue(everythingFactory)
        return everythingFactory
    }
}