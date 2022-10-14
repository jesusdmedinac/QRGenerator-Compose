package androidmads.library.qrgenearator

import com.google.zxing.BarcodeFormat
import androidmads.library.qrgenearator.QRGContents
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.provider.ContactsContract
import android.graphics.Bitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class QRGEncoder(
    data: String? = null,
    bundle: Bundle? = null,
    type: String = QRGContents.Type.TEXT,
    private val dimension: Int = Int.MIN_VALUE,
) {
    var colorWhite = -0x1
    var colorBlack = -0x1000000
    private var contents: String? = null
    private var displayContents: String? = null
    var title: String? = null
        private set
    private var format: BarcodeFormat? = null
    private var encoded = false

    init {
        println("dani init $data")
        println("dani init $bundle")
        println("dani init $type")
        encoded = encodeContents(data, bundle, type)
    }

    private fun encodeContents(data: String?, bundle: Bundle?, type: String): Boolean {
        // Default to QR_CODE if no format given.
        format = BarcodeFormat.QR_CODE
        if (format == BarcodeFormat.QR_CODE) {
            format = BarcodeFormat.QR_CODE
            encodeQRCodeContents(data, bundle, type)
        } else if (data != null && data.length > 0) {
            contents = data
            displayContents = data
            title = "Text"
        }
        return contents != null && contents!!.length > 0
    }

    private fun encodeQRCodeContents(data: String?, bundle: Bundle?, type: String) {
        var data = data
        when (type) {
            QRGContents.Type.TEXT -> if (data != null && data.length > 0) {
                contents = data
                displayContents = data
                title = "Text"
            }
            QRGContents.Type.EMAIL -> {
                data = trim(data)
                if (data != null) {
                    contents = "mailto:$data"
                    displayContents = data
                    title = "E-Mail"
                }
            }
            QRGContents.Type.PHONE -> {
                data = trim(data)
                if (data != null) {
                    contents = "tel:$data"
                    displayContents = PhoneNumberUtils.formatNumber(data)
                    title = "Phone"
                }
            }
            QRGContents.Type.SMS -> {
                data = trim(data)
                if (data != null) {
                    contents = "sms:$data"
                    displayContents = PhoneNumberUtils.formatNumber(data)
                    title = "SMS"
                }
            }
            QRGContents.Type.CONTACT -> if (bundle != null) {
                val newContents = StringBuilder(100)
                val newDisplayContents = StringBuilder(100)
                newContents.append("BEGIN:VCARD\n")
                val name = trim(bundle.getString(ContactsContract.Intents.Insert.NAME))
                if (name != null) {
                    newContents.append("N:").append(escapeVCard(name)).append(';')
                    newDisplayContents.append(name)
                    newContents.append("\n")
                }
                val address = trim(bundle.getString(ContactsContract.Intents.Insert.POSTAL))
                if (address != null) {
                    //the append ; is removed because it is unnecessary because we are breaking into new row
                    newContents.append("ADR:").append(escapeVCard(address)) //.append(';')
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(address)
                }
                val uniquePhones: MutableCollection<String> = HashSet(QRGContents.PHONE_KEYS.size)
                run {
                    var x = 0
                    while (x < QRGContents.PHONE_KEYS.size) {
                        val phone = trim(bundle.getString(QRGContents.PHONE_KEYS[x]))
                        if (phone != null) {
                            uniquePhones.add(phone)
                        }
                        x++
                    }
                }
                for (phone in uniquePhones) {
                    newContents.append("TEL:").append(escapeVCard(phone)) //.append(';')
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(PhoneNumberUtils.formatNumber(phone))
                }
                val uniqueEmails: MutableCollection<String> = HashSet(QRGContents.EMAIL_KEYS.size)
                var x = 0
                while (x < QRGContents.EMAIL_KEYS.size) {
                    val email = trim(bundle.getString(QRGContents.EMAIL_KEYS[x]))
                    if (email != null) {
                        uniqueEmails.add(email)
                    }
                    x++
                }
                for (email in uniqueEmails) {
                    newContents.append("EMAIL:").append(escapeVCard(email)) //.append(';')
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(email)
                }
                val organization = trim(bundle.getString(ContactsContract.Intents.Insert.COMPANY))
                if (organization != null) {
                    newContents.append("ORG:").append(organization) //.append(';')
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(organization)
                }
                val url = trim(bundle.getString(ContactsContract.Intents.Insert.DATA))
                if (url != null) {
                    // in this field only the website name and the domain are necessary (example : somewebsite.com)
                    newContents.append("URL:").append(escapeVCard(url)) //.append(';');
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(url)
                }
                val note = trim(bundle.getString(ContactsContract.Intents.Insert.NOTES))
                if (note != null) {
                    newContents.append("NOTE:").append(escapeVCard(note)) //.append(';')
                    newContents.append("\n")
                    newDisplayContents.append('\n').append(note)
                }

                // Make sure we've encoded at least one field.
                if (newDisplayContents.length > 0) {
                    //this end vcard needs to be at the end in order for the default phone reader to recognize it as a contact
                    newContents.append("END:VCARD")
                    newContents.append(';')
                    contents = newContents.toString()
                    displayContents = newDisplayContents.toString()
                    title = "Contact"
                } else {
                    contents = null
                    displayContents = null
                }
            }
            QRGContents.Type.LOCATION -> if (bundle != null) {
                // These must use Bundle.getFloat(), not getDouble(), it's part of the API.
                val latitude = bundle.getFloat("LAT", Float.MAX_VALUE)
                val longitude = bundle.getFloat("LONG", Float.MAX_VALUE)
                if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
                    contents = "geo:$latitude,$longitude"
                    displayContents = "$latitude,$longitude"
                    title = "Location"
                }
            }
        }
    }

    // All are 0, or black, by default
    val bitmap: Bitmap
        get() = when {
            !encoded -> throw Exception("Encoded should not be null")
            else -> {
                var hints: MutableMap<EncodeHintType?, Any?>? = null
                val encoding = guessAppropriateEncoding(contents)
                if (encoding != null) {
                    hints = EnumMap(EncodeHintType::class.java)
                    hints[EncodeHintType.CHARACTER_SET] = encoding
                }
                val writer = MultiFormatWriter()
                val result = writer.encode(contents, format, dimension, dimension, hints)
                val width = result.width
                val height = result.height
                val pixels = IntArray(width * height)
                // All are 0, or black, by default
                for (y in 0 until height) {
                    val offset = y * width
                    for (x in 0 until width) {
                        pixels[offset + x] = if (result[x, y]) colorBlack else colorWhite
                    }
                }
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
                bitmap
            }
        }

    private fun guessAppropriateEncoding(contents: CharSequence?): String? {
        // Very crude at the moment
        for (i in 0 until contents!!.length) {
            if (contents[i].code > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }

    private fun trim(s: String?): String? {
        if (s == null) {
            return null
        }
        val result = s.trim { it <= ' ' }
        return if (result.length == 0) null else result
    }

    private fun escapeVCard(input: String?): String? {
        if (input == null || input.indexOf(':') < 0 && input.indexOf(';') < 0) {
            return input
        }
        val length = input.length
        val result = StringBuilder(length)
        for (i in 0 until length) {
            val c = input[i]
            if (c == ':' || c == ';') {
                result.append('\\')
            }
            result.append(c)
        }
        return result.toString()
    }
}