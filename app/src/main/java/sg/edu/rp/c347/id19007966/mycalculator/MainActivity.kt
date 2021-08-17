package sg.edu.rp.c347.id19007966.mycalculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.Toast


import sg.edu.rp.c347.id19007966.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Considering that Kotlin synthetics is deprecated,
    // I thought it would be good to try the new view binding as recommended by Google.
    // https://developer.android.com/topic/libraries/view-binding/migration#kts
    //
    // this implementation uses that.
    private lateinit var binding: ActivityMainBinding

    private var currentDisplayText = "0"

    private var ex1: Double = 0.0
    private var ex2: Operator? = null
    private var ex3: Double = 0.0

    private var changeContainer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.textViewDisplay.textSize = 30F
            binding.spacer.layoutParams = LayoutParams(MATCH_PARENT, 0, 0F)
        }
        else {
            binding.textViewDisplay.textSize = 60F
            binding.spacer.layoutParams = LayoutParams(MATCH_PARENT, 0, 1F)
        }
    }

    fun clearDisplay(view: View) {
        currentDisplayText = "0"
        binding.textViewDisplay.text = currentDisplayText
        ex1 = 0.0
        ex2 = null
        ex3 = 0.0
    }

    fun inputReceived(view: View) {
        val button = view as Button
        val title = button.text.toString()

        if (changeContainer) {
            currentDisplayText = "0"
            changeContainer = false
        }

        if (currentDisplayText.equals("0")) {
            currentDisplayText = if (title.equals(".")) "0." else "$title"
        }
        else {
            if (title.equals(".") && currentDisplayText.count{ ".".contains(it) } > 0) {
                return;
            }
            currentDisplayText += title;
        }

        binding.textViewDisplay.text = currentDisplayText
        if (ex2 == null)  {
            ex1 = currentDisplayText.toDouble()
        }
        else {
            ex3 = currentDisplayText.toDouble()
        }
    }

    fun operationsReceived(view: View) {
        val button = view as Button

        when (button.id) {
            binding.buttonOperationAddition.id -> ex2 = Operator.ADDITION
            binding.buttonOperationSubtract.id -> ex2 = Operator.SUBTRACTION
            binding.buttonOperationDivide.id -> ex2 = Operator.DIVISION
            binding.buttonOperationMultiply.id -> ex2 = Operator.MULTIPLICATION
        }

        changeContainer = true
    }

    fun doExpression() {
        ex2?.also { operator ->
            var result = operator.calculate(ex1, ex3)
            currentDisplayText = "$result"
            binding.textViewDisplay.text = currentDisplayText
            ex1 = result
            changeContainer = true
        }
    }

    fun shouldEqualsNow(view: View) {
        doExpression()
    }

    fun quiz(grade: Char) {
        var msg = ""
        when(grade) {
            'A', 'B', 'C', 'D' -> msg = "pass, your grade is " + grade
            'E', 'F' -> msg = "fail, your grade is " + grade
            else -> msg = "invalid"
        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}

