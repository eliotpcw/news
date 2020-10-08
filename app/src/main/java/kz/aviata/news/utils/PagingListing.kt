package kz.aviata.news.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import kz.aviata.news.ui.Status
import kz.aviata.news.utils.EventWrapper
import kz.aviata.news.utils.ResourceString

data class PagingListing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<Status>,
    val error: LiveData<EventWrapper<ResourceString>>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)