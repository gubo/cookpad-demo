
package com.gubo.cookpad.home

import android.os.*
import android.view.*
import android.support.v4.app.*

import com.gubo.cookpad.*

/**
 * Created by GUBO on 7/26/2017.
 */
class EmptyFragment : Fragment()
{
    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
    }

    override fun onCreateView( inflater:LayoutInflater?,container:ViewGroup?,savedInstanceState:Bundle? ) : View? {
        val view = inflater?.inflate( R.layout.empty,container,false );
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

fun newEmptyFragment() : EmptyFragment
{
    return EmptyFragment()
}