package karel.hudera.novak.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import karel.hudera.novak.data.manager.LocalUserMangerImpl
import karel.hudera.novak.data.remote.NewsApi
import karel.hudera.novak.data.NewsRepositoryImpl
import karel.hudera.novak.domain.manger.LocalUserManger
import karel.hudera.novak.domain.repository.NewsRepository
import karel.hudera.novak.domain.usecases.app_entry.AppEntryUseCases
import karel.hudera.novak.domain.usecases.app_entry.ReadAppEntry
import karel.hudera.novak.domain.usecases.app_entry.SaveAppEntry
import karel.hudera.novak.domain.usecases.news.GetNews
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import karel.hudera.novak.domain.usecases.news.SearchNews
import karel.hudera.novak.util.Constants.PRIMARY_NEWS_API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
        }

        return OkHttpClient.Builder()
            .protocols(
                listOf(
                    Protocol.HTTP_1_1,
                    Protocol.HTTP_2,
                    Protocol.HTTP_3
                )
            )
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInstance(): NewsApi {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
        }

        // Build OkHttpClient with the loggingInterceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(PRIMARY_NEWS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideImageLoader(application: Application, okHttpClient: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(application)
            .crossfade(true)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
            }
            .build()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ImageLoaderEntryPoint {
        fun imageLoader(): ImageLoader
    }
}