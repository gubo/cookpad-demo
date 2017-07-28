
package gubo.slipwire

import android.support.v7.widget.*

import org.reactivestreams.*

/**
 * Created by JEFF on 7/17/2016.
 */
class ReactiveLinearScrollListener(
        private val linearlayoutmanager: LinearLayoutManager,
        private val subscriber: Subscriber<in Page>,
        private val pagesize: Int,
        private val lookahead: Int ) : RecyclerView.OnScrollListener()
{
    private val previous = Previous()
    private val orientation: Int

    internal class Previous {
        var firstvisibleposition = -1
        var lastvisibleposition = -1
    }

    init {
        this.orientation = linearlayoutmanager.orientation
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        val direction = normalize(if (orientation == LinearLayoutManager.HORIZONTAL) dx else dy)
        when (direction) {
            -1 -> {
                val firstvisibleposition = linearlayoutmanager.findFirstVisibleItemPosition()
                if (firstvisibleposition > 0 && firstvisibleposition != previous.firstvisibleposition) {
                    val boundary = firstvisibleposition % pagesize == 0
                    if (boundary) {
                        val count = pagesize * lookahead
                        val start = Math.max(firstvisibleposition - count, 0)
                        subscriber.onNext(Page(start, count))
                    }
                }
                previous.firstvisibleposition = firstvisibleposition
            }
            +1 -> {
                val lastvisibleposition = linearlayoutmanager.findLastVisibleItemPosition()
                if (lastvisibleposition > 0 && lastvisibleposition != previous.lastvisibleposition) {
                    val lastitemposition = linearlayoutmanager.itemCount - 1
                    val boundary = lastvisibleposition % pagesize == 0
                    val end = lastvisibleposition == lastitemposition
                    if (boundary || end) {
                        val start = lastvisibleposition
                        val count = pagesize * lookahead
                        subscriber.onNext(Page(start, count))
                    }
                }
                previous.lastvisibleposition = lastvisibleposition
            }
        }
    }

    private fun normalize(n: Int): Int {
        val normalized = if (n < 0) -1 else +1
        return normalized
    }
}
