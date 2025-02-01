package karel.hudera.novak.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import karel.hudera.novak.data.manager.LocalUserMangerImpl
import karel.hudera.novak.data.remote.NewsApi
import karel.hudera.novak.data.repository.NewsRepositoryImpl
import karel.hudera.novak.domain.manger.LocalUserManger
import karel.hudera.novak.domain.repository.NewsRepository
import karel.hudera.novak.domain.usecases.app_entry.AppEntryUseCases
import karel.hudera.novak.domain.usecases.app_entry.ReadAppEntry
import karel.hudera.novak.domain.usecases.app_entry.SaveAppEntry
import karel.hudera.novak.domain.usecases.news.GetNews
import karel.hudera.novak.domain.usecases.news.NewsUseCases
import karel.hudera.novak.domain.usecases.news.SearchNews
import karel.hudera.novak.util.Constants.SECONDARY_NEWS_API_BASE_URL
import okhttp3.OkHttpClient
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
    fun provideApiInstance(): NewsApi {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response body
        }

        // Build OkHttpClient with the interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the interceptor
            .build()

        return Retrofit
            .Builder()
            .client(okHttpClient) // Use OkHttpClient
            .baseUrl(SECONDARY_NEWS_API_BASE_URL)
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
}