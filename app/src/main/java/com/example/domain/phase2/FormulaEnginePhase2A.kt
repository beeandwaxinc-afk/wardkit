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
import com.example.domain.FormulaEngine.toKg
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

internal fun evaluatePhase2A(
    calculatorId: String,
    state: CalculatorInputState
): CalculationResult? = when (calculatorId) {
    // Cardiology (+4)
    "cardio_framingham" -> calcFramingham(state)
    "cardio_hasbled" -> calcHasBled(state)
    "cardio_timi" -> calcTimi(state)
    "cardio_wells_pe" -> calcWellsPe(state)
    // Emergency & Anesthesia (+4)
    "em_caprini" -> calcCaprini(state)
    "em_mme" -> calcMme(state)
    "em_nexus" -> calcNexus(state)
    "em_paracetamol" -> calcParacetamol(state)
    // Gastro & Hepatology (+4)
    "gastro_fib4" -> calcFib4(state)
    "gastro_gbs" -> calcGlasgowBlatchford(state)
    "gastro_lille" -> calcLille(state)
    "gastro_rockall" -> calcRockall(state)
    // General Surgery (+4)
    "surg_fluid_deficit" -> calcFluidDeficit(state)
    "surg_glasgow_imrie" -> calcGlasgowImrie(state)
    "surg_ranson" -> calcRanson(state)
    "surg_rts" -> calcRts(state)
    // ICU & Critical Care (+4)
    "icu_aa_gradient" -> calcAaGradient(state)
    "icu_sofa" -> calcFullSofa(state)
    "icu_rox" -> calcRoxIndex(state)
    "icu_vasopressor" -> calcVasopressor(state)
    else -> null
}

// ============================================================================
// 1. CARDIOLOGY PHASE 2
// ============================================================================
private fun calcFramingham(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val rawTc = state.number("tc") ?: return CalculationResult()
    val rawHdl = state.number("hdl") ?: return CalculationResult()
    val sbp = state.number("sbp") ?: return CalculationResult()
    if (age <= 0.0 || rawTc <= 0.0 || rawHdl <= 0.0 || sbp <= 0.0) {
        return CalculationResult(warnings = listOf("Age, cholesterol, HDL, and SBP must be positive."))
    }

    val isFemale = state.choice("sex", "male") == "female"
    val tcMg = if (state.unitIndex("tc") == 1) rawTc / 0.02586 else rawTc
    val hdlMg = if (state.unitIndex("hdl") == 1) rawHdl / 0.02586 else rawHdl
    val treated = state.toggle("htn_tx")
    val smoker = state.toggle("smoker")
    val dm = state.toggle("diabetes")

    val warnings = buildList {
        if (age < 30.0 || age > 74.0) add("Framingham 2008 is validated for ages 30–74 years.")
        if (tcMg < 100.0 || tcMg > 450.0) add("Total cholesterol (${fmt0(tcMg)} mg/dL) is outside typical range.")
        if (hdlMg < 15.0 || hdlMg > 120.0) add("HDL (${fmt0(hdlMg)} mg/dL) is outside typical range.")
        if (sbp < 80.0 || sbp > 240.0) add("SBP (${fmt0(sbp)} mmHg) is outside typical range.")
    }

    val riskPct = if (!isFemale) {
        val sum = 3.06117 * ln(age) +
            1.12370 * ln(tcMg) -
            0.93263 * ln(hdlMg) +
            (if (treated) 1.99881 else 1.93303) * ln(sbp) +
            (if (smoker) 0.65451 else 0.0) +
            (if (dm) 0.57367 else 0.0)
        ((1.0 - 0.88936.pow(exp(sum - 23.9802))) * 100.0).coerceIn(0.0, 99.9)
    } else {
        val sum = 2.32888 * ln(age) +
            1.20904 * ln(tcMg) -
            0.70833 * ln(hdlMg) +
            (if (treated) 2.82263 else 2.76157) * ln(sbp) +
            (if (smoker) 0.52873 else 0.0) +
            (if (dm) 0.69154 else 0.0)
        ((1.0 - 0.95012.pow(exp(sum - 26.1931))) * 100.0).coerceIn(0.0, 99.9)
    }

    val (tierLabel, tier) = when {
        riskPct < 10.0 -> "Low 10-Yr Risk (<10%)" to ClinicalTier.GREEN
        riskPct <= 20.0 -> "Intermediate Risk (10–20%)" to ClinicalTier.AMBER
        else -> "High 10-Yr CVD Risk (>20%)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "${fmt1(riskPct)}%",
        primaryUnit = "10-Yr CVD",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("TC / HDL Used", "${fmt0(tcMg)} / ${fmt0(hdlMg)} mg/dL")
        ),
        warnings = warnings
    )
}

