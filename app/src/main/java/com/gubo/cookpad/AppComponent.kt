
package com.gubo.cookpad

import javax.inject.*

import dagger.*

import com.gubo.cookpad.home.*

/**
 * Created by GUBO on 7/26/2017.
 */
@Singleton
@Component( modules = arrayOf( AppModule::class ) )
interface AppComponent
{
    fun inject( homeActivity : HomeActivity )

    fun inject( recipesFragment : RecipesFragment )
    fun inject( recipesPresenter : RecipesPresenter )
    fun inject( trendingFragment : TrendingFragment )
    fun inject( trendingPresenter : TrendingPresenter )

    fun inject( fetchRecipes : FetchRecipes )
}