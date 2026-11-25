package com.example.domain

import com.example.data.CalculationResult
import com.example.data.CalculatorInputState
import com.example.data.ClinicalTier
import com.example.data.SecondaryOutput
import com.example.domain.FormulaEngine.fmt0
import com.example.domain.FormulaEngine.fmt1
import com.example.domain.FormulaEngine.fmt2
import com.example.domain.FormulaEngine.fmtClean
import com.example.domain.FormulaEngine.missingChoicePrompt
import com.example.domain.FormulaEngine.toCm
import com.example.domain.FormulaEngine.toInches
import com.example.domain.FormulaEngine.toKg
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

internal fun evaluatePart2(
    calculatorId: String,
    state: CalculatorInputState
): CalculationResult? = when (calculatorId) {
    "neuro_gcs" -> calcGcs(state)
    "neuro_cpp" -> calcCpp(state)
    "neuro_abc2" -> calcAbc2(state)
    "neuro_ich" -> calcIchScore(state)
    "icu_map" -> calcMap(state)
    "icu_qsofa" -> calcQsofa(state)
    "icu_pf_ratio" -> calcPfRatio(state)
    "icu_ardsnet" -> calcArdsnet(state)
    "nephro_cg" -> calcCockcroftGault(state)
    "nephro_ckdepi" -> calcCkdEpi2021(state)
    "nephro_fena" -> calcFena(state)
    "nephro_fwd" -> calcFreeWaterDeficit(state)
    "gastro_child_pugh" -> calcChildPugh(state)
    "gastro_meld_na" -> calcMeldNa(state)
    "gastro_maddrey" -> calcMaddrey(state)
    "gastro_saag" -> calcSaag(state)
    "em_steroids" -> calcSteroidEquivalence(state)
    "em_ibw" -> calcIbwAdjBw(state)
    "em_mallampati_asa" -> calcMallampatiAsa(state)
    "em_resus" -> calcEmergencyResus(state)
    else -> null
}

private fun calcGcs(state: CalculatorInputState): CalculationResult {
    val e = state.choice("eye")?.toIntOrNull()
    val v = state.choice("verbal")?.toIntOrNull()
    val m = state.choice("motor")?.toIntOrNull()
    val count = listOfNotNull(e, v, m).size
    if (count < 3) return missingChoicePrompt(count, 3)

    val total = e!! + v!! + m!!
    val (tierLabel, tier, note) = when {
        total <= 8 -> Triple("Severe · Consider Intubation (≤8)", ClinicalTier.RED, "Coma threshold (GCS ≤8): protect airway promptly and evaluate for ICP/CT head.")
        total <= 12 -> Triple("Moderate Brain Injury (9–12)", ClinicalTier.AMBER, "Close neuro-observations, repeat GCS frequently, obtain neuroimaging.")
        else -> Triple("Mild (13–15)", ClinicalTier.GREEN, "Reassuring consciousness level; continue serial clinical monitoring.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 15",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Breakdown", "E$e  V$v  M$m", highlight = true)
        ),
        clinicalNote = note
    )
}

private fun calcCpp(state: CalculatorInputState): CalculationResult {
    val mode = state.choice("map_mode", "direct") ?: "direct"
    val icp = state.number("icp") ?: return CalculationResult()
    val warnings = mutableListOf<String>()

    val map = if (mode == "direct") {
        val directMap = state.number("map") ?: return CalculationResult()
        if (directMap < 20.0 || directMap > 220.0) warnings += "MAP (${fmt0(directMap)} mmHg) is outside typical physiological range."
        directMap
    } else {
        val sbp = state.number("sbp") ?: return CalculationResult()
        val dbp = state.number("dbp") ?: return CalculationResult()
        if (sbp < dbp) warnings += "SBP (${fmt0(sbp)}) is lower than DBP (${fmt0(dbp)})."
        if (sbp > 300.0 || dbp < 10.0) warnings += "Blood pressure values are outside typical range."
        (sbp + 2.0 * dbp) / 3.0
    }

    if (icp < -5.0 || icp > 100.0) warnings += "ICP (${fmt1(icp)} mmHg) is outside typical range (0–100)."

    val cpp = map - icp
    val (tierLabel, tier) = when {
        cpp < 50.0 -> "Critical Hypoperfusion (<50)" to ClinicalTier.RED
        cpp < 60.0 -> "Hypoperfusion (<60 mmHg)" to ClinicalTier.RED
        cpp <= 70.0 -> "Target Range (60–70 mmHg)" to ClinicalTier.GREEN
        else -> "Above Target (>70 mmHg)" to ClinicalTier.AMBER
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(cpp),
        primaryUnit = "mmHg",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("MAP Used", "${fmt1(map)} mmHg", highlight = mode == "bp"),
            SecondaryOutput("ICP", "${fmt1(icp)} mmHg")
        ),
        warnings = warnings
    )
}

