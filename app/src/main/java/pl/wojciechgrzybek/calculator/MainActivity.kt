package pl.wojciechgrzybek.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSimple: Button = findViewById(R.id.btnSimple)
        btnSimple.setOnClickListener {
            startActivity(Intent(this, SimpleCalcActivity::class.java))
        }

        val btnAdvanced: Button = findViewById(R.id.btnAdvanced)
        btnAdvanced.setOnClickListener {
            startActivity(Intent(this, AdvancedCalcActivity::class.java))
        }

        val btnAbout: Button = findViewById(R.id.btnAbout)
        btnAbout.setOnClickListener {
            Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show()
        }

        val btnExit: Button = findViewById(R.id.btnExit)
        btnExit.setOnClickListener {
            Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show()
        }
    }
}