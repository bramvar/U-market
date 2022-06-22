package com.icesi.umarket.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.R
import com.icesi.umarket.SellerOrderOverviewFragment
import com.icesi.umarket.databinding.ActivitySellerHomeBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller

class SellerHomeActivity : AppCompatActivity(),
    SellerMainOverviewFragment.onProductsOnSellerObserver,
    SellerOrderOverviewFragment.onConfirmOrderListener {

    private var newProductFragment = NewProductFragment.newInstance()
    private var sellerMainOverviewFragment = SellerMainOverviewFragment.newInstance()
    private var productSellerFragment = ProductSellerFragment.newInstance()
    private var sellerOrderOverviewFragment = SellerOrderOverviewFragment.newInstance()
    private lateinit var user: Seller

    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = loadUser()!!
        sellerMainOverviewFragment.adapter.onProductSellerObserver = this
        sellerOrderOverviewFragment.adapterOrder.onOrderConfirmObserver = this
        newProductFragment.listener = sellerMainOverviewFragment

        showFragment(sellerMainOverviewFragment)

        binding.sellerNavigator.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem ->  showFragment(sellerMainOverviewFragment)
                R.id.addProductItem -> showFragment(newProductFragment)
                R.id.ordersItem -> showFragment(sellerOrderOverviewFragment)
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

    override fun confirmOrder(idOrder: String){
        Log.e("Confirma orden ", idOrder)
        changeFlag("confirm", idOrder)
    }

    override fun cancelOrder(idOrder: String){
        Log.e("Cancela orden ", idOrder)
        changeFlag("cancel", idOrder)
    }

    override fun editOrder(idOrder: String){
        Log.e("Edita orden ", idOrder)
        changeFlag("edit", idOrder)

    }

    fun changeFlag(valueFlag:String, idOrder: String ){
        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("orders")
            .document(idOrder)
            .update("orderFlag", valueFlag)
    }

    fun loadUser(): Seller?{
        val sp = getSharedPreferences("u-market", AppCompatActivity.MODE_PRIVATE)
        val json = sp?.getString("user","NO_USER")

        if(json == "NO_USER"){
            return null
        } else{
            return Gson().fromJson(json, Seller::class.java)
        }
    }
}