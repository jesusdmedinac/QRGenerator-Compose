package androidmads.example

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.os.Environment
import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGEncoder
import android.os.Bundle
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidmads.library.qrgenearator.QRGSaver
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.widget.ImageView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var edtValue: EditText? = null
    private var qrImage: ImageView? = null
    private var inputValue: String? = null
    private val savePath = Environment.getExternalStorageDirectory().path + "/QRCode/"
    private var bitmap: Bitmap? = null
    private var qrgEncoder: QRGEncoder? = null
    private lateinit var activity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        qrImage = findViewById(R.id.qr_image)
        edtValue = findViewById(R.id.edt_value)
        activity = this
        findViewById<View>(R.id.generate_barcode).setOnClickListener {
            inputValue = edtValue?.text.toString().trim { it <= ' ' }
            if (inputValue!!.length > 0) {
                val manager = getSystemService(WINDOW_SERVICE) as WindowManager
                val display = manager.defaultDisplay
                val point = Point()
                display.getSize(point)
                val width = point.x
                val height = point.y
                var smallerDimension = if (width < height) width else height
                smallerDimension = smallerDimension * 3 / 4
                qrgEncoder = QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension
                )
                qrgEncoder!!.colorBlack = Color.RED
                qrgEncoder!!.colorWhite = Color.BLUE
                try {
                    bitmap = qrgEncoder!!.bitmap
                    qrImage?.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                edtValue?.error = resources.getString(R.string.value_required)
            }
        }
        findViewById<View>(R.id.save_barcode).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    val save = QRGSaver().save(
                        savePath,
                        edtValue?.text.toString().trim { it <= ' ' },
                        bitmap!!,
                        QRGContents.ImageType.IMAGE_JPEG
                    )
                    val result = if (save) "Image Saved" else "Image Not Saved"
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                    edtValue?.setText(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
            }
        }
    }

    fun goto_CQBarcode(view: View?) {
        startActivity(Intent(applicationContext, GenDataActivity::class.java))
    }
}