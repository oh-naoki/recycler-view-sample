package com.example.quipper_cording_test.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.quipper_cording_test.R
import com.example.quipper_cording_test.ui.fragment.CustomNavigator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment)
        val navigator = CustomNavigator(this, navHostFragment!!.childFragmentManager, R.id.nav_fragment)

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.navigation_graph)

        setupWithNavController(navigation, navController)
    }

}
