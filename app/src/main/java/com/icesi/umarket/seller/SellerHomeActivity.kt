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
import com.icesi.umarket.*
import com.icesi.umarket.databinding.ActivitySellerHomeBinding
import com.icesi.umarket.model.AuxOrder
import com.icesi.umarket.model.Order
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
    private var editOrderSellerDialog =  EditOrderSellerDialogFragment()

    private lateinit var user: Seller
    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = Gson().fromJson(intent.extras?.getString("currentUser",""), Seller::class.java)
        loadUser()
        loadAdapters()
        showFragment(sellerMainOverviewFragment)

        binding.sellerNavigator.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem ->  showFragment(sellerMainOverviewFragment)
                R.id.addProductItem -> showFragment(newProductFragment)
                R.id.ordersItem -> showFragment(sellerOrderOverviewFragment)
                R.id.profileItem -> showFragment(sellerProfileFragment)
            }
            true
        }
    }

    override fun onBackPressed() {
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
        editOrderSellerDialog.onProductSellerObserver = this
    }


    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sellerFragmentContainer,fragment)
        transaction.commit()
    }

    override fun sendProduct(product: Product) {
        productSellerFragment.setCurrentProduct(product)
        productSellerFragment.onProductSellerObserver = this
        showFragment(productSellerFragment)
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
        showFragment(sellerMainOverviewFragment)
    }

    override fun editProduct(product: Product) {
        Firebase.firestore.collection("markets")
            .document(user.marketID)
            .collection("products")
            .document(product.id)
            .set(product)
        showFragment(sellerMainOverviewFragment)
        editProductDialogFragment.dismiss()
    }

    override fun backToOverview() {
        showFragment(sellerMainOverviewFragment)
    }

    override fun confirmOrder(idOrder: String, idUser: String){
        changeFlag("Exitosa", idOrder)
    }

    override fun cancelOrder(idOrder: String, idUser: String){
        changeFlag("Cancelada", idOrder)
    }

    override fun editOrder(currentOrder: Order){
        editOrderSellerDialog.setOrder(currentOrder)
        editOrderSellerDialog.show(supportFragmentManager,editOrderSellerDialog.tag)
    }

    override fun editOrderSuccessfull(currentOrder: Order){
        var status = "Editada"
        currentOrder.orderFlag = status
        changeFlag(status, currentOrder.idOrder)
        Log.e(" Order in Editor Success: ", currentOrder.orderFlag)
        sellerOrderOverviewFragment.adapterOrder.deleteOrder(currentOrder)
        Firebase.firestore.collection("orders")
            .document(currentOrder.idOrder)
            .set(currentOrder)
    }

    private fun changeFlag(valueFlag:String, idOrder: String){
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