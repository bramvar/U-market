package com.icesi.umarket.consumer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.icesi.umarket.databinding.FragmentProductBinding
import com.icesi.umarket.model.*
import com.icesi.umarket.util.Constants
import com.icesi.umarket.util.Util

class ProductFragment : Fragment() {

    /// View
    private lateinit var _binding: FragmentProductBinding
    private val binding get() = _binding!!

    /// Objects
    private lateinit var currentUser: User
    private lateinit var currentMarket: Market
    private lateinit var product: Product

    /// Listeners
    lateinit var onOrderObserver: ConsumerMainOverviewFragment.SellerObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater,container,false)
        loadInfo()

        binding.moreText.setOnClickListener {
            changeAmountText(true)
        }

        binding.lessText.setOnClickListener {
            changeAmountText(false)
        }

        binding.pedirBtn.setOnClickListener {
            onOrderObserver.loadOrder(buildOrder())
        }

        return _binding.root
    }

    private fun buildOrder(): Order{
        val amount:Int = Integer.valueOf(binding.amountText.text.toString())
        val name = binding.productName.text.toString()
        val price = Integer.valueOf(binding.priceProduct.text.toString().replace("$",""))
        val totalPrice = price*amount

        return Order(amount,name,price, totalPrice, product.imageID, currentMarket.id,
            product.id,"", Constants.pendentFlag, currentUser.id, Timestamp.now())
    }

    private fun loadInfo(){
        binding.productName.text = product.name
        binding.productInfo.text = product.description
        binding.priceProduct.text = "$" + product.price.toString()
        binding.amoutStockProduct.text  = product.amount.toString()
        Util.loadImage(product.imageID, binding.productImg,"product-images")
    }

    private fun changeAmountText(state: Boolean){
        var amount = Integer.parseInt(_binding.amountText.text.toString())
        val productStock = product.amount
        when(state){
            true -> if(amount<productStock){ amount++ }
            false -> if(amount>1){amount--}
        }
        binding.amountText.text = amount.toString()
    }

    fun setMarket(market: Market){
        currentMarket = market
    }

    fun setProduct(product: Product){
        this.product = product
    }

    fun setUser(user: User){
        currentUser = user
    }

    companion object {
        fun newInstance() = ProductFragment()
    }
}