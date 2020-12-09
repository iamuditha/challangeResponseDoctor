package com.example.crcheckdoctor

import ChallengeResponse.MessageSerializerHandler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.crcheckdoctor.challangeResponse.ChallengeResponse
//import com.example.crcheckdoctor.challangeResponseService.MessageObject
//import com.example.crcheckdoctor.challangeResponseService.MessageSerializerHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNext.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
        }

//        val messageSerializerHandler = MessageSerializerHandler()

//        val sMessage = messageSerializerHandler.serialize("helloworld")


    buttonConnectToServer.setOnClickListener{

//        val dMessage = messageSerializerHandler.deserialize(sMessage)
//        Log.i("serilazecheck",sMessage.toString())
//
//        Log.i("serilazecheck",dMessage.toString())
        val thread = Thread{
//            ChallengeResponse("did").challengeResponse()
            challangeResponseService().startService(Intent(this,challangeResponseService::class.java))
        }
        thread.start()

    }
    }




}