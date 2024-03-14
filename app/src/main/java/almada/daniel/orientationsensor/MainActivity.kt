package almada.daniel.orientationsensor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var orientationSensor: Sensor? = null
    private lateinit var orientationTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orientationTextView = findViewById(R.id.orientationTextView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        orientationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            val azimuth = event.values[0]
            val pitch = event.values[1]
            val roll = event.values[2]

            orientationTextView.text = "Azimuth: $azimuth\nPitch: $pitch\nRoll: $roll"

            // Guardar la información en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putFloat("azimuth", azimuth)
            editor.putFloat("pitch", pitch)
            editor.putFloat("roll", roll)
            editor.apply()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario implementar nada aquí para este ejemplo
    }
}
