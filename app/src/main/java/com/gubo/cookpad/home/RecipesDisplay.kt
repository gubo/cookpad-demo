
package com.gubo.cookpad.home

import gubo.slipwire.*
import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/26/2017.
 */
interface RecipesDisplay : Display,DataSink<Recipe>
{
    fun setRecipesListener( recipesListener : RecipesListener? )
    fun getFirstVisiblePosition() : Int
}