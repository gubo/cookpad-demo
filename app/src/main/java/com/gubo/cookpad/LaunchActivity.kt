
package com.gubo.cookpad

import android.os.*
import android.content.*
import android.support.v7.app.*

/**
 * Created by GUBO on 7/24/2017.
 */
class LaunchActivity : AppCompatActivity()
{
    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        go()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun go() {
        val intent = Intent( baseContext,HomeActivity::class.java )
        startActivity( intent )
        finish()
    }
}