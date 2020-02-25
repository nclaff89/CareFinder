package com.claffey.carefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.claffey.carefinder.viewModels.LoginViewModel
import com.claffey.carefinder.models.LoginEvent

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

         loginViewModel.getLoginEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    LoginEvent.LOGGED_IN -> {
                        navController.popBackStack(R.id.nav_graph, true)
                        navController.navigate(R.id.nav_graph)
                    }
                    LoginEvent.FAILED, LoginEvent.LOGGED_OUT -> navController.navigate(R.id.action_global_navigation_login)
                    else -> {
                    }
                }
            }

        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
             when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> View.GONE
                else -> {
                    // check for jwt
                    loginViewModel.hasSavedToken()
                    View.VISIBLE
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