private fun calcAbc2(state: CalculatorInputState): CalculationResult {
    val a = state.number("a_cm") ?: return CalculationResult()
    val b = state.number("b_cm") ?: return CalculationResult()
    val slices = state.number("slices") ?: return CalculationResult()
    val thicknessMode = state.choice("thickness_mode", "0.5") ?: "0.5"
    val thickness = if (thicknessMode == "custom") {
        state.number("custom_thickness") ?: return CalculationResult()
    } else {
        thicknessMode.toDoubleOrNull() ?: 0.5
    }

    if (a <= 0.0 || b <= 0.0 || slices <= 0.0 || thickness <= 0.0) {
        return CalculationResult(warnings = listOf("All dimensions must be positive."))
    }

    val warnings = buildList {
        if (a > 20.0 || b > 20.0) add("Diameter >20 cm exceeds human intracranial dimensions — check cm vs mm.")
        if (slices > 60.0) add("Slice count (${fmt1(slices)}) is unusually high.")
    }

    val c = slices * thickness
    val volume = (a * b * c) / 2.0

    val (tierLabel, tier) = when {
        volume >= 60.0 -> "Large Hematoma (≥60 mL · High Risk)" to ClinicalTier.RED
        volume >= 30.0 -> "Significant Volume (≥30 mL · +1 ICH)" to ClinicalTier.RED
        else -> "Volume <30 mL" to ClinicalTier.BLUE
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(volume),
        primaryUnit = "mL",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("C (Height)", "${fmt1(c)} cm"),
            SecondaryOutput("Dimensions", "${fmt1(a)} × ${fmt1(b)} × ${fmt1(c)} cm")
        ),
        warnings = warnings
    )
}

private fun calcIchScore(state: CalculatorInputState): CalculationResult {
    val gcsPts = state.choice("gcs_tier")?.toIntOrNull()
        ?: return CalculationResult(isReady = false, emptyPrompt = "Select GCS tier below")

    var score = gcsPts
    if (state.toggle("vol30")) score += 1
    if (state.toggle("ivh")) score += 1
    if (state.toggle("infra")) score += 1
    if (state.toggle("age80")) score += 1

    val mortality = when (score) {
        0 -> "0%"
        1 -> "13%"
        2 -> "26%"
        3 -> "72%"
        4 -> "97%"
        else -> "~100%"
    }

    val tier = when {
        score <= 1 -> ClinicalTier.GREEN
        score == 2 -> ClinicalTier.AMBER
        else -> ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 6",
        tierLabel = "30-Day Mortality: $mortality",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Predicted 30-Day Mortality", mortality, highlight = true)
        ),
        clinicalNote = "Baseline prognostic severity score; avoid premature limitation of care."
    )
}

private fun calcMap(state: CalculatorInputState): CalculationResult {
    val sbp = state.number("sbp") ?: return CalculationResult()
    val dbp = state.number("dbp") ?: return CalculationResult()
    if (sbp <= 0.0 || dbp <= 0.0) return CalculationResult(warnings = listOf("SBP and DBP must be positive."))

    val warnings = buildList {
        if (sbp <= dbp) add("SBP (${fmt0(sbp)}) should be greater than DBP (${fmt0(dbp)}).")
        if (sbp > 300.0 || dbp > 200.0) add("BP values exceed typical physiological range.")
    }

    val map = (sbp + 2.0 * dbp) / 3.0
    val pulsePressure = sbp - dbp

    val (tierLabel, tier) = when {
        map < 65.0 -> "Hypoperfusion (<65 mmHg)" to ClinicalTier.RED
        map <= 105.0 -> "Adequate Perfusion (65–105)" to ClinicalTier.GREEN
        else -> "Hypertensive (>105 mmHg)" to ClinicalTier.AMBER
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(map),
        primaryUnit = "mmHg",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Pulse Pressure", "${fmt0(pulsePressure)} mmHg", highlight = true)
        ),
        warnings = warnings
    )
}

