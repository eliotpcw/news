package kz.aviata.news.di

import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kz.aviata.news.BuildConfig
import kz.aviata.news.data.Repository
import kz.aviata.news.data.database.NewsDatabase
import kz.aviata.news.data.network.IApi
import kz.aviata.news.ui.everything.EverythingViewModel
import kz.aviata.news.ui.everything.paging.EverythingDataSource
import kz.aviata.news.ui.everything.paging.EverythingDataSourceFactory
import kz.aviata.news.ui.savedarticles.SavedArticlesViewModel
import kz.aviata.news.ui.topheadlines.TopHeadLinesViewModel
import kz.aviata.news.ui.topheadlines.paging.TopHeadLinesDataSource
import kz.aviata.news.ui.topheadlines.paging.TopHeadLinesDataSourceFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

val appModule = module {
    single<CoroutineContext>(named(NAME_IO)) { Dispatchers.IO }
    single<CoroutineContext>(named(NAME_MAIN)) { Dispatchers.Main }
}

val networkModule = module {
    factory { provideDefaultOkHttpClient() }

    single { retrofit(client = get()) }

    single{ get<Retrofit>().create(IApi::class.java) }

    factory { Repository(topHeadLinesFactory = get(), everythingDataSourceFactory = get(), dao = get()) }
}

val paging = module {
    single { TopHeadLinesDataSourceFactory(api = get()) }
    single { EverythingDataSourceFactory(api = get()) }
}

val viewModelModule = module {
    viewModel { TopHeadLinesViewModel(repository = get()) }
    viewModel { EverythingViewModel(repository =  get()) }
    viewModel { SavedArticlesViewModel(repository = get()) }
}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            NewsDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
    single { get<NewsDatabase>().newsDao() }
}

private fun retrofit(client: OkHttpClient) = Retrofit.Builder()
    .client(client)
    .baseUrl(BuildConfig.URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

private fun provideDefaultOkHttpClient(): OkHttpClient = try {
    val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            println("okHttp: $message")
        }
    })
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .build()
} catch (e: Exception) {
    throw RuntimeException(e)
}

const val NAME_IO = "io"
const val NAME_MAIN = "main"
const val DATABASE_NAME = "NewsDb"