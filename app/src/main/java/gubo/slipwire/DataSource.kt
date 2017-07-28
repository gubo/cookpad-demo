
package gubo.slipwire

/**
 * Created by GUBO on 7/21/2017.
 */
interface DataSource<D>
{
    fun getDataFor( position:Int ) : D
    fun getReadyFor( start:Int,count:Int )
    fun requestRefresh()
}