private fun calcQsofa(state: CalculatorInputState): CalculationResult {
    var score = 0
    if (state.toggle("rr")) score += 1
    if (state.toggle("gcs")) score += 1
    if (state.toggle("sbp")) score += 1

    val (tierLabel, tier, note) = if (score >= 2) {
        Triple("High Risk Sepsis (≥2)", ClinicalTier.RED, "Elevated risk of ICU admission or mortality; trigger sepsis bundle, lactate, blood cultures, and early antibiotics.")
    } else {
        Triple("Lower Risk (0–1)", ClinicalTier.GREEN, "Does not rule out sepsis; reassess clinically if infection is suspected.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 3",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcPfRatio(state: CalculatorInputState): CalculationResult {
    val pao2 = state.number("pao2") ?: return CalculationResult()
    val fio2 = state.number("fio2") ?: return CalculationResult()
    if (pao2 <= 0.0 || fio2 <= 0.0) return CalculationResult(warnings = listOf("PaO₂ and FiO₂ must be positive."))

    val warnings = buildList {
        if (fio2 < 21.0 || fio2 > 100.0) add("FiO₂ (${fmt0(fio2)}%) must be between 21% (room air) and 100%.")
        if (pao2 > 650.0) add("PaO₂ (${fmt0(pao2)} mmHg) exceeds physiological maximum.")
    }

    val ratio = pao2 / (fio2 / 100.0)
    val (tierLabel, tier) = when {
        ratio <= 100.0 -> "Severe ARDS (≤100)" to ClinicalTier.RED
        ratio <= 200.0 -> "Moderate ARDS (101–200)" to ClinicalTier.RED
        ratio <= 300.0 -> "Mild ARDS (201–300)" to ClinicalTier.AMBER
        else -> "Normal Oxygenation (>300)" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(ratio),
        primaryUnit = "mmHg",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("FiO₂ Fraction", fmt2(fio2 / 100.0))
        ),
        warnings = warnings
    )
}

private fun calcArdsnet(state: CalculatorInputState): CalculationResult {
    val rawH = state.number("height") ?: return CalculationResult()
    if (rawH <= 0.0) return CalculationResult(warnings = listOf("Height must be positive."))

    val inches = toInches(rawH, state.unitIndex("height"))
    val cm = toCm(rawH, state.unitIndex("height"))
    val isFemale = state.choice("sex", "male") == "female"

    val warnings = buildList {
        if (cm < 120.0 || cm > 240.0) add("Height (${fmt0(cm)} cm) is outside typical adult range (120–240 cm). Verify units.")
        if (inches < 60.0) add("Height <152 cm (5 ft): standard Devine PBW formula is less accurate below 60 inches.")
    }

    val pbw = max(20.0, (if (isFemale) 45.5 else 50.0) + 2.3 * (inches - 60.0))
    val tv6 = pbw * 6.0
    val tv7 = pbw * 7.0
    val tv8 = pbw * 8.0

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(pbw),
        primaryUnit = "kg PBW",
        tierLabel = "Target 6 mL/kg: ${fmt0(tv6)} mL",
        tier = ClinicalTier.BLUE,
        secondaryOutputs = listOf(
            SecondaryOutput("6 mL/kg (Target)", "${fmt0(tv6)} mL", highlight = true),
            SecondaryOutput("7 mL/kg", "${fmt0(tv7)} mL"),
            SecondaryOutput("8 mL/kg (Max)", "${fmt0(tv8)} mL")
        ),
        warnings = warnings
    )
}

private fun calcCockcroftGault(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val rawW = state.number("weight") ?: return CalculationResult()
    val rawScr = state.number("scr") ?: return CalculationResult()
    if (age <= 0.0 || rawW <= 0.0 || rawScr <= 0.0) {
        return CalculationResult(warnings = listOf("Age, weight, and SCr must be positive."))
    }

    val isFemale = state.choice("sex", "male") == "female"
    val kg = toKg(rawW, state.unitIndex("weight"))
    val scrMgDl = if (state.unitIndex("scr") == 1) rawScr / 88.4 else rawScr

    val warnings = buildList {
        if (age < 18.0 || age > 120.0) add("Age (${fmt0(age)} yrs) is outside adult Cockcroft-Gault range (18–120).")
        if (kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — consider Adjusted Body Weight in obesity.")
        if (scrMgDl < 0.2 || scrMgDl > 25.0) add("SCr (${fmt2(scrMgDl)} mg/dL) is extreme. Verify units.")
    }

    val base = ((140.0 - age) * kg) / (72.0 * scrMgDl)
    val crcl = max(0.0, if (isFemale) base * 0.85 else base)

    val (tierLabel, tier) = when {
        crcl < 15.0 -> "Severe Impairment (<15)" to ClinicalTier.RED
        crcl < 30.0 -> "Severely Reduced (15–29)" to ClinicalTier.RED
        crcl < 60.0 -> "Moderately Reduced (30–59)" to ClinicalTier.AMBER
        else -> "Preserved Clearance (≥60)" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(crcl),
        primaryUnit = "mL/min",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("SCr Used", "${fmt2(scrMgDl)} mg/dL"),
            SecondaryOutput("Weight Used", "${fmt1(kg)} kg")
        ),
        warnings = warnings
    )
}

private fun calcCkdEpi2021(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val rawScr = state.number("scr") ?: return CalculationResult()
    if (age <= 0.0 || rawScr <= 0.0) {
        return CalculationResult(warnings = listOf("Age and SCr must be positive."))
    }

    val isFemale = state.choice("sex", "male") == "female"
    val scr = if (state.unitIndex("scr") == 1) rawScr / 88.4 else rawScr

    val warnings = buildList {
        if (age < 18.0 || age > 120.0) add("CKD-EPI 2021 is validated for adults ≥18 years.")
        if (scr < 0.2 || scr > 25.0) add("SCr (${fmt2(scr)} mg/dL) is extreme. Verify units.")
    }

    val kappa = if (isFemale) 0.7 else 0.9
    val alpha = if (isFemale) -0.241 else -0.302
    val sexFactor = if (isFemale) 1.012 else 1.0
    val ratio = scr / kappa

    val egfr = 142.0 *
        min(ratio, 1.0).pow(alpha) *
        max(ratio, 1.0).pow(-1.200) *
        0.9938.pow(age) *
        sexFactor

    val (stage, desc, tier) = when {
        egfr >= 90.0 -> Triple("KDIGO G1 (≥90)", "Normal or high", ClinicalTier.GREEN)
        egfr >= 60.0 -> Triple("KDIGO G2 (60–89)", "Mildly decreased", ClinicalTier.GREEN)
        egfr >= 45.0 -> Triple("KDIGO G3a (45–59)", "Mild–moderate decrease", ClinicalTier.AMBER)
        egfr >= 30.0 -> Triple("KDIGO G3b (30–44)", "Moderate–severe decrease", ClinicalTier.AMBER)
        egfr >= 15.0 -> Triple("KDIGO G4 (15–29)", "Severely decreased", ClinicalTier.RED)
        else -> Triple("KDIGO G5 (<15)", "Kidney failure", ClinicalTier.RED)
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(egfr),
        primaryUnit = "mL/min/1.73m²",
        tierLabel = stage,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Category", desc, highlight = true),
            SecondaryOutput("SCr Used", "${fmt2(scr)} mg/dL")
        ),
        warnings = warnings
    )
}

