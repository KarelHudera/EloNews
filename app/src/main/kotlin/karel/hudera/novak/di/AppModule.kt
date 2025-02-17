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
import karel.hudera.novak.data.manager.LocalUserManagerImpl
import karel.hudera.novak.data.remote.NewsApi
import karel.hudera.novak.data.NewsRepositoryImpl
import karel.hudera.novak.domain.manger.LocalUserManager
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

/**
 * Dagger Hilt module that provides singleton dependencies used throughout the app.
 *
 * This module includes the necessary configuration and initialization for various components
 * such as network clients, repositories, use cases, and image loading.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of [LocalUserManager].
     *
     * This manager is responsible for handling local user-related data and is used to
     * check if the user has completed the app entry process.
     *
     * @param application The application context used to initialize the manager.
     * @return A singleton instance of [LocalUserManager].
     */
    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(context = application)

    /**
     * Provides a singleton instance of [AppEntryUseCases].
     *
     * This use case class bundles the logic for reading and saving app entry preferences
     * such as whether the user has seen the onboarding screen.
     *
     * @param localUserManager The manager responsible for managing the local user data.
     * @return A singleton instance of [AppEntryUseCases].
     */
    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    /**
     * Provides a singleton [OkHttpClient] with logging support.
     *
     * This client is used for HTTP requests, and a logging interceptor is added to log
     * request and response bodies for debugging purposes.
     *
     * @return A configured [OkHttpClient] instance.
     */
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

    /**
     * Provides a singleton instance of [NewsApi] using Retrofit for API communication.
     *
     * This API is used for fetching news data from the remote service, and it is configured
     * with a base URL and Gson converter for JSON parsing.
     *
     * @return A singleton instance of [NewsApi].
     */
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

    /**
     * Provides a singleton instance of [NewsRepository].
     *
     * This repository is responsible for fetching news data from the API and managing
     * news-related business logic.
     *
     * @param newsApi The [NewsApi] instance used for fetching news.
     * @return A singleton instance of [NewsRepository].
     */
    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi)
    }

    /**
     * Provides a singleton instance of [NewsUseCases].
     *
     * This use case class bundles the logic for fetching and searching for news articles.
     *
     * @param newsRepository The [NewsRepository] instance used to access the news data.
     * @return A singleton instance of [NewsUseCases].
     */
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

    /**
     * Provides a singleton [ImageLoader] instance used for loading images asynchronously.
     *
     * This loader is configured with crossfade animations and OkHttp as the network fetcher.
     *
     * @param application The application context used to initialize the loader.
     * @param okHttpClient The [OkHttpClient] used for image network fetching.
     * @return A singleton [ImageLoader] instance.
     */
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

    /**
     * Entry point for retrieving the [ImageLoader] instance.
     *
     * This interface is used by other components to get access to the [ImageLoader] provided by Hilt.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ImageLoaderEntryPoint {
        fun imageLoader(): ImageLoader
    }
}