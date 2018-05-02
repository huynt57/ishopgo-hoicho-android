package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Module
import dagger.Provides
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.domain.MockNoAuthService
import ishopgo.com.exhibition.domain.auth.AppAuthenticator
import ishopgo.com.exhibition.model.UserDataManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

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
                    .url(original
                            .url()
                            .newBuilder()
                            .addQueryParameter("id_app", UserDataManager.appId)
                            .build()
                    )
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
    fun provideRetrofitNoAuth(@Named("okhttp_authenticator") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
    }

//    @Provides
//    @Singleton
//    fun provideNoAuthService(@Named("retrofit_no_authenticator") retrofitNoAuth: Retrofit): ApiService.NoAuth {
//        return retrofitNoAuth.create(ApiService.NoAuth::class.java)
//    }

    @Provides
    @Singleton
    fun provideMockNoAuthService(@Named("retrofit_no_authenticator") retrofitNoAuth: Retrofit): ApiService.NoAuth {

        val networkBehavior = NetworkBehavior.create()
        // Reduce the delay to make the next calls complete faster.
        networkBehavior.setDelay(500, TimeUnit.MILLISECONDS)
        val mockRetrofit = MockRetrofit.Builder(retrofitNoAuth).networkBehavior(networkBehavior).build()
        val restServiceBehaviorDelegate = mockRetrofit.create(ApiService.NoAuth::class.java)
        return MockNoAuthService(restServiceBehaviorDelegate)
    }

//    @Provides
//    @Singleton
//    fun provideMockAuthService(@Named("retrofit_authenticator") retrofit: Retrofit): ApiService.Auth {
//        val networkBehavior = NetworkBehavior.create()
//        // Reduce the delay to make the next calls complete faster.
//        networkBehavior.setDelay(500, TimeUnit.MILLISECONDS)
//        val mockRetrofit = MockRetrofit.Builder(retrofit).networkBehavior(networkBehavior).build()
//        val restServiceBehaviorDelegate = mockRetrofit.create(ApiService.Auth::class.java)
//        return MockAuthService(restServiceBehaviorDelegate)
//    }

    @Provides
    @Singleton
    fun provideAuthService(@Named("retrofit_authenticator") retrofit: Retrofit,
                           auth: Authenticator): ApiService.Auth {

        val authService = retrofit.create(ApiService.Auth::class.java)
        if (auth is AppAuthenticator)
            auth.authService = authService
        return authService
    }

    companion object {
        const val TIME_OUT: Long = 30
        const val BASE_URL = "http://ishopgo.com/api/v1/expo/"
    }
}