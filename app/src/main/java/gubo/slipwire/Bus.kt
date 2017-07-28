
package gubo.slipwire

import io.reactivex.*
import io.reactivex.schedulers.*

import com.jakewharton.rxrelay2.*

/**
 * Created by GUBO on 4/27/2017.
 */
open class Bus<T>
{
    private val bus:PublishRelay<Any> = PublishRelay.create<Any>()

    init {
        bus.toSerialized().subscribeOn( Schedulers.io() )
    }

    fun send( event: Any ) {
        bus.accept( event )
    }

    fun toObservable(): Observable<Any> {
        return bus
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}
