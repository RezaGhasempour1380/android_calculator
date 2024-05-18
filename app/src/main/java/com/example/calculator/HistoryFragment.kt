package com.example.calculator

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONArray
import java.io.File

class HistoryFragment : Fragment() {

    private lateinit var historyContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var clearHistoryButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        historyContainer = view.findViewById(R.id.historyContainer)
        clearHistoryButton = view.findViewById(R.id.btnClearHistory)
        sharedPreferences = requireContext().getSharedPreferences("AppSettings", 0)
        applySavedColor(view)
        loadHistory()

        clearHistoryButton.setOnClickListener {
            clearHistory()
        }

        return view
    }

    private fun applySavedColor(view: View) {
        val savedColor = sharedPreferences.getInt("backgroundColor", Color.WHITE)
        view.setBackgroundColor(savedColor)
    }

    private fun loadHistory() {
        historyContainer.removeAllViews()
        val file = File(requireContext().filesDir, "history.json")
        if (file.exists()) {
            val historyJson = file.readText()
            val jsonArray = JSONArray(historyJson)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val resultView = TextView(requireContext()).apply {
                    text = "${item.getString("firstNumber")} ${item.getString("operator")} ${item.getString("secondNumber")} = ${item.getString("result")}"
                }
                historyContainer.addView(resultView)
            }
        }
    }

    private fun clearHistory() {
        val file = File(requireContext().filesDir, "history.json")
        if (file.exists()) {
            file.delete()
        }
        loadHistory() // Refresh the view
    }
}
