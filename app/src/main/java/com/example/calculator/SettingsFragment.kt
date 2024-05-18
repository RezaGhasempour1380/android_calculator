package com.example.calculator

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat

class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var colorOptions: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        sharedPreferences = requireContext().getSharedPreferences("AppSettings", 0)
        colorOptions = view.findViewById(R.id.colorOptions)
        val btnApplyColor = view.findViewById<Button>(R.id.btnApplyColor)

        // Apply saved color on startup
        applySavedColor(view)

        btnApplyColor.setOnClickListener {
            val selectedColorId = colorOptions.checkedRadioButtonId
            if (selectedColorId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedColorId)
                val selectedColor = when (selectedRadioButton.text.toString()) {
                    "Red" -> ContextCompat.getColor(requireContext(), R.color.colorRed)
                    "Green" -> ContextCompat.getColor(requireContext(), R.color.colorGreen)
                    "Blue" -> ContextCompat.getColor(requireContext(), R.color.colorBlue)
                    "Yellow" -> ContextCompat.getColor(requireContext(), R.color.colorYellow)
                    "White" -> ContextCompat.getColor(requireContext(), R.color.colorWhite)
                    else -> ContextCompat.getColor(requireContext(), R.color.colorWhite)
                }
                saveColor(selectedColor)
                applyColor(view, selectedColor)
            }
        }

        return view
    }

    private fun saveColor(color: Int) {
        sharedPreferences.edit().putInt("backgroundColor", color).apply()
    }

    private fun applySavedColor(view: View) {
        val savedColor = sharedPreferences.getInt("backgroundColor", Color.WHITE)
        applyColor(view, savedColor)
    }

    private fun applyColor(view: View, color: Int) {
        view.setBackgroundColor(color)
        // Also change the navigation bar color
        (activity as? MainActivity)?.updateNavigationBarColor(color)
    }
}
