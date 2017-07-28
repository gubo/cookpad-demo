
package gubo.slipwire

import android.util.*

/**
 * Created by GUBO on 4/22/2017.
 */

val TAG : String = "DBG"

inline fun message( lambda: () -> String ) {
    Log.d( TAG,lambda() )
}

inline fun warning( lambda: () -> String ) {
    Log.w( TAG,lambda() )
}

inline fun exception( x:Throwable ) {
    Log.d( TAG,"XXX",x )
}

inline fun debug( lambda: () -> String ) {
    if ( com.gubo.cookpad.BuildConfig.DEBUG ) {
        Log.d( TAG,lambda() )
    }
}



