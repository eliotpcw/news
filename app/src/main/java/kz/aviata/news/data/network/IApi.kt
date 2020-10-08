package kz.aviata.news.data.network

import kz.aviata.news.BuildConfig
import kz.aviata.news.data.models.ResponseNews
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("pageSize") pageSize:Int = DEFAULT_PAGE_SIZE,
        @Query("country") country: String = "ru",
        @Query("category") keyWord: String = "technology"
    ): ResponseNews

    @GET("everything")
    suspend fun getEverything(
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("pageSize") pageSize:Int = DEFAULT_PAGE_SIZE,
        @Query("language") language: String = "ru",
        @Query("q") keyWord: String = "technology"
    ): ResponseNews

    companion object{
        const val API_KEY = BuildConfig.API_KEY
        const val DEFAULT_PAGE_SIZE = 15
    }
}