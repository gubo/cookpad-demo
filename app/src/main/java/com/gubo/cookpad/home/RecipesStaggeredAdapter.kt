
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
 * Created by GUBO on 7/26/2017.
 */
class RecipesStaggeredAdapter( var view:View? ) : RecipesDisplay
{
    private var staggeredGridLayoutManager:StaggeredGridLayoutManager? = null
    private var dataSource:DataSource<Recipe>? = null
    private var recipeAdapter:RecipeAdapter? = null
    private var recyclerView: RecyclerView? = null

    init {
        staggeredGridLayoutManager = StaggeredGridLayoutManager( 2,StaggeredGridLayoutManager.VERTICAL )
        staggeredGridLayoutManager?.setGapStrategy( StaggeredGridLayoutManager.GAP_HANDLING_NONE )

        recipeAdapter = RecipeAdapter()

        recyclerView = view?.findViewById( R.id.recipesrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.adapter = recipeAdapter
        recyclerView?.setHasFixedSize( true )

        val swipeRefreshLayout = view?.findViewById( R.id.recipesswiperefreshlayout ) as SwipeRefreshLayout
        swipeRefreshLayout?.isEnabled = false

        setupScrolling()
    }

    override fun release() {
        view = null
        dataSource = null
        recyclerView = null
        recipeAdapter = null
        staggeredGridLayoutManager = null
    }

    fun using( dataSource:DataSource<Recipe> ) : RecipesStaggeredAdapter {
        this.dataSource = dataSource
        recipeAdapter?.dataSource = this.dataSource
        return this
    }

    override fun setItemCount( count:Int ) {
        val positionStart = recipeAdapter?.itemCount ?: 0
        val itemCount = ( count - positionStart )
        recipeAdapter?.setItemCount( count )
        recipeAdapter?.notifyItemRangeInserted( positionStart,itemCount )
    }

    override fun setPosition( position:Int ) {
        recyclerView?.scrollToPosition( position )
    }

    override fun setRecipesListener( recipesListener:RecipesListener? ) {}

    override fun getFirstVisiblePosition() : Int {
        var firstvisibleposition = 0
        try {
            val into = staggeredGridLayoutManager?.findFirstVisibleItemPositions( null )
            if ( into!!.isNotEmpty() ) {
                firstvisibleposition = into[ 0 ]
            }
        } catch (x: Exception) {
            exception( x )
        }
        return firstvisibleposition
    }

    private fun setupScrolling() {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled( recyclerView:RecyclerView?, dx:Int,dy:Int ) {
                val childCount = staggeredGridLayoutManager?.childCount ?: 0
                val itemCount = staggeredGridLayoutManager?.itemCount ?: 0
                val firstVisibleItem = getFirstVisiblePosition()
                val reach = ( firstVisibleItem + childCount )
                if ( reach >= itemCount ) {
                    debug { "::::: REACHED $childCount $itemCount $reach" }
                    dataSource?.getReadyFor( itemCount,30 )
                }
            }
        }
        recyclerView?.addOnScrollListener( listener )
    }

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