
package com.gubo.cookpad.home

import javax.inject.*

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import gubo.slipwire.*

import com.gubo.cookpad.*
import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/26/2017.
 */
class RecipesPresenter : Presenter<RecipesDisplay>,DataSource<Recipe>,RecipesListener
{
    private val compositeDisposable = CompositeDisposable()
    private val recipes : MutableList<Recipe> = ArrayList()

    private var databusDisposable:Disposable? = null
    private var display : RecipesDisplay? = null
    private var firstVisiblePosition : Int = 0

    @Inject lateinit var eventbus : EventBus
    @Inject lateinit var databus : DataBus
    @Inject lateinit var domain: Domain

    init {
        CookpadApplication.appComponent.inject( this )
    }

    override fun bind( d:RecipesDisplay ) {
        display = d
        display?.setRecipesListener( this )
        display?.setItemCount( recipes.size )
        display?.setPosition( firstVisiblePosition )

        if ( databusDisposable == null ) {
            databusDisposable = databus.toObservable()
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe(
                            { data -> onData( data ) },
                            { error -> exception( error ) }
                    )
        }

        if ( recipes.isEmpty() ) {
            fetchRecipes( 0,30 )
        }
    }

    override fun unbind() {
        firstVisiblePosition = display?.getFirstVisiblePosition() ?: 0
        display?.setRecipesListener( null )
        display?.release()
        display = null
    }

    override fun release() {
        display?.setRecipesListener( null )
        display?.release()
        display = null

        databusDisposable?.dispose()
        databusDisposable = null

        compositeDisposable.clear()
        recipes.clear()
    }

    override fun getDataFor( position:Int ) : Recipe {
        try {
            return recipes[ position ]
        } catch ( x:Exception ) {
            exception( x )
        }
        return Recipe()
    }

    override fun getReadyFor( start:Int,count:Int ) {
        val s = Math.max( start,0 );
        val c = Math.max( count,0 );
        val reach = ( s + c );
        if ( reach >= recipes.size ) {
            fetchRecipes( s,c );
        }
    }

    override fun requestRefresh() {}

    private fun fetchRecipes( start:Int,count:Int ) {
        if ( compositeDisposable.size() > 0 ) { return }

        debug { "fetchRecipes $start,$count" }
        compositeDisposable.clear()

        val observable = domain?.fetchRecipes( start,count )
        val disposable = observable
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { next -> run{ onNext( next ) } },
                        { error -> run{ onError( error ) } },
                        { run{ onComplete() } }
                )

        compositeDisposable.add( disposable )
    }

    private fun onNext( recipe:Recipe ) {
        val indexOf = recipes.indexOf( recipe )
        when ( indexOf ) {
            -1 -> { recipes.add( recipe ) }
            else -> { recipes[ indexOf ] = recipe }
        }
    }

    private fun onComplete() {
        debug { "onComplete ${recipes.size}" }
        compositeDisposable.clear()
        if ( recipes.size <= 30 ) {
            fetchRecipes( 30,30 )
        } else {
            display?.setItemCount(recipes.size)
        }
    }

    private fun onError( x:Throwable ) {
        compositeDisposable.clear()
        exception( x )
    }

    private fun onData( data:Any? ) {}
}