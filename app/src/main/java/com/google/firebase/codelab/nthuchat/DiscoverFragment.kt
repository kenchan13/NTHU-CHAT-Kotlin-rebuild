package com.google.firebase.codelab.nthuchat

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import android.Manifest
import android.location.Location
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase

//import com.google.firebase.codelab.nthuchat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DiscoverFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DiscoverFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    // Firebase instance variables
    private var mFirebaseUser: FirebaseUser? = null
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDatabaseReference: DatabaseReference

    // Get Permission for Google Map
    private val LOCATION_REQUEST_CODE = 101
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        val mapFragment = fragmentManager?.findFragmentById(R.id.mapBox) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
        //GPS button Position
//        val locationButton = (mapFragment.view!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
//
//        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//        rlp.setMargins(0, 50, 30, 0)
    }

    // upload my Location
    override fun onMyLocationChange(location: Location){
        // Getting latitude of the current location
        var latitude = location.latitude;
        // Getting longitude of the current location
        var longitude = location.longitude;


//        var database = FirebaseDatabase.getInstance()
//        var gps = GPS(latitude, longitude)
//        var mAuth = FirebaseAuth.getInstance()
//        var currentUser = mAuth!!.currentUser
//        val userID = currentUser!!.uid
//        var ref = database.getReference("gps_data/$userID")
//        ref.setValue(gps)

//        mMap.setOnMyLocationChangeListener(null)
    }



    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap  ?: return

        val NTHU = LatLng(24.794740, 120.993217)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NTHU, 16.5F))

        if (mMap != null) {
//            val NTHU = LatLng(24.794740, 120.993217)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NTHU, 16.5F))
//        map.setOnMyLocationClickListener(this)
            //map.setOnMyLocationChangeListener(myLocationChangeListener)
//            val updateUserLocationDelay = Handler()
//            updateUserLocationDelay.postDelayed(object : Runnable {
//                override fun run() {
////                    mMap.setOnMyLocationChangeListener(this@DiscoverFragment)
////                    getOtherUserLocation()
//                    updateUserLocationDelay.postDelayed(this, 100000)
//                }
//            }, 100)
        }
    }

    private fun requestPermission(permissionType: String,
                                  requestCode: Int) {

        requestPermissions(arrayOf(permissionType), requestCode)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // Initialize Firebase Auth
//        mFirebaseAuth = FirebaseAuth.getInstance()
//        mFirebaseUser = mFirebaseAuth.currentUser



//        Log.d("currentUser", mFirebaseUser?.displayName.toString())



        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscoverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
//        fun newInstance(param1: String, param2: String) =
                DiscoverFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }
}
