package kz.aviata.news.ui.everything

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kz.aviata.news.data.Repository
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.utils.PagingListing

class EverythingViewModel(
    repository: Repository
) : ViewModel() {

    private val _everythingPagedList = MutableLiveData<PagingListing<ArticlesItem>>()


    init {
        _everythingPagedList.value = repository.getEverything()
    }

    internal val everythingPagedList =
        Transformations.switchMap(_everythingPagedList) { it.pagedList }

    fun refresh() = _everythingPagedList.value!!.refresh.invoke()

}