private fun calcHasBled(state: CalculatorInputState): CalculationResult {
    val keys = listOf("htn", "renal", "liver", "stroke", "bleeding", "inr", "elderly", "drugs", "alcohol")
    val score = keys.count { state.toggle(it) }
    val (tierLabel, tier, note) = when {
        score <= 1 -> Triple("Low Bleeding Risk (0–1)", ClinicalTier.GREEN, "Annual major bleeding risk ~1.0–1.1%; proceed with indicated OAC.")
        score == 2 -> Triple("Moderate Bleeding Risk (2)", ClinicalTier.AMBER, "Annual major bleeding risk ~1.9%; monitor and optimize BP/medications.")
        else -> Triple("High Bleeding Risk (≥3)", ClinicalTier.RED, "Caution & address modifiable factors (BP, NSAIDs, alcohol, INR)—not an absolute contraindication to OAC.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 9",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcTimi(state: CalculatorInputState): CalculationResult {
    val keys = listOf("age65", "cad_rf", "known_cad", "aspirin", "angina", "st_dev", "markers")
    val score = keys.count { state.toggle(it) }
    val mace14d = when (score) {
        0, 1 -> "4.7%"
        2 -> "8.3%"
        3 -> "13.2%"
        4 -> "19.9%"
        5 -> "26.2%"
        else -> "40.9%"
    }
    val (tierLabel, tier) = when {
        score <= 2 -> "Low Risk (0–2)" to ClinicalTier.GREEN
        score <= 4 -> "Intermediate Risk (3–4)" to ClinicalTier.AMBER
        else -> "High Risk (5–7 · Early Invasive)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 7",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("14-Day MACE Risk", mace14d, highlight = score >= 3)
        )
    )
}

private fun calcWellsPe(state: CalculatorInputState): CalculationResult {
    var score = 0.0
    if (state.toggle("dvt_signs")) score += 3.0
    if (state.toggle("pe_likely")) score += 3.0
    if (state.toggle("hr100")) score += 1.5
    if (state.toggle("immob_surg")) score += 1.5
    if (state.toggle("prev_vte")) score += 1.5
    if (state.toggle("hemoptysis")) score += 1.0
    if (state.toggle("malignancy")) score += 1.0

    val twoTier = if (score <= 4.0) "PE Unlikely (≤4)" else "PE Likely (>4)"
    val (threeTier, tier) = when {
        score < 2.0 -> "Low (<2 · ~1.3%)" to ClinicalTier.GREEN
        score <= 6.0 -> "Moderate (2–6 · ~16.2%)" to ClinicalTier.AMBER
        else -> "High (>6 · ~37.5%)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmtClean(score),
        primaryUnit = "pts",
        tierLabel = twoTier,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Three-Tier Risk", threeTier, highlight = score > 4.0)
        ),
        clinicalNote = if (score <= 4.0) "Consider PERC rule or high-sensitivity D-dimer to exclude PE." else "Proceed directly to CTPA (or V/Q scan if CTPA contraindicated)."
    )
}

// ============================================================================
// 2. EMERGENCY & ANESTHESIA PHASE 2
// ============================================================================
private fun calcCaprini(state: CalculatorInputState): CalculationResult {
    val pts1 = listOf("age_41_60", "minor_surg", "bmi_25", "swollen_legs", "varicose", "preg", "ocp_hrt", "sepsis", "lung", "mi_chf", "med_bed", "ibd")
    val pts2 = listOf("age_61_74", "major_surg", "malignancy", "bed_72", "cast", "cvc")
    val pts3 = listOf("age_75", "hx_vte", "fhx_vte", "thrombophilia")
    val pts5 = listOf("stroke_1m", "arthroplasty", "fracture", "sci")

    val score = pts1.count { state.toggle(it) } * 1 +
        pts2.count { state.toggle(it) } * 2 +
        pts3.count { state.toggle(it) } * 3 +
        pts5.count { state.toggle(it) } * 5

    val (tierLabel, tier, note) = when {
        score == 0 -> Triple("Lowest Risk (0)", ClinicalTier.GREEN, "Early ambulation; no specific pharmacologic or mechanical prophylaxis required.")
        score <= 2 -> Triple("Low Risk (1–2)", ClinicalTier.GREEN, "Mechanical prophylaxis (intermittent pneumatic compression / graduated stockings).")
        score <= 4 -> Triple("Moderate Risk (3–4)", ClinicalTier.AMBER, "Pharmacologic (LMWH/UFH) OR mechanical prophylaxis depending on bleeding risk.")
        else -> Triple("High Risk (≥5)", ClinicalTier.RED, "Pharmacologic (LMWH/UFH) PLUS mechanical prophylaxis; consider extended duration in cancer/ortho.")
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

private fun calcMme(state: CalculatorInputState): CalculationResult {
    val dose = state.number("daily_dose") ?: return CalculationResult()
    if (dose <= 0.0) return CalculationResult(warnings = listOf("24-hour dose must be greater than 0."))

    val opioid = state.choice("opioid", "morphine") ?: "morphine"
    val (factor, name) = when (opioid) {
        "morphine" -> 1.0 to "Morphine"
        "codeine" -> 0.15 to "Codeine"
        "tramadol" -> 0.2 to "Tramadol"
        "hydrocodone" -> 1.0 to "Hydrocodone"
        "oxycodone" -> 1.5 to "Oxycodone"
        "hydromorphone" -> 5.0 to "Hydromorphone"
        "tapentadol" -> 0.4 to "Tapentadol"
        "fentanyl_patch" -> 2.4 to "Fentanyl Patch"
        else -> 1.0 to "Morphine"
    }

    val warnings = buildList {
        if (opioid == "fentanyl_patch" && state.unitIndex("daily_dose") == 0) {
            add("Fentanyl patch uses mcg/hr (e.g., 25 mcg/hr → 60 MME/day). Verify patch strength in mcg/hr.")
        }
        if (opioid != "fentanyl_patch" && state.unitIndex("daily_dose") == 1) {
            add("$name is typically entered as total mg/day rather than mcg/hr.")
        }
    }

    val mme = dose * factor
    val (tierLabel, tier, note) = when {
        mme < 50.0 -> Triple("Standard (<50 MME/day)", ClinicalTier.GREEN, "Monitor pain control & sedation; avoid concurrent benzodiazepines.")
        mme < 90.0 -> Triple("Caution (50–89 · Consider Naloxone)", ClinicalTier.AMBER, "Elevated overdose risk; reassess benefit-to-risk and co-prescribe take-home naloxone.")
        else -> Triple("High Overdose Risk (≥90 MME/day)", ClinicalTier.RED, "Significantly increased fatal overdose risk; co-prescribe naloxone & justify/taper if feasible.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(mme),
        primaryUnit = "MME/day",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Opioid", name),
            SecondaryOutput("CDC Factor", "× $factor")
        ),
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcNexus(state: CalculatorInputState): CalculationResult {
    val keys = listOf("midline", "neuro", "alertness", "intox", "distracting")
    val positive = keys.count { state.toggle(it) }

    return if (positive == 0) {
        CalculationResult(
            isReady = true,
            primaryValue = "Low Risk",
            primaryUnit = "0 / 5",
            tierLabel = "C-Spine Imaging Not Indicated by NEXUS",
            tier = ClinicalTier.GREEN,
            clinicalNote = "All 5 NEXUS low-risk criteria satisfied; clinical clearance possible if reliable exam."
        )
    } else {
        CalculationResult(
            isReady = true,
            primaryValue = "High Risk",
            primaryUnit = "$positive / 5",
            tierLabel = "C-Spine Imaging Indicated",
            tier = ClinicalTier.RED,
            clinicalNote = "Maintain cervical spine precautions and obtain CT C-spine imaging."
        )
    }
}

private fun calcParacetamol(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    val rawDose = state.number("dose") ?: return CalculationResult()
    val hours = state.number("hours") ?: return CalculationResult()
    val rawLevel = state.number("level") ?: return CalculationResult()

    if (rawW <= 0.0 || rawDose <= 0.0 || hours < 0.0 || rawLevel < 0.0) {
        return CalculationResult(warnings = listOf("Weight and ingested dose must be positive."))
    }

    val kg = toKg(rawW, state.unitIndex("weight"))
    val doseMg = if (state.unitIndex("dose") == 0) rawDose * 1000.0 else rawDose
    val dosePerKg = doseMg / kg
    val levelMcg = if (state.unitIndex("level") == 1) rawLevel / 6.614 else rawLevel

    val warnings = buildList {
        if (hours < 4.0) add("Time <4 hrs: Serum level CANNOT be reliably plotted on the Rumack-Matthew nomogram before 4 hours post-ingestion. Redraw at 4 hours.")
        if (hours > 24.0) add("Time >24 hrs: Outside nomogram window. Consult toxicology; start NAC if detectable level or elevated transaminases.")
        if (dosePerKg >= 150.0 || doseMg >= 10000.0) add("Toxic acute dose ingested (${fmt0(dosePerKg)} mg/kg · ${fmt1(doseMg / 1000.0)} g ≥150 mg/kg or ≥10 g).")
    }

    val tClamped = hours.coerceIn(4.0, 24.0)
    val line150 = 150.0 * 2.0.pow(-(tClamped - 4.0) / 4.0)
    val line100 = 100.0 * 2.0.pow(-(tClamped - 4.0) / 4.0)

    val (primary, tierLabel, tier) = when {
        hours < 4.0 || hours > 24.0 -> Triple(
            "${fmt1(levelMcg)} mcg/mL",
            "Outside 4–24h Nomogram Window",
            ClinicalTier.AMBER
        )
        levelMcg >= line150 -> Triple(
            "START NAC",
            "ABOVE 150-LINE TREATMENT THRESHOLD",
            ClinicalTier.RED
        )
        levelMcg >= line100 -> Triple(
            "BELOW 150",
            "Below 150-Line · Above UK 100-Line",
            ClinicalTier.AMBER
        )
        else -> Triple(
            "BELOW LINE",
            "Below Rumack-Matthew 150-Line",
            ClinicalTier.GREEN
        )
    }

    return CalculationResult(
        isReady = true,
        primaryValue = primary,
        primaryUnit = if (hours in 4.0..24.0) "@ ${fmtClean(hours)}h" else "",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("150-Line @ ${fmtClean(tClamped)}h", "${fmt1(line150)} mcg/mL", highlight = levelMcg >= line150),
            SecondaryOutput("UK 100-Line @ ${fmtClean(tClamped)}h", "${fmt1(line100)} mcg/mL"),
            SecondaryOutput("Patient Level", "${fmt1(levelMcg)} mcg/mL"),
            SecondaryOutput("Ingested Dose", "${fmt0(dosePerKg)} mg/kg")
        ),
        warnings = warnings
    )
}

// ============================================================================
// 3. GASTRO & HEPATOLOGY PHASE 2
// ============================================================================
private fun calcFib4(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val ast = state.number("ast") ?: return CalculationResult()
    val alt = state.number("alt") ?: return CalculationResult()
    val plt = state.number("plt") ?: return CalculationResult()
    if (age <= 0.0 || ast <= 0.0 || alt <= 0.0 || plt <= 0.0) {
        return CalculationResult(warnings = listOf("Age, AST, ALT, and Platelets must be positive."))
    }

    val warnings = buildList {
        if (age < 35.0) add("FIB-4 has lower accuracy in patients <35 years.")
        if (plt > 1500.0) add("Enter platelets in 10⁹/L (e.g., 180 rather than 180,000).")
    }

    val fib4 = (age * ast) / (plt * sqrt(alt))
    val lowCut = if (age >= 65.0) 2.0 else 1.30

    val (tierLabel, tier, note) = when {
        fib4 < lowCut -> Triple("Low Risk (<${fmt2(lowCut)})", ClinicalTier.GREEN, "Advanced fibrosis unlikely; repeat noninvasive testing in 1–3 years if metabolic risk persists.")
        fib4 <= 2.67 -> Triple("Indeterminate (${fmt2(lowCut)}–2.67)", ClinicalTier.AMBER, "Order secondary elastography (FibroScan) or ELF blood test to clarify fibrosis stage.")
        else -> Triple("High Risk Advanced Fibrosis (>2.67)", ClinicalTier.RED, "High probability of F3–F4 fibrosis (note >3.25 cutoff for chronic HCV); refer to hepatology.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(fib4),
        primaryUnit = "FIB-4",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcGlasgowBlatchford(state: CalculatorInputState): CalculationResult {
    val sex = state.choice("sex", "male") ?: "male"
    val urea = state.choice("urea")?.toIntOrNull()
    val hb = state.choice(if (sex == "male") "hb_male" else "hb_female")?.toIntOrNull()
    val sbp = state.choice("sbp")?.toIntOrNull()
    val selected = listOfNotNull(urea, hb, sbp).size
    if (selected < 3) return missingChoicePrompt(selected, 3)

    var score = urea!! + hb!! + sbp!!
    if (state.toggle("hr100")) score += 1
    if (state.toggle("melena")) score += 1
    if (state.toggle("syncope")) score += 2
    if (state.toggle("hepatic")) score += 2
    if (state.toggle("cardiac")) score += 2

    val (tierLabel, tier, note) = when {
        score == 0 -> Triple("Very Low Risk (0 · Outpatient)", ClinicalTier.GREEN, "Minimal risk of transfusion or endoscopic intervention; candidate for urgent outpatient endoscopy.")
        score <= 5 -> Triple("Inpatient Monitoring (1–5)", ClinicalTier.AMBER, "Admit for hemodynamic monitoring, IV PPI, type & screen, and inpatient endoscopy.")
        else -> Triple("High Risk · Urgent Endoscopy (≥6)", ClinicalTier.RED, "High probability (>50%) of needing clinical intervention; resuscitate & arrange urgent GI consult.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 23",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcLille(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val rawAlb = state.number("albumin") ?: return CalculationResult()
    val rawBili0 = state.number("bili0") ?: return CalculationResult()
    val rawBili7 = state.number("bili7") ?: return CalculationResult()
    val pt = state.number("pt") ?: return CalculationResult()
    if (age <= 0.0 || rawAlb <= 0.0 || rawBili0 < 0.0 || rawBili7 < 0.0 || pt <= 0.0) {
        return CalculationResult(warnings = listOf("Enter valid positive clinical values."))
    }

    val albGL = if (state.unitIndex("albumin") == 1) rawAlb * 10.0 else rawAlb
    val bili0Umol = if (state.unitIndex("bili0") == 1) rawBili0 * 17.1 else rawBili0
    val bili7Umol = if (state.unitIndex("bili7") == 1) rawBili7 * 17.1 else rawBili7
    val renal = if (state.toggle("renal")) 1.0 else 0.0

    val r = 3.19 -
        0.101 * age +
        0.147 * albGL +
        0.0165 * (bili0Umol - bili7Umol) -
        0.206 * renal -
        0.0065 * bili0Umol -
        0.0096 * pt
    val lille = exp(-r) / (1.0 + exp(-r))

    val (tierLabel, tier, note) = if (lille < 0.45) {
        Triple("Steroid Responder (<0.45 · Continue)", ClinicalTier.GREEN, "6-month survival ~85%; complete 28-day corticosteroid course.")
    } else {
        Triple("Non-Responder (≥0.45 · Stop Steroids)", ClinicalTier.RED, "6-month survival ~25%; discontinue steroids to avoid infection risk & evaluate transplant/supportive options.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(lille),
        primaryUnit = "Lille",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Δ Bilirubin (D0→D7)", "${fmt0(bili0Umol - bili7Umol)} µmol/L")
        ),
        clinicalNote = note
    )
}

private fun calcRockall(state: CalculatorInputState): CalculationResult {
    val age = state.choice("age")?.toIntOrNull()
    val shock = state.choice("shock")?.toIntOrNull()
    val comorb = state.choice("comorb")?.toIntOrNull()
    val preCount = listOfNotNull(age, shock, comorb).size
    if (preCount < 3) return missingChoicePrompt(preCount, 3)

    val preScore = age!! + shock!! + comorb!!
    val dx = state.choice("diagnosis")?.toIntOrNull()
    val stigmata = state.choice("stigmata")?.toIntOrNull()
    val hasPost = dx != null && stigmata != null
    val fullScore = if (hasPost) preScore + dx!! + stigmata!! else null

    val displayScore = fullScore ?: preScore
    val (tierLabel, tier) = if (fullScore != null) {
        when {
            fullScore <= 2 -> "Low Risk (Full 0–2 · ~5% rebleed)" to ClinicalTier.GREEN
            fullScore <= 5 -> "Intermediate Risk (Full 3–5)" to ClinicalTier.AMBER
            else -> "High Risk (Full ≥6 · High Mortality)" to ClinicalTier.RED
        }
    } else {
        when {
            preScore == 0 -> "Low Pre-Endoscopy Risk (0)" to ClinicalTier.GREEN
            preScore <= 3 -> "Moderate Pre-Endoscopy Risk (1–3)" to ClinicalTier.AMBER
            else -> "High Pre-Endoscopy Risk (≥4)" to ClinicalTier.RED
        }
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$displayScore",
        primaryUnit = if (fullScore != null) "/ 11 Full" else "/ 7 Pre-Endo",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Pre-Endoscopy", "$preScore / 7", highlight = !hasPost),
            SecondaryOutput("Full Rockall", if (fullScore != null) "$fullScore / 11" else "Select post-endo below", highlight = hasPost)
        )
    )
}

// ============================================================================
// 4. GENERAL SURGERY PHASE 2
// ============================================================================
private fun calcFluidDeficit(state: CalculatorInputState): CalculationResult {
    val rawW = state.number("weight") ?: return CalculationResult()
    if (rawW <= 0.0) return CalculationResult(warnings = listOf("Weight must be positive."))

    val kg = toKg(rawW, state.unitIndex("weight"))
    val pctChoice = state.choice("dehydration_pct", "5") ?: "5"
    val pct = if (pctChoice == "custom") {
        state.number("custom_pct") ?: return CalculationResult()
    } else {
        pctChoice.toDoubleOrNull() ?: 5.0
    }
    if (pct <= 0.0) return CalculationResult(warnings = listOf("Dehydration percentage must be positive."))

    val warnings = buildList {
        if (pct > 15.0) add("Dehydration >15% is life-threatening shock — administer immediate isotonic boluses first.")
        if (kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — verify units.")
    }

    val deficit = kg * pct * 10.0
    val maint24h = when {
        kg <= 10.0 -> kg * 100.0
        kg <= 20.0 -> 1000.0 + (kg - 10.0) * 50.0
        else -> 1500.0 + (kg - 20.0) * 20.0
    }
    val total24h = deficit + maint24h
    val first8hRate = (0.5 * deficit + maint24h / 3.0) / 8.0
    val next16hRate = (0.5 * deficit + (2.0 * maint24h) / 3.0) / 16.0

    val tier = when {
        pct >= 10.0 -> ClinicalTier.RED
        pct >= 7.5 -> ClinicalTier.AMBER
        else -> ClinicalTier.BLUE
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt0(total24h),
        primaryUnit = "mL / 24h",
        tierLabel = "${fmtClean(pct)}% Dehydration Rehydration",
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("First 8h Rate", "${fmt0(first8hRate)} mL/hr", highlight = true),
            SecondaryOutput("Next 16h Rate", "${fmt0(next16hRate)} mL/hr", highlight = true),
            SecondaryOutput("Fluid Deficit", "${fmt0(deficit)} mL"),
            SecondaryOutput("24h Maintenance", "${fmt0(maint24h)} mL")
        ),
        warnings = warnings
    )
}

private fun calcGlasgowImrie(state: CalculatorInputState): CalculationResult {
    val keys = listOf("pao2", "age55", "wbc", "ca", "urea", "ldh", "alb", "glu")
    val score = keys.count { state.toggle(it) }
    val (tierLabel, tier, note) = if (score >= 3) {
        Triple("Severe Acute Pancreatitis (≥3)", ClinicalTier.RED, "High complication risk; arrange HDU/ICU review, aggressive IV crystalloids, and organ support.")
    } else {
        Triple("Mild / Moderate (0–2)", ClinicalTier.GREEN, "Lower predicted severity; continue supportive ward care and reassess within 48h.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 8",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note
    )
}

private fun calcRanson(state: CalculatorInputState): CalculationResult {
    val isGallstone = state.choice("etiology", "non_gallstone") == "gallstone"
    val admKeys = if (isGallstone) {
        listOf("g_age", "g_wbc", "g_glu", "g_ldh", "g_ast")
    } else {
        listOf("ng_age", "ng_wbc", "ng_glu", "ng_ldh", "ng_ast")
    }
    val h48Keys = if (isGallstone) {
        listOf("g_hct", "g_bun", "g_ca", "g_pao2", "g_bd", "g_seq")
    } else {
        listOf("ng_hct", "ng_bun", "ng_ca", "ng_pao2", "ng_bd", "ng_seq")
    }

    val admScore = admKeys.count { state.toggle(it) }
    val h48Score = h48Keys.count { state.toggle(it) }
    val total = admScore + h48Score

    val (mort, tier) = when {
        total <= 2 -> "~2% Mortality (Mild)" to ClinicalTier.GREEN
        total <= 4 -> "~15% Mortality (Moderate–Severe)" to ClinicalTier.AMBER
        total <= 6 -> "~40% Mortality (Severe)" to ClinicalTier.RED
        else -> "~100% Mortality (Critical)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$total",
        primaryUnit = "/ 11",
        tierLabel = mort,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Admission", "$admScore / 5"),
            SecondaryOutput("48-Hour", "$h48Score / 6")
        )
    )
}

private fun calcRts(state: CalculatorInputState): CalculationResult {
    val gcs = state.choice("gcs")?.toIntOrNull()
    val sbp = state.choice("sbp")?.toIntOrNull()
    val rr = state.choice("rr")?.toIntOrNull()
    val count = listOfNotNull(gcs, sbp, rr).size
    if (count < 3) return missingChoicePrompt(count, 3)

    val triageRts = gcs!! + sbp!! + rr!!
    val codedRts = 0.9368 * gcs + 0.7326 * sbp + 0.2908 * rr

    val (tierLabel, tier) = when {
        triageRts == 12 -> "Normal Physiology (12/12)" to ClinicalTier.GREEN
        triageRts >= 10 -> "Trauma Center Transfer (≤11)" to ClinicalTier.AMBER
        else -> "Critical Trauma (≤9 · Immediate Resus)" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$triageRts",
        primaryUnit = "/ 12 Triage",
        tierLabel = tierLabel,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Coded RTS", "${fmt2(codedRts)} / 7.84", highlight = true)
        )
    )
}

// ============================================================================
// 5. ICU & CRITICAL CARE PHASE 2
// ============================================================================
private fun calcAaGradient(state: CalculatorInputState): CalculationResult {
    val age = state.number("age") ?: return CalculationResult()
    val fio2 = state.number("fio2") ?: return CalculationResult()
    val pao2 = state.number("pao2") ?: return CalculationResult()
    val paco2 = state.number("paco2") ?: return CalculationResult()
    val patm = state.number("patm") ?: 760.0

    if (age < 0.0 || fio2 <= 0.0 || pao2 <= 0.0 || paco2 <= 0.0 || patm <= 47.0) {
        return CalculationResult(warnings = listOf("Enter valid positive ABG and age values."))
    }

    val warnings = buildList {
        if (fio2 < 21.0 || fio2 > 100.0) add("FiO₂ (${fmt0(fio2)}%) must be between 21% and 100%.")
        if (pao2 > 650.0) add("PaO₂ (${fmt0(pao2)} mmHg) exceeds physiological maximum.")
    }

    val pAlveolarO2 = (fio2 / 100.0) * (patm - 47.0) - (paco2 / 0.8)
    val aaGradient = pAlveolarO2 - pao2
    val expectedNormal = (age / 4.0) + 4.0
    val isElevated = aaGradient > expectedNormal

    return CalculationResult(
        isReady = true,
        primaryValue = fmt1(aaGradient),
        primaryUnit = "mmHg",
        tierLabel = if (isElevated) "Elevated A-a Gradient" else "Normal for Age (≤${fmt1(expectedNormal)})",
        tier = if (isElevated) ClinicalTier.AMBER else ClinicalTier.GREEN,
        secondaryOutputs = listOf(
            SecondaryOutput("Expected Normal", "≤ ${fmt1(expectedNormal)} mmHg", highlight = true),
            SecondaryOutput("Alveolar PAO₂", "${fmt1(pAlveolarO2)} mmHg")
        ),
        clinicalNote = if (isElevated) "Suggests V/Q mismatch, right-to-left shunt, or alveolar-capillary diffusion defect." else "Normal gas exchange gradient; if hypoxemic, consider hypoventilation or low FiO₂/altitude.",
        warnings = warnings
    )
}

private fun calcFullSofa(state: CalculatorInputState): CalculationResult {
    val keys = listOf("resp", "coag", "liver", "cardio", "cns", "renal")
    val vals = keys.mapNotNull { state.choice(it)?.toIntOrNull() }
    if (vals.size < keys.size) return missingChoicePrompt(vals.size, keys.size)

    val score = vals.sum()
    val (mort, tier) = when {
        score <= 6 -> "<10% Mortality" to ClinicalTier.GREEN
        score <= 9 -> "15–20% Mortality" to ClinicalTier.AMBER
        score <= 12 -> "40–50% Mortality" to ClinicalTier.RED
        score <= 14 -> "50–60% Mortality" to ClinicalTier.RED
        else -> ">80% Mortality" to ClinicalTier.RED
    }

    return CalculationResult(
        isReady = true,
        primaryValue = "$score",
        primaryUnit = "/ 24",
        tierLabel = mort,
        tier = tier,
        secondaryOutputs = listOf(
            SecondaryOutput("Sepsis-3 Threshold", if (score >= 2) "Meets Δ ≥2 if acute" else "Below Δ2 threshold", highlight = score >= 2)
        )
    )
}

private fun calcRoxIndex(state: CalculatorInputState): CalculationResult {
    val spo2 = state.number("spo2") ?: return CalculationResult()
    val fio2 = state.number("fio2") ?: return CalculationResult()
    val rr = state.number("rr") ?: return CalculationResult()
    if (spo2 <= 0.0 || fio2 <= 0.0 || rr <= 0.0) {
        return CalculationResult(warnings = listOf("SpO₂, FiO₂, and RR must be positive."))
    }

    val warnings = buildList {
        if (spo2 > 100.0 || spo2 < 40.0) add("SpO₂ (${fmt0(spo2)}%) is outside valid range (40–100%).")
        if (fio2 < 21.0 || fio2 > 100.0) add("FiO₂ (${fmt0(fio2)}%) must be 21–100%.")
        if (rr > 80.0 || rr < 4.0) add("Respiratory rate (${fmt0(rr)} /min) is extreme.")
    }

    val rox = (spo2 / (fio2 / 100.0)) / rr
    val (tierLabel, tier, note) = when {
        rox >= 4.88 -> Triple("Low Intubation Risk (≥4.88)", ClinicalTier.GREEN, "High probability of HFNC success; continue current therapy and reassess serially.")
        rox >= 3.85 -> Triple("Borderline (3.85–4.87 · Reassess)", ClinicalTier.AMBER, "Close bedside reassessment in 1–2 hours; prepare escalation plan if falling.")
        else -> Triple("High Risk HFNC Failure (<3.85)", ClinicalTier.RED, "High likelihood of requiring intubation; notify ICU airway team early.")
    }

    return CalculationResult(
        isReady = true,
        primaryValue = fmt2(rox),
        primaryUnit = "ROX",
        tierLabel = tierLabel,
        tier = tier,
        clinicalNote = note,
        warnings = warnings
    )
}

private fun calcVasopressor(state: CalculatorInputState): CalculationResult {
    val mode = state.choice("mode", "dose_to_rate") ?: "dose_to_rate"
    val preset = state.choice("preset", "norepi") ?: "norepi"
    val doseUnit = state.choice("dose_unit", "mcg_kg_min") ?: "mcg_kg_min"

    val (drugMg, volMl, drugLabel) = when (preset) {
        "norepi" -> Triple(4.0, 250.0, "Norepinephrine 4mg/250mL")
        "epi" -> Triple(4.0, 250.0, "Epinephrine 4mg/250mL")
        "dopa" -> Triple(400.0, 250.0, "Dopamine 400mg/250mL")
        "dobut" -> Triple(250.0, 250.0, "Dobutamine 250mg/250mL")
        else -> {
            val mg = state.number("custom_mg") ?: return CalculationResult()
            val ml = state.number("custom_ml") ?: return CalculationResult()
            Triple(mg, ml, "Custom ${fmtClean(mg)}mg/${fmtClean(ml)}mL")
        }
    }

    if (drugMg <= 0.0 || volMl <= 0.0) {
        return CalculationResult(warnings = listOf("Drug mg and bag volume mL must be positive."))
    }

    val kg = if (doseUnit == "mcg_kg_min") {
        val rawW = state.number("weight") ?: return CalculationResult()
        if (rawW <= 0.0) return CalculationResult(warnings = listOf("Weight must be positive."))
        toKg(rawW, state.unitIndex("weight"))
    } else {
        null
    }

    val concMcgPerMl = (drugMg * 1000.0) / volMl
    val warnings = buildList {
        if (kg != null && kg > 250.0) add("Weight (${fmt1(kg)} kg) exceeds 250 kg — verify units.")
    }

    return if (mode == "dose_to_rate") {
        val dose = state.number("target_dose") ?: return CalculationResult()
        if (dose <= 0.0) return CalculationResult(warnings = listOf("Target dose must be positive."))

        val rateMlHr = if (kg != null) {
            (dose * kg * 60.0) / concMcgPerMl
        } else {
            (dose * 60.0) / concMcgPerMl
        }

        CalculationResult(
            isReady = true,
            primaryValue = fmt1(rateMlHr),
            primaryUnit = "mL/hr",
            tierLabel = drugLabel,
            tier = if (rateMlHr > 100.0) ClinicalTier.AMBER else ClinicalTier.BLUE,
            secondaryOutputs = listOf(
                SecondaryOutput("Concentration", "${fmtClean(concMcgPerMl)} mcg/mL", highlight = true),
                SecondaryOutput("Ordered Dose", "${fmtClean(dose)} ${if (kg != null) "mcg/kg/min" else "mcg/min"}")
            ),
            warnings = warnings
        )
    } else {
        val rate = state.number("pump_rate") ?: return CalculationResult()
        if (rate <= 0.0) return CalculationResult(warnings = listOf("Pump rate must be positive."))

        val calculatedDose = if (kg != null) {
            (rate * concMcgPerMl) / (kg * 60.0)
        } else {
            (rate * concMcgPerMl) / 60.0
        }

        CalculationResult(
            isReady = true,
            primaryValue = fmt2(calculatedDose),
            primaryUnit = if (kg != null) "mcg/kg/min" else "mcg/min",
            tierLabel = drugLabel,
            tier = ClinicalTier.BLUE,
            secondaryOutputs = listOf(
                SecondaryOutput("Concentration", "${fmtClean(concMcgPerMl)} mcg/mL", highlight = true),
                SecondaryOutput("Pump Rate", "${fmt1(rate)} mL/hr")
            ),
            warnings = warnings
        )
    }
}
