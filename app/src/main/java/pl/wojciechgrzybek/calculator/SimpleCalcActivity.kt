package pl.wojciechgrzybek.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class SimpleCalcActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var reg1: Double = 0.0
    private var reg2: Double = 0.0
    private var op: String = ""
    private var isCounterCleared = true
    private var isDotPresent = false
    private var lastKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calc)

        display = findViewById(R.id.textView)

        (findViewById<Button>(R.id.button0)).setOnClickListener {onNumberButtonClicked(R.string.btn0)}
        (findViewById<Button>(R.id.button1)).setOnClickListener {onNumberButtonClicked(R.string.btn1)}
        (findViewById<Button>(R.id.button2)).setOnClickListener {onNumberButtonClicked(R.string.btn2)}
        (findViewById<Button>(R.id.button3)).setOnClickListener {onNumberButtonClicked(R.string.btn3)}
        (findViewById<Button>(R.id.button4)).setOnClickListener {onNumberButtonClicked(R.string.btn4)}
        (findViewById<Button>(R.id.button5)).setOnClickListener {onNumberButtonClicked(R.string.btn5)}
        (findViewById<Button>(R.id.button6)).setOnClickListener {onNumberButtonClicked(R.string.btn6)}
        (findViewById<Button>(R.id.button7)).setOnClickListener {onNumberButtonClicked(R.string.btn7)}
        (findViewById<Button>(R.id.button8)).setOnClickListener {onNumberButtonClicked(R.string.btn8)}
        (findViewById<Button>(R.id.button9)).setOnClickListener {onNumberButtonClicked(R.string.btn9)}

        (findViewById<Button>(R.id.buttonAdd)).setOnClickListener {onOperatorButtonClicked(R.string.btnAdd)}
        (findViewById<Button>(R.id.buttonSub)).setOnClickListener {onOperatorButtonClicked(R.string.btnSub)}
        (findViewById<Button>(R.id.buttonMul)).setOnClickListener {onOperatorButtonClicked(R.string.btnMul)}
        (findViewById<Button>(R.id.buttonDiv)).setOnClickListener {onOperatorButtonClicked(R.string.btnDiv)}

        (findViewById<Button>(R.id.buttonSign)).setOnClickListener {onSignButtonClicked()}
        (findViewById<Button>(R.id.buttonDot)).setOnClickListener {onDotButtonClicked()}
        (findViewById<Button>(R.id.buttonClear)).setOnClickListener {onClearButtonClicked()}
        (findViewById<Button>(R.id.buttonResult)).setOnClickListener {onEqualButtonClicked()}
    }

    private fun onNumberButtonClicked(label: Int) {
        Log.d("Number Button", getString(label))

        if (display.text.toString() == "0") {
            Log.d("NB", "1")
            display.text = getString(label)
//            isCounterCleared = false
        } else if ((lastKey == "oper2") || (lastKey == "ce")) {
            Log.d("NB", "2")
            display.text = getString(label)
//            isCounterCleared = false
        } else {
            Log.d("NB", "3")
            if (display.text.toString().countDigitsRegex() < 10) {
//                isCounterCleared = false
                val newValue = display.text.toString() + getString(label)
                display.text = newValue
            }
        }
        lastKey = "digit"
    }

    private fun onOperatorButtonClicked(label: Int) {
        Log.d("Operator Button", getString(label))
        if (op == "") {
            reg1 = display.text.toString().toDouble()
            isDotPresent = false
//            isCounterCleared = true
            Log.d("Parsed", reg1.toString())
        }
        op = getString(label)
        lastKey = "oper2"
    }

    private fun onDotButtonClicked() {
        Log.d("Dot Button", getString(R.string.btnDot))
        if ((!isDotPresent) && (display.text.toString().countDigitsRegex() != 10)) {
            val newValue = display.text.toString() + getString(R.string.btnDot)
            display.text = newValue
            isDotPresent = true
//            isCounterCleared = false
            // ??
            lastKey = "digit"
        }
        // display.text = getString(R.string.btnDot)
    }

    private fun onSignButtonClicked() {
        Log.d("Sign Button", getString(R.string.btnSign))
        if (!isCounterCleared) {
            if (display.text.toString()[0] != '-') {
                val newValue = '-' + display.text.toString()
                display.text = newValue
            } else {
                display.text = display.text.drop(1)
            }
        }
    }

    private fun onClearButtonClicked() {
        Log.d("Clear Button", getString(R.string.btnClear))
        Log.d("cleared?", if (lastKey == "ce") "true" else "false")

        if (lastKey == "ce") {
            isCounterCleared = true
            isDotPresent = false
            display.text = "0"
            lastKey = "none"
            reg1 = 0.0
            reg2 = 0.0
            op = ""

        } else {
            isCounterCleared = true
            isDotPresent = false
            display.text = "0"
            lastKey = "ce"
        }
    }

    private fun onEqualButtonClicked() {
        Log.d("Equal Button", getString(R.string.btnResult))

        if (op != "") {
            Log.d("reg1", reg1.toString())
            Log.d("op", op)
            Log.d("disp", display.text.toString())

            if (lastKey == "digit") {
                reg2 = display.text.toString().toDouble()
            }

            reg1 = doOperation(reg1, op, reg2)

            display.text = reg1.toString()
            lastKey = "equal"
        }
    }

    private fun doOperation(arg1: Double, oper: String, arg2: Double): Double {

        when (oper) {
            "+" -> {
                return arg1 + arg2
            }
            "-" -> {
                display.text = (reg1 - arg2.toDouble()).toString()
            }
            "*" -> {
                display.text = (reg1 * arg2.toDouble()).toString()
            }
            "/" -> {
                display.text = (reg1 / arg2.toDouble()).toString()
            }
        }

        return -1.0
    }

    private fun CharSequence.countDigitsRegex(): Int = Regex("\\d").findAll(this).count()
}
