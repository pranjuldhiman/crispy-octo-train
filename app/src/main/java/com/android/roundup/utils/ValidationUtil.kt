package com.android.roundup.utils

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

object ValidationUtil {

    private val EmailValidation by lazy { Pattern.compile("[a-zA-Z0-9_\\-\\&\\*\\+='/\\{\\}~][a-zA-Z0-9_\\-\\.&\\*\\+='/\\{\\}~]* ") }

    fun isEmailValid(email: String) = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

 /*   fun toast(context:Context,text:String)
    {
        var toast: Toast = Toast.makeText(context, text.toString(), Toast.LENGTH_LONG);
        var toastView : View = toast.getView(); // This'll return the default View of the Toast.
        val drawable = ContextCompat.getDrawable(context as AppCompatActivity, R.drawable.toast_round_background)
        var toastMessage: TextView =  toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(14f);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.background = drawable
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setPadding(20,0,20,0)
       // toastMessage.setCompoundDrawablePadding(18);
        toastView.setBackgroundColor(Color.TRANSPARENT);
        toast.show();
    }*/
}