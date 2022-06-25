package com.icesi.umarket.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.EditProductDialogFragment
import com.icesi.umarket.R
import com.icesi.umarket.SellerOrderOverviewFragment
import com.icesi.umarket.SellerProfileFragment
import com.icesi.umarket.databinding.ActivitySellerHomeBinding
import com.icesi.umarket.model.AuxOrder
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller

class SellerHomeActivity : AppCompatActivity(),
    SellerMainOverviewFragment.OnProductsOnSellerObserver,
    SellerOrderOverviewFragment.OnConfirmOrderListener {

    private var sellerProfileFragment = SellerProfileFragment.newInstance()
    private var newProductFragment = NewProductFragment.newInstance()
    private var sellerMainOverviewFragment = SellerMainOverviewFragment.newInstance()
    private var productSellerFragment = ProductSellerFragment.newInstance()
    private var sellerOrderOverviewFragment = SellerOrderOverviewFragment.newInstance()
    private var editProductDialogFragment =  EditProductDialogFragment.newInstance()


    private lateinit var user: Seller
    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = Gson().fromJson(intent.extras?.getString("currentUser",""), Seller::class.java)
        loadUser()
        loadAdapters()
        showFragment(sellerMainOverviewFragment, true)

        binding.sellerNavigator.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem ->  showFragment(sellerMainOverviewFragment, true)
                R.id.addProductItem -> showFragment(newProductFragment, true)
                R.id.ordersItem -> showFragment(sellerOrderOverviewFragment, true)
                R.id.profileItem -> showFragment(sellerProfileFragment, true)
            }
            true
        }
    }

    private fun loadUser(){
        newProductFragment.setUser(user)
        sellerMainOverviewFragment.setUser(user)
        sellerOrderOverviewFragment.setUser(user)
        sellerProfileFragment.setUser(user)
        productSellerFragment.setUser(user)

    }

    private fun loadAdapters(){
        sellerMainOverviewFragment.adapter.onProductSellerObserver = this
        editProductDialogFragment.onProductSellerObserver = this
        sellerOrderOverviewFragment.adapterOrder.onOrderConfirmObserver = this
        newProductFragment.listener = sellerMainOverviewFragment
    }

    override fun onBackPressed() {
    }

    private fun showFragment(fragment: Fragment, showBar: Boolean){
        binding.sellerNavigator.isVisible = showBar
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sellerFragmentContainer,fragment)
        transaction.commit()
    }

    override fun sendProduct(product: Product) {
        productSellerFragment.setCurrentProduct(product)
        productSellerFragment.onProductSellerObserver = this
        showFragment(productSellerFragment, false)
    }

    override fun showEditProduct(product: Product) {
        editProductDialogFragment.setProduct(product)
        editProductDialogFragment.show(supportFragmentManager,editProductDialogFragment.tag)
    }

    override fun deleteProduct(product: Product) {
        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("products")
            .document(product.id)
            .delete()
        showFragment(sellerMainOverviewFragment, true)
    }

    override fun editProduct(product: Product) {
        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("products")
            .document(product.id)
            .set(product)
        showFragment(sellerMainOverviewFragment, true)
        editProductDialogFragment.dismiss()
        Log.e(" EDIT PRODUCT ", "entro")
    }

    override fun backToOverview() {
        showFragment(sellerMainOverviewFragment, false)
    }

    override fun confirmOrder(idOrder: String, idUser: String){
        changeFlag("exitosa", idOrder, idUser)
    }

    override fun cancelOrder(idOrder: String, idUser: String){
        changeFlag("cancelada", idOrder, idUser)
    }

    override fun editOrder(idOrder: String, idUser: String){
        changeFlag("editada", idOrder, idUser)
    }

    private fun changeFlag(valueFlag:String, idOrder: String, idUser: String){
        Firebase.firestore.collection("orders")
            .document(idOrder)
            .update("orderFlag", valueFlag)


        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("pendentOrders")
            .document(idOrder)
            .delete()

        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("historyOrders")
            .document(idOrder)
            .set(AuxOrder(idOrder))
            .addOnSuccessListener {
                sellerOrderOverviewFragment.reloadOrders(idOrder)
            }
    }
}