
package com.gubo.cookpad.home

import android.databinding.*

/**
 * Created by GUBO on 7/27/2017.
 */
class RecipeBinder
{
    var expandComment = ObservableBoolean()

    fun toggleComment() {
        expandComment.set( !expandComment.get() )
    }
}
