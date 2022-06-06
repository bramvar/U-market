package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.icesi.umarket.databinding.ActivitySellerHomeBinding
import com.icesi.umarket.model.Product

class SellerHomeActivity : AppCompatActivity(),
    SellerMainOverviewFragment.onProductsOnSellerObserver {

    private lateinit var newProductFragment: NewProductFragment
    private lateinit var sellerMainOverviewFragment: SellerMainOverviewFragment
    private lateinit var productSellerFragment: ProductSellerFragment

    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        productSellerFragment = ProductSellerFragment.newInstance()
        newProductFragment = NewProductFragment.newInstance()
        sellerMainOverviewFragment = SellerMainOverviewFragment.newInstance()

        sellerMainOverviewFragment.adapter.onProductSellerObserver = this
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

    override fun onBackPressed() {
    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sellerFragmentContainer,fragment)
        transaction.commit()
    }

    override fun sendProduct(product: Product) {
        productSellerFragment.product = product
        productSellerFragment.onProductSellerObserver = this
        showFragment(productSellerFragment)
    }

    override fun backToOverview() {
        showFragment(sellerMainOverviewFragment)
    }
}