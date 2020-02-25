package com.claffey.carefinder.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.claffey.carefinder.R
import com.claffey.carefinder.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        login_button.setOnClickListener {
            viewModel.loginOnClick(
                email = email_et.text.toString(),
                password = password_et.text.toString()
            )
        }

        viewModel.getErrorMessage.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { pair ->
                when (pair.first) {
                    "email" -> if (pair.second) email_et.error = null else email_et.error =
                        "Not a valid Email"
                    "password" -> if (pair.second) password_et.error =
                        null else password_et.error = "Password error"
                    "all" -> arrayOf(email_et, password_et).forEach { it.error = null }
                }
            }
        })

    }


}
