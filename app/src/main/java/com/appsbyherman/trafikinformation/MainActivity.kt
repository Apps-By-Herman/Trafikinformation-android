package com.appsbyherman.trafikinformation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    /*
    * TODO
    * - Upon starting app show map as in rastplatser and situations within 200 kilometers?
    *   - If position is allowed, should be asked automatically
    *   - If not, show all within sweden?
    * - Use view models...
    * - Choose if use in foreground or background
    *   - Foreground: Map as in rastplatser with icons for each specific situation
    * - Send local notification when new situation occurs
    *   - Background: local push notification
    *   - Foreground: some other type of notification?
    * - Is there a way of automatically start a location service when start driving?
    * */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnStart).let {
            it.setOnClickListener {
                actionOnService(Actions.START)
            }
        }

        findViewById<Button>(R.id.btnStop).let {
            it.setOnClickListener {
                actionOnService(Actions.STOP)
            }
        }
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return

        Intent(this, EndlessService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }

            startService(it)
        }
    }
}