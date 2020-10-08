package kz.aviata.news.utils

import androidx.paging.PagedList
import kz.aviata.news.data.network.IApi.Companion.DEFAULT_PAGE_SIZE

object PagingUtil {
    val listConfig: PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(DEFAULT_PAGE_SIZE * 2)
        .setPageSize(DEFAULT_PAGE_SIZE)
        .setPrefetchDistance(DEFAULT_PAGE_SIZE)
        .build()
}