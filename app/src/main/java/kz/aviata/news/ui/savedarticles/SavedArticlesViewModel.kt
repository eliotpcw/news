package kz.aviata.news.ui.savedarticles

import kotlinx.coroutines.launch
import kz.aviata.news.data.Repository
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.baseclass.BaseViewModel

class SavedArticlesViewModel(
    private val repository: Repository
) : BaseViewModel() {

    fun deleteArticle(article: ArticlesItem){
        scope.launch {
            repository.delete(article)
        }
    }

    fun getAllArticle() = repository.getArticles()

}