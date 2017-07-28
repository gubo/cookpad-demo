
package com.gubo.cookpad

import android.app.*

/**
 * Created by GUBO on 7/26/2017.
 */
class CookpadApplication : Application()
{
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule( AppModule( this ) )
                .build()
    }
}