private fun calcFena(state: CalculatorInputState): CalculationResult {
    val uNa = state.number("una") ?: return CalculationResult()
    val sNa = state.number("sna") ?: return CalculationResult()
    val uCr = state.number("ucr") ?: return CalculationResult()
    val sCr = state.number("scr") ?: return CalculationResult()
    if (uNa < 0.0 || sNa <= 0.0 || uCr <= 0.0 || sCr <= 0.0) {
        return CalculationResult(warnings = listOf("Serum Na, Urine Cr, and Serum Cr must be >0."))
    }

    val warnings = buildList {
        if (sNa < 90.0 || sNa > 200.0) add("Serum Na (${fmt0(sNa)} mEq/L) is outside physiological range (90–200).")
    }

    val fena = ((uNa * sCr) / (sNa * uCr)) * 100.0
    val (tierLabel, tier, note) = when {
        fena < 1.0 -> Triple("Prerenal Azotemia (<1%)", ClinicalTier.BLUE, "Intact tubular sodium reabsorption (hypovolemia, CHF, hepatorenal, early contrast/NSAIDs).")
        fena <= 2.0 -> Triple("Indeterminate (1–2%)", ClinicalTier.AMBER, "Overlaps prerenal, early ATN, and diuretic use — correlate with urine microscopy.")
        else -> Triple("Intrinsic Renal / ATN (>2%)", ClinicalTier.RED, "Impaired tubular Na reabsorption consistent with ATN or recent loop/thiazide diuretics.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(fena),
        primaryUnit = "%",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcFreeWaterDeficit(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val currentNa = state.number("current_na") ?: return CalculationResult()
    val targetNa = state.number("target_na") ?: 140.0
    if (rawW <= 0.0 || currentNa <= 0.0 || targetNa <= 0.0) {
        return CalculationResult(warnings = listOf("Weight and sodium values must be positive."))
    }

    val kg = toKg(rawW, state.unitIndex("weight"))
    val factor = when (state.choice("group", "0.60")) {
        "0.50" -> 0.50
        "0.45" -> 0.45
        else -> 0.60
    }

    val warnings = buildList {
        if (currentNa < 90.0 || currentNa > 200.0) add("Current Na (${fmt0(currentNa)} mEq/L) is outside physiological range (90–200).")
        if (currentNa <= targetNa) add("Current Na (${fmt0(currentNa)}) is not greater than Target Na (${fmt0(targetNa)}). Free water deficit applies to hypernatremia.")
        if (kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — verify units.")
    }

    val tbw = kg * factor
    val deficitLiters = max(0.0, tbw * ((currentNa / targetNa) - 1.0))
    val deltaNa = max(0.0, currentNa - targetNa)
    val safeHours = max(24.0, (deltaNa / 10.0) * 24.0)
    val hourlyMl = if (deficitLiters > 0.0) (deficitLiters * 1000.0) / safeHours else 0.0

    val tier = when {
        currentNa >= 160.0 -> ClinicalTier.RED
        currentNa > 145.0 -> ClinicalTier.AMBER
        else -> ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(deficitLiters),
        primaryUnit = "Liters",
        tierLabel = "Safe Drop: ≤10–12 mEq/L/24h",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Est. Total Body Water", "${fmt1(tbw)} L"),
            SecondaryOutput("Conservative Rate", "${fmt0(hourlyMl)} mL/hr over ${fmt0(safeHours)}h", highlight = deficitLiters > 0)
        ),
        clinicalNote = "Replace slowly (max Na drop ≤10–12 mEq/L per 24h) to avoid cerebral edema; add ongoing free water losses.",
        warnings = warnings
    )
}

private fun calcChildPugh(state: CalculatorInputState): CalculationResult {
    val keys = listOf("bili", "albumin", "inr", "ascites", "enceph")
    val values = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (values.size < keys.size) return missingChoicePrompt(values.size, keys.size)

    val score = values.sum()
    val (tierLabel, tier, surv) = when {
        score <= 6 -> Triple("Class A (5–6 · Compensated)", ClinicalTier.GREEN, "1-yr ~100% · 2-yr ~85%")
        score <= 9 -> Triple("Class B (7–9 · Significant)", ClinicalTier.AMBER, "1-yr ~80% · 2-yr ~60%")
        else -> Triple("Class C (10–15 · Decompensated)", ClinicalTier.RED, "1-yr ~45% · 2-yr ~35%")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 15",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Estimated Survival", surv, highlight = true)
        )
    )
}

private fun calcMeldNa(state: CalculatorInputState): CalculationResult {
    val rawBili = state.number("bili") ?: return CalculationResult()
    val rawInr = state.number("inr") ?: return CalculationResult()
    val rawScr = state.number("scr") ?: return CalculationResult()
    val rawNa = state.number("na") ?: return CalculationResult()
    val dialysis = state.toggle("dialysis")

    if (rawBili <= 0.0 || rawInr <= 0.0 || rawScr <= 0.0 || rawNa <= 0.0) {
        return CalculationResult(warnings = listOf("All lab values must be positive."))
    }

    val warnings = buildList {
        if (rawNa < 90.0 || rawNa > 200.0) add("Sodium (${fmt0(rawNa)} mEq/L) is outside physiological range (90–200).")
        if (rawInr > 20.0) add("INR (${fmt1(rawInr)}) is extremely elevated.")
    }

    val bili = max(rawBili, 1.0)
    val inr = max(rawInr, 1.0)
    val scr = if (dialysis) 4.0 else rawScr.coerceIn(1.0, 4.0)

    val meldRaw = 3.78 * ln(bili) + 11.2 * ln(inr) + 9.57 * ln(scr) + 6.43
    val meldClassic = meldRaw.roundToInt().coerceIn(6, 40)

    val meldNaFinal = if (meldRaw > 11.0) {
        val naClamped = rawNa.coerceIn(125.0, 137.0)
        val adjusted = meldRaw + 1.32 * (137.0 - naClamped) - 0.033 * meldRaw * (137.0 - naClamped)
        adjusted.roundToInt().coerceIn(6, 40)
    } else {
        meldClassic
    }

    val (mort90d, tier) = when {
        meldNaFinal <= 9 -> "1.9%" to ClinicalTier.GREEN
        meldNaFinal <= 19 -> "6.0%" to ClinicalTier.AMBER
        meldNaFinal <= 29 -> "19.6%" to ClinicalTier.RED
        meldNaFinal <= 39 -> "52.6%" to ClinicalTier.RED
        else -> "71.3%" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$meldNaFinal",
        primaryUnit = "MELD-Na",
        tierLabel = "Est. 90-Day Mortality: $mort90d",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Classic MELD", "$meldClassic"),
            SecondaryOutput("Na Applied", if (meldRaw > 11.0) "${fmt0(rawNa.coerceIn(125.0, 137.0))} mEq/L" else "Not applied (MELD ≤11)")
        ),
        warnings = warnings
    )
}

