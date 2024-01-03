package com.novelitech.calculatorkotlintwo

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.novelitech.calculatorkotlintwo.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding:ActivityMainBinding

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if(view is Button) {

            // Melhorar aqui
            if(view.text == ".") {

                if(canAddDecimal) {
                    binding.workingsTV.append(view.text)
                }

                canAddDecimal = false
            } else {
                binding.workingsTV.append(view.text)
            }

            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        print("olá")
        if(view is Button && canAddOperation) {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.length()

        if(length > 0) {
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
        }
    }

    fun equalsAction(view: View) {

        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {

        val digitsOperators = digitsOperators()

        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)

        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)

        return result.toString();
    }

    private fun addSubtractCalculate(list: MutableList<Any>): Float {

        var result = list[0] as Float

        for(i in list.indices) {
            if(list[i] is Char && i != list.lastIndex) {

                val operator = list[i]
                val nextDigit = list[i + 1] as Float

                if(operator == '+') {
                    result += nextDigit
                }

//                println("AQUIIIIIIIIII ${operator == "-"}")

                if(operator == '-') {
                    result -= nextDigit
                }
            }
        }

        return result
    }

    private fun timesDivisionCalculate(digitsOperators: MutableList<Any>): MutableList<Any> {

        var list = digitsOperators

        while(list.contains('x') || list.contains('/')) {
            list = calculateTimesDiv(list)
        }

        return list
    }

    private fun calculateTimesDiv(list: MutableList<Any>): MutableList<Any> {

        val newList = mutableListOf<Any>()

        var restartIndex = list.size

        for (i in list.indices) {
            if(list[i] is Char && i != list.lastIndex && i < restartIndex) {

                val operator = list[i]
                val prevDigit = list[i - 1] as Float
                val nextDigit = list[i + 1] as Float

                when(operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex) {
                newList.add(list[i])
            }
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any> {

        val list = mutableListOf<Any>()
        var currentDigit = ""

        for(character in binding.workingsTV.text) {
            // isDigit = it's a number
            if(character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "") {
            list.add(currentDigit.toFloat())
        }

        return list
    }
}

