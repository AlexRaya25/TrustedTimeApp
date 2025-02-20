package com.rayadev.trustedtime

import android.app.Application
import android.util.Log
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient

class TrustedTimeApp:Application() {

    companion object{
        var trustedTime: TrustedTimeClient? = null
    }

    override fun onCreate() {
        super.onCreate()
        TrustedTime.createClient(this).addOnSuccessListener { client ->
            trustedTime = client
        }.addOnFailureListener{
            Log.e("Error TrustedTiime", it.message.orEmpty())
            trustedTime
        }
    }
}