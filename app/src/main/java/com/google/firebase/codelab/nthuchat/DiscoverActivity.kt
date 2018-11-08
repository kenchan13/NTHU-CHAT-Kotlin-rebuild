/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.codelab.nthuchat

//import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
//import jdk.nashorn.internal.runtime.ECMAException.getException
import com.google.firebase.auth.FirebaseUser
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layers_demo.*


/**
 * The main activity of the API library demo gallery.
 * The main layout lists the demonstrated features, with buttons to launch them.
 */

class DiscoverActivity : AppCompatActivity() {

//    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val demo: DemoDetails = parent?.adapter?.getItem(position) as DemoDetails
//        startActivity(Intent(this, demo.activityClass))
//    }

    var mAuth: FirebaseAuth? = null
    var currentUser:FirebaseUser ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //direct to layer demo
        mAuth = FirebaseAuth.getInstance()
        signInAnonymous()
        val intent = Intent(this, LayersDemoActivity::class.java)
        startActivity(intent)


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        var currentUser = mAuth!!.getCurrentUser()
    }

    private fun signInAnonymous(){
        mAuth!!.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
//                Log.d(FragmentActivity.TAG, "signInAnonymously:success")
                val user = mAuth!!.currentUser
                Toast.makeText(applicationContext, "Authentication success.",
                        Toast.LENGTH_SHORT).show()
            } else {
                // If sign in fails, display a message to the user.
//                Log.w(FragmentActivity.TAG, "signInAnonymously:failure", task.exception)
                Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }

            // ...
        }



    }

}

    /**
     * A custom array adapter that shows a {@link FeatureView} containing details about the demo.
     *
     * @property context current activity
     * @property demos An array containing the details of the demos to be displayed.
     */
//    class CustomArrayAdapter(context: Context, demos: List<DemoDetails>) :
//            ArrayAdapter<DemoDetails>(context, R.id.title, demos) {
//
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val demo: DemoDetails = getItem(position)
//            return (convertView as? FeatureView ?: FeatureView(context)).apply {
//                setTitleId(demo.titleId)
//                setDescriptionId(demo.descriptionId)
//                contentDescription = resources.getString(demo.titleId)
//            }
//        }
//    }
//}
