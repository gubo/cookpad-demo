
package com.gubo.cookpad.home

import android.net.*
import android.view.*
import android.widget.*
import android.support.v4.widget.*
import android.support.v7.widget.*

import com.squareup.picasso.*

import gubo.slipwire.*

import com.gubo.cookpad.*
import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/27/2017.
 */
class RecipesGridAdapter( var view : View ? ) : RecipesDisplay
{
    private var gridLayoutManager: GridLayoutManager? = null
    private var dataSource:DataSource<Recipe>? = null
    private var recipeAdapter:RecipeAdapter? = null
    private var recyclerView: RecyclerView? = null
//    private var disposable:Disposable? = null

    init {
        gridLayoutManager = GridLayoutManager( view?.context,2 )
        recipeAdapter = RecipeAdapter()

        recyclerView = view?.findViewById( R.id.recipesrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.adapter = recipeAdapter
        recyclerView?.setHasFixedSize( true )

        val swipeRefreshLayout = view?.findViewById( R.id.recipesswiperefreshlayout ) as SwipeRefreshLayout
        swipeRefreshLayout?.isEnabled = false

        setupScrolling()
    }

    override fun release() {
        view = null
        recyclerView = null
        recipeAdapter = null
        gridLayoutManager = null

        dataSource = null

//        disposable?.dispose()
    }

    fun using( dataSource:DataSource<Recipe> ) : RecipesGridAdapter {
        this.dataSource = dataSource
        recipeAdapter?.dataSource = this.dataSource
        return this
    }

    override fun setItemCount( count:Int ) {
        recipeAdapter?.setItemCount( count )
        recipeAdapter?.notifyDataSetChanged()
    }

    override fun setPosition( position:Int ) {
        recyclerView?.scrollToPosition( position )
    }

    override fun setRecipesListener( recipesListener:RecipesListener? ) {}

    override fun getFirstVisiblePosition() : Int {
        val firstvisibleposition = gridLayoutManager?.findFirstVisibleItemPosition() ?: 0
        return firstvisibleposition
    }

    private fun setupScrolling() {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled( recyclerView:RecyclerView?, dx:Int,dy:Int ) {
                val childCount = gridLayoutManager?.childCount ?: 0
                val itemCount = gridLayoutManager?.itemCount ?: 0
                val firstVisibleItem = gridLayoutManager?.findFirstVisibleItemPosition() ?: 0
                val reach = ( firstVisibleItem + childCount )
                if ( reach >= itemCount ) {
                    debug { "::::: REACHED $childCount $itemCount $reach" }
                    dataSource?.getReadyFor( itemCount,30 )
                }
            }
        }
        recyclerView?.addOnScrollListener( listener )
    }

//    private fun setupReactiveScrolling() {
//        val observer = object : DisposableObserver<Page>() {
//            override fun onStart() {
//                val staggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager
//                val reactivescrolllistener = ReactiveStaggeredScrollListener( staggeredGridLayoutManager,this,30,1 )
//                recyclerView?.addOnScrollListener( reactivescrolllistener )
//            }
//            override fun onNext( page:Page? ) {
//                page( page ?: Page( 0,0 ) )
//            }
//            override fun onComplete() {}
//            override fun onError( e:Throwable? ) { exception( e ?: Exception() ) }
//        }
//
//        disposable = Observable.create<Page> {}
//                .debounce( 500,TimeUnit.MILLISECONDS )
//                .observeOn( AndroidSchedulers.mainThread() )
//                .subscribeWith( observer )
//    }

//    private fun page( page:Page ) {
//        dataSource?.getReadyFor( page.start,page.count )
//    }

    private class RecipeHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private lateinit var recipeImageView : ImageView

        init {
            recipeImageView = itemView.findViewById( R.id.recipeimageview ) as ImageView
        }

        fun bind( recipe:Recipe ) {
            recipeImageView.setImageDrawable( null )
            val uri = Uri.parse( recipe.image_url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .into( recipeImageView )

        }
    }

    private class RecipeAdapter : RecyclerView.Adapter<RecipeHolder>()
    {
        private var itemCount = 0

        var dataSource : DataSource<Recipe>? = null

        fun setItemCount( count:Int ) {
            this.itemCount = count
        }

        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : RecipeHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.recipe,parent,false )
            return RecipeHolder( view )
        }

        override fun onBindViewHolder( holder:RecipeHolder?,position:Int ) {
            val recipe = dataSource?.getDataFor( position ) ?: Recipe()
            holder?.bind( recipe )
        }
    }
}