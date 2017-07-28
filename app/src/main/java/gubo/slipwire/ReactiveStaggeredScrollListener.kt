
package gubo.slipwire

import android.support.v7.widget.*

import io.reactivex.observers.*

/**
 * Created by JEFF on 7/17/2016.
 */
class ReactiveStaggeredScrollListener(
        private val staggeredGridLayoutManager:StaggeredGridLayoutManager,
        private val subscriber: DisposableObserver<Page>,
        private val pageSize:Int,
        private val lookAhead:Int ) : RecyclerView.OnScrollListener()
{
    private val previous = Previous()
    private val orientation: Int

    internal class Previous {
        var firstVisiblePosition = -1
        var lastVisiblePosition = -1
    }

    init {
        this.orientation = staggeredGridLayoutManager.orientation
    }

    override fun onScrolled( recyclerView:RecyclerView?, dx:Int,dy:Int ) {
        val direction = normalize( if ( orientation == LinearLayoutManager.HORIZONTAL ) dx else dy )
        when ( direction ) {
            -1 -> {
                val firsts = staggeredGridLayoutManager.findFirstVisibleItemPositions( null )
                val firstvisibleposition = if ( firsts != null && firsts.size > 1 ) firsts[0] else 0
                if ( firstvisibleposition > 0 && firstvisibleposition != previous.firstVisiblePosition ) {
                    val boundary = firstvisibleposition % pageSize == 0
                    if ( boundary ) {
                        val count = pageSize * lookAhead
                        val start = Math.max( firstvisibleposition - count,0 )
                        subscriber.onNext( Page( start,count ) )
                    }
                }
                previous.firstVisiblePosition = firstvisibleposition
            }
            +1 -> {
                val lasts = staggeredGridLayoutManager.findLastVisibleItemPositions(null)
                val lastvisibleposition = if (lasts != null && lasts.size > 1) lasts[0] else 0
                if (lastvisibleposition > 0 && lastvisibleposition != previous.lastVisiblePosition) {
                    val lastitemposition = staggeredGridLayoutManager.itemCount - 1
                    val boundary = lastvisibleposition % pageSize == 0
                    val end = lastvisibleposition == lastitemposition
                    if (boundary || end) {
                        val start = lastvisibleposition
                        val count = pageSize * lookAhead
                        subscriber.onNext(Page(start, count))
                    }
                }
                previous.lastVisiblePosition = lastvisibleposition
            }
        }
    }

    private fun normalize(n: Int): Int {
        val normalized = if (n < 0) -1 else +1
        return normalized
    }
}