private fun calcMaddrey(state: CalculatorInputState): CalculationResult {
    val ptPat = state.number("pt_patient") ?: return CalculationResult()
    val ptCtrl = state.number("pt_control") ?: return CalculationResult()
    val rawBili = state.number("bili") ?: return CalculationResult()
    if (ptPat <= 0.0 || ptCtrl <= 0.0 || rawBili < 0.0) {
        return CalculationResult(warnings = listOf("PT values must be positive."))
    }

    val biliMgDl = if (state.unitIndex("bili") == 1) rawBili / 17.1 else rawBili
    val warnings = buildList {
        if (ptCtrl < 8.0 || ptCtrl > 20.0) add("Control PT (${fmt1(ptCtrl)} sec) is outside typical lab control range (10–15 sec).")
    }

    val df = 4.6 * (ptPat - ptCtrl) + biliMgDl
    val (tierLabel, tier, note) = if (df >= 32.0) {
        Triple("Severe Alc. Hepatitis (≥32)", ClinicalTier.RED, "High 30-day mortality (~35–50%); evaluate for prednisolone after ruling out infection/GI bleed.")
    } else {
        Triple("Non-Severe (<32)", ClinicalTier.GREEN, "Supportive nutritional care & alcohol abstinence; steroids generally not indicated.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(df),
        primaryUnit = "DF",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Bilirubin Used", "${fmt1(biliMgDl)} mg/dL")
        ),
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcSaag(state: CalculatorInputState): CalculationResult {
    val sAlb = state.number("serum_alb") ?: return CalculationResult()
    val aAlb = state.number("ascitic_alb") ?: return CalculationResult()
    if (sAlb <= 0.0 || aAlb < 0.0) return CalculationResult(warnings = listOf("Serum albumin must be positive."))

    val warnings = buildList {
        if (sAlb > 7.0 || aAlb > 7.0) add("Albumin >7 g/dL is outside physiological range — verify g/dL vs g/L.")
    }

    val saag = sAlb - aAlb
    val (tierLabel, tier, note) = if (saag >= 1.1) {
        Triple("Portal Hypertension (≥1.1)", ClinicalTier.AMBER, "Consistent with cirrhosis, alcoholic hepatitis, CHF, or Budd-Chiari syndrome.")
    } else {
        Triple("Non-Portal Hypertension (<1.1)", ClinicalTier.BLUE, "Consider peritoneal carcinomatosis, TB peritonitis, pancreatitis, serositis, or nephrotic syndrome.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(saag),
        primaryUnit = "g/dL",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcSteroidEquivalence(state: CalculatorInputState): CalculationResult {
    val dose = state.number("dose_mg") ?: return CalculationResult()
    if (dose <= 0.0) return CalculationResult(warnings = listOf("Dose must be greater than 0 mg."))

    val source = state.choice("source_steroid", "pred") ?: "pred"
    val factor = when (source) {
        "hydro" -> 20.0
        "pred" -> 5.0
        "methyl" -> 4.0
        "triam" -> 4.0
        "dex" -> 0.75
        else -> 5.0
    }
    val units = dose / factor

    val hydro = units * 20.0
    val pred = units * 5.0
    val methyl = units * 4.0
    val triam = units * 4.0
    val dex = units * 0.75

    return CalculationResult(
        isReady = true,
        primaryValue = fmtClean(pred),
        primaryUnit = "mg Pred eq.",
        tierLabel = "Simultaneous Equivalence Grid",
        tier = ClinicalTier.BLUE,
        secondaryOutputs = listOf(
            SecondaryOutput("Hydrocortisone", "${fmtClean(hydro)} mg", highlight = source == "hydro"),
            SecondaryOutput("Prednisolone", "${fmtClean(pred)} mg", highlight = source == "pred"),
            SecondaryOutput("Methylprednisolone", "${fmtClean(methyl)} mg", highlight = source == "methyl"),
            SecondaryOutput("Triamcinolone", "${fmtClean(triam)} mg", highlight = source == "triam"),
            SecondaryOutput("Dexamethasone", "${fmt2(dex)} mg", highlight = source == "dex")
        )
    )
}

private fun calcIbwAdjBw(state: CalculatorInputState): CalculationResult {
    val rawH = state.number("height") ?: return CalculationResult()
    if (rawH <= 0.0) return CalculationResult(warnings = listOf("Height must be positive."))

    val isFemale = state.choice("sex", "male") == "female"
    val inches = toInches(rawH, state.unitIndex("height"))
    val cm = toCm(rawH, state.unitIndex("height"))
    val rawActual = state.number("actual_weight")
    val actualKg = rawActual?.let { toKg(it, state.unitIndex("actual_weight")) }

    val warnings = buildList {
        if (cm < 120.0 || cm > 240.0) add("Height (${fmt0(cm)} cm) is outside typical adult range (120–240 cm).")
        if (inches < 60.0) add("Height <60 in (152 cm): Devine IBW loses accuracy below 5 feet.")
        if (actualKg != null && actualKg > 350.0) add("Actual weight (${fmt1(actualKg)} kg) is extreme — verify units.")
    }

    val ibw = max(20.0, (if (isFemale) 45.5 else 50.0) + 2.3 * (inches - 60.0))

    val secondary = buildList {
        if (actualKg != null && actualKg > 0.0) {
            val adjBw = ibw + 0.4 * (actualKg - ibw)
            val pctIbw = (actualKg / ibw) * 100.0
            add(SecondaryOutput("Adjusted Body Wt", "${fmt1(adjBw)} kg", highlight = true))
            add(SecondaryOutput("% of IBW", "${fmt0(pctIbw)}%"))
        } else {
            add(SecondaryOutput("Adjusted Body Wt", "Enter actual weight below"))
        }
        add(SecondaryOutput("Height", "${fmt0(cm)} cm (${fmt1(inches)} in)"))
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(ibw),
        primaryUnit = "kg IBW",
        tierLabel = if (actualKg != null && actualKg > ibw * 1.2) "Obese (>120% IBW · Use AdjBW)" else "Devine Formula",
        tier = if (actualKg != null && actualKg > ibw * 1.2) ClinicalTier.AMBER else ClinicalTier.GREEN,
        secondaryOutputs = secondary,
        warnings = warnings
    )
}

private fun calcMallampatiAsa(state: CalculatorInputState): CalculationResult {
    val mall = state.choice("mallampati")
    val asa = state.choice("asa")
    val count = listOfNotNull(mall, asa).size
    if (count < 2) return missingChoicePrompt(count, 2)

    val isEmergency = state.toggle("emergency_e")
    val asaLabel = if (isEmergency) "ASA ${asa}E" else "ASA $asa"
    val difficultAirway = mall == "III" || mall == "IV"
    val highSystemic = asa == "III" || asa == "IV" || asa == "V"

    val (tierLabel, tier, note) = when {
        asa == "IV" || asa == "V" || (difficultAirway && isEmergency) -> Triple(
            "High Perioperative / Airway Risk",
            ClinicalTier.RED,
            "Senior anesthesia backup & video laryngoscope/difficult airway cart ready before induction."
        )
        difficultAirway || highSystemic || isEmergency -> Triple(
            "Elevated Pre-Op Vigilance",
            ClinicalTier.AMBER,
            "Optimize comorbidities pre-op; prepare backup airway adjuncts and clear Plan A/B/C."
        )
        else -> Triple(
            "Low Baseline Pre-Op Risk",
            ClinicalTier.GREEN,
            "Standard airway assessment & routine perioperative monitoring."
        )
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "M-$mall · $asaLabel",
        primaryUnit = "",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Airway Screen", if (difficultAirway) "Mallampati $mall (Higher Difficult Risk)" else "Mallampati $mall (Favorable View)", highlight = difficultAirway),
            SecondaryOutput("Systemic Status", asaLabel, highlight = highSystemic || isEmergency)
        ),
        clinicalNote = note
    )
}

private fun calcEmergencyResus(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    if (rawW <= 0.0) return CalculationResult(warnings = listOf("Weight must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val warnings = buildList {
        if (kg > 200.0) add("Weight (${fmt1(kg)} kg) exceeds 200 kg — verify units.")
    }

    // Anaphylaxis IM 1:1,000 (1 mg/mL): 0.01 mg/kg, max 0.5 mg
    val imMg = min(0.01 * kg, 0.5)
    val imMl = imMg // 1:1,000 = 1 mg/mL

    // Arrest IV 1:10,000 (0.1 mg/mL): 0.01 mg/kg, max 1.0 mg
    val ivMg = min(0.01 * kg, 1.0)
    val ivMl = ivMg * 10.0 // 1:10,000 = 0.1 mg/mL

    // Defib: 2 J/kg & 4 J/kg, max 200 J
    val defib2 = min(2.0 * kg, 200.0)
    val defib4 = min(4.0 * kg, 200.0)

    // Isotonic Bolus: 20 mL/kg
    val bolusMl = 20.0 * kg

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(imMl),
        primaryUnit = "mL IM (1:1,000)",
        tierLabel = "Anaphylaxis: ${fmt2(imMg)} mg IM · Weight ${fmt1(kg)} kg",
        tier = ClinicalTier.RED,
        secondaryOutputs = listOf(
            SecondaryOutput("Anaphylaxis IM 1:1,000", "${fmt2(imMg)} mg  (${fmt2(imMl)} mL)", highlight = true),
            SecondaryOutput("Arrest IV 1:10,000", "${fmt2(ivMg)} mg  (${fmt1(ivMl)} mL)", highlight = true),
            SecondaryOutput("Peads Defib (2 → 4 J/kg)", "${fmt0(defib2)} J → ${fmt0(defib4)} J"),
            SecondaryOutput("Isotonic Bolus (20 mL/kg)", "${fmt0(bolusMl)} mL")
        ),
        clinicalNote = "Verify 1:1,000 (IM only) vs 1:10,000 (IV/IO arrest). Reassess after each 20 mL/kg bolus.",
        warnings = warnings
    )
}
