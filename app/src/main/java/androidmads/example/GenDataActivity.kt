package androidmads.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class GenDataActivity : AppCompatActivity() {
    private var editTextName: EditText? = null
    private var editTextAddress: EditText? = null
    private var editTextPhone: EditText? = null
    private var editTextAddressMail: EditText? = null
    private var editTextNotes: EditText? = null
    private var editTextOrganization: EditText? = null
    private var editTextURL: EditText? = null
    private var btnGenerate: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_data)
        editTextName = findViewById(R.id.editTextName)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAddressMail = findViewById(R.id.editTextAddressMail)
        btnGenerate = findViewById(R.id.btnGenerate)
        editTextNotes = findViewById(R.id.editTextNotes)
        editTextOrganization = findViewById(R.id.editTextOrganization)
        editTextURL = findViewById(R.id.editTextURL)
        btnGenerate?.setOnClickListener { //keys in bundle correspond to the fields in the ContactsContract.class
            val intent = Intent(applicationContext, GenQRActivity::class.java)
            val bundle = Bundle()
            bundle.putString("name", editTextName?.text.toString())
            bundle.putString("postal", editTextAddress?.text.toString())
            bundle.putString("phone", editTextPhone?.text.toString())
            bundle.putString("email", editTextAddressMail?.text.toString())
            bundle.putString("notes", editTextNotes?.text.toString())
            bundle.putString("company", editTextOrganization?.text.toString())
            bundle.putString("data", editTextURL?.text.toString())
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}