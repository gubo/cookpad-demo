
package com.gubo.cookpad.home

import javax.inject.*

import android.os.*
import android.view.*
import android.support.v4.app.*

import com.gubo.cookpad.*

/**
 * Created by GUBO on 7/26/2017.
 */
class TrendingFragment : Fragment()
{
    @Inject lateinit var trendingPresenter : TrendingPresenter

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        CookpadApplication.appComponent.inject( this )
    }

    override fun onCreateView( inflater:LayoutInflater?,container:ViewGroup?,savedInstanceState:Bundle? ) : View? {
        val view = inflater?.inflate( R.layout.trending,container,false );
        trendingPresenter.bind( TrendingStaggeredAdapter( view ).using( trendingPresenter ) )
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trendingPresenter.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        val changingConfigurations = activity?.isChangingConfigurations ?: false
        if ( !changingConfigurations ) {
            trendingPresenter.release()
        }
    }
}

fun newTrendingFragment() : TrendingFragment
{
    return TrendingFragment()
}