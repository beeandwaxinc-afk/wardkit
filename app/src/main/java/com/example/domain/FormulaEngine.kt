package com.example.domain

import com.example.data.CalculationResult
import com.example.data.CalculatorInputState
import com.example.domain.phase2.evaluatePhase2A
import com.example.domain.phase2.evaluatePhase2B
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

object FormulaEngine {

    fun evaluate(
        calculatorId: String,
        state: CalculatorInputState,
        nowMillis: Long = System.currentTimeMillis()
    ): CalculationResult {
        return evaluatePart1(calculatorId, state, nowMillis)
            ?: evaluatePart2(calculatorId, state)
            ?: evaluatePhase2A(calculatorId, state)
            ?: evaluatePhase2B(calculatorId, state)
            ?: CalculationResult(isReady = false, emptyPrompt = "Enter values below")
    }

    internal fun toKg(value: Double, unitIndex: Int): Double =
        if (unitIndex == 1) value * 0.45359237 else value

    internal fun toInches(value: Double, unitIndex: Int): Double =
        if (unitIndex == 0) value / 2.54 else value

    internal fun toCm(value: Double, unitIndex: Int): Double =
        if (unitIndex == 1) value * 2.54 else value

    internal fun fmt0(v: Double): String = String.format(Locale.US, "%.0f", v)

    internal fun fmt1(v: Double): String = String.format(Locale.US, "%.1f", v)

    internal fun fmt2(v: Double): String = String.format(Locale.US, "%.2f", v)

    internal fun fmtClean(v: Double): String {
        val rounded = v.roundToInt().toDouble()
        return if (abs(v - rounded) < 0.05) {
            String.format(Locale.US, "%.0f", rounded)
        } else if (abs(v * 10 - (v * 10).roundToInt()) < 0.05) {
            String.format(Locale.US, "%.1f", v)
        } else {
            String.format(Locale.US, "%.2f", v)
        }
    }

    internal fun missingChoicePrompt(selectedCount: Int, totalCount: Int): CalculationResult {
        val remaining = totalCount - selectedCount
        val prompt = if (selectedCount == 0) {
            "Select all $totalCount criteria below"
        } else {
            "$selectedCount of $totalCount selected · $remaining remaining"
        }
        return CalculationResult(isReady = false, emptyPrompt = prompt)
    }
}
