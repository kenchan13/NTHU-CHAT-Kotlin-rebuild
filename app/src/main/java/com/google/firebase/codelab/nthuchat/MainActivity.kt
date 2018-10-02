package com.google.firebase.codelab.nthuchat

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.SubMenu
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.facebook.stetho.Stetho
import com.google.android.gms.ads.AdView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.picasso.Picasso

import jp.wasabeef.picasso.transformations.CropCircleTransformation

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private val mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    private val mFirebaseAnalytics: FirebaseAnalytics? = null
    private val mAdView: AdView? = null
    var mUsername: String? = null
    var mPhotoUrl: String? = null
    var mUid: String? = null
    private var mNameView: TextView? = null
    private var mEmailView: TextView? = null
    private var mIconView: ImageView? = null
    var navigationView: NavigationView? = null
    var drawer: DrawerLayout? = null
    private var headerView: View? = null
    var fab: FloatingActionButton? = null
    var currentFragment: Fragment? = null
    private val mFirebaseDB: FirebaseDatabase? = null
    private val mFBdiv: DatabaseReference? = null
    var dbinstance: AppDatabase? = null
    var user: User? = null
    var sub1: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main)
        navigationView = findViewById(R.id.nav_view)
        headerView = navigationView.getHeaderView(0)
        mNameView = headerView!!.findViewById(R.id.nameView)
        mEmailView = headerView!!.findViewById(R.id.emailView)
        mIconView = headerView!!.findViewById(R.id.iconView)

        val toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        dbinstance = AppDatabase.getAppDatabase(applicationContext)
        user = dbinstance.userDao().getUser()

        drawer = findViewById(R.id.drawer_layout)
        val toggle = object : ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                if (slideOffset != 0f) {
                    hideKeyboard(this@MainActivity)
                }
            }
        }
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        } else {
            if (mFirebaseUser!!.photoUrl != null) {
                mPhotoUrl = mFirebaseUser!!.photoUrl!!.toString()
                mUsername = mFirebaseUser!!.displayName
                mUid = mFirebaseUser!!.uid
                if (mPhotoUrl != null && mPhotoUrl!!.contains("..")) {
                    mPhotoUrl = "https://nthuchat.com" + mPhotoUrl!!.replace("..", "")
                }
                //Toast.makeText(this, "name:  "+mPhotoUrl, Toast.LENGTH_SHORT).show();
                mNameView!!.text = mUsername
                mEmailView!!.text = mFirebaseUser!!.email
                Picasso.with(this@MainActivity).load(mPhotoUrl).transform(CropCircleTransformation()).into(mIconView)
                //mIconView.setImageURI(Uri.parse(mPhotoUrl));
            } else {
                val picnum = Math.round(Math.random() * 12 + 1).toInt()
                val namelist = arrayOf("葉葉", "畫眉", "JIMMY", "阿醜", "茶茶", "麥芽", "皮蛋", "小豬", "布丁", "黑嚕嚕", "憨吉", "LALLY", "花捲")
                val namenum = Math.round(Math.random() * namelist.size).toInt()
                val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(namelist[namenum])
                        .setPhotoUri(Uri.parse("../images/user$picnum.jpg")).build()
                mFirebaseUser!!.updateProfile(profileUpdate)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                mUsername = mFirebaseUser!!.displayName
                                mUid = mFirebaseUser!!.uid
                                mPhotoUrl = mFirebaseUser!!.photoUrl!!.toString()
                                mNameView!!.text = mUsername
                                mEmailView!!.text = mFirebaseUser!!.email
                                if (mPhotoUrl != null && mPhotoUrl!!.contains("..")) {
                                    mPhotoUrl = "https://nthuchat.com" + mPhotoUrl!!.replace("..", "")
                                }
                                Picasso.with(this@MainActivity).load(mPhotoUrl).transform(CropCircleTransformation()).into(mIconView)
                            }
                        }
            }

        }

        if (user != null) {
            navigationView.menu.findItem(R.id.div).setTitle(user!!.getDiv())
            val coursename = user!!.getClasses()
            val course_title = coursename.split("#".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            //Toast.makeText(this, "course.length: "+course_title.length, Toast.LENGTH_SHORT).show();
            if (course_title.size > 1) {
                sub1 = navigationView.menu.addSubMenu(R.id.course_menu, 49, 49, R.string.courses)
                for (id in 0..course_title.size - 1) {
                    //Toast.makeText(MainActivity.this, course_title[id], Toast.LENGTH_SHORT).show();
                    sub1.add(0, 50 + id, 50 + id, course_title[id]).setIcon(R.drawable.ic_assignment_black_18dp)
                }
            }
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
        navigationView.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.school)
    }


    override fun onBackPressed() {
        drawer = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    public override fun onResume() {
        super.onResume()
        //getSupportFragmentManager().beginTransaction().add(R.id.content_frame, currentFragment).commit();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val uri = Uri.parse("https://www.facebook.com/nthuchat/")
                val it = Intent(Intent.ACTION_VIEW, uri)
                startAnimatedActivity(it)
                return true
            }
            else -> {
                hideKeyboard(this)
                return super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        when (id) {
            R.id.school -> displaySelectedScreen(R.id.school)
            R.id.div -> displaySelectedScreen(R.id.div)
            R.id.change_name -> {
                val title = getString(R.string.change_name)
                val intro = getString(R.string.change_name_intro)
                val confirm = getString(R.string.confirm)
                val cancel = getString(R.string.cancel)
                val lastname = mFirebaseUser!!.displayName

                val alertdialog = AlertDialog.Builder(this@MainActivity)
                val editText = EditText(this@MainActivity)
                val container = FrameLayout(this@MainActivity)
                val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                params.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
                params.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
                editText.layoutParams = params
                editText.hint = lastname
                editText.maxLines = 1
                editText.setSingleLine()
                editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
                container.addView(editText)

                alertdialog.setTitle(title)//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage(intro)//設定顯示的文字
                        .setView(container)
                        .setNegativeButton(cancel) { dialog, which -> Toast.makeText(this@MainActivity, "Canceled Change Name", Toast.LENGTH_SHORT).show() }//設定結束的子視窗
                        .setPositiveButton(confirm) { dialog, which ->
                            val changename = editText.text.toString()
                            if (changename.contains(" ")) {
                                changename.replace(" ".toRegex(), "")
                            }
                            if (changename.trim { it <= ' ' }.length > 0) {
                                val profileUpdate = UserProfileChangeRequest.Builder()
                                        .setDisplayName(changename).build()
                                mFirebaseUser!!.updateProfile(profileUpdate)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                mUsername = mFirebaseUser!!.displayName
                                                mUid = mFirebaseUser!!.uid
                                                mPhotoUrl = mFirebaseUser!!.photoUrl!!.toString()
                                                Toast.makeText(this@MainActivity, "Now your name: " + mUsername!!, Toast.LENGTH_SHORT).show()
                                                mNameView!!.text = mUsername
                                            }
                                        }
                            }
                        }//設定結束的子視窗
                        .show()
            }
            R.id.sign_out_menu -> {
                mFirebaseAuth!!.signOut()
                dbinstance.userDao().delete(dbinstance.userDao().getUser())
                AppDatabase.destroyInstance()
                startAnimatedActivity(Intent(this, SignInActivity::class.java))
            }

            else -> displaySelectedScreen(id)
        }
        hideKeyboard(this)
        drawer = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    protected fun startAnimatedActivity(intent: Intent) {
        startActivity(intent)
        hideKeyboard(this)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun displaySelectedScreen(itemId: Int) {
        //creating fragment object
        var fragment: Fragment? = null
        //initializing the fragment object which is selected
        when (itemId) {
            R.id.school -> {
                fragment = Schoolchat()
                navigationView.setCheckedItem(itemId)
            }
            R.id.div -> {
                fragment = Department()
                navigationView.setCheckedItem(itemId)
            }
            else -> {
                fragment = Course(sub1.findItem(itemId).toString())
                navigationView.setCheckedItem(itemId)
            }
        }

        //replacing the fragment
        if (fragment != null) {
            hideKeyboard(this)
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            ft.replace(R.id.content_frame, fragment)
            ft.commit()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:$connectionResult")
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = "MainActivity"

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}