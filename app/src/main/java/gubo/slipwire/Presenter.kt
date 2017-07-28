
package gubo.slipwire

/**
 * Created by GUBO on 7/21/2017.
 */
interface Presenter<D : Display>
{
    fun bind( d:D )
    fun unbind()
    fun release()
}