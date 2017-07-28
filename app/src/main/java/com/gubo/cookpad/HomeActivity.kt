
package com.gubo.cookpad

import javax.inject.*

import android.os.*
import android.view.*
import android.widget.Button
import android.support.v4.app.*
import android.support.v7.app.*
import android.support.v4.view.*
import android.support.v4.widget.*
import android.support.v7.widget.*
import android.support.design.widget.*
import android.support.v7.app.ActionBarDrawerToggle

import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import gubo.slipwire.*

import com.gubo.cookpad.home.*

/**
 * Created by GUBO on 7/24/2017.
 */
class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener
{
    @Inject lateinit var eventbus : EventBus

    private var appBarLayout: AppBarLayout? = null
    private var mainRecipesButton: Button? = null
    private var mainTrendingButton: Button? = null
    private var mainNearbyButton: Button? = null
    private var disposable: Disposable? = null
    private var viewPager: ViewPager? = null

    override fun onCreate( savedInstanceState:Bundle? ) {
        super.onCreate( savedInstanceState )
        debug { "HomeActivity.onCreate" }

        CookpadApplication.appComponent.inject( this )

        setContentView( R.layout.home )
        configure()
    }

    override fun onStart() {
        super.onStart()

        disposable = eventbus.toObservable()
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { event -> onEvent( event ) },
                        { error -> exception( error ) }
                )
    }

    override fun onCreateOptionsMenu( menu: Menu? ): Boolean {
        menuInflater.inflate( R.menu.home,menu )
        return true
    }

    override fun onOptionsItemSelected( item: MenuItem? ): Boolean {
        val id = item?.itemId ?: -1

        when ( id ) {
            R.id.action_search -> {
                appBarLayout?.setExpanded( true )
                return true
            }
            R.id.action_settings -> {
               return true
            }
        }

        return super.onOptionsItemSelected( item )
    }

    override fun onNavigationItemSelected( item : MenuItem ) : Boolean {
        val drawer = findViewById( R.id.homedrawerlayout ) as DrawerLayout
        drawer.closeDrawer( GravityCompat.START )

        return true
    }

    override fun onPageScrolled( position:Int,positionOffset:Float,positionOffsetPixels:Int ) {}
    override fun onPageScrollStateChanged( state:Int ) {}

    override fun onPageSelected( position:Int ) {
        mainRecipesButton?.isSelected = false
        mainTrendingButton?.isSelected = false
        mainNearbyButton?.isSelected = false

        when ( position ) {
            0 -> {
                mainRecipesButton?.isSelected = true
                //appBarLayout?.setExpanded( false )
            }
            1 -> {
                mainTrendingButton?.isSelected = true
                //appBarLayout?.setExpanded( false )
            }
            2 -> {
                mainNearbyButton?.isSelected = true
                //appBarLayout?.setExpanded( false )
            }
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById( R.id.homedrawerlayout ) as DrawerLayout
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START )
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()

        disposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable?.dispose()
        debug { "HomeActivity.onDestroy" }
    }

    private fun configure() {
        val toolbar = findViewById( R.id.hometoolbar ) as Toolbar
        toolbar.title = "cookpad"
        setSupportActionBar( toolbar )

        val drawer = findViewById( R.id.homedrawerlayout ) as DrawerLayout
        val toggle = ActionBarDrawerToggle( this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close )
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById( R.id.homenavview ) as NavigationView
        navigationView.setNavigationItemSelectedListener( this )

        appBarLayout = findViewById( R.id.homeappbar ) as AppBarLayout

        val mainFragmentStatePagerAdapter = MainFragmentStatePagerAdapter( supportFragmentManager )
        viewPager = findViewById( R.id.homeviewpager ) as ViewPager
        viewPager?.setAdapter( mainFragmentStatePagerAdapter )
        viewPager?.addOnPageChangeListener( this )
        viewPager?.setOffscreenPageLimit( 3 )
        viewPager?.setCurrentItem( 0 )

        mainRecipesButton = findViewById( R.id.homerecipesbutton ) as Button
        mainTrendingButton = findViewById( R.id.hometrendingbutton ) as Button
        mainNearbyButton = findViewById( R.id.homenearbybutton ) as Button

        val onRecipesClickListener = View.OnClickListener { viewPager?.setCurrentItem( 0 ) }
        mainRecipesButton?.setOnClickListener( onRecipesClickListener )

        val onTrendingClickListener = View.OnClickListener { viewPager?.setCurrentItem( 1 ) }
        mainTrendingButton?.setOnClickListener( onTrendingClickListener )

        val onNearbyClickListener = View.OnClickListener { viewPager?.setCurrentItem( 2 ) }
        mainNearbyButton?.setOnClickListener( onNearbyClickListener )
    }

    private fun onEvent( any : Any? ) {

    }

    private class MainFragmentStatePagerAdapter( fragmentManager:FragmentManager ) : FragmentStatePagerAdapter( fragmentManager )
    {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem( position : Int ) : Fragment {
            val fragment: Fragment
            when ( position ) {
                0 -> fragment = newRecipesFragment()
                1 -> fragment = newTrendingFragment()
                else -> fragment = newEmptyFragment()
            }
            return fragment
        }
    }
}