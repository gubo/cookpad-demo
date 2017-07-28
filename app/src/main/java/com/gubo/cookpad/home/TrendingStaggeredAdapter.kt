
package com.gubo.cookpad.home

import android.net.*
import android.view.*
import android.widget.ImageView
import android.support.v4.widget.*
import android.support.v7.widget.*

import com.squareup.picasso.*

import gubo.slipwire.*

import com.gubo.cookpad.*
import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/28/2017.
 */
class TrendingStaggeredAdapter(var view : View? ) : TrendingDisplay
{
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var dataSource: DataSource<Recipe>? = null
    private var trendAdapter:TrendAdapter? = null
    private var recyclerView: RecyclerView? = null

    init {
        staggeredGridLayoutManager = StaggeredGridLayoutManager( 2,StaggeredGridLayoutManager.VERTICAL )
        staggeredGridLayoutManager?.setGapStrategy( StaggeredGridLayoutManager.GAP_HANDLING_NONE )

        trendAdapter = TrendAdapter()

        recyclerView = view?.findViewById( R.id.trendingrecyclerview ) as RecyclerView
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.adapter = trendAdapter
        recyclerView?.setHasFixedSize( true )

        val swipeRefreshLayout = view?.findViewById( R.id.trendingswiperefreshlayout ) as SwipeRefreshLayout
        swipeRefreshLayout?.isEnabled = false

        //setupScrolling()
    }

    override fun release() {
        view = null
        dataSource = null
        recyclerView = null
        trendAdapter = null
        staggeredGridLayoutManager = null
    }

    fun using( dataSource:DataSource<Recipe> ) : TrendingStaggeredAdapter {
        this.dataSource = dataSource
        trendAdapter?.dataSource = this.dataSource
        return this
    }

    override fun setItemCount( count:Int ) {
        val positionStart = trendAdapter?.itemCount ?: 0
        val itemCount = ( count - positionStart )
        trendAdapter?.setItemCount( count )
        trendAdapter?.notifyItemRangeInserted( positionStart,itemCount )
    }

    override fun setPosition( position:Int ) {
        recyclerView?.scrollToPosition( position )
    }

    override fun setTrendingListener( trendingListener:TrendingListener? ) {}

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

    private class TrendHolder( view:View ) : RecyclerView.ViewHolder( view )
    {
        private lateinit var trendImageView : ImageView

        init {
            trendImageView = itemView.findViewById( R.id.trendimageview ) as ImageView
        }

        fun bind( recipe:Recipe ) {
            trendImageView.setImageDrawable( null )
            val uri = Uri.parse( recipe.image_url )
            Picasso.with( itemView.context )
                    .load( uri )
                    .into( trendImageView )
        }
    }

    private class TrendAdapter : RecyclerView.Adapter<TrendHolder>()
    {
        private var itemCount = 0

        var dataSource : DataSource<Recipe>? = null

        fun setItemCount( count:Int ) {
            this.itemCount = count
        }

        override fun getItemCount(): Int {
            return itemCount
        }

        override fun onCreateViewHolder( parent:ViewGroup?,viewType:Int ) : TrendHolder {
            val view = LayoutInflater.from( parent?.getContext() ).inflate( R.layout.trend,parent,false )
            return TrendHolder( view )
        }

        override fun onBindViewHolder( holder:TrendHolder?,position:Int ) {
            val recipe = dataSource?.getDataFor( position ) ?: Recipe()
            holder?.bind( recipe )
        }
    }
}