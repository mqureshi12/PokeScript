package com.mohammad.pokescript.fragments
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.mohammad.pokescript.R
import com.mohammad.pokescript.databinding.FragmentAuthBinding
import org.json.JSONObject

class AuthFragment : Fragment(R.layout.fragment_auth) {

        val TAG = "AuthActivity"
        lateinit var binding: FragmentAuthBinding
        lateinit var callbackManager: CallbackManager

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentAuthBinding.bind(view)
            callbackManager = CallbackManager.Factory.create()
            binding.loginButton.setReadPermissions(listOf("email", "public_profile", "user_gender", "user_birthday", "user_friends"))
            binding.loginButton.registerCallback(callbackManager, object  :FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val graphRequest = GraphRequest.newMeRequest(result?.accessToken){`object`,response->
//                        getFacebookData(`object`)
                    }
                }

                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException) {
                    TODO("Not yet implemented")
                }
            })
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(resultCode, resultCode, data)
        }

//        private fun getFacebookData(obj: JSONObject?) {
//            val profilePic = "https://graph.facebook.com/${obj.getString("id")}/picture?width=200&height=200"
//            Glide.with(this)
//                .load(profilePic)
//                .into(binding.imgAvatar)
//            val name = obj?.getString("name")
//            binding.tvName.text = "NAME: ${(name)}"
//        }
}