package sg.edu.rp.c347.id19007966.mycalculator

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