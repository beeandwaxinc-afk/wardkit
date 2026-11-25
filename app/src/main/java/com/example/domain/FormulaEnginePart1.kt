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
import com.example.domain.FormulaEngine.toKg
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal fun evaluatePart1(
    calculatorId: String,
    state: CalculatorInputState,
    nowMillis: Long
): CalculationResult? = when (calculatorId) {
    "peads_holliday_segar" -> calcHollidaySegar(state)
    "peads_drug_dosing" -> calcPeadsDrugDosing(state)
    "peads_ett" -> calcPeadsEtt(state)
    "peads_apgar" -> calcApgar(state)
    "im_corrected_ca" -> calcCorrectedCalcium(state)
    "im_anion_gap" -> calcAnionGapWinters(state)
    "im_corrected_na" -> calcCorrectedSodium(state)
    "im_curb65" -> calcCurb65(state)
    "cardio_qtc" -> calcQtc(state)
    "cardio_chads2vasc" -> calcChads2Vasc(state)
    "cardio_heart" -> calcHeartScore(state)
    "cardio_wells_dvt" -> calcWellsDvt(state)
    "surg_parkland" -> calcParkland(state)
    "surg_alvarado" -> calcAlvarado(state)
    "surg_shock_index" -> calcShockIndex(state)
    "surg_iv_drip" -> calcIvDripRate(state)
    "obgyn_naegele" -> calcNaegele(state, nowMillis)
    "obgyn_bishop" -> calcBishopScore(state)
    "obgyn_shock_index" -> calcObstetricShockIndex(state)
    "obgyn_johnson" -> calcJohnsonEfw(state)
    else -> null
}

private fun calcHollidaySegar(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    if (rawW <= 0.0) return CalculationResult(warnings = listOf("Weight must be greater than 0."))
    val kg = toKg(rawW, state.unitIndex("weight"))
    val warnings = buildList {
        if (kg > 150.0) add("Weight (${fmt1(kg)} kg) exceeds typical pediatric range (>150 kg). Verify units.")
        if (kg < 0.5) add("Weight (${fmt2(kg)} kg) is extremely low (<0.5 kg). Verify neonatal protocol.")
    }

    val hourly = when {
        kg <= 10.0 -> kg * 4.0
        kg <= 20.0 -> 40.0 + (kg - 10.0) * 2.0
        else -> 60.0 + (kg - 20.0) * 1.0
    }
    val daily = when {
        kg <= 10.0 -> kg * 100.0
        kg <= 20.0 -> 1000.0 + (kg - 10.0) * 50.0
        else -> 1500.0 + (kg - 20.0) * 20.0
    }

    val secondary = buildList {
        add(SecondaryOutput("24-Hour Total", "${fmt0(daily)} mL/24h", highlight = true))
        if (state.unitIndex("weight") == 1) {
            add(SecondaryOutput("Converted Weight", "${fmt1(kg)} kg"))
        }
        if (daily > 2400.0) {
            add(SecondaryOutput("Adult Routine Cap", "~100 mL/hr (2,400 mL/d)"))
        }
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(hourly),
        primaryUnit = "mL/hr",
        tierLabel = if (daily > 2400.0) "Consider Adult Cap" else "4-2-1 Maintenance",
        tier = if (daily > 2400.0) ClinicalTier.AMBER else ClinicalTier.BLUE,
        secondaryOutputs = secondary,
        warnings = warnings
    )
}

