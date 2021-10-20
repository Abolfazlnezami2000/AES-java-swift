package com.example.aes

import aes.aes
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/battery"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "encrypt") {
                val data = call.argument<String>("value")
                val key = call.argument<String>("key")

                result.success("Hello, ${call.arguments}")
            } else if(call.method == "decrypt"){
                val data = call.argument<String>("value")
                val key = call.argument<String>("key")
                val output = aes.aesDecryption(data,key)
                result.success(output)
            }
            else {
                result.notImplemented()
            }
        }
    }
}
