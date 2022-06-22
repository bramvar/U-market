package com.icesi.umarket.seller

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.icesi.umarket.databinding.FragmentNewProductBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller
import com.icesi.umarket.util.Util
import java.io.File
import java.util.*

class NewProductFragment : Fragment() {

    private var _binding: FragmentNewProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: Seller

    private var file: File? = null
    private lateinit var productImageUri: Uri

    var listener: OnNewProductListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewProductBinding.inflate(inflater, container, false)

        binding.newProductImage.setOnClickListener {
            loadImage()
        }

        binding.postNewProductBtn.setOnClickListener {
            uploadProduct()
        }

        return binding.root
    }

    private fun uploadProduct() {
        val product = buildProduct()
        if (checkFields(product)) {
            Firebase.firestore.collection("markets")
                .document(user.marketID)
                .collection("products")
                .document(product.id)
                .set(product)
                .addOnSuccessListener {
                    if (Util.sendImg(product.imageID, "product-images", productImageUri)){
                        clearNewProductFields()
                        Toast.makeText(activity, "Producto publicado", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun buildProduct(): Product{
        val productID = UUID.randomUUID().toString()
        val productName = binding.nameNewProductTextFiled.text.toString()
        val productPrice = Integer.parseInt(binding.priceNewProductTextField.text.toString())
        val productDescription = binding.descriptionNewProductTextField.text.toString()
        val imageID = UUID.randomUUID().toString()
        return Product(productID,productName,productPrice,productDescription,imageID)
    }

    private fun checkFields(product: Product): Boolean{
        var flag = true
        if (product.name == "" || product.price == 0 || product.description == ""){
            Toast.makeText(activity,"Campos invalidos", Toast.LENGTH_LONG).show()
            flag=false
        }
        return flag
    }

    private fun clearNewProductFields(){
        binding.nameNewProductTextFiled.text.clear()
        binding.priceNewProductTextField.text.clear()
        binding.descriptionNewProductTextField.text.clear()
        binding.newProductImage.setImageURI(null)
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

    private fun loadImage(){
        val camLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onCameraResult)
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onGalleryResult)

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
                .joinToString("")
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${activity?.getExternalFilesDir(null)}/"+randomString+".png")
            val uri = FileProvider.getUriForFile(requireActivity(), "com.icesi.umarket", file!!)
            productImageUri = uri
            i.putExtra(MediaStore.EXTRA_OUTPUT,uri)
            camLauncher.launch(i)
        }
        myAlertDialog.show()
    }

    fun setUser(user: Seller){
        this.user = user
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