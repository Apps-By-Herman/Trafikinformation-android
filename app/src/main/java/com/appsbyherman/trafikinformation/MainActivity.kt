package com.appsbyherman.trafikinformation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.preference.PreferenceManager
import com.appsbyherman.trafikinformation.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {

    /*
    * TODO
    * - Upon starting app show map as in rastplatser and situations within 200 kilometers?
    *   - Should be settings how big area is accepted
    *   - If position is allowed, should be asked automatically
    *   - If not, show all within sweden or within selected area code? Eg. Örebro län...
    * - If chosen area code, one can keep polling for new messages and show as push notifications
    *   that is pulled in a regular interval
    * - Use view models...
    * - Test ads to see what can be earned? Or the freemium model?
    * - Choose if use in foreground or background
    *   - Foreground: Map as in rastplatser with icons for each specific situation
    * - Send local notification when new situation occurs
    *   - Background: local push notification
    *   - Foreground: some other type of notification?
    * - Is there a way of automatically start a location service when start driving?
    * */

    private val debugTag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private val doubleBackToExitRunnable = Runnable { doubleBackToExitPressedOnce = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Osmdroid stuff
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStart.setOnClickListener {
            actionOnService(Actions.START)
        }

        binding.btnStop.setOnClickListener {
            actionOnService(Actions.STOP)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                }
                else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@MainActivity,
                        getString(R.string.main_activity_double_back_press_to_exit_message),
                        Toast.LENGTH_SHORT).show()

                    Handler(Looper.myLooper()!!).postDelayed(doubleBackToExitRunnable, 2000)
                }
            }
        })
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