private fun calcPeadsDrugDosing(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val dosePerKg = state.number("dose_per_kg") ?: return CalculationResult()
    val concMg = state.number("conc_mg") ?: return CalculationResult()
    val concMl = state.number("conc_ml") ?: return CalculationResult()
    if (rawW <= 0.0 || dosePerKg <= 0.0 || concMg <= 0.0 || concMl <= 0.0) {
        return CalculationResult(warnings = listOf("All entered values must be greater than 0."))
    }

    val kg = toKg(rawW, state.unitIndex("weight"))
    val freq = (state.choice("frequency", "2") ?: "2").toIntOrNull() ?: 2
    val warnings = buildList {
        if (kg > 150.0) add("Pediatric weight (${fmt1(kg)} kg) exceeds 150 kg — verify unit.")
        if (dosePerKg > 500.0) add("Daily dose (${fmt1(dosePerKg)} mg/kg/day) is unusually high. Double-check formulary.")
    }

    val dailyMg = kg * dosePerKg
    val doseMg = dailyMg / freq
    val doseMl = doseMg * (concMl / concMg)
    val freqLabel = when (freq) {
        1 -> "OD (1×/day)"
        2 -> "BD (2×/day)"
        3 -> "TDS (3×/day)"
        else -> "QID (4×/day)"
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(doseMl),
        primaryUnit = "mL / dose",
        tierLabel = freqLabel,
        tier = ClinicalTier.BLUE,
        secondaryOutputs = listOf(
            SecondaryOutput("Single Dose", "${fmt1(doseMg)} mg", highlight = true),
            SecondaryOutput("Total Daily Dose", "${fmt1(dailyMg)} mg/day"),
            SecondaryOutput("Concentration", "${fmtClean(concMg)} mg / ${fmtClean(concMl)} mL")
        ),
        warnings = warnings
    )
}

private fun calcPeadsEtt(state: CalculatorInputState): CalculationResult {
    val age = state.number("age_years") ?: return CalculationResult()
    if (age < 0.0) return CalculationResult(warnings = listOf("Age cannot be negative."))

    val warnings = buildList {
        if (age < 1.0) add("Age <1 year: age-based ETT formulas do not apply. Use neonatal sizing (3.0–3.5 mm uncuffed/cuffed).")
        if (age > 16.0) add("Age >16 years: use standard adult ETT sizes (7.0–8.0 mm).")
    }

    if (age < 1.0) {
        return CalculationResult(
            isReady = true,
            primaryValue = "3.0–3.5",
            primaryUnit = "mm ID",
            tierLabel = "Neonatal / Infant Protocol",
            tier = ClinicalTier.AMBER,
            secondaryOutputs = listOf(
                SecondaryOutput("Term Newborn", "3.5 mm (Depth 9–10 cm)"),
                SecondaryOutput("Preterm <2 kg", "2.5–3.0 mm")
            ),
            warnings = warnings
        )
    }

    val uncuffed = (age / 4.0) + 4.0
    val cuffed = (age / 4.0) + 3.5
    val depthFromCuffed = cuffed * 3.0
    val depthFromUncuffed = uncuffed * 3.0
    val depthAgeRule = (age / 2.0) + 12.0

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(cuffed),
        primaryUnit = "mm Cuffed",
        tierLabel = "Uncuffed: ${fmt1(uncuffed)} mm",
        tier = ClinicalTier.GREEN,
        secondaryOutputs = listOf(
            SecondaryOutput("Uncuffed ETT ID", "${fmt1(uncuffed)} mm", highlight = true),
            SecondaryOutput("Oral Depth (3× Cuffed)", "${fmt1(depthFromCuffed)} cm", highlight = true),
            SecondaryOutput("Oral Depth (3× Uncuffed)", "${fmt1(depthFromUncuffed)} cm"),
            SecondaryOutput("Depth (Age/2 + 12)", "${fmt1(depthAgeRule)} cm")
        ),
        warnings = warnings
    )
}

private fun calcApgar(state: CalculatorInputState): CalculationResult {
    val keys = listOf("appearance", "pulse", "grimace", "activity", "respiration")
    val values = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (values.size < keys.size) return missingChoicePrompt(values.size, keys.size)

    val score = values.sum()
    val (tierLabel, tier, note) = when {
        score >= 7 -> Triple("Reassuring", ClinicalTier.GREEN, "Routine newborn care and thermal support.")
        score >= 4 -> Triple("Moderately Abnormal", ClinicalTier.AMBER, "Stimulation, airway clearance, and supplemental O₂ as needed; reassess at 5 min.")
        else -> Triple("Immediate Resuscitation", ClinicalTier.RED, "Urgent NRP resuscitation (PPV, airway, compressions if HR <60).")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 10",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("A · P · G · A · R", values.joinToString(" · "))
        ),
        clinicalNote = note
    )
}

private fun calcCorrectedCalcium(state: CalculatorInputState): CalculationResult {
    val rawCa = state.number("calcium") ?: return CalculationResult()
    val rawAlb = state.number("albumin") ?: return CalculationResult()
    if (rawCa <= 0.0 || rawAlb <= 0.0) {
        return CalculationResult(warnings = listOf("Calcium and albumin must be positive values."))
    }

    val caMgDl = if (state.unitIndex("calcium") == 1) rawCa * 4.0 else rawCa
    val albGDl = if (state.unitIndex("albumin") == 1) rawAlb / 10.0 else rawAlb

    val warnings = buildList {
        if (caMgDl < 3.0 || caMgDl > 20.0) add("Serum Ca (${fmt1(caMgDl)} mg/dL) is outside typical physiological range (3–20 mg/dL).")
        if (albGDl < 0.5 || albGDl > 7.0) add("Albumin (${fmt1(albGDl)} g/dL) is outside typical range (0.5–7.0 g/dL).")
    }

    val correctedMgDl = caMgDl + 0.8 * (4.0 - albGDl)
    val correctedMmol = correctedMgDl / 4.0

    val (tierLabel, tier) = when {
        correctedMgDl < 7.0 -> "Severe Hypocalcemia" to ClinicalTier.RED
        correctedMgDl < 8.5 -> "Hypocalcemia" to ClinicalTier.AMBER
        correctedMgDl <= 10.5 -> "Normal (8.5–10.5)" to ClinicalTier.GREEN
        correctedMgDl <= 12.0 -> "Mild–Mod Hypercalcemia" to ClinicalTier.AMBER
        else -> "Severe Hypercalcemia" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(correctedMgDl),
        primaryUnit = "mg/dL",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("SI Equivalent", "${fmt2(correctedMmol)} mmol/L", highlight = true),
            SecondaryOutput("Albumin Used", "${fmt1(albGDl)} g/dL")
        ),
        warnings = warnings
    )
}

private fun calcAnionGapWinters(state: CalculatorInputState): CalculationResult {
    val na = state.number("na") ?: return CalculationResult()
    val cl = state.number("cl") ?: return CalculationResult()
    val hco3 = state.number("hco3") ?: return CalculationResult()
    val measuredPaco2 = state.number("paco2")

    val warnings = buildList {
        if (na < 90.0 || na > 200.0) add("Sodium (${fmt0(na)} mEq/L) is outside physiological bounds (90–200).")
        if (cl < 50.0 || cl > 150.0) add("Chloride (${fmt0(cl)} mEq/L) is outside typical range (50–150).")
        if (hco3 < 2.0 || hco3 > 60.0) add("HCO₃ (${fmt1(hco3)} mEq/L) is outside typical range (2–60).")
        if (measuredPaco2 != null && (measuredPaco2 < 8.0 || measuredPaco2 > 140.0)) {
            add("PaCO₂ (${fmt1(measuredPaco2)} mmHg) is outside typical range (8–140).")
        }
    }

    val ag = na - (cl + hco3)
    val expectedPaco2 = (1.5 * hco3) + 8.0
    val lowPaco2 = expectedPaco2 - 2.0
    val highPaco2 = expectedPaco2 + 2.0

    val (tierLabel, tier) = when {
        ag > 16.0 -> "Marked High Anion Gap" to ClinicalTier.RED
        ag > 12.0 -> "High Anion Gap (>12)" to ClinicalTier.AMBER
        ag < 4.0 -> "Low Anion Gap (<4)" to ClinicalTier.BLUE
        else -> "Normal Anion Gap (≤12)" to ClinicalTier.GREEN
    }

    val secondary = buildList {
        add(SecondaryOutput("Winter's PaCO₂", "${fmt1(expectedPaco2)} mmHg (${fmt1(lowPaco2)}–${fmt1(highPaco2)})", highlight = true))
        if (measuredPaco2 != null) {
            val comp = when {
                measuredPaco2 < lowPaco2 -> "Mixed: Concomitant Resp. Alkalosis"
                measuredPaco2 > highPaco2 -> "Mixed: Concomitant Resp. Acidosis"
                else -> "Appropriate Respiratory Compensation"
            }
            add(SecondaryOutput("PaCO₂ Check (${fmt1(measuredPaco2)})", comp, highlight = true))
        }
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(ag),
        primaryUnit = "mEq/L",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = secondary,
        warnings = warnings
    )
}

private fun calcCorrectedSodium(state: CalculatorInputState): CalculationResult {
    val na = state.number("na") ?: return CalculationResult()
    val rawGlu = state.number("glucose") ?: return CalculationResult()
    if (rawGlu <= 0.0) return CalculationResult(warnings = listOf("Glucose must be greater than 0."))

    val gluMgDl = if (state.unitIndex("glucose") == 1) rawGlu * 18.0182 else rawGlu
    val warnings = buildList {
        if (na < 90.0 || na > 200.0) add("Measured Na (${fmt0(na)} mEq/L) is outside physiological range (90–200).")
        if (gluMgDl < 20.0 || gluMgDl > 2000.0) add("Glucose (${fmt0(gluMgDl)} mg/dL) is extreme. Verify unit.")
    }

    val katz = na + 0.016 * (gluMgDl - 100.0)
    val hillier = na + 0.024 * (gluMgDl - 100.0)

    val (tierLabel, tier) = when {
        katz < 125.0 -> "Severe Hyponatremia" to ClinicalTier.RED
        katz < 135.0 -> "True Hyponatremia" to ClinicalTier.AMBER
        katz <= 145.0 -> "Normal Corrected Na" to ClinicalTier.GREEN
        katz <= 155.0 -> "Hypernatremia" to ClinicalTier.AMBER
        else -> "Severe Hypernatremia" to ClinicalTier.RED
    }

    val secondary = buildList {
        if (gluMgDl > 400.0) {
            add(SecondaryOutput("Hillier (0.024 Factor)", "${fmt1(hillier)} mEq/L", highlight = true))
        }
        add(SecondaryOutput("Glucose Used", "${fmt0(gluMgDl)} mg/dL"))
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(katz),
        primaryUnit = "mEq/L",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = secondary,
        clinicalNote = if (gluMgDl > 400.0) "Glucose >400 mg/dL: Hillier's 0.024 correction (${fmt1(hillier)} mEq/L) is often more accurate." else null,
        warnings = warnings
    )
}

private fun calcCurb65(state: CalculatorInputState): CalculationResult {
    val keys = listOf("confusion", "urea", "rr", "bp", "age")
    val score = keys.count { state.toggle(it) }
    val (tierLabel, tier, mort, disp) = when {
        score <= 1 -> Quad("Outpatient Treatment", ClinicalTier.GREEN, "0.6–2.7%", "Low severity; oral antibiotics & outpatient follow-up.")
        score == 2 -> Quad("Inpatient Admission", ClinicalTier.AMBER, "6.8%", "Consider short inpatient stay or closely supervised hospital-at-home.")
        else -> Quad("ICU Evaluation", ClinicalTier.RED, "14–27.8%", "Severe CAP; admit urgently and assess for ICU/HDU level care.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 5",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("30-Day Mortality", mort, highlight = score >= 3)
        ),
        clinicalNote = disp
    )
}

private fun calcQtc(state: CalculatorInputState): CalculationResult {
    val qt = state.number("qt") ?: return CalculationResult()
    val hr = state.number("hr") ?: return CalculationResult()
    if (qt <= 0.0 || hr <= 0.0) return CalculationResult(warnings = listOf("QT and HR must be greater than 0."))

    val warnings = buildList {
        if (hr < 20.0 || hr > 300.0) add("Heart rate (${fmt0(hr)} bpm) is outside physiological range (20–300).")
        if (qt < 150.0 || qt > 900.0) add("Measured QT (${fmt0(qt)} ms) is outside typical ECG range (150–900 ms).")
    }

    val rr = 60.0 / hr
    val bazett = qt / sqrt(rr)
    val fridericia = qt / cbrt(rr)

    val (tierLabel, tier) = when {
        bazett > 500.0 -> "Critical (>500 ms · Torsades Risk)" to ClinicalTier.RED
        bazett > 450.0 -> "Prolonged (>450 ms)" to ClinicalTier.AMBER
        bazett < 340.0 -> "Short QTc (<340 ms)" to ClinicalTier.AMBER
        else -> "Normal QTc (≤450 ms)" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(bazett),
        primaryUnit = "ms (Bazett)",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Fridericia QTc", "${fmt0(fridericia)} ms", highlight = true),
            SecondaryOutput("RR Interval", "${fmt2(rr)} sec")
        ),
        clinicalNote = if (hr > 100.0 || hr < 60.0) "HR is outside 60–100 bpm: prefer Fridericia (${fmt0(fridericia)} ms) over Bazett." else null,
        warnings = warnings
    )
}

private fun calcChads2Vasc(state: CalculatorInputState): CalculationResult {
    var score = 0
    if (state.toggle("chf")) score += 1
    if (state.toggle("htn")) score += 1
    if (state.toggle("age75")) score += 2
    if (state.toggle("dm")) score += 1
    if (state.toggle("stroke")) score += 2
    if (state.toggle("vasc")) score += 1
    if (state.toggle("age65")) score += 1
    val isFemale = state.toggle("female")
    if (isFemale) score += 1

    val annualStrokeRisk = when (score) {
        0 -> "0.2%"
        1 -> "0.6%"
        2 -> "2.2%"
        3 -> "3.2%"
        4 -> "4.8%"
        5 -> "7.2%"
        6 -> "9.7%"
        7 -> "11.2%"
        8 -> "10.8%"
        else -> "12.2%"
    }

    val nonSexScore = if (isFemale) score - 1 else score
    val (tierLabel, tier, rec) = when {
        nonSexScore == 0 -> Triple("Low Risk · No OAC", ClinicalTier.GREEN, "Anticoagulation not recommended (female sex alone does not trigger OAC).")
        nonSexScore == 1 -> Triple("Moderate · Consider OAC", ClinicalTier.AMBER, "Consider oral anticoagulation based on bleeding risk (HAS-BLED) & patient values.")
        else -> Triple("High Risk · OAC Recommended", ClinicalTier.RED, "Oral anticoagulation (DOAC preferred over warfarin in non-valvular AF) is recommended.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 9",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Est. Annual Stroke", annualStrokeRisk, highlight = nonSexScore >= 2)
        ),
        clinicalNote = rec
    )
}

private fun calcHeartScore(state: CalculatorInputState): CalculationResult {
    val keys = listOf("history", "ecg", "age", "risk", "troponin")
    val values = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (values.size < keys.size) return missingChoicePrompt(values.size, keys.size)

    val score = values.sum()
    val (tierLabel, tier, mace, action) = when {
        score <= 3 -> Quad("Low MACE Risk", ClinicalTier.GREEN, "0.9–1.7%", "Candidate for early discharge with serial negative troponins & outpatient follow-up.")
        score <= 6 -> Quad("Moderate MACE Risk", ClinicalTier.AMBER, "12–16.6%", "Admit for observation, serial troponins, and noninvasive ischemia testing.")
        else -> Quad("High MACE Risk", ClinicalTier.RED, "50–65%", "Urgent cardiology consult; initiate ACS medical therapy & consider early invasive strategy.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 10",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("6-Week MACE Risk", mace, highlight = score >= 4)
        ),
        clinicalNote = action
    )
}

private fun calcWellsDvt(state: CalculatorInputState): CalculationResult {
    val plusOneKeys = listOf("cancer", "paralysis", "bedridden", "tenderness", "entire_leg", "calf", "pitting", "collateral", "prev_dvt")
    var score = plusOneKeys.count { state.toggle(it) }
    if (state.toggle("alt_dx")) score -= 2

    val twoTier = if (score <= 1) "DVT Unlikely (≤1)" else "DVT Likely (≥2)"
    val (threeTier, tier) = when {
        score <= 0 -> "Low Probability (≤0 · ~5%)" to ClinicalTier.GREEN
        score <= 2 -> "Moderate Probability (1–2 · ~17%)" to ClinicalTier.AMBER
        else -> "High Probability (≥3 · ~53%)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "pts",
        tierLabel = twoTier,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Three-Tier Risk", threeTier, highlight = score >= 2)
        ),
        clinicalNote = if (score <= 1) "Obtain high-sensitivity D-dimer; negative result safely excludes DVT." else "Proceed directly to proximal compression venous ultrasound."
    )
}

private fun calcParkland(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val tbsa = state.number("tbsa") ?: return CalculationResult()
    if (rawW <= 0.0 || tbsa <= 0.0) return CalculationResult(warnings = listOf("Weight and %TBSA must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val warnings = buildList {
        if (tbsa > 100.0) add("%TBSA (${fmt1(tbsa)}%) cannot exceed 100%.")
        if (kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — verify units.")
    }

    val total24h = 4.0 * kg * tbsa
    val first8hVol = total24h / 2.0
    val first8hRate = first8hVol / 8.0
    val next16hVol = total24h / 2.0
    val next16hRate = next16hVol / 16.0

    val tier = when {
        tbsa >= 40.0 -> ClinicalTier.RED
        tbsa >= 20.0 -> ClinicalTier.AMBER
        else -> ClinicalTier.BLUE
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(total24h),
        primaryUnit = "mL / 24h",
        tierLabel = "Lactated Ringer's (4 mL/kg/%)",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("First 8 hrs (50%)", "${fmt0(first8hVol)} mL (${fmt0(first8hRate)} mL/hr)", highlight = true),
            SecondaryOutput("Next 16 hrs (50%)", "${fmt0(next16hVol)} mL (${fmt0(next16hRate)} mL/hr)", highlight = true)
        ),
        clinicalNote = "First 8 hrs starts from TIME OF BURN. Titrate to urine output 0.5–1.0 mL/kg/hr.",
        warnings = warnings
    )
}

private fun calcAlvarado(state: CalculatorInputState): CalculationResult {
    var score = 0
    if (state.toggle("migration")) score += 1
    if (state.toggle("anorexia")) score += 1
    if (state.toggle("nausea")) score += 1
    if (state.toggle("rlq")) score += 2
    if (state.toggle("rebound")) score += 1
    if (state.toggle("temp")) score += 1
    if (state.toggle("wbc")) score += 2
    if (state.toggle("shift")) score += 1

    val (tierLabel, tier, note) = when {
        score <= 4 -> Triple("Appendicitis Unlikely (0–4)", ClinicalTier.GREEN, "Low probability; consider alternative diagnoses & discharge with strict return precautions.")
        score <= 6 -> Triple("Observe / Imaging (5–6)", ClinicalTier.AMBER, "Compatible with appendicitis; admit for serial exams and ultrasound or CT.")
        score <= 8 -> Triple("Probable Appendicitis (7–8)", ClinicalTier.RED, "High likelihood; surgical consultation and imaging confirmation.")
        else -> Triple("Very Probable · Surgery (9–10)", ClinicalTier.RED, "Urgent surgical review for operative appendectomy.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 10",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcShockIndex(state: CalculatorInputState): CalculationResult {
    val hr = state.number("hr") ?: return CalculationResult()
    val sbp = state.number("sbp") ?: return CalculationResult()
    if (hr <= 0.0 || sbp <= 0.0) return CalculationResult(warnings = listOf("HR and SBP must be greater than 0."))

    val warnings = buildList {
        if (hr > 300.0 || hr < 20.0) add("HR (${fmt0(hr)} bpm) is outside physiological bounds (20–300).")
        if (sbp > 300.0 || sbp < 30.0) add("SBP (${fmt0(sbp)} mmHg) is outside typical range (30–300).")
    }

    val si = hr / sbp
    val (tierLabel, tier, note) = when {
        si >= 1.0 -> Triple("Hemodynamically Unstable (≥1.0)", ClinicalTier.RED, "High risk of shock / occult hemorrhage; activate trauma resuscitation & blood products.")
        si >= 0.8 -> Triple("Elevated (0.8–0.99)", ClinicalTier.AMBER, "Early hemodynamic stress; repeat vitals, assess volume status & bleeding source.")
        si >= 0.5 -> Triple("Normal (0.5–0.79)", ClinicalTier.GREEN, "Within normal hemodynamic range (ideal 0.5–0.7).")
        else -> Triple("Low (<0.5)", ClinicalTier.BLUE, "Low ratio (bradycardia, athletic baseline, beta-blockers, or hypertension).")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(si),
        primaryUnit = "HR/SBP",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcIvDripRate(state: CalculatorInputState): CalculationResult {
    val volume = state.number("volume") ?: return CalculationResult()
    val duration = state.number("duration") ?: return CalculationResult()
    if (volume <= 0.0 || duration <= 0.0) {
        return CalculationResult(warnings = listOf("Volume and duration must be greater than 0."))
    }

    val isMinutes = state.unitIndex("duration") == 1
    val durationMinutes = if (isMinutes) duration else duration * 60.0
    val durationHours = durationMinutes / 60.0
    val dropFactor = (state.choice("drop_factor", "20") ?: "20").toDoubleOrNull() ?: 20.0

    val gttPerMin = (volume * dropFactor) / durationMinutes
    val mlPerHr = volume / durationHours

    val warnings = buildList {
        if (mlPerHr > 2000.0) add("Infusion rate (${fmt0(mlPerHr)} mL/hr) is very rapid. Confirm order & catheter gauge.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(gttPerMin),
        primaryUnit = "gtt/min",
        tierLabel = "Drop Factor: ${fmt0(dropFactor)} gtt/mL",
        tier = ClinicalTier.BLUE,
        secondaryOutputs = listOf(
            SecondaryOutput("Pump Rate", "${fmt1(mlPerHr)} mL/hr", highlight = true),
            SecondaryOutput("Exact Drip", "${fmt1(gttPerMin)} drops/min")
        ),
        warnings = warnings
    )
}

private fun calcNaegele(state: CalculatorInputState, nowMillis: Long): CalculationResult {
    val lmpMillis = state.dateMillis("lmp") ?: return CalculationResult(
        isReady = false,
        emptyPrompt = "Select LMP date below"
    )
    val rawCycle = state.number("cycle")
    val cycleDays = rawCycle?.roundToInt() ?: 28
    val warnings = buildList {
        if (rawCycle != null && (cycleDays < 18 || cycleDays > 50)) {
            add("Cycle length ($cycleDays days) is outside typical range (18–50 days).")
        }
    }

    val dayMs = 86_400_000L
    val cycleOffset = cycleDays - 28
    val eddMillis = lmpMillis + (280L + cycleOffset) * dayMs

    val utcTz = TimeZone.getTimeZone("UTC")
    val localCal = Calendar.getInstance().apply { timeInMillis = nowMillis }
    val todayUtcMidnight = Calendar.getInstance(utcTz).apply {
        clear()
        set(localCal.get(Calendar.YEAR), localCal.get(Calendar.MONTH), localCal.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis

    val lmpCal = Calendar.getInstance(utcTz).apply { timeInMillis = lmpMillis }
    val lmpUtcMidnight = Calendar.getInstance(utcTz).apply {
        clear()
        set(lmpCal.get(Calendar.YEAR), lmpCal.get(Calendar.MONTH), lmpCal.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis

    val gaDaysTotal = ((todayUtcMidnight - lmpUtcMidnight) / dayMs) - cycleOffset
    val eddFormatted = SimpleDateFormat("dd MMM yyyy", Locale.US).apply { timeZone = utcTz }
        .format(Date(eddMillis))

    val (gaText, trimesterText, tier) = if (gaDaysTotal < 0) {
        Triple("LMP is in future", "Check LMP Date", ClinicalTier.AMBER)
    } else {
        val wks = gaDaysTotal / 7
        val days = gaDaysTotal % 7
        val gaStr = "$wks Wks, $days Days"
        when {
            wks < 14 -> Triple(gaStr, "1st Trimester", ClinicalTier.BLUE)
            wks < 28 -> Triple(gaStr, "2nd Trimester", ClinicalTier.GREEN)
            wks < 42 -> Triple(gaStr, "3rd Trimester", ClinicalTier.GREEN)
            else -> Triple(gaStr, "Post-Term (≥42 Wks)", ClinicalTier.AMBER)
        }
    }

    val daysToEdd = ((eddMillis - todayUtcMidnight) / dayMs)
    val countdown = when {
        daysToEdd > 0 -> "$daysToEdd days remaining"
        daysToEdd == 0L -> "Due today"
        else -> "${-daysToEdd} days past EDD"
    }

    return CalculationResult(
        isReady = true,
        primaryValue = eddFormatted,
        primaryUnit = "EDD",
        tierLabel = trimesterText,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Gestational Age", gaText, highlight = true),
            SecondaryOutput("Timeline", "$countdown · ${cycleDays}d cycle")
        ),
        warnings = warnings
    )
}

private fun calcBishopScore(state: CalculatorInputState): CalculationResult {
    val keys = listOf("dilation", "effacement", "station", "consistency", "position")
    val values = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (values.size < keys.size) return missingChoicePrompt(values.size, keys.size)

    val score = values.sum()
    val (tierLabel, tier, note) = when {
        score <= 5 -> Triple("Unfavorable Cervix (≤5)", ClinicalTier.RED, "Induction less likely to succeed without cervical ripening agent first.")
        score <= 7 -> Triple("Intermediate (6–7)", ClinicalTier.AMBER, "Moderate likelihood of successful induction; individualize ripening vs oxytocin.")
        else -> Triple("Favorable Cervix (≥8)", ClinicalTier.GREEN, "High likelihood of successful vaginal delivery with induction (similar to spontaneous labor).")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 13",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcObstetricShockIndex(state: CalculatorInputState): CalculationResult {
    val hr = state.number("hr") ?: return CalculationResult()
    val sbp = state.number("sbp") ?: return CalculationResult()
    if (hr <= 0.0 || sbp <= 0.0) return CalculationResult(warnings = listOf("Maternal HR and SBP must be positive."))

    val warnings = buildList {
        if (hr > 260.0 || hr < 30.0) add("Maternal HR (${fmt0(hr)} bpm) is outside typical range (30–260).")
        if (sbp > 260.0 || sbp < 30.0) add("Maternal SBP (${fmt0(sbp)} mmHg) is outside typical range (30–260).")
    }

    val osi = hr / sbp
    val (tierLabel, tier, note) = when {
        osi >= 1.1 -> Triple("Severe PPH · Activate MTP (≥1.1)", ClinicalTier.RED, "High probability of massive hemorrhage; activate Massive Transfusion Protocol & senior obstetric team.")
        osi >= 0.9 -> Triple("Alert · PPH Escalation (0.9–1.09)", ClinicalTier.AMBER, "Abnormal maternal hemodynamic stress; establish 2 large-bore IVs, type & crossmatch, inspect uterus.")
        else -> Triple("Normal Obstetric Range (<0.9)", ClinicalTier.GREEN, "Reassuring maternal shock index; continue routine postpartum monitoring.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(osi),
        primaryUnit = "HR/SBP",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcJohnsonEfw(state: CalculatorInputState): CalculationResult {
    val sfh = state.number("sfh") ?: return CalculationResult()
    if (sfh <= 0.0) return CalculationResult(warnings = listOf("SFH must be greater than 0."))

    val n = (state.choice("station_n", "12") ?: "12").toDoubleOrNull() ?: 12.0
    val gt91 = state.toggle("maternal_gt_91")
    val adjustedSfh = if (gt91) sfh - 1.0 else sfh

    val warnings = buildList {
        if (sfh < 15.0 || sfh > 50.0) add("SFH (${fmt1(sfh)} cm) is outside typical third-trimester range (15–50 cm).")
    }

    val efwGrams = max(0.0, (adjustedSfh - n) * 155.0)
    val efwKg = efwGrams / 1000.0

    val (tierLabel, tier) = when {
        efwGrams < 2500.0 -> "Low Birth Weight (<2,500 g)" to ClinicalTier.AMBER
        efwGrams > 4000.0 -> "Suspected Macrosomia (>4,000 g)" to ClinicalTier.AMBER
        else -> "Appropriate Term Range" to ClinicalTier.GREEN
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(efwGrams),
        primaryUnit = "g",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("In Kilograms", "${fmt2(efwKg)} kg", highlight = true),
            SecondaryOutput("Formula Terms", "SFH ${fmt1(adjustedSfh)} − ${fmt0(n)}")
        ),
        warnings = warnings
    )
}

private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
