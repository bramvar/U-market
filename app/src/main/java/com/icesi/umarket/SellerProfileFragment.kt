package com.icesi.umarket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentSellerOrderOverviewBinding
import com.icesi.umarket.databinding.FragmentSellerProfileBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Seller
import com.icesi.umarket.util.Util
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentSellerProfileBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product

class SellerProfileFragment : Fragment() {

    private var _binding: FragmentSellerProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var market: Market
    private lateinit var seller: Seller

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerProfileBinding.inflate(inflater, container, false)
        getMarketInfo()

        binding.logoutSellerBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.settingsSellerBtn.setOnClickListener {
            startActivity(
                Intent(requireActivity(), SellerEditProfileActivity::class.java).apply {
                    putExtra("currentSeller", Gson().toJson(seller))
                }
            )
        }

        return binding.root
    }

    private fun getMarketInfo(){
        Util.loadImage(market.imageID, binding.profilePhotoSeller, "market-image-profile")
        binding.nameMarket.setText(market.marketName)
        binding.emailSeller.setText(seller.email)
        binding.phoneSeller.setText(market.phoneNumber)
    }

    fun setUser(user: Seller) {
        seller = user
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                market = it.toObject(Market::class.java)!!
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerProfileFragment()

    }

    fun downloadMarketProfileImage(imageID: String?){
        if(imageID == null) return

        Firebase.storage.reference.child("market-image-profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.MarketImageProfile).load(it).into(binding.MarketImageProfile)
            }
    }

    fun getProducts(marketID: String){
        Firebase.firestore.collection("markets").document(marketID).collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
                for(doc in product.result!!){
                    val prod =doc.toObject(Product::class.java)
                    adapter.addProduct(prod)
                }

            }
    }

}