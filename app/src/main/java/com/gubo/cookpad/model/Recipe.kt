
package com.gubo.cookpad.model

/**
 * Created by GUBO on 7/26/2017.
 */
data class Recipe(
        val publisher:String = "",
        val f2f_url:String = "",
        val title:String = "",
        val source_url:String = "",
        val recipe_id:String = "",
        val image_url:String = "",
        val social_rank:Double = 0.0,
        val publisher_url:String = ""
)
{
    override fun equals( other:Any? ): Boolean {
        if ( other is Recipe ) {
            return ( this.recipe_id == other.recipe_id )
        }
        return false
    }

    override fun hashCode(): Int {
        return recipe_id.hashCode()
    }
}