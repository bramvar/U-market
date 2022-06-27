package com.icesi.umarket.consumer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentConfirmPurchaseDiaglogBinding

class ConfirmPurchaseDiaglogFragment : DialogFragment() {
    private lateinit var _binding: FragmentConfirmPurchaseDiaglogBinding
    lateinit var orderText: String
    lateinit var onConfirmPurchaseObserver: ConfirmPurchaseObserver

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
        _binding = FragmentConfirmPurchaseDiaglogBinding.inflate(inflater, container, false)
        _binding.acceptOrderConsumerBtn.setOnClickListener {
            onConfirmPurchaseObserver.confirm()
            dismiss()
        }

        _binding.discardOrderConsumerBtn.setOnClickListener {
            onConfirmPurchaseObserver.discard()
            dismiss()
        }

        _binding.keepBuyingConsumerBtn.setOnClickListener {
            dismiss()
        }
            return _binding.root
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }

    interface ConfirmPurchaseObserver{
        fun confirm()
        fun discard()
    }
}





