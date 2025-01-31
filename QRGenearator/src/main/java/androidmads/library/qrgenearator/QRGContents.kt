package androidmads.library.qrgenearator

import android.provider.ContactsContract

object QRGContents {
    // When using Type.CONTACT, these arrays provide the keys for adding or retrieving multiple
    // phone numbers and addresses.
    @JvmField
    val PHONE_KEYS = arrayOf(
        ContactsContract.Intents.Insert.PHONE,
        ContactsContract.Intents.Insert.SECONDARY_PHONE,
        ContactsContract.Intents.Insert.TERTIARY_PHONE
    )
    val PHONE_TYPE_KEYS = arrayOf(
        ContactsContract.Intents.Insert.PHONE_TYPE,
        ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
        ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
    )
    @JvmField
    val EMAIL_KEYS = arrayOf(
        ContactsContract.Intents.Insert.EMAIL,
        ContactsContract.Intents.Insert.SECONDARY_EMAIL,
        ContactsContract.Intents.Insert.TERTIARY_EMAIL
    )
    val EMAIL_TYPE_KEYS = arrayOf(
        ContactsContract.Intents.Insert.EMAIL_TYPE,
        ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
        ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE
    )

    object ImageType {
        const val IMAGE_PNG = 0
        const val IMAGE_JPEG = 1
        const val IMAGE_WEBP = 2
    }

    object Type {
        // Plain text. Use Intent.putExtra(DATA, string). This can be used for URLs too, but string
        // must include "http://" or "https://".
        const val TEXT = "TEXT_TYPE"

        // An email type. Use Intent.putExtra(DATA, string) where string is the email address.
        const val EMAIL = "EMAIL_TYPE"

        // Use Intent.putExtra(DATA, string) where string is the phone number to call.
        const val PHONE = "PHONE_TYPE"

        // An SMS type. Use Intent.putExtra(DATA, string) where string is the number to SMS.
        const val SMS = "SMS_TYPE"
        const val CONTACT = "CONTACT_TYPE"
        const val LOCATION = "LOCATION_TYPE"
    }
}