package com.stella.calculator3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var processView: TextView
    private lateinit var resultView: TextView
    private lateinit var btn0: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btn00: Button
    private lateinit var btnDot: Button
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var btnPercent: Button
    private lateinit var btnEquals: Button
    private lateinit var btnBackspace: Button
    private lateinit var btnClean: Button

    private var currentInput = ""
    private var fullExpression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // View 초기화
        processView = findViewById(R.id.process_view)
        resultView = findViewById(R.id.result_view)
        btn0 = findViewById(R.id.btn_0)
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        btn3 = findViewById(R.id.btn_3)
        btn4 = findViewById(R.id.btn_4)
        btn5 = findViewById(R.id.btn_5)
        btn6 = findViewById(R.id.btn_6)
        btn7 = findViewById(R.id.btn_7)
        btn8 = findViewById(R.id.btn_8)
        btn9 = findViewById(R.id.btn_9)
        btn00 = findViewById(R.id.btn_00)
        btnDot = findViewById(R.id.btn_dot)
        btnPlus = findViewById(R.id.btn_plus)
        btnMinus = findViewById(R.id.btn_minus)
        btnMultiply = findViewById(R.id.btn_multiply)
        btnDivide = findViewById(R.id.btn_divide)
        btnPercent = findViewById(R.id.btn_percent)
        btnEquals = findViewById(R.id.btn_equals)
        btnBackspace = findViewById(R.id.btn_backspace)
        btnClean = findViewById(R.id.btn_clean)

        // 숫자 버튼 클릭 리스너
        val numberClickListener = { number: String ->
            currentInput += number
            fullExpression += number
            processView.text = fullExpression
            updateResultView()
        }

        btn0.setOnClickListener { numberClickListener("0") }
        btn1.setOnClickListener { numberClickListener("1") }
        btn2.setOnClickListener { numberClickListener("2") }
        btn3.setOnClickListener { numberClickListener("3") }
        btn4.setOnClickListener { numberClickListener("4") }
        btn5.setOnClickListener { numberClickListener("5") }
        btn6.setOnClickListener { numberClickListener("6") }
        btn7.setOnClickListener { numberClickListener("7") }
        btn8.setOnClickListener { numberClickListener("8") }
        btn9.setOnClickListener { numberClickListener("9") }
        btn00.setOnClickListener { numberClickListener("00") }
        btnDot.setOnClickListener { numberClickListener(".") }

        // 연산 버튼 클릭 리스너
        btnPlus.setOnClickListener { operatorClicked("+") }
        btnMinus.setOnClickListener { operatorClicked("-") }
        btnMultiply.setOnClickListener { operatorClicked("*") }
        btnDivide.setOnClickListener { operatorClicked("/") }
        btnPercent.setOnClickListener { percentClicked() }
        btnEquals.setOnClickListener { equalsClicked() }
        btnBackspace.setOnClickListener { backspaceClicked() }
        btnClean.setOnClickListener { clearAll() }
    }

    private fun operatorClicked(operator: String) {
        if (currentInput.isNotEmpty()) {
            fullExpression += " $operator "
            processView.text = fullExpression
            currentInput = ""
        }
    }

    private fun percentClicked() {
        if (currentInput.isNotEmpty()) {
            currentInput = formatResult(currentInput.toDouble() / 100)
            fullExpression = currentInput
            processView.text = fullExpression
            updateResultView()
        }
    }

    private fun equalsClicked() {
        if (currentInput.isNotEmpty() || fullExpression.isNotEmpty()) {
//            if (currentInput.isNotEmpty()) {
//                fullExpression += currentInput
//            }
            val result = evaluateExpression(fullExpression)
            processView.text = formatResult(result)
            resultView.text = ""
            currentInput = formatResult(result)
            fullExpression = formatResult(result)
        }
    }

    private fun updateResultView() {
        if (fullExpression.isNotEmpty()) {
            val result = evaluateExpression(fullExpression)
            resultView.text = formatResult(result)
        }
    }

    private fun evaluateExpression(expression: String): Double {
        try {
            val tokens = StringTokenizer(expression)
            val values = Stack<Double>()
            val ops = Stack<String>()

            while (tokens.hasMoreTokens()) {
                val token = tokens.nextToken()

                when {
                    token.toDoubleOrNull() != null -> values.push(token.toDouble())
                    token == "(" -> ops.push(token)
                    token == ")" -> {
                        while (ops.peek() != "(") values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                        ops.pop()
                    }
                    token == "+" || token == "-" || token == "*" || token == "/" -> {
                        while (ops.isNotEmpty() && hasPrecedence(token, ops.peek()))
                            values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                        ops.push(token)
                    }
                }
            }

            while (ops.isNotEmpty()) values.push(applyOp(ops.pop(), values.pop(), values.pop()))

            return values.pop()
        } catch (e: Exception) {
            return 0.0
        }
    }

    private fun hasPrecedence(op1: String, op2: String): Boolean {
        if (op2 == "(" || op2 == ")") return false
        if ((op1 == "*" || op1 == "/") && (op2 == "+" || op2 == "-")) return false
        return true
    }

    private fun applyOp(op: String, b: Double, a: Double): Double {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> if (b != 0.0) a / b else throw UnsupportedOperationException("Cannot divide by zero")
            else -> 0.0
        }
    }

    private fun backspaceClicked() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            fullExpression = fullExpression.dropLast(1)
            processView.text = fullExpression
            updateResultView()
        }
    }

    private fun clearAll() {
        currentInput = ""
        fullExpression = ""
        processView.text = ""
        resultView.text = ""
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
}
