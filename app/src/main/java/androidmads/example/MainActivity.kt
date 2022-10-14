package androidmads.example

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private var edtValue: EditText? = null
    private var qrImage: ImageView? = null
    //private var inputValue: String? = null
    private val savePath = Environment.getExternalStorageDirectory().path + "/QRCode/"
    private var bitmap: Bitmap? = null
    private var qrgEncoder: QRGEncoder? = null
    private lateinit var activity: ComponentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //qrImage = findViewById(R.id.qr_image)
        //edtValue = findViewById(R.id.edt_value)
        //activity = this
        //findViewById<View>(R.id.generate_barcode).setOnClickListener {
//            inputValue = edtValue?.text.toString().trim { it <= ' ' }
//            if (inputValue!!.length > 0) {
//                val manager = getSystemService(WINDOW_SERVICE) as WindowManager
//                val display = manager.defaultDisplay
//                val point = Point()
//                display.getSize(point)
//                val width = point.x
//                val height = point.y
//                var smallerDimension = if (width < height) width else height
//                smallerDimension = smallerDimension * 3 / 4
//                qrgEncoder = QRGEncoder(
//                    inputValue, null,
//                    QRGContents.Type.TEXT,
//                    smallerDimension
//                )
//                qrgEncoder!!.colorBlack = Color.RED
//                qrgEncoder!!.colorWhite = Color.BLUE
//                try {
//                    bitmap = qrgEncoder!!.bitmap
//                    qrImage?.setImageBitmap(bitmap)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
//                edtValue?.error = resources.getString(R.string.value_required)
//            }
//        }
//        findViewById<View>(R.id.save_barcode).setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    applicationContext,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                try {
//                    val save = QRGSaver().save(
//                        savePath,
//                        edtValue?.text.toString().trim { it <= ' ' },
//                        bitmap!!,
//                        QRGContents.ImageType.IMAGE_JPEG
//                    )
//                    val result = if (save) "Image Saved" else "Image Not Saved"
//                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
//                    edtValue?.setText(null)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    0
//                )
//            }
//        }

        setContent {
            var text: String by remember { mutableStateOf("") }
            var qrImage: Bitmap? by remember { mutableStateOf(null) }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                ) {
                    TextField(
                        value = text,
                        onValueChange = { changedText ->
                            text = changedText
                        },
                        placeholder = { Text(text = "Enter Text") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Button(onClick = {
                            qrImage = generateQrImage(text)
                        }, modifier = Modifier.weight(1f)) {
                            Text(text = "Generate")
                        }

                        Spacer(modifier = Modifier.weight(0.1f))

                        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                            Text(text = "Save")
                        }

                        Spacer(modifier = Modifier.weight(0.1f))

                        Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                            Text(text = "QR contact")
                        }
                    }

                    qrImage?.asImageBitmap()?.let {
                        Image(bitmap = it, contentDescription = "QRGenerator")
                    }
                }
            }
        }
    }

    fun generateQrImage(text: String): Bitmap? {
        //inputValue = edtValue?.text.toString().trim { it <= ' ' }
        println("dani 1 $text")
        if (text.isNotEmpty()) {
            val manager = getSystemService(WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            var smallerDimension = if (width < height) width else height
            smallerDimension = smallerDimension * 3 / 4

            println("dani 2 $width")
            println("dani 3 $height")

            val qrgEncoder = QRGEncoder(
                text,
                null,
                QRGContents.Type.TEXT,
                smallerDimension,
            )
            qrgEncoder.colorBlack = Color.RED
            qrgEncoder.colorWhite = Color.BLUE
            return try {
                qrgEncoder.bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return null
            //edtValue?.error = resources.getString(R.string.value_required)
        }
    }

    fun goto_CQBarcode(view: View?) {
        startActivity(Intent(applicationContext, GenDataActivity::class.java))
    }
}