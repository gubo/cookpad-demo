
package com.gubo.cookpad.home

import android.net.*
import android.view.*
import android.widget.*
import android.databinding.*
import android.support.v4.widget.*
import android.support.v7.widget.*

import com.squareup.picasso.*

import gubo.slipwire.*

import com.gubo.cookpad.*
import com.gubo.cookpad.model.*
import com.gubo.cookpad.databinding.*

/**
 * Created by GUBO on 7/26/2017.
 */
class RecipesLinearAdapter( var view:View? ) : RecipesDisplay
{
    private var linearLayoutManager:LinearLayoutManager? = null
    private var dataSource:DataSource<Recipe>? = null
    private var recipeAdapter:RecipeAdapter? = null
    private var recyclerView: RecyclerView? = null

    init {
        linearLayoutManager = LinearLayoutManager( view?.context,LinearLayoutManager.VERTICAL,false )

        recipeAdapter = RecipeAdapter()

        recyclerView = view?.findViewById( R.id.recipesrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = linearLayoutManager
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
        linearLayoutManager = null
    }

    fun using( dataSource:DataSource<Recipe> ) : RecipesLinearAdapter {
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
        var firstvisibleposition = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0
        return firstvisibleposition
    }

    private fun setupScrolling() {
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled( recyclerView:RecyclerView?, dx:Int,dy:Int ) {
                val childCount = linearLayoutManager?.childCount ?: 0
                val itemCount = linearLayoutManager?.itemCount ?: 0
                val firstVisibleItem = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0
                val reach = ( firstVisibleItem + childCount )
                if ( reach >= itemCount ) {
                    dataSource?.getReadyFor( itemCount,30 )
                }
            }
        }
        recyclerView?.addOnScrollListener( listener )
    }

    private class RecipeHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private var recipeImageView : ImageView
        private var recipeTitleView : TextView

        init {
            recipeImageView = itemView.findViewById( R.id.recipeimageview ) as ImageView
            recipeTitleView = itemView.findViewById( R.id.recipetitleview ) as TextView
        }

        fun bind( recipe:Recipe ) {
            val binding: RecipeBinding = DataBindingUtil.bind( itemView )
            binding.binding = RecipeBinder()

            recipeImageView.setImageDrawable( null )
            val uri = Uri.parse( recipe.image_url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .into( recipeImageView )

            recipeTitleView.text = recipe.title
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