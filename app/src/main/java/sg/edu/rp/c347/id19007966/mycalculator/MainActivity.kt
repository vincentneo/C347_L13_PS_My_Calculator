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
    //private var lastOperation: Operator? = null
    //private var expressions = ArrayList<ExpressionItem>()

    private var ex1: Double = 0.0
    private var ex2: Operator? = null
    private var ex3: Double = 0.0

    private var changeContainer = false

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
        ex1 = 0.0
        ex2 = null
        ex3 = 0.0
        saveToExpression = false
        //lastOperation = null
    }

    fun inputReceived(view: View) {
        val button = view as Button
        val title = button.text.toString()

        //if (saveToExpression && lastOperation != null) {
//        if (ex2 == null) {
//            var item = ExpressionItem()
//            item.operator = lastOperation
//            expressions.add(item)
//            saveToExpression = false
//        }
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
//        if (!saveToExpression) {
//            var item = ExpressionItem()
//            item.number = currentDisplayText.toDouble()
//            //expressions.add(item)
//            saveToExpression = true
//            currentDisplayText = "0"
//        }
        when (button.id) {
            binding.buttonOperationAddition.id -> ex2 = Operator.ADDITION
            binding.buttonOperationSubtract.id -> ex2 = Operator.SUBTRACTION
            binding.buttonOperationDivide.id -> ex2 = Operator.DIVISION
            binding.buttonOperationMultiply.id -> ex2 = Operator.MULTIPLICATION
        }
        changeContainer = true
    }

    fun doExpression() {
        var result: Double = 0.0

//        val count = expressions.size
//
//        if (count < 3) {
//            return
//        }
//
//        val ex1 = expressions.get(0)
//        val ex2 = expressions.get(1)
//        val ex3 = expressions.get(2)

        //if (ex1.number != null && ex2.operator != null && ex3.number != null) {
        if (ex1 != null && ex2 != null && ex3 != null) {
            result = ex2!!.calculate(ex1!!, ex3!!)
            //expressions.clear()
            //var item = ExpressionItem()
            //item.number = result
            //expressions.add(item)
            saveToExpression = true
            //lastOperation = null
            currentDisplayText = "$result"
            binding.textViewDisplay.text = currentDisplayText
            ex1 = result
            ex2 = null
        }
    }

    fun shouldEqualsNow(view: View) {
        System.out.println(saveToExpression)
        //System.out.println(expressions.size)
        doExpression()
        if (!saveToExpression) {
            var item = ExpressionItem()
            item.number = currentDisplayText.toDouble()
            //expressions.add(item)
            //currentDisplayText = "0"
            //doExpression()
        }
    }

}

enum class LastClick {
    NUMBER, OPERATOR, EQUAL
}

enum class Operator {
    DIVISION,MULTIPLICATION,ADDITION,SUBTRACTION;

    fun calculate(left: Double, right: Double): Double {
        return when(this) {
            DIVISION -> left / right
            MULTIPLICATION -> left * right
            ADDITION -> left + right
            SUBTRACTION -> left - right
        }
    }
}

class ExpressionItem {
    var number: Double? = null
    var operator: Operator? = null
}
