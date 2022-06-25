package com.icesi.umarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentEditProductDialogBinding
import com.icesi.umarket.databinding.FragmentProductSellerBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.seller.SellerMainOverviewFragment

class EditProductDialogFragment : DialogFragment() {

    private lateinit var _binding: FragmentEditProductDialogBinding
    private val binding get() = _binding!!
    private lateinit var product : Product
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductDialogBinding.inflate(inflater, container, false)
        loadData()

        binding.acceptChangesButton.setOnClickListener {
            var name= binding.nameProductEditSeller.text.toString()
            var description= binding.descriptProductEditSeller.text.toString()
            var price= Integer.parseInt(binding.priceProductEditSeller.text.toString())
            var newproduct = Product(product.id, name, price,description, product.imageID)
            onProductSellerObserver.editProduct(newproduct)
        }

        binding.cancelChangesButton.setOnClickListener {
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun loadData(){
        var price = Integer.parseInt(product.price.toString())
        Log.e("Product in Dialog",product.name +" - "+product.description+" - "+product.price )
        binding.descriptProductEditSeller.setText(product.description)
        binding.nameProductEditSeller.setText(product.name)
        //binding.priceProductEditSeller.setText(price)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        var dialog = AlertDialog.Builder(requireContext())
            .setView(inflater.inflate(R.layout.fragment_edit_product_dialog, null))
            .create()
        return dialog
    }

    fun setProduct(product: Product) {
        this.product = product
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProductDialogFragment()
        const val TAG = "EditProductDialogFragment"
    }
}