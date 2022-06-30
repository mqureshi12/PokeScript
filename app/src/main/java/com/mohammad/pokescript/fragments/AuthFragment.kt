package com.mohammad.pokescript.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.mohammad.pokescript.R
import com.mohammad.pokescript.databinding.FragmentAuthBinding

class AuthFragment : Fragment(R.layout.fragment_auth) {

        lateinit var binding: FragmentAuthBinding
        lateinit var callbackManager: CallbackManager

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentAuthBinding.bind(view)
            callbackManager = CallbackManager.Factory.create()
            binding.loginButton.setReadPermissions(listOf("email", "public_profile"))
            binding.loginButton.registerCallback(callbackManager, object  :FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    findNavController().navigate(R.id.action_authFragment_to_listFragment)
                    Toast.makeText(context, "Sign in success!", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    findNavController().navigate(R.id.authFragment)
                    Toast.makeText(context, "Please sign in!", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    findNavController().navigate(R.id.authFragment)
                    Toast.makeText(context, "Unable to sign in!", Toast.LENGTH_SHORT).show()
                }
            })
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(resultCode, resultCode, data)
        }
}