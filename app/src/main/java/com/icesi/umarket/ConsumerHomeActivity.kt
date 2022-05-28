package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ConsumerHomeActivity : AppCompatActivity() {

    private lateinit var menuConsumer:BottomNavigationView
    private lateinit var consumerMainOverviewFragment: ConsumerMainOverviewFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer_home)

        consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()

        menuConsumer = findViewById(R.id.menuConsumer)

        menuConsumer.setOnItemReselectedListener { menuItem->
            if(menuItem.itemId == R.id.homeItem){
                showFragment(consumerMainOverviewFragment)
            }else if (menuItem.itemId == R.id.profileItem){
                showFragment(consumerMainOverviewFragment)
            }else if (menuItem.itemId == R.id.ordersItem){
                showFragment(consumerMainOverviewFragment)
            }
            true
        }
    }
    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}