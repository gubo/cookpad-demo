
package com.gubo.cookpad

import io.reactivex.*

import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/26/2017.
 */
class Domain
{
    fun fetchRecipes( start:Int,count:Int ) : Observable<Recipe> {
        return FetchRecipes().fetch( start,count )
    }
}