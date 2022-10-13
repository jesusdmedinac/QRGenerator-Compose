package androidmads.example

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity

class GenQRActivity : AppCompatActivity() {
    private var imageQR: ImageView? = null
    private var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_q_r)
        imageQR = findViewById(R.id.imageQR)
        val bundle = intent.extras
        //        String name = bundle.getString("name");
//        String address = bundle.getString("postal");
//        String phone = bundle.getString("phone");
//        String email = bundle.getString("email");
//        String notes = bundle.getString("notes");
//        String organization = bundle.getString("company");
//        String url = bundle.getString("data");
//        Toast.makeText(this, name + address + phone, Toast.LENGTH_SHORT).show();


        //setting the data as null and bundle of data from the previous activity because of the type of the QR
        val qrgEncoder = QRGEncoder(null, bundle, QRGContents.Type.CONTACT, 500)
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            imageQR?.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.v(TAG, e.toString())
        }
    }

    companion object {
        private const val TAG = "QRActivity"
    }
}