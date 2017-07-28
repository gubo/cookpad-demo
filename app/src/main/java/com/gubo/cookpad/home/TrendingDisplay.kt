
package com.gubo.cookpad.home

import gubo.slipwire.*

import com.gubo.cookpad.model.*

/**
 * Created by GUBO on 7/28/2017.
 */
interface TrendingDisplay : Display,DataSink<Recipe>
{
    fun setTrendingListener( trendingListener : TrendingListener? )
    fun getFirstVisiblePosition() : Int
}