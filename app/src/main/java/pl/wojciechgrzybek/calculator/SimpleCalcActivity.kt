package pl.wojciechgrzybek.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class SimpleCalcActivity : AppCompatActivity() {

    private lateinit var display: TextView

//    private var reg1: Double = 0.0
//    private var reg2: Double = 0.0
//    private var op: String = ""
//    private var isCounterCleared = true
//    private var isDotPresent = false
//    private var lastKey = ""

    private var reg: Double = 0.0
    private var reg2: Double = 0.0

    //    private var calcState = ""
    private var operand = ""
    private var displayState: DisplayState = DisplayState.NEW
    private var lastAction: LastAction = LastAction.NONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calc)

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
        Log.d("Operator Button", getString(label))

        if (operand != "") {
            Log.d("oper", "operand != 0")
            if (lastAction == LastAction.OPER4) {
                Log.d("oper", "last to oper4")
                displayState = DisplayState.NEW
                operand = getString(label)
            } else {
                Log.d("oper", "last inny niz oper4")
                displayState = DisplayState.NEW
                if (lastAction != LastAction.EQUAL) {
                    Log.d("oper", "last inny niz equal")
                    reg = doOperation(reg, operand, display.text.toString().toDouble())

                }
                reg2 = reg
                lastAction = LastAction.OPER4
                Log.d("oper", "po sprawdzeniu czy inny niz equal")
                display.text = reg.toString()
                operand = getString(label)
            }
        } else {
            Log.d("oper", "oper to pusty")
            reg = display.text.toString().toDouble()
            reg2 = reg
            displayState = DisplayState.NEW
            lastAction = LastAction.OPER4
            operand = getString(label)
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
        } else {
            displayState = DisplayState.NEW
            lastAction = LastAction.CLEAR
            display.text = "0"
        }
    }

    private fun onEqualButtonClicked() {
        Log.d("Equal Button", getString(R.string.btnResult))

        if (operand != "") {
            Log.d("reg", reg.toString())
            Log.d("op", operand)
            Log.d("disp", display.text.toString())

            Log.d("reg2", reg2.toString())
            Log.d("lastoper", lastAction.toString())

            if (lastAction == LastAction.DIGIT) {
                Log.d("digit", "true")
                reg = doOperation(reg, operand, display.text.toString().toDouble())
                reg2 = reg
            } else {
                Log.d("digit", "false")
                reg = doOperation(reg, operand, reg2)
            }

            display.text = reg.toString()
            lastAction = LastAction.EQUAL
        }

//        if (op != "") {
//            Log.d("reg1", reg1.toString())
//            Log.d("op", op)
//            Log.d("disp", display.text.toString())
//
//            if (lastKey == "digit") {
//                reg2 = display.text.toString().toDouble()
//            }
//
//            reg1 = doOperation(reg1, op, reg2)
//
//            display.text = reg1.toString()
//            lastKey = "equal"
//        }
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
                result = arg1 / arg2
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
