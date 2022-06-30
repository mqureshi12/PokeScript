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
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authFragment)
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), "Unable to sign in!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authFragment)
                }
            })
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(resultCode, resultCode, data)
        }
}