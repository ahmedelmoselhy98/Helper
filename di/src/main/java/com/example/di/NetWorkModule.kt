package com.example.di


import PrefKeys
import android.content.Context
import com.chefshub.data.BuildConfig
import com.chefshub.data.cache.PreferencesGateway
import com.chefshub.data.remote.AppApi
import com.chefshub.data.remote.AuthApi
import com.chefshub.data.remote.CommentsApi
import com.chefshub.data.remote.TutorialApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


var APP_VERSION = "1.0.0"

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {
    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
  /*  private fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { hostname, session -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }*/

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        gateway: PreferencesGateway,
        @ApplicationContext context: Context
    ): OkHttpClient {

//trustAllCertificates()

        val okHttpClient = getUnsafeOkHttpClient()?.newBuilder()
//        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addNetworkInterceptor { chain ->

            val devId = gateway.load(PrefKeys.DEVICE_ID, UUID.randomUUID().toString())
            gateway.save(PrefKeys.DEVICE_ID, devId.toString())
            val lang = gateway.getLangCode()
            val token = gateway.load(PrefKeys.TOKEN, "") ?: ""

            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)

            requestBuilder.addHeader("Accept", "application/json")

            requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded")
            requestBuilder.addHeader("APP-LANG", lang)
//            requestBuilder.addHeader("DEVICE-ID", devId!!)
            requestBuilder.addHeader("Authorization", "Bearer $token")
            requestBuilder.addHeader("CLIENT-TYPE", "android")
            requestBuilder.addHeader("CLIENT-VERSION", APP_VERSION)
//            requestBuilder.addHeader("os", "android")
//            requestBuilder.addHeader("DEVICE-TYPE", "android")

            val request = requestBuilder
                .build()
            return@addNetworkInterceptor chain.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(loggingInterceptor)
        }
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
    }

    @Provides
    fun providesBaseUrl(): String {
        return "https://chefshub.site/api/v1/"
    }

    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }


//    fun trustAllCertificates() {
//        try {
//            val trustAllCerts = arrayOf<TrustManager>(
//                object : X509TrustManager {
//                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
//                        return arrayOfNulls(0)
//                    }
//
//                    override fun checkClientTrusted(
//                        certs: Array<X509Certificate?>?,
//                        authType: String?
//                    ) {
//                    }
//
//                    override fun checkServerTrusted(
//                        certs: Array<X509Certificate?>?,
//                        authType: String?
//                    ) {
//                    }
//                }
//            )
//            val sc = SSLContext.getInstance("SSL")
//            sc.init(null, trustAllCerts, SecureRandom())
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
//            HttpsURLConnection.setDefaultHostnameVerifier { arg0, arg1 -> true }
//        } catch (e: java.lang.Exception) {
//        }
//    }


    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideTutorialApi(retrofit: Retrofit): TutorialApi {
        return retrofit.create(TutorialApi::class.java)
    }

    @Provides
    fun provideCommentsApi(retrofit: Retrofit): CommentsApi {
        return retrofit.create(CommentsApi::class.java)
    }
    @Provides
    fun provideAppApi(retrofit: Retrofit): AppApi {
        return retrofit.create(AppApi::class.java)
    }
}