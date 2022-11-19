package pl.wojciechgrzybek.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class AdvancedCalcActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var reg: Double = 0.0
    private var reg2: Double = 0.0
    private var operand = ""
    private var result: Double = 0.0
    private var displayState: DisplayState = DisplayState.NEW
    private var lastAction: LastAction = LastAction.NONE

    private fun variables() {
        Log.d("----", "dump start")
        Log.d("display", display.text.toString())
        Log.d("reg", reg.toString())
        Log.d("reg2", reg2.toString())
        Log.d("operand", operand)
        Log.d("result", result.toString())
        Log.d("displayState", displayState.toString())
        Log.d("lastAction", lastAction.toString())
        Log.d("----", "dump end")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_calc)

        display = findViewById(R.id.textView)

        (findViewById<Button>(R.id.button0)).setOnClickListener { onNumberButtonClicked(R.string.btn0) }
        (findViewById<Button>(R.id.button1)).setOnClickListener { onNumberButtonClicked(R.string.btn1) }
        (findViewById<Button>(R.id.button2)).setOnClickListener { onNumberButtonClicked(R.string.btn2) }
        (findViewById<Button>(R.id.button3)).setOnClickListener { onNumberButtonClicked(R.string.btn3) }
        (findViewById<Button>(R.id.button4)).setOnClickListener { onNumberButtonClicked(R.string.btn4) }
        (findViewById<Button>(R.id.button5)).setOnClickListener { onNumberButtonClicked(R.string.btn5) }
        (findViewById<Button>(R.id.button6)).setOnClickListener { onNumberButtonClicked(R.string.btn6) }
        (findViewById<Button>(R.id.button7)).setOnClickListener { onNumberButtonClicked(R.string.btn7) }
        (findViewById<Button>(R.id.button8)).setOnClickListener { onNumberButtonClicked(R.string.btn8) }
        (findViewById<Button>(R.id.button9)).setOnClickListener { onNumberButtonClicked(R.string.btn9) }

        (findViewById<Button>(R.id.buttonAdd)).setOnClickListener { onOperatorButtonClicked(R.string.btnAdd) }
        (findViewById<Button>(R.id.buttonSub)).setOnClickListener { onOperatorButtonClicked(R.string.btnSub) }
        (findViewById<Button>(R.id.buttonMul)).setOnClickListener { onOperatorButtonClicked(R.string.btnMul) }
        (findViewById<Button>(R.id.buttonDiv)).setOnClickListener { onOperatorButtonClicked(R.string.btnDiv) }

        (findViewById<Button>(R.id.buttonSign)).setOnClickListener { onSignButtonClicked() }
        (findViewById<Button>(R.id.buttonDot)).setOnClickListener { onDotButtonClicked() }
        (findViewById<Button>(R.id.buttonClear)).setOnClickListener { onClearButtonClicked() }
        (findViewById<Button>(R.id.buttonResult)).setOnClickListener { onEqualButtonClicked() }
    }

    private fun onNumberButtonClicked(label: Int) {
        Log.d("Number Button", getString(label))

        if (display.text.toString() == "0") {
            Log.d("NB", "0")
            display.text = getString(label)
            displayState = DisplayState.CONTINUE
            lastAction = LastAction.DIGIT
        } else {
            Log.d("NB", "0-9")
            if (display.text.toString().countDigitsRegex() < 10) {
                if (displayState == DisplayState.CONTINUE) {
                    val newValue = display.text.toString() + getString(label)
                    display.text = newValue
                } else {
                    display.text = getString(label)
                }
                displayState = DisplayState.CONTINUE
                lastAction = LastAction.DIGIT
            }
        }
    }

    private fun onOperatorButtonClicked(label: Int) {
        Log.d("New Operator Button", getString(label))

        if (operand != "") {
            when (lastAction) {
                LastAction.NONE -> {
                }
                LastAction.DIGIT -> {
                    Log.d("oper DIGIT", getString(label))
                    reg2 = display.text.toString().toDouble()
                    if (operand != "") {
                        result = doOperation(reg, operand, reg2)
                        reg = result
                        display.text = result.toString()
                    }
                    operand = getString(label)
                    lastAction = LastAction.OPER4
                    displayState = DisplayState.NEW
                }
                LastAction.OPER4 -> {
                    Log.d("oper OPER4", getString(label))
                    operand = getString(label)
                }
                LastAction.EQUAL -> {
                    Log.d("oper EQUAL", getString(label))
                    operand = getString(label)
                    lastAction = LastAction.OPER4
                }
                else -> {
                    Log.d("oper other", getString(label))
                }
            }
        } else {
            Log.d("oper NONE", getString(label))
            reg = display.text.toString().toDouble()
            operand = getString(label)
            lastAction = LastAction.OPER4
            displayState = DisplayState.NEW
        }
    }

    private fun onDotButtonClicked() {
        if (!(display.text.toString().contains('.'))
            && (display.text.toString().countDigitsRegex() != 10)
        ) {
            val newValue = display.text.toString() + getString(R.string.btnDot)
            display.text = newValue
            displayState = DisplayState.CONTINUE
        } else if (lastAction == LastAction.OPER4) {
            display.text = "0."
            displayState = DisplayState.CONTINUE
            lastAction = LastAction.DIGIT
        }
    }

    private fun onSignButtonClicked() {
        Log.d("Sign Button", getString(R.string.btnSign))
        if (displayState != DisplayState.NEW) {
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
        Log.d("Cleared? ", if (lastAction == LastAction.CLEAR) "true" else "false")

        if (lastAction == LastAction.CLEAR) {
            displayState = DisplayState.NEW
            lastAction = LastAction.NONE
            display.text = "0"
            reg = 0.0
            reg2 = 0.0
            operand = ""
            result = 0.0
        } else {
            displayState = DisplayState.NEW
            lastAction = LastAction.CLEAR
            display.text = "0"
        }
    }

    private fun onEqualButtonClicked() {
        Log.d("New Equal Button", getString(R.string.btnResult))
        if (operand != "") {
            if (lastAction == LastAction.DIGIT) {
                reg2 = display.text.toString().toDouble()
                result = doOperation(reg, operand, reg2)
                reg = result
                display.text = result.toString()
                lastAction = LastAction.EQUAL
                displayState = DisplayState.NEW
            } else if (lastAction == LastAction.EQUAL) {
                result = doOperation(reg, operand, reg2)
                reg = result
                display.text = result.toString()
                displayState = DisplayState.NEW
            } else if (lastAction == LastAction.OPER4) {
                variables()
                result = doOperation(reg, operand, reg)
                reg2 = reg
                reg = result
                display.text = result.toString()
                displayState = DisplayState.NEW
                lastAction = LastAction.EQUAL
                variables()
            }
        }
    }

    private fun doOperation(arg1: Double, oper: String, arg2: Double): Double {

        Log.d("doOperation arg1", arg1.toString())
        Log.d("doOperation oper", oper)
        Log.d("doOperation arg2", arg2.toString())

        var result: Double = 0.0
        when (oper) {
            "+" -> {
                result = arg1 + arg2
            }
            "-" -> {
                result = arg1 - arg2
            }
            "*" -> {
                result = arg1 * arg2
            }
            "/" -> {
                // TODO add catching division by 0
                result = if (arg2 == 0.0) {
                    Toast.makeText(this, "Division by 0 is not allowed.", Toast.LENGTH_LONG).show()
                    0.0
                } else {
                    arg1 / arg2
                }
            }
        }

        return result
    }

    private fun CharSequence.countDigitsRegex(): Int = Regex("\\d").findAll(this).count()

    private enum class DisplayState {
        NEW, CONTINUE
    }

    private enum class LastAction {
        NONE, DIGIT, OPER4, EQUAL, CLEAR
    }
}
