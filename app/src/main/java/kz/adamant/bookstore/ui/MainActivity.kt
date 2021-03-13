package kz.adamant.bookstore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.adamant.bookstore.R

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
//        bottomNavigation.setOnNavigationItemSelectedListener {
//            if (!navController.popBackStack(it.itemId, false)) {
//                navController.navigate(it.itemId)
//                true
//            }
//            else
//                NavigationUI.onNavDestinationSelected(it , navController)
//        }
    }
}