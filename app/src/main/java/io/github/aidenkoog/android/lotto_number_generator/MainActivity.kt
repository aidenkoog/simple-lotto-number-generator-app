package io.github.aidenkoog.android.lotto_number_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

/**
 * lotto number generator.
 */
class MainActivity : AppCompatActivity() {

    /* button views. */
    private lateinit var clearButton: Button
    private lateinit var addButton: Button
    private lateinit var runButton: Button
    private lateinit var numberPicker: NumberPicker

    private lateinit var numberTextViewList: List<TextView>

    /* check if generation is already executed. */
    private var isGeneratorRun = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeNumberTextViewList()
        initializeNumberPicker()
        initializeButtonUi()
    }

    /**
     * initialize number text view list.
     */
    private fun initializeNumberTextViewList() {
        numberTextViewList = listOf<TextView>(
            findViewById(R.id.firstNumberText),
            findViewById(R.id.secondNumberText),
            findViewById(R.id.thirdNumberText),
            findViewById(R.id.fourthNumberText),
            findViewById(R.id.fifthNumberText),
            findViewById(R.id.sixthNumberText)
        )
    }

    /**
     * setup number picket's value setting.
     */
    private fun initializeNumberPicker() {
        numberPicker = findViewById(R.id.numberPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = 45
    }

    /**
     * call function which can initialize generate, clear and add button views.
     */
    private fun initializeButtonUi() {
        initializeRunButton()
        initializeAddButton()
        initializeClearButton()
    }

    /**
     * initialize generate button view.
     */
    private fun initializeRunButton() {
        runButton = findViewById(R.id.runButton)
        runButton.setOnClickListener {
            val list = getRandomNumber()
            isGeneratorRun = true
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumberBackground(number, textView)
            }
        }
    }

    /**
     * initialize add button view.
     */
    private fun initializeAddButton() {
        addButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            if (isGeneratorRun) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.initializing_warning_text),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.number_selection_limit_warning_text),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.already_choosed_number_warning_text),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()
            setNumberBackground(numberPicker.value, textView)
            pickNumberSet.add(numberPicker.value)
        }
    }

    /**
     * select circular number shape corresponding to number range.
     */
    private fun setNumberBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 10..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 20..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 30..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(
                this, R.drawable.circle_green
            )
        }
    }

    /**
     * initialize clear button view.
     */
    private fun initializeClearButton() {
        clearButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach { it.isVisible = false }
            isGeneratorRun = false
        }
    }

    /**
     * generate random number.
     * @return {List<Int>}
     */
    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()
        return (pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)).sorted()
    }
}