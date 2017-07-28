
package com.gubo.cookpad.home

import javax.inject.*

import android.os.*
import android.view.*
import android.support.v4.app.*

import com.gubo.cookpad.*

/**
 * Created by GUBO on 7/26/2017.
 */
class RecipesFragment : Fragment()
{
    @Inject lateinit var recipesPresenter : RecipesPresenter

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        CookpadApplication.appComponent.inject( this )
    }

    override fun onCreateView( inflater:LayoutInflater?,container:ViewGroup?,savedInstanceState:Bundle? ) : View? {
        val view = inflater?.inflate( R.layout.recipes,container,false );
        recipesPresenter.bind( RecipesLinearAdapter( view ).using( recipesPresenter ) )
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipesPresenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        val changingConfigurations = activity?.isChangingConfigurations ?: false
        if ( !changingConfigurations ) {
            recipesPresenter.release()
        }
    }
}

fun newRecipesFragment() : RecipesFragment
{
    return RecipesFragment()
}