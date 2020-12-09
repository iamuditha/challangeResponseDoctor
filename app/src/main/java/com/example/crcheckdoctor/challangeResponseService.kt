package com.example.crcheckdoctor

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.crcheckdoctor.challangeResponse.ChallengeResponse

class challangeResponseService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        ChallengeResponse("did").challengeResponse()
    }
}
