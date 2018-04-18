package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Module
import dagger.Provides
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.domain.auth.AppAuthenticator
import ishopgo.com.exhibition.model.UserDataManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author Steve
 * @since 10/22/17
 */

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): okhttp3.Cache {
        val size: Long = 10 * 1024 * 1024 // 10Mb
        return okhttp3.Cache(application.cacheDir, size)
    }

    @Provides
    @Singleton
    @Named("log")
    fun provideLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    @Named("minute_cache")
    fun provideCacheInterceptor(): Interceptor {
        // cache response for 1 minutes
        return Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .cacheControl(CacheControl.Builder()
                            .maxAge(30, TimeUnit.SECONDS)
                            .build())
                    .build()

            return@Interceptor chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Named("header")
    fun provideHeaderInterceptor(app: Application): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                    .header("Authorization", "Bearer " + UserDataManager.accessToken)
                    .method(original.method(), original.body())
                    .build()

            return@Interceptor chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideAuthenticator(application: Application): Authenticator {
        return AppAuthenticator(application)
    }

    @Provides
    @Singleton
    @Named("okhttp_authenticator")
    fun provideOkHttpClient(auth: Authenticator, cache: okhttp3.Cache,
                            @Named("log") logger: Interceptor,
                            @Named("header") header: Interceptor,
                            @Named("minute_cache") minuteCache: Interceptor): okhttp3.OkHttpClient {
        val builder = okhttp3.OkHttpClient.Builder()
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        builder
                .cache(cache)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .authenticator(auth)
                .addInterceptor(header)
                .addInterceptor(minuteCache)
                .addInterceptor(logger)
                .dispatcher(dispatcher)

        return builder.build()
    }

    @Provides
    @Singleton
    @Named("retrofit_authenticator")
    fun provideRetrofit(@Named("okhttp_authenticator") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    @Named("retrofit_no_authenticator")
    fun provideRetrofitNoAuth(): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("retrofit_authenticator") retrofit: Retrofit,
                          @Named("retrofit_no_authenticator") retrofitNoAuth: Retrofit,
                          auth: Authenticator): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    companion object {
        const val TIME_OUT: Long = 30
        const val BASE_URL = "http://ishopgo.com/api/v1/"
    }
}