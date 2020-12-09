package com.example.crcheckdoctor.challangeResponse
import ChallengeResponse.MessageObject
import ChallengeResponse.MessageSerializerHandler
import ChallengeResponse.MessageType
import android.os.Handler
import android.os.Looper
import android.util.Log
//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
import crypto.AsymmetricEncDec
import crypto.KeyHandler
import crypto.PublicPrivateKeyPairGenerator
import io.socket.client.IO
import io.socket.client.Socket

import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.PrivateKey
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.math.floor
//import com.example.crcheckdoctor.challangeResponseService.MessageObject
//import com.example.crcheckdoctor.challangeResponseService.MessageSerializerHandler



class ChallengeResponse(private val id: String) {

    private var isValidated: Boolean = false
//    private  var secretKey: Key? = null;
    private  var decryptionKey: String? = null;
    private val cipher : Cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
    private val privateKeyStrings : String = "-----BEGIN RSA PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPxKZCdjbYx+6zkfVV3o2pomdUZVeBGdwx1JbHWxWjxVCjBOtMvP1Wo4P1+iisYPMgqc/E/scL2Sa6pOwqU66zYZw3wXTU+olWW9HZMYYMsBd2D2Ta6iQcS+cLkDnpVW9IeCumaWhLJZzdUJGEWXNVbQtFh/0qyn42w+mjGsYzhLlV1WC7Mi6t2go3ClkhiUSCb6XNJCUmNz8fbMcLtPEYaMl7XML8CB2JIxrDUzhAVfvJHeFO0m/62Y0YEgXsYJ2MH5sc4g4OGD6B15bRmT7XCVcd7VkhN/P+p7UCrlSHgbRFRvTYP6GLvAZHXQHSyEI1bAo5VdGewg9nlKmai17tAgMBAAECggEBAI5VX1D/Xk+CqTUItP5dhpzCF1C7wEUlSBXhZaFa8PKhn7K1ZQslNB85ZaT6FJiMp0fYNsXOg1uckjZInzNMg/I+Gf4tPjC5FYz0/K3t1Mvs7l1I4k3FbxjAoK6XDG6Q0crKhlSLmOCFDWcio96G/KM/gOBGwQNDVG99yGpdUIqhRzHnauaK572puWXaua3/hLBNhxOz5cDTPqXefQQafRdUv5srjt3cM0YFh8NEjRG0G8fUBCoVVkAsvpT9jHwn90nZkOG/mv+ou8d0miXiEj1yHGNS1MOpS+3IhsEx7wi9bqH3uVJ5jiIXT61Wu/FTLd6yvixiBIF6+BZwK+EngfUCgYEA1RGBTbKEmbi41eL1PP34PFk4hAHbm/Aag5ktzG3eSp3cqHDxf5MLiVT10AwPeS9BIZ1BZMo7MTx1eyy5vrFWBmy5hUae8wY1KcATsPAPj01/DmqxKCh8HwYYxY1I/wB5V3DW/aAHj/Kqf7ptVFh4JPKj+NKgynlFV9aP/lrIBhsCgYEArLx//kG4RvSYYD+Ikh7JpOEmFvFfuOYHdcpBUbNhF2rPJQu3aJpwqCjbnZfeWRWxcBpeEQ5hNpYSOmbfPQJDT1uGmgwwK/zgQyDcFPcnmzw8wBhZ5aghKt2Tp/ZNdWWmv3ahU9dG6dsMYakwVR3dLOG/8tGSbP7IhFj2bJifn5cCgYBAahNiTvcBljPjDcNAVjOG4x5P4lPnN9Z8NIIBDLIocsMz9Tk9bVIOia3q4MqIw314URdlBjKQ9ws5WtMo0GoGdTY9EZWaOSU3YwdQ9bJui2o/DIhUcLDdnDU+/DC2TwlgIxAMCyZeIWnkRZMp7bkcynJCFkNgEvugy5AqwH06QwKBgHpB3FFn0HagDe0pao8JLWz+UggxBLAIV0VeRob97/00ArLUrRZjINNUP8Q1xhDFUtXmQ6fMxq9s3i/puZ0jqeJUBFupvrqTe2Lnsf1AxQ6RPuozEgWCzx7YvvZFVKN/s9qVqy99EdrYx9WjeurzfSSUD1hcTkfIuME7egZ4q5m3AoGAeFcJo3EshSXkJZ1mW1Y8r04/WhZzVEzlsbzQGQ6kJWezmsae2JDWaU99MRMwCf3L+rzFCUCpfENcoy0WIv6O9kue2pnhIVV67l+h54tk+svHHvbW9aGjBj8Uhn2/jzfyAZeiibKpH82QIub3hwp2fNTB+g9kiqVdtM1viE3J+Q8=------END RSA PRIVATE KEY-----"

    fun challengeResponse() {

        val opts = IO.Options()
        opts.query = "type=doctor&&id=${id}"


        val socket = IO.socket("https://55d3268447c5.ngrok.io", opts)
//        KeyHandler.getInstance().writePlainKeyPair(PublicPrivateKeyPairGenerator.getInstance().generateRSAKeyPair(),"./","./")


        socket
            .on(Socket.EVENT_CONNECT) {
                Log.i("challengeResponse", "onConnect");

            }.on("fromServer") { parameters ->
                val myJSON = parameters[0] as JSONObject
                val id = myJSON.get("id")
                val msg : String  = myJSON.get("msg") as String
                val challengeCipherc: String = myJSON.get("msg") as String
                Log.i("challangeresponsemsg",challengeCipherc)

                val messageObject = MessageSerializerHandler.getInstance()?.deserialize(challengeCipherc) as MessageObject







                when (messageObject.messageType) {
                    MessageType.CHALLENGE -> challengeHandler(
                        socket,
                        id as String,
                        messageObject.msg
                    )
                    MessageType.RESPONSE -> Log.i("message", "Invalid...........")
                    MessageType.DECRYPTION_KEY -> decryptionKeyHandler(socket, messageObject.msg, id as String);
                    MessageType.TERMINATE -> mTerminate(socket)
                    MessageType.PING -> ping(id as String, socket)
                    MessageType.VALIDATION -> validationHandler(socket, id as String, messageObject.msg)
                }
            }
        socket.on(Socket.EVENT_DISCONNECT) { Log.i("msg", "asdfghjhgfds") }
        socket.connect()
    }

    private fun validationHandler(socket: Socket, id: String, message: String) {
        if(message.equals("True"))  {
            isValidated = true;
            val serializeMsg: String? = MessageSerializerHandler.getInstance()?.serialize(MessageObject(MessageType.PING,"success"))
            socket.emit("sendTo", serializeMsg?.let { createMessage(id, it) })
        } else {
            isValidated = false
            val serializeMsg: String? = MessageSerializerHandler.getInstance()?.serialize(MessageObject(MessageType.PING,"fail"))
            socket.emit("sendTo", serializeMsg?.let { createMessage(id, it) })
        }
    }

    private fun getRandomString(): String? {
        val length = floor(Math.random() * 10 + 20).toInt()
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < length) { // length of the random string.
            val index = (rnd.nextFloat() * chars.length).toInt()
            salt.append(chars[index])
        }
        return salt.toString()
    }

    private fun challengeHandler(socket: Socket, id: String, challenge: String) {
        var privateKey: PrivateKey = KeyHandler.getInstance().loadRSAPrivateFromPlainText(privateKeyStrings);
        val msgObjString = AsymmetricEncDec.getInstance().decryptString(challenge, privateKey)
        val serializeMsg: String? = MessageSerializerHandler.getInstance()?.serialize(MessageObject(MessageType.RESPONSE, msgObjString))


//        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//        var encryptedString: String= String(cipher.doFinal(serializeMsg?.toByteArray()), StandardCharsets.UTF_8)
//
        socket.emit("sendTo", createMessage(id, serializeMsg!!) )
    }

    private fun decryptionKeyHandler(socket: Socket, decryptionKey: String, id: String) {
        var privateKey: PrivateKey = KeyHandler.getInstance().loadRSAPrivateFromPlainText(privateKeyStrings);
        val msgObjString = AsymmetricEncDec.getInstance().decryptString(decryptionKey, privateKey)
//        if(isValidated) {
            this.decryptionKey = msgObjString;
        Log.i("challangeresponse1",this.decryptionKey)
            val serializeMsg: String? = MessageSerializerHandler.getInstance()?.serialize(MessageObject(MessageType.PING, "success"))
            socket.emit("sendTo", serializeMsg?.let { createMessage(id, it) })

//        }else {
//            val serializeMsg: String? = MessageSerializerHandler.getInstance()?.serialize(MessageObject(MessageType.PING, "fail"))
//            socket.emit("sendTo", serializeMsg?.let { createMessage(id, it) })
//        }
    }

    private fun mTerminate(socket: Socket) {
        isValidated = false
        socket.emit(
            "sendTo", createMessage(
                id,
                MessageSerializerHandler.getInstance()?.serialize(
                    MessageObject(
                        MessageType.TERMINATE,
                        "terminate"
                    )
                )!!
            )
        )
        socket.disconnect()
    }

    private fun ping(id: String, socket: Socket) {
        Handler(Looper.getMainLooper()).postDelayed({
            socket.emit(
                "sendTo", createMessage(
                    id,
                    MessageSerializerHandler.getInstance()?.serialize(
                        MessageObject(
                            MessageType.PING,
                            "ping"
                        )
                    )!!
                )
            )
        }, 10*1000)
    }

    private fun createMessage(id: String, message: String): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", id)
            jsonObject.put("msg", message)
        } catch (e: JSONException) {
            Log.i("message", e.printStackTrace().toString())
        }
        return jsonObject
    }


}