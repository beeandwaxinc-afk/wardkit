package com.example.domain.phase2

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
import com.example.domain.FormulaEngine.toKg
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

internal fun evaluatePhase2B(
    calculatorId: String,
    state: CalculatorInputState
): CalculationResult? = when (calculatorId) {
    // Internal Medicine (+4)
    "im_bmi_bsa" -> calcBmiBsa(state)
    "im_centor" -> calcCentor(state)
    "im_news2" -> calcNews2(state)
    "im_osmolality" -> calcOsmolality(state)
    // Nephrology (+4)
    "nephro_adrogue_madias" -> calcAdrogueMadias(state)
    "nephro_feurea" -> calcFeUrea(state)
    "nephro_k_replace" -> calcPotassiumReplacement(state)
    "nephro_uag" -> calcUrineAnionGap(state)
    // Neuro & Neurosurgery (+4)
    "neuro_abcd2" -> calcAbcd2(state)
    "neuro_four" -> calcFourScore(state)
    "neuro_mod_fisher" -> calcModFisher(state)
    "neuro_mrs" -> calcMrs(state)
    // Obs & Gynae (+4)
    "obgyn_ebl" -> calcEbl(state)
    "obgyn_gwg" -> calcGwgIom(state)
    "obgyn_preeclampsia" -> calcPreeclampsia(state)
    "obgyn_vbac" -> calcVbacGrobman(state)
    // Pediatrics (+5)
    "peads_gcs" -> calcPediatricGcs(state)
    "peads_urine_output" -> calcPeadsUrineOutput(state)
    "peads_pews" -> calcPews(state)
    "peads_westley" -> calcWestleyCroup(state)
    "peads_wetflag" -> calcWetflag(state)
    else -> null
}

// ============================================================================
// 6. INTERNAL MEDICINE PHASE 2
// ============================================================================
private fun calcBmiBsa(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val rawH = state.number("height") ?: return CalculationResult()
    if (rawW <= 0.0 || rawH <= 0.0) return CalculationResult(warnings = listOf("Weight and height must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val cm = toCm(rawH, state.unitIndex("height"))
    val m = cm / 100.0

    val warnings = buildList {
        if (cm < 40.0 || cm > 250.0) add("Height (${fmt0(cm)} cm) is outside typical range (40–250 cm).")
        if (kg > 350.0) add("Weight (${fmt1(kg)} kg) exceeds 350 kg — verify units.")
    }

    val bmi = kg / (m * m)
    val bsa = sqrt((cm * kg) / 3600.0)

    val (tierLabel, tier) = when {
        bmi < 18.5 -> "Underweight (<18.5)" to ClinicalTier.BLUE
        bmi < 25.0 -> "Normal Weight (18.5–24.9)" to ClinicalTier.GREEN
        bmi < 30.0 -> "Overweight (25.0–29.9)" to ClinicalTier.AMBER
        bmi < 35.0 -> "Obese Class I (30.0–34.9)" to ClinicalTier.AMBER
        bmi < 40.0 -> "Obese Class II (35.0–39.9)" to ClinicalTier.RED
        else -> "Obese Class III (≥40.0)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(bmi),
        primaryUnit = "kg/m²",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Mosteller BSA", "${fmt2(bsa)} m²", highlight = true),
            SecondaryOutput("Metrics", "${fmt1(kg)} kg · ${fmt0(cm)} cm")
        ),
        warnings = warnings
    )
}

private fun calcCentor(state: CalculatorInputState): CalculationResult {
    val agePts = state.choice("age_group")?.toIntOrNull()
        ?: return CalculationResult(isReady = false, emptyPrompt = "Select patient age group below")

    var score = agePts
    if (state.toggle("exudate")) score += 1
    if (state.toggle("nodes")) score += 1
    if (state.toggle("fever")) score += 1
    if (state.toggle("no_cough")) score += 1

    val (tierLabel, tier, note) = when {
        score <= 1 -> Triple("Low Risk (≤1 · No Test / No Abx)", ClinicalTier.GREEN, "GAS probability <10%; symptomatic care only—neither RADT nor antibiotics recommended.")
        score <= 3 -> Triple("Intermediate (2–3 · Rapid Strep)", ClinicalTier.AMBER, "GAS probability ~15–35%; perform Rapid Antigen Detection Test (RADT) or throat culture.")
        else -> Triple("High Probability (≥4 · Empiric/RADT)", ClinicalTier.RED, "GAS probability ~50%+; perform RADT or consider empiric penicillin/amoxicillin per local guidance.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "pts",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcNews2(state: CalculatorInputState): CalculationResult {
    val scale = state.choice("spo2_scale", "scale1") ?: "scale1"
    val rrId = state.choice("rr")
    val spo2Id = state.choice(if (scale == "scale1") "spo2_s1" else "spo2_s2")
    val o2Id = state.choice("oxygen")
    val sbpId = state.choice("sbp")
    val hrId = state.choice("pulse")
    val acvpuId = state.choice("acvpu")
    val tempId = state.choice("temp")

    val selectedIds = listOfNotNull(rrId, spo2Id, o2Id, sbpId, hrId, acvpuId, tempId)
    if (selectedIds.size < 7) return missingChoicePrompt(selectedIds.size, 7)

    fun mapScore(id: String): Int = when (id) {
        "rr_low3", "rr_hi3", "s1_3", "s2_low3", "s2_hi3", "sbp_low3", "sbp_hi3", "hr_low3", "hr_hi3", "3", "t_low3" -> 3
        "rr_hi2", "s1_2", "s2_low2", "s2_hi2", "2", "sbp_low2", "hr_hi2", "t_hi2" -> 2
        "rr_low1", "s1_1", "s2_low1", "s2_hi1", "sbp_low1", "hr_low1", "hr_hi1", "t_low1", "t_hi1" -> 1
        else -> 0
    }

    val scores = selectedIds.map { mapScore(it) }
    val total = scores.sum()
    val hasRedScore = scores.any { it == 3 }

    val (tierLabel, tier, freq, note) = when {
        total >= 7 -> Quad4(
            "HIGH RISK — EMERGENCY",
            ClinicalTier.RED,
            "Continuous Monitoring",
            "Urgent critical care / registrar assessment immediately; consider HDU/ICU transfer."
        )
        total >= 5 -> Quad4(
            "MEDIUM RISK (5–6)",
            ClinicalTier.AMBER,
            "Min q1h Obs",
            "Urgent review by ward clinician with core acute-care competencies; screen for sepsis."
        )
        hasRedScore -> Quad4(
            "LOW-MEDIUM · RED SCORE",
            ClinicalTier.RED,
            "Min q1h Obs",
            "Single parameter scored 3 (RED SCORE): urgent review by ward-based doctor required."
        )
        total >= 1 -> Quad4(
            "LOW RISK (1–4)",
            ClinicalTier.GREEN,
            "Min q4–6h Obs",
            "Registered nurse assessment to decide whether to increase monitoring frequency."
        )
        else -> Quad4(
            "LOW RISK (0)",
            ClinicalTier.GREEN,
            "Min q12h Obs",
            "Continue routine NEWS2 monitoring every 12 hours."
        )
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 20",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Monitoring", freq, highlight = total >= 5 || hasRedScore),
            SecondaryOutput("Red Score (Any 3)", if (hasRedScore) "YES — Alert!" else "None", highlight = hasRedScore)
        ),
        clinicalNote = note
    )
}

private fun calcOsmolality(state: CalculatorInputState): CalculationResult {
    val na = state.number("na") ?: return CalculationResult()
    val rawGlu = state.number("glucose") ?: return CalculationResult()
    val rawBun = state.number("bun") ?: return CalculationResult()
    val ethanol = state.number("ethanol") ?: 0.0
    val measured = state.number("measured_osm")

    if (na <= 0.0 || rawGlu < 0.0 || rawBun < 0.0) {
        return CalculationResult(warnings = listOf("Sodium must be positive; glucose & BUN non-negative."))
    }

    val gluMg = if (state.unitIndex("glucose") == 1) rawGlu * 18.0182 else rawGlu
    val bunMg = if (state.unitIndex("bun") == 1) rawBun * 2.801 else rawBun

    val warnings = buildList {
        if (na < 90.0 || na > 200.0) add("Sodium (${fmt0(na)} mEq/L) is outside physiological range (90–200).")
    }

    val calcOsm = (2.0 * na) + (gluMg / 18.0) + (bunMg / 2.8) + (ethanol / 4.6)
    val gap = measured?.let { it - calcOsm }

    val (tierLabel, tier) = when {
        gap != null && gap > 10.0 -> "Elevated Osmolar Gap (>10 · Toxic Alcohol?)" to ClinicalTier.RED
        calcOsm < 275.0 -> "Hypo-Osmolar (<275)" to ClinicalTier.BLUE
        calcOsm <= 295.0 -> "Normal Osmolality (275–295)" to ClinicalTier.GREEN
        else -> "Hyperosmolar (>295)" to ClinicalTier.AMBER
    }

    val secondary = buildList {
        if (gap != null) {
            add(SecondaryOutput("Osmolar Gap", "${fmt1(gap)} mOsm/kg", highlight = gap > 10.0))
            add(SecondaryOutput("Measured Osm", "${fmt1(measured)} mOsm/kg"))
        } else {
            add(SecondaryOutput("Normal Range", "275–295 mOsm/kg"))
        }
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(calcOsm),
        primaryUnit = "mOsm/kg",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = secondary,
        warnings = warnings
    )
}

// ============================================================================
// 7. NEPHROLOGY PHASE 2
// ============================================================================
private fun calcAdrogueMadias(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val serumNa = state.number("serum_na") ?: return CalculationResult()
    if (rawW <= 0.0 || serumNa <= 0.0) return CalculationResult(warnings = listOf("Weight and serum Na must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val factor = when (state.choice("group", "0.60")) {
        "0.50" -> 0.50
        "0.45" -> 0.45
        else -> 0.60
    }
    val infusateNa = (state.choice("infusate", "154") ?: "154").toDoubleOrNull() ?: 154.0
    val infusateK = state.number("infusate_k") ?: 0.0

    val warnings = buildList {
        if (serumNa < 90.0 || serumNa > 200.0) add("Serum Na (${fmt0(serumNa)} mEq/L) is outside physiological range (90–200).")
        if (kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — verify units.")
    }

    val tbw = kg * factor
    val deltaPerLiter = (infusateNa + infusateK - serumNa) / (tbw + 1.0)
    val sign = if (deltaPerLiter >= 0) "+" else ""
    val volFor8Meq = if (kotlin.math.abs(deltaPerLiter) > 0.01) (8.0 / kotlin.math.abs(deltaPerLiter)) * 1000.0 else 0.0

    val tier = when {
        kotlin.math.abs(deltaPerLiter) >= 8.0 -> ClinicalTier.RED
        kotlin.math.abs(deltaPerLiter) >= 4.0 -> ClinicalTier.AMBER
        else -> ClinicalTier.BLUE
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$sign${fmt2(deltaPerLiter)}",
        primaryUnit = "mEq/L per 1 L",
        tierLabel = "Max Safe 24h Δ: ≤8–10 mEq/L (Avoid ODS)",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Est. TBW", "${fmt1(tbw)} L"),
            SecondaryOutput("Vol for 8 mEq/L Δ", if (volFor8Meq > 0) "${fmt0(volFor8Meq)} mL" else "—", highlight = true)
        ),
        clinicalNote = "Recheck serum Na every 4–6 hours; rapid overcorrection risks Osmotic Demyelination Syndrome (ODS).",
        warnings = warnings
    )
}

private fun calcFeUrea(state: CalculatorInputState): CalculationResult {
    val uUrea = state.number("u_urea") ?: return CalculationResult()
    val sUrea = state.number("s_urea") ?: return CalculationResult()
    val uCr = state.number("u_cr") ?: return CalculationResult()
    val sCr = state.number("s_cr") ?: return CalculationResult()
    if (uUrea < 0.0 || sUrea <= 0.0 || uCr <= 0.0 || sCr <= 0.0) {
        return CalculationResult(warnings = listOf("Serum Urea, Urine Cr, and Serum Cr must be >0."))
    }

    val feUrea = ((uUrea * sCr) / (sUrea * uCr)) * 100.0
    val (tierLabel, tier, note) = when {
        feUrea <= 35.0 -> Triple("Prerenal AKI (≤35% · Valid on Diuretics)", ClinicalTier.BLUE, "Consistent with prerenal hypoperfusion even during loop/thiazide diuretic therapy.")
        feUrea <= 50.0 -> Triple("Indeterminate (36–50%)", ClinicalTier.AMBER, "Overlap zone; integrate clinical volume status and urine sediment.")
        else -> Triple("Intrinsic AKI / ATN (>50%)", ClinicalTier.RED, "Impaired proximal tubular urea reabsorption consistent with intrinsic tubular injury.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(feUrea),
        primaryUnit = "%",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcPotassiumReplacement(state: CalculatorInputState): CalculationResult {
    val currentK = state.number("current_k") ?: return CalculationResult()
    val targetK = state.number("target_k") ?: 4.0
    val route = state.choice("route", "peripheral") ?: "peripheral"

    if (currentK <= 0.0 || targetK <= 0.0) {
        return CalculationResult(warnings = listOf("Serum potassium values must be positive."))
    }

    val warnings = buildList {
        if (currentK >= targetK) add("Current K⁺ (${fmt1(currentK)}) is ≥ Target K⁺ (${fmt1(targetK)}). No replacement deficit calculated.")
        if (currentK < 1.5 || currentK > 9.0) add("Serum K⁺ (${fmt1(currentK)} mEq/L) is extreme — verify sample (rule out hemolysis).")
    }

    val deficitMeq = max(0.0, ((targetK - currentK) / 0.3) * 100.0)
    val maxRate = if (route == "central") "≤20 mEq/hr (Central + ECG)" else "≤10 mEq/hr (Peripheral IV)"

    val (tierLabel, tier) = when {
        currentK < 2.5 -> "Severe Hypokalemia (<2.5 · Urgent ECG)" to ClinicalTier.RED
        currentK < 3.0 -> "Moderate Hypokalemia (2.5–2.9)" to ClinicalTier.AMBER
        currentK < 3.5 -> "Mild Hypokalemia (3.0–3.4)" to ClinicalTier.AMBER
        else -> "Near Target (≥3.5)" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(deficitMeq),
        primaryUnit = "mEq Deficit",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Max IV Rate", maxRate, highlight = true),
            SecondaryOutput("Target K⁺", "${fmt1(targetK)} mEq/L")
        ),
        clinicalNote = "MANDATORY: Check & replete Serum Magnesium (Mg²⁺)—hypokalemia is refractory until Mg²⁺ is corrected. Recheck K⁺ after every 40–60 mEq.",
        warnings = warnings
    )
}

private fun calcUrineAnionGap(state: CalculatorInputState): CalculationResult {
    val uNa = state.number("una") ?: return CalculationResult()
    val uK = state.number("uk") ?: return CalculationResult()
    val uCl = state.number("ucl") ?: return CalculationResult()

    val uag = (uNa + uK) - uCl
    val (tierLabel, tier, note) = when {
        uag < 0.0 -> Triple("Negative (<0 · GI Loss / Diarrhea)", ClinicalTier.BLUE, "Intact renal NH₄⁺ excretion; points to extrarenal GI bicarbonate loss (diarrhea, fistula).")
        uag > 0.0 -> Triple("Positive (>0 · Renal Tubular Acidosis)", ClinicalTier.AMBER, "Impaired renal NH₄⁺ acidification; points to distal (Type 1) or hyperkalemic (Type 4) RTA.")
        else -> Triple("Borderline (0 mEq/L)", ClinicalTier.BLUE, "Correlate with urine pH, serum K⁺, and urine osmolal gap.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = if (uag > 0) "+${fmt1(uag)}" else fmt1(uag),
        primaryUnit = "mEq/L",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

// ============================================================================
// 8. NEURO & NEUROSURGERY PHASE 2
// ============================================================================
private fun calcAbcd2(state: CalculatorInputState): CalculationResult {
    val clinical = state.choice("clinical")?.toIntOrNull()
    val duration = state.choice("duration")?.toIntOrNull()
    val count = listOfNotNull(clinical, duration).size
    if (count < 2) return missingChoicePrompt(count, 2)

    var score = clinical!! + duration!!
    if (state.toggle("age60")) score += 1
    if (state.toggle("bp140")) score += 1
    if (state.toggle("diabetes")) score += 1

    val (tierLabel, risk2d, tier) = when {
        score <= 3 -> Triple("Low Risk (0–3)", "1.0%", ClinicalTier.GREEN)
        score <= 5 -> Triple("Moderate Risk (4–5)", "4.1%", ClinicalTier.AMBER)
        else -> Triple("High Risk (6–7)", "8.1%", ClinicalTier.RED)
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 7",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("2-Day Stroke Risk", risk2d, highlight = score >= 4)
        ),
        clinicalNote = "Ensure rapid antiplatelet therapy, carotid imaging, ECG/telemetry, and stroke neurology evaluation."
    )
}

private fun calcFourScore(state: CalculatorInputState): CalculationResult {
    val e = state.choice("eye")?.toIntOrNull()
    val m = state.choice("motor")?.toIntOrNull()
    val b = state.choice("brainstem")?.toIntOrNull()
    val r = state.choice("respiration")?.toIntOrNull()
    val count = listOfNotNull(e, m, b, r).size
    if (count < 4) return missingChoicePrompt(count, 4)

    val total = e!! + m!! + b!! + r!!
    val (tierLabel, tier) = when {
        total == 0 -> "Score 0 · Evaluate Brain Death" to ClinicalTier.RED
        total <= 7 -> "Severe Coma (≤7 · High Mortality)" to ClinicalTier.RED
        total <= 12 -> "Moderate Impairment (8–12)" to ClinicalTier.AMBER
        else -> "Mild / Intact (13–16)" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 16",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Breakdown", "E$e  M$m  B$b  R$r", highlight = true)
        )
    )
}

private fun calcModFisher(state: CalculatorInputState): CalculationResult {
    val grade = state.choice("grade")?.toIntOrNull()
        ?: return CalculationResult(isReady = false, emptyPrompt = "Select CT SAH grade below")

    val (risk, tier, note) = when (grade) {
        0 -> Triple("0%", ClinicalTier.GREEN, "No subarachnoid or intraventricular blood visualized on CT.")
        1 -> Triple("24%", ClinicalTier.AMBER, "Focal or diffuse thin SAH without IVH; start nimodipine & monitor for DCI.")
        2 -> Triple("33%", ClinicalTier.AMBER, "Thin SAH with IVH; elevated symptomatic vasospasm & hydrocephalus risk.")
        3 -> Triple("33%", ClinicalTier.RED, "Thick cisternal/fissural SAH; high risk for delayed cerebral ischemia.")
        else -> Triple("40%", ClinicalTier.RED, "Thick SAH with bilateral IVH; highest symptomatic vasospasm risk—ICU monitoring & nimodipine.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "Grade $grade",
        primaryUnit = "",
        tierLabel = "Symptomatic Vasospasm Risk: $risk",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Vasospasm Risk", risk, highlight = grade >= 2)
        ),
        clinicalNote = note
    )
}

private fun calcMrs(state: CalculatorInputState): CalculationResult {
    val grade = state.choice("mrs_grade")?.toIntOrNull()
        ?: return CalculationResult(isReady = false, emptyPrompt = "Select functional status below")

    val (tierLabel, tier) = when {
        grade <= 2 -> "Functionally Independent (0–2)" to ClinicalTier.GREEN
        grade <= 5 -> "Functionally Dependent (3–5)" to ClinicalTier.AMBER
        else -> "Deceased (6)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "mRS $grade",
        primaryUnit = "/ 6",
        tierLabel = tierLabel,
        tier = tier
    )
}

// ============================================================================
// 9. OBS & GYNAE PHASE 2
// ============================================================================
private fun calcEbl(state: CalculatorInputState): CalculationResult {
    val wetG = state.number("wet_g") ?: return CalculationResult()
    val dryG = state.number("dry_g") ?: return CalculationResult()
    val suction = state.number("suction_ml") ?: 0.0

    if (wetG < 0.0 || dryG < 0.0 || suction < 0.0) {
        return CalculationResult(warnings = listOf("Weights and volumes cannot be negative."))
    }

    val warnings = buildList {
        if (wetG < dryG) add("Wet weight (${fmt0(wetG)} g) is less than dry weight (${fmt0(dryG)} g); pad blood loss clamped to 0 mL.")
    }

    val padBlood = max(0.0, wetG - dryG)
    val totalEbl = padBlood + suction

    val (tierLabel, tier, note) = when {
        totalEbl < 500.0 -> Triple("Normal (<500 mL)", ClinicalTier.GREEN, "Within normal vaginal delivery blood loss threshold.")
        totalEbl < 1000.0 -> Triple("PPH Alert · Vaginal (500–999 mL)", ClinicalTier.AMBER, "Increased monitoring, uterotonics, fundal massage, and obstetric shock index check.")
        else -> Triple("Major PPH · ACOG Threshold (≥1,000 mL)", ClinicalTier.RED, "Activate PPH bundle: IV access, uterotonics, TXA 1 g IV, crossmatch & senior escalation.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(totalEbl),
        primaryUnit = "mL EBL",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Pads / Sponges", "${fmt0(padBlood)} mL"),
            SecondaryOutput("Net Suction", "${fmt0(suction)} mL")
        ),
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcGwgIom(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val rawH = state.number("height") ?: return CalculationResult()
    if (rawW <= 0.0 || rawH <= 0.0) return CalculationResult(warnings = listOf("Weight and height must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val cm = toCm(rawH, state.unitIndex("height"))
    val m = cm / 100.0
    val bmi = kg / (m * m)
    val isTwin = state.choice("gestation", "singleton") == "twin"

    val warnings = buildList {
        if (cm < 120.0 || cm > 220.0) add("Height (${fmt0(cm)} cm) is outside typical adult range.")
    }

    val (rangeKg, rangeLbs, weeklyRate, category, tier) = if (!isTwin) {
        when {
            bmi < 18.5 -> Quint("12.5–18 kg", "28–40 lbs", "0.51 kg/wk", "Underweight (<18.5)", ClinicalTier.BLUE)
            bmi < 25.0 -> Quint("11.5–16 kg", "25–35 lbs", "0.42 kg/wk", "Normal BMI (18.5–24.9)", ClinicalTier.GREEN)
            bmi < 30.0 -> Quint("7–11.5 kg", "15–25 lbs", "0.28 kg/wk", "Overweight (25–29.9)", ClinicalTier.AMBER)
            else -> Quint("5–9 kg", "11–20 lbs", "0.22 kg/wk", "Obese (≥30)", ClinicalTier.AMBER)
        }
    } else {
        when {
            bmi < 18.5 -> Quint("Individualized", "Specialist Care", "Individualized", "Twin · Underweight", ClinicalTier.AMBER)
            bmi < 25.0 -> Quint("17–25 kg", "37–54 lbs", "Twin Target", "Twin · Normal BMI", ClinicalTier.GREEN)
            bmi < 30.0 -> Quint("14–23 kg", "31–50 lbs", "Twin Target", "Twin · Overweight", ClinicalTier.AMBER)
            else -> Quint("11–19 kg", "25–42 lbs", "Twin Target", "Twin · Obese", ClinicalTier.AMBER)
        }
    }

    return CalculationResult(
        isReady = true,
        primaryValue = rangeKg,
        primaryUnit = "Total Gain",
        tierLabel = "$category · BMI ${fmt1(bmi)}",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("In Pounds", rangeLbs, highlight = true),
            SecondaryOutput("2nd/3rd Tri Rate", weeklyRate, highlight = !isTwin)
        ),
        warnings = warnings
    )
}

private fun calcPreeclampsia(state: CalculatorInputState): CalculationResult {
    val keys = listOf("bp", "plt", "renal", "liver", "edema", "neuro")
    val count = keys.count { state.toggle(it) }

    return if (count == 0) {
        CalculationResult(
            isReady = true,
            primaryValue = "No Severe",
            primaryUnit = "0 / 6",
            tierLabel = "Without Severe Features",
            tier = ClinicalTier.GREEN,
            clinicalNote = "Continue close maternal/fetal surveillance, serial BP/labs, and delivery planning at 37 0/7 weeks."
        )
    } else {
        CalculationResult(
            isReady = true,
            primaryValue = "SEVERE",
            primaryUnit = "$count / 6",
            tierLabel = "PREECLAMPSIA WITH SEVERE FEATURES",
            tier = ClinicalTier.RED,
            clinicalNote = "Initiate IV MgSO₄ seizure prophylaxis + urgent BP control if ≥160/110 + prompt obstetric delivery evaluation."
        )
    }
}

private fun calcVbacGrobman(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val rawW = state.number("weight") ?: return CalculationResult()
    val rawH = state.number("height") ?: return CalculationResult()
    if (age <= 0.0 || rawW <= 0.0 || rawH <= 0.0) {
        return CalculationResult(warnings = listOf("Age, weight, and height must be positive."))
    }

    val kg = toKg(rawW, state.unitIndex("weight"))
    val cm = toCm(rawH, state.unitIndex("height"))
    val hx = state.choice("obstetric_hx", "none") ?: "none"
    val priorVag = if (hx == "prior_vag") 1.0 else 0.0
    val priorVbac = if (hx == "prior_vbac") 1.0 else 0.0
    val arrest = if (state.toggle("arrest")) 1.0 else 0.0
    val htn = if (state.toggle("htn")) 1.0 else 0.0

    val warnings = buildList {
        if (age < 15.0 || age > 55.0) add("Maternal age (${fmt0(age)} yrs) is outside typical reproductive range.")
        if (cm < 120.0 || cm > 220.0) add("Height (${fmt0(cm)} cm) is outside typical range.")
    }

    val w = -5.952 -
        0.023 * age -
        0.024 * kg +
        0.056 * cm -
        0.597 * arrest +
        0.868 * priorVag +
        1.869 * priorVbac -
        0.966 * htn
    val prob = (1.0 / (1.0 + exp(-w))) * 100.0

    val (tierLabel, tier) = when {
        prob >= 70.0 -> "High Likelihood of VBAC (≥70%)" to ClinicalTier.GREEN
        prob >= 60.0 -> "Moderate Likelihood (60–69%)" to ClinicalTier.AMBER
        else -> "Lower Likelihood (<60% · Counsel)" to ClinicalTier.AMBER
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "${fmt1(prob)}%",
        primaryUnit = "VBAC Chance",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Model", "Grobman 2021 (Race-Free)")
        ),
        warnings = warnings
    )
}

// ============================================================================
// 10. PEDIATRICS PHASE 2
// ============================================================================
private fun calcPediatricGcs(state: CalculatorInputState): CalculationResult {
    val ageGroup = state.choice("age_group", "infant") ?: "infant"
    val e = state.choice("eye")?.toIntOrNull()
    val v = state.choice(if (ageGroup == "infant") "verbal_infant" else "verbal_child")?.toIntOrNull()
    val m = state.choice("motor")?.toIntOrNull()
    val count = listOfNotNull(e, v, m).size
    if (count < 3) return missingChoicePrompt(count, 3)

    val total = e!! + v!! + m!!
    val (tierLabel, tier, note) = when {
        total <= 8 -> Triple("Severe · Airway Protection (≤8)", ClinicalTier.RED, "Coma threshold: secure airway promptly and escalate to pediatric critical care.")
        total <= 12 -> Triple("Moderate (9–12)", ClinicalTier.AMBER, "Close neuro-observations and urgent pediatric imaging/review.")
        else -> Triple("Mild (13–15)", ClinicalTier.GREEN, "Reassuring consciousness score; continue serial neuro checks.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 15",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Breakdown", "E$e  V$v  M$m", highlight = true),
            SecondaryOutput("Scale", if (ageGroup == "infant") "Infant (<2y)" else "Child (≥2y)")
        ),
        clinicalNote = note
    )
}

private fun calcPeadsUrineOutput(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val vol = state.number("volume") ?: return CalculationResult()
    val hrs = state.number("hours") ?: return CalculationResult()
    if (rawW <= 0.0 || vol < 0.0 || hrs <= 0.0) {
        return CalculationResult(warnings = listOf("Weight and collection duration must be >0."))
    }

    val kg = toKg(rawW, state.unitIndex("weight"))
    val warnings = buildList {
        if (kg > 150.0) add("Weight (${fmt1(kg)} kg) exceeds 150 kg — verify units.")
    }

    val rate = vol / (kg * hrs)
    val (tierLabel, tier, note) = when {
        rate < 0.5 -> Triple("Severe Oliguria (<0.5 mL/kg/hr)", ClinicalTier.RED, "Assess perfusion, bladder retention, and AKI urgently; consider fluid challenge if hypovolemic.")
        rate <= 1.0 -> Triple("Borderline (0.5–1.0 mL/kg/hr)", ClinicalTier.AMBER, "Adequate for adolescents/older children, but low for infants & young children (target >1–2 mL/kg/hr).")
        else -> Triple("Adequate Output (>1.0 mL/kg/hr)", ClinicalTier.GREEN, "Reassuring renal perfusion (infants ideally ≥1.5–2.0 mL/kg/hr).")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(rate),
        primaryUnit = "mL/kg/hr",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcPews(state: CalculatorInputState): CalculationResult {
    val b = state.choice("behavior")?.toIntOrNull()
    val c = state.choice("cardio")?.toIntOrNull()
    val r = state.choice("resp")?.toIntOrNull()
    val count = listOfNotNull(b, c, r).size
    if (count < 3) return missingChoicePrompt(count, 3)

    var total = b!! + c!! + r!!
    if (state.toggle("nebs")) total += 2
    if (state.toggle("vomiting")) total += 2

    val (tierLabel, tier, note) = when {
        total <= 2 -> Triple("Low Risk (0–2)", ClinicalTier.GREEN, "Continue routine pediatric ward observations.")
        total <= 4 -> Triple("Moderate Risk (3–4 · Charge RN)", ClinicalTier.AMBER, "Increase observation frequency and notify Charge Nurse for bedside review.")
        else -> Triple("High Risk (≥5 · Urgent Doctor)", ClinicalTier.RED, "Urgent pediatric physician/rapid-response evaluation required immediately.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 13",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("B · C · R", "$b · $c · $r")
        ),
        clinicalNote = note
    )
}

private fun calcWestleyCroup(state: CalculatorInputState): CalculationResult {
    val keys = listOf("stridor", "retractions", "air_entry", "cyanosis", "loc")
    val vals = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (vals.size < keys.size) return missingChoicePrompt(vals.size, keys.size)

    val score = vals.sum()
    val (tierLabel, tier, note) = when {
        score <= 2 -> Triple("Mild Croup (≤2)", ClinicalTier.GREEN, "Single-dose oral dexamethasone (0.15–0.6 mg/kg); home care with return precautions.")
        score <= 7 -> Triple("Moderate Croup (3–7)", ClinicalTier.AMBER, "Oral/IM dexamethasone + consider nebulized epinephrine if stridor at rest; observe 3–4 hrs.")
        score <= 11 -> Triple("Severe Croup (8–11)", ClinicalTier.RED, "Immediate nebulized epinephrine + dexamethasone + humidified O₂; admit for close airway monitoring.")
        else -> Triple("Impending Resp. Failure (≥12)", ClinicalTier.RED, "EMERGENCY: Immediate airway/PICU escalation, continuous nebulized epi, and intubation readiness.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 17",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcWetflag(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    if (age <= 0.0) return CalculationResult(warnings = listOf("Age must be greater than 0."))

    val knownRawW = state.number("known_weight")
    val usedOverride = knownRawW != null && knownRawW > 0.0
    val kg = if (usedOverride) {
        toKg(knownRawW!!, state.unitIndex("known_weight"))
    } else {
        if (age <= 5.0) (age + 4.0) * 2.0 else (age * 3.0) + 7.0
    }

    val warnings = buildList {
        if (age < 1.0 || age > 12.0) add("APLS WETFLAG age formulas are validated for ages 1–12 years. Verify infant/adolescent dosing.")
        if (kg > 100.0) add("Weight (${fmt1(kg)} kg) exceeds 100 kg — verify adult dosing caps.")
    }

    val energyJ = min(4.0 * kg, 200.0)
    val ettUncuffed = (age / 4.0) + 4.0
    val ettCuffed = (age / 4.0) + 3.5
    val fluid10 = 10.0 * kg
    val fluid20 = 20.0 * kg
    val lorazepamMg = min(0.1 * kg, 4.0)
    val adrenalineMl = min(0.1 * kg, 10.0)
    val adrenalineMcg = min(10.0 * kg, 1000.0)
    val glucoseMl = 2.0 * kg

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(kg),
        primaryUnit = if (usedOverride) "kg (Known)" else "kg (Est.)",
        tierLabel = "WETFLAG · Age ${fmtClean(age)} yr APLS Sheet",
        tier = ClinicalTier.RED,
        secondaryOutputs = listOf(
            SecondaryOutput("W · Weight", "${fmt1(kg)} kg", highlight = true),
            SecondaryOutput("E · Defib (4 J/kg)", "${fmt0(energyJ)} J", highlight = true),
            SecondaryOutput("T · ETT (Cuff/Uncuff)", "${fmt1(ettCuffed)} / ${fmt1(ettUncuffed)} mm"),
            SecondaryOutput("F · Bolus (10 / 20)", "${fmt0(fluid10)} / ${fmt0(fluid20)} mL"),
            SecondaryOutput("L · Lorazepam", "${fmt2(lorazepamMg)} mg"),
            SecondaryOutput("A · Adren 1:10,000", "${fmt1(adrenalineMl)} mL (${fmt0(adrenalineMcg)} mcg)", highlight = true),
            SecondaryOutput("G · 10% Dextrose", "${fmt0(glucoseMl)} mL")
        ),
        warnings = warnings
    )
}

private data class Quad4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
private data class Quint<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
