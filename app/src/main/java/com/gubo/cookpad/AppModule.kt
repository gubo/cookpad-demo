
package com.gubo.cookpad

import javax.inject.*
import java.util.concurrent.*

import dagger.*

import com.google.gson.*

import okhttp3.*
import okhttp3.logging.*

import retrofit2.*
import retrofit2.converter.gson.*

import com.jakewharton.retrofit2.adapter.rxjava2.*

import com.gubo.cookpad.home.*

/**
 * Created by GUBO on 7/26/2017.
 */
@Module
class AppModule( val application:CookpadApplication )
{
    @Provides
    @Singleton
    fun provideDataBus() : DataBus = DataBus()

    @Provides
    @Singleton
    fun provideEventBus() : EventBus = EventBus()

    @Provides
    @Singleton
    fun provideDomain() : Domain = Domain()

    @Provides
    @Singleton
    fun provideRecipesPresenter() : RecipesPresenter = RecipesPresenter()

    @Provides
    @Singleton
    fun provideTrendingPresenter() : TrendingPresenter = TrendingPresenter()

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        class Logging : HttpLoggingInterceptor.Logger {
            override fun log( m:String ) {
                android.util.Log.d( "HTTP",m )
            }
        }

        val loggingInterceptor:HttpLoggingInterceptor = HttpLoggingInterceptor( Logging() )
        loggingInterceptor.setLevel( if ( com.gubo.cookpad.BuildConfig.DEBUG ) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE )
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout( 15000, TimeUnit.MILLISECONDS )
                .addInterceptor( loggingInterceptor )
                .build()

        val gson = GsonBuilder().setPrettyPrinting().create()

        val retrofit : Retrofit = Retrofit.Builder()
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .baseUrl( "http://food2fork.com" )
                .client( okHttpClient )
                .build()

        return retrofit
    }
}