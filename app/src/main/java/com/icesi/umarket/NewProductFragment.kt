package com.icesi.umarket

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentNewProductBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.util.UtilDomi
import java.io.File
import java.util.*

class NewProductFragment : Fragment() {

    val STRING_LENGTH = 10
    val ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+"

    private var _binding: FragmentNewProductBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null
    private lateinit var productImageUri: Uri
    private lateinit var user: Seller

    var listener: OnNewProductListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewProductBinding.inflate(inflater, container, false)

        val camLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onCameraResult)
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onGalleryResult)

        user = loadUser()!!

        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ),1)


        binding.newProductImage.setOnClickListener {
            val myAlertDialog = AlertDialog.Builder(this.context)
            myAlertDialog.setTitle("Upload Pictures Option")
            myAlertDialog.setMessage("How do you want to set your picture?")

            myAlertDialog.setPositiveButton("Gallery") { arg0, arg1 ->
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    galleryLauncher.launch(intent)
                }

            myAlertDialog.setNegativeButton("Camera") { arg0, arg1 ->
                val STRING_LENGTH = 10
                val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                val randomString = (1..STRING_LENGTH)
                    .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("");
                    val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    file = File("${activity?.getExternalFilesDir(null)}/"+randomString+".png")
                    val uri = FileProvider.getUriForFile(requireActivity(), "com.icesi.umarket", file!!)            //i.putExtra(MediaStore.EXTRA_OUTPUT,uri)
                    i.putExtra(MediaStore.EXTRA_OUTPUT,uri)
                    camLauncher.launch(i)
                }
            myAlertDialog.show()
        }

        binding.postNewProductBtn.setOnClickListener {

            val productID = UUID.randomUUID().toString()
            val productName = binding.nameNewProductTextFiled.text.toString()
            val productPrice = binding.priceNewProductTextField.text.toString()
            val productDescription = binding.descriptionNewProductTextField.toString()
            val ImageID = UUID.randomUUID().toString()

            if( productName.isBlank() or productPrice.isBlank() or productDescription.isBlank()){
                Toast.makeText(activity,"Faltan campos por completar", Toast.LENGTH_LONG).show()
            }else{
                //it.onNewProduct(UUID.randomUUID().toString(),productName,productPrice.toInt(),productDescription,productImage!!)
                val product = Product(productID,productName,productPrice.toInt(),productDescription,ImageID)
                Firebase.firestore.collection("markets").document(user.marketID).collection("products").document(productID).set(product)
                    .addOnSuccessListener {
                        Firebase.storage.getReference().child("product-images").child(ImageID).putFile(productImageUri)
                            .addOnSuccessListener {
                                Toast.makeText(activity,"producto publicado", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener{
                                Toast.makeText(activity,it.message, Toast.LENGTH_LONG).show()
                            }
                    }.addOnFailureListener{
                        Toast.makeText(activity,it.message, Toast.LENGTH_LONG).show()
                    }

            }


        }

        return binding.root
    }

    private fun onCameraResult(activityResult: ActivityResult){
        if(activityResult.resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            val thumpnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/4, bitmap.height/4, true)
            binding.newProductImage.setImageBitmap(thumpnail)

        }
    }

    private fun onGalleryResult(activityResult: ActivityResult){
        if(activityResult.resultCode == AppCompatActivity.RESULT_OK){
            productImageUri = activityResult.data?.data!!
            binding.newProductImage.setImageURI(productImageUri)
        }
    }

    fun loadUser(): Seller?{
        val sp = this.context?.getSharedPreferences("u-market", AppCompatActivity.MODE_PRIVATE)
        val json = sp?.getString("user","NO_USER")

        if(json == "NO_USER"){
            return null
        } else{
            return Gson().fromJson(json, Seller::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnNewProductListener{
        fun onNewProduct(id:String,productName:String,productPrice:Int,productDescription:String, productImage:String )
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewProductFragment()
    }
}