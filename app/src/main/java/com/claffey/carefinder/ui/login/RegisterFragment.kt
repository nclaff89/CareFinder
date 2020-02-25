package com.claffey.carefinder.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.claffey.carefinder.R
import com.claffey.carefinder.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_fragment_button.setOnClickListener {
            viewModel.registerOnClick(
                register_email_et.text.toString(),
                register_password_et.text.toString(),
                confirm_password_et.text.toString()
            )
        }

        viewModel.getErrorMessage.observe(this, Observer {
            it.getContentIfNotHandled()?.let { pair ->
                when (pair.first) {
                    "email" -> if (pair.second) register_email_et.error =
                        null else register_email_et.error = "Invalid Email"
                    "password" -> if (pair.second) register_password_et.error =
                        null else register_password_et.error = "Password to weak."
                    "passwordConfirm" -> if (pair.second) confirm_password_et.error =
                        null else confirm_password_et.error = "Passwords do not match."
                }
            }
        })
    }


}
