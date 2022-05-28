package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.icesi.umarket.databinding.ActivitySellerHomeBinding

class SellerHomeActivity : AppCompatActivity() {

    private lateinit var newProductFragment: NewProductFragment
    private lateinit var sellerMainOverviewFragment: SellerMainOverviewFragment

    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newProductFragment = NewProductFragment.newInstance()
        sellerMainOverviewFragment = SellerMainOverviewFragment.newInstance()

        newProductFragment.listener = sellerMainOverviewFragment
        showFragment(sellerMainOverviewFragment)

        binding.sellerNavigator.setOnItemSelectedListener { menuItem->
            if(menuItem.itemId == R.id.homeItem){
                showFragment(sellerMainOverviewFragment)
            }else if(menuItem.itemId == R.id.addProductItem){
                showFragment(newProductFragment)
            }
            true
        }
    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sellerFragmentContainer,fragment)
        transaction.commit()
    }
}