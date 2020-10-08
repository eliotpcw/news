package kz.aviata.news.ui.topheadlines.paging

import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.data.network.ApiCaller
import kz.aviata.news.data.network.CoroutineCaller
import kz.aviata.news.data.network.IApi
import kz.aviata.news.data.network.RequestResult
import kz.aviata.news.ui.baseclass.BasePagingClass

val fstPage = 1

class TopHeadLinesDataSource(
    private val api: IApi
) : BasePagingClass<ArticlesItem>(), CoroutineCaller by ApiCaller {

    private suspend fun getHeadLinesPagedList(page: Int) = apiCall {
        api.getTopHeadlines(page)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ArticlesItem>
    ) {
        super.loadInitial(params, callback)
        uiCaller.makeRequest({
            getHeadLinesPagedList(fstPage)
        }){
            when(it){
                is RequestResult.Success ->{
                    callback.onResult(
                        it.result.articles,
                        null,
                        fstPage + 1
                    )
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        super.loadAfter(params, callback)
        uiCaller.makeRequest({
            getHeadLinesPagedList(params.key)
        }){
            when(it){
                is RequestResult.Success ->{
                    callback.onResult(
                        it.result.articles,
                        params.key + 1
                    )
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        super.loadBefore(params, callback)
    }
}