//import android.app.Activity
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.MutableLiveData
//import com.chefshub.app.R
//import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.Scopes
//import com.google.android.gms.common.api.Scope
//
//private const val TAG = "GoogleSingingHelper"
//
//class GoogleSingingHelper(private val activity: AppCompatActivity) {
//    private var mGoogleApiClient: GoogleSignInClient? = null
//
//    ///observe it
//    val userAccountLivedata = MutableLiveData<GoogleSignInAccount?>()
//
//    init {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestScopes(Scope(Scopes.EMAIL))
//            .requestIdToken(activity.getString(R.string.default_web_client_id))
//            .requestServerAuthCode(activity.getString(R.string.default_web_client_id))
////               .requestIdToken("679427183868-6m58sspbmi1rggs4qu7jhk09si21dr2h.apps.googleusercontent.com")
////            .requestServerAuthCode("679427183868-6m58sspbmi1rggs4qu7jhk09si21dr2h.apps.googleusercontent.com")
//            .requestEmail()
//            .build()
//        mGoogleApiClient = GoogleSignIn.getClient(activity, gso)
//    }
//
//
//    fun singIn() {
//        try {
//            val signInIntent = mGoogleApiClient?.signInIntent
//            launcher.launch(signInIntent)
//        } catch (ex: Exception) {
//            userAccountLivedata.value = null
//        }
//    }
//
//    private val launcher =
//        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            Log.e("mmmmmmmm" , "test "+result)
//
//            if (result.resultCode == Activity.RESULT_OK) {
//                val results = Auth.GoogleSignInApi.getSignInResultFromIntent(result.data!!)
//
//                if (results != null && results.isSuccess) {
//                    val acct = results.signInAccount
//                    userAccountLivedata.value = acct
//                    Log.e("kkkkkk" , "test "+acct?.email)
//                } else {
//                    userAccountLivedata.value = null
//                }
//            }
//        }
//}








import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.chefshub.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

private const val TAG = "GoogleSignInHelper"

class GoogleSingingHelper(private val activity: AppCompatActivity) {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>

    val userAccountLivedata = MutableLiveData<GoogleSignInAccount?>()

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL))
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestServerAuthCode(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

        launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleSignInResult(result.resultCode, result.data)
        }
    }

    fun singIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        launcher.launch(signInIntent)
    }

    private fun handleSignInResult(resultCode: Int, data: Intent?) {
        Log.e(TAG, "Sign-in result $resultCode data ${data?.data}")
        if (resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                userAccountLivedata.value = account
            } catch (e: ApiException) {
                Log.e(TAG, "Sign-in failed", e)
                userAccountLivedata.value = null
            }
        } else {
            userAccountLivedata.value = null
        }
    }
}
