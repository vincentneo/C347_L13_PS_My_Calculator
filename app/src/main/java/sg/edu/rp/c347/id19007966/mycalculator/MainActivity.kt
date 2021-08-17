package sg.edu.rp.c347.id19007966.mycalculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT


import sg.edu.rp.c347.id19007966.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //
    // Considering that Kotlin synthetics is deprecated,
    // I thought it would be good to try the new view binding as recommended by Google.
    // https://developer.android.com/topic/libraries/view-binding/migration#kts
    //
    private lateinit var binding: ActivityMainBinding

    private var currentDisplayText = "0"
    private var calculatedValue = 0F
    private var lastOperation: Operator? = null
    private var expression = ArrayList<ExpressionItem>()
    private var saveToExpression = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this implementation uses that.
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
    }

    fun inputReceived(view: View) {
        val button = view as Button
        val title = button.text.toString()

        if (saveToExpression && lastOperation != null) {
            var item = ExpressionItem()
            item.operator = lastOperation
            expression.add(item)
            saveToExpression = false
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
    }

    fun operationsReceived(view: View) {
        val button = view as Button
        if (!saveToExpression) {
            var item = ExpressionItem()
            item.number = currentDisplayText.toDouble()
            expression.add(item)
            saveToExpression = true
            currentDisplayText = "0"
        }
        when (button.id) {
            binding.buttonOperationAddition.id -> lastOperation = Operator.ADDITION
            binding.buttonOperationSubtract.id -> lastOperation = Operator.SUBTRACTION
            binding.buttonOperationDivide.id -> lastOperation = Operator.DIVISION
            binding.buttonOperationMultiply.id -> lastOperation = Operator.MULTIPLICATION
        }
    }

}

enum class Operator {
    DIVISION,MULTIPLICATION,ADDITION,SUBTRACTION
}

class ExpressionItem {
    var number: Double? = null
    var operator: Operator? = null
}
