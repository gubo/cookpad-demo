
package com.gubo.cookpad

import javax.inject.*

import io.reactivex.*
import io.reactivex.functions.*
import io.reactivex.schedulers.*
import io.reactivex.android.schedulers.*

import retrofit2.*
import retrofit2.http.*

import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/26/2017.
 */
class FetchRecipes()
{
    @Inject lateinit var retrofit: Retrofit

    private data class Result(
            val count:Int = 0,
            val recipes : List<Recipe> = ArrayList()
    )

    private interface search
    {
        @GET( "/api/search" )
        fun get(
                @Query( "key" ) key:String,
                @Query( "page" ) page:Int? = 0,
                @Query( "q" ) q:String? = null
        ) : Observable<Result>
    }

    init {
        CookpadApplication.appComponent.inject( this )
    }

    fun fetch( start:Int,count:Int ) : Observable<Recipe> {
        val map = Function<Result,Observable<Recipe>> {
            result -> Observable.fromIterable( result.recipes ?: listOf() )
        }

        val page = ( start + 1 + count ) / 30

        val observable : Observable<Recipe> = retrofit.create( search::class.java )
                .get( Food2Fork.APIKEY,page )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .concatMap( map )
        return observable
    }
}
