package com.icesi.umarket.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentEditProductDialogBinding
import com.icesi.umarket.model.Product

class EditProductDialogFragment : DialogFragment() {

    /// View
    private lateinit var _binding: FragmentEditProductDialogBinding
    private val binding get() = _binding!!

    /// Objects
    private lateinit var product : Product
    private lateinit var newProduct : Product

    /// Listeners
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductDialogBinding.inflate(inflater, container, false)
        loadData()

        _binding.acceptChangesButton.setOnClickListener {
            if(checkData()){
                onProductSellerObserver.editProduct(newProduct)
            }else{
                Toast.makeText(requireContext(), "Faltan campos por completar", Toast.LENGTH_SHORT).show()
            }
        }

        _binding.cancelChangesButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun checkData() : Boolean {
        val name= binding.nameProductEditSeller.text.toString()
        val description= binding.descriptProductEditSeller.text.toString()
        val price= binding.priceProductEditSeller.text.toString()
        val amount = binding.amountProductEdit.text.toString()

        if(name != "" && description != "" && price !="" && amount !=""){
            val priceInt = Integer.parseInt(price)
            val amountInt = Integer.parseInt(amount)
            newProduct = Product(product.id, name, priceInt,description, product.imageID , amountInt)
            return true
        }else{
            return false
        }
    }

    private fun loadData(){
        binding.nameProductEditSeller.setText(product.name)
        binding.descriptProductEditSeller.setText(product.description)
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