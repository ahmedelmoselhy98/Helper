import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val TAG = "FacebookLoginHelper"

class FacebookLoginHelper(private val activity: AppCompatActivity) {

    //   implementation 'com.facebook.android:facebook-login:12.0.0'
    val userAccountLivedata = MutableLiveData<JSONObject?>()
    fun singIn() {
        LoginManager.getInstance()?.logOut()
        LoginManager.getInstance().logInWithReadPermissions(
            activity, callbackManager,
            listOf("email", "public_profile")
        )
    }

    private fun printHashKey() {
        try {
            val info: PackageInfo = activity.packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
//                FirebaseDatabase.getInstance().getReference("key").setValue(hashKey)
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    init {
        printHashKey()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    if (loginResult.accessToken.token == null) {
                        AccessToken.refreshCurrentAccessTokenAsync()
                        return
                    }
                    requestData()
                }

                override fun onCancel() {
                    userAccountLivedata.value = null
                    Toast.makeText(activity, "login cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {
//                    exception.logE(TAG, "onError facebook")
                    userAccountLivedata.value = null
                    AccessToken.refreshCurrentAccessTokenAsync()
                    Log.e("mmmmmmm","error "+ exception.message)
                    Toast.makeText(
                        activity,
                        "error " + exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun requestData() {
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            val json = response?.jsonObject
            try {
                if (json != null) {
//                    json.toString().logD(TAG, "get user data facebook login")
                    Log.d(TAG, "requestData: $json")
                    userAccountLivedata.value = json
                    /*    providerName = "facebook",
                        providerID = json.optString("id"),
                        email = json.optString("email"),
                        name = json.optString("name"),
                        profileImage = "https://graph.facebook.com/${json.optString("id")}/picture?type=large"
                    */

                }
            } catch (e: JSONException) {
                userAccountLivedata.value = null
//                e.logE(TAG, "facebook login")
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()
    }
}