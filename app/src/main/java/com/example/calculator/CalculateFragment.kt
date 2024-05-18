package com.example.calculator

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

class CalculateFragment : Fragment() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var currentNumber: String = ""
    private var operator: String = ""
    private var firstNumber: String = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)

        tvExpression = view.findViewById(R.id.tvExpression)
        tvResult = view.findViewById(R.id.tvResult)
        sharedPreferences = requireContext().getSharedPreferences("AppSettings", 0)
        applySavedColor(view)

        val buttons = listOf(
            view.findViewById<Button>(R.id.btn0),
            view.findViewById<Button>(R.id.btn1),
            view.findViewById<Button>(R.id.btn2),
            view.findViewById<Button>(R.id.btn3),
            view.findViewById<Button>(R.id.btn4),
            view.findViewById<Button>(R.id.btn5),
            view.findViewById<Button>(R.id.btn6),
            view.findViewById<Button>(R.id.btn7),
            view.findViewById<Button>(R.id.btn8),
            view.findViewById<Button>(R.id.btn9),
            view.findViewById<Button>(R.id.btnAdd),
            view.findViewById<Button>(R.id.btnSubtract),
            view.findViewById<Button>(R.id.btnMultiply),
            view.findViewById<Button>(R.id.btnDivide),
            view.findViewById<Button>(R.id.btnEquals),
            view.findViewById<Button>(R.id.btnClear),
            view.findViewById<Button>(R.id.btnDot)
        )

        for (button in buttons) {
            button.setOnClickListener { onButtonClick(it) }
        }

        return view
    }

    private fun applySavedColor(view: View) {
        val savedColor = sharedPreferences.getInt("backgroundColor", Color.WHITE)
        view.setBackgroundColor(savedColor)
    }

    private fun onButtonClick(view: View) {
        if (view is Button) {
            when (view.text.toString()) {
                in "0".."9" -> {
                    currentNumber += view.text.toString()
                    tvResult.text = currentNumber
                    tvExpression.text = "${firstNumber}${operator}${currentNumber}"
                }
                "." -> {
                    if (!currentNumber.contains(".")) {
                        currentNumber += "."
                        tvResult.text = currentNumber
                        tvExpression.text = "${firstNumber}${operator}${currentNumber}"
                    }
                }
                "+", "-", "*", "÷" -> {
                    firstNumber = currentNumber
                    currentNumber = ""
                    operator = view.text.toString()
                    tvExpression.text = "$firstNumber$operator"
                    tvResult.text = currentNumber
                }
                "=" -> calculate()
                "C" -> clear()
            }
        }
    }

    private fun calculate() {
        try {
            val secondNumber = currentNumber
            val result = when (operator) {
                "+" -> firstNumber.toDouble() + secondNumber.toDouble()
                "-" -> firstNumber.toDouble() - secondNumber.toDouble()
                "*" -> firstNumber.toDouble() * secondNumber.toDouble()
                "÷" -> firstNumber.toDouble() / secondNumber.toDouble()
                else -> 0.0
            }
            val formattedResult = formatNumber(result)
            tvResult.text = formattedResult
            tvExpression.text = "$firstNumber$operator$secondNumber=$formattedResult"
            saveToHistory(firstNumber, operator, secondNumber, formattedResult)
            clearCalculationState()
        } catch (e: Exception) {
            tvResult.text = "Error"
        }
    }

    private fun formatNumber(number: Double): String {
        val bigDecimal = BigDecimal(number)
        return if (bigDecimal.stripTrailingZeros().scale() <= 0) {
            bigDecimal.toPlainString()
        } else {
            bigDecimal.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        }
    }

    private fun saveToHistory(firstNumber: String, operator: String, secondNumber: String, result: String) {
        val file = File(requireContext().filesDir, "history.json")
        val historyArray: JSONArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            JSONArray()
        }

        val historyItem = JSONObject().apply {
            put("firstNumber", firstNumber)
            put("operator", operator)
            put("secondNumber", secondNumber)
            put("result", result)
        }

        historyArray.put(historyItem)

        file.writeText(historyArray.toString())
    }

    private fun clearCalculationState() {
        currentNumber = ""
        firstNumber = ""
        operator = ""
    }

    private fun clear() {
        clearCalculationState()
        tvResult.text = "0"
        tvExpression.text = ""
    }
}
