import android.app.Activity
import android.content.Intent

import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment

import com.google.firebase.dynamiclinks.ktx.*

import com.google.firebase.ktx.Firebase

fun createDynamicLink(activity: Activity, uid: String, onLinkCreated: (link: String) -> Unit) {

    val dynamicLink =

        Firebase.dynamicLinks.dynamicLink { // or Firebase.dynamicLinks.shortLinkAsync

            link = Uri.parse("https://chefshup.page.link/chefs?uid=$uid")

//            domainUriPrefix = "https://chefshub.page.link/"
            domainUriPrefix = "https://chefshup.page.link/chefs"

            androidParameters(activity.packageName) {

                minimumVersion = 125

            }

            iosParameters("com.example.ios") {

                appStoreId = "123456789"

                minimumVersion = "1.0.1"

            }
        }


    Firebase.dynamicLinks.shortLinkAsync {

        longLink = dynamicLink.uri

    }.addOnSuccessListener { (shortLink, flowChartLink) ->

        onLinkCreated(shortLink.toString())

    }.addOnFailureListener {

        onLinkCreated(dynamicLink.uri.toString())

    }

}


fun Fragment.shareDeepLink(deepLink: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_SUBJECT,
        "You have been shared an amazing meme, check it out ->"
    )
    intent.putExtra(Intent.EXTRA_TEXT, deepLink)
    if (isAdded) {
        requireContext().startActivity(intent)
    }
}