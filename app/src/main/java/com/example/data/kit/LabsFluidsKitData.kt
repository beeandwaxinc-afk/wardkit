package com.example.data.kit

// ============================================================================
// BLOCK 2: NORMAL LABS & BODY FLUIDS (4 INTERACTIVE CARDS · SI & CONV DUAL UNITS)
// ============================================================================

data class DualLabRow(
    val analyte: String,
    val note: String = "",
    val convRange: String,
    val convUnit: String,
    val siRange: String,
    val siUnit: String
) {
    fun rangeFor(system: LabUnitSystem): String =
        if (system == LabUnitSystem.SI) "$siRange $siUnit" else "$convRange $convUnit"
}

data class AdultLabPanel(
    val id: String,
    val label: String,
    val rows: List<DualLabRow>
)

data class PedsLabAgeGroup(
    val id: String,
    val ageLabel: String,
    val wbcShiftNote: String,
    val rows: List<DualLabRow>
)

data class AbgNormalRow(
    val parameter: String,
    val convRange: String,
    val siRange: String
) {
    fun valueFor(system: LabUnitSystem): String =
        if (system == LabUnitSystem.SI) siRange else convRange
}

data class AbgDisorderRow(
    val disorder: String,
    val primaryChange: String,
    val compensationFormula: String
)

data class CsfProfileRow(
    val condition: String,
    val pressureCmH2O: String,
    val wbcAndDiff: String,
    val proteinConv: String,
    val proteinSi: String,
    val glucoseConv: String,
    val glucoseSi: String,
    val isUrgent: Boolean = false
)

data class SynovialProfileRow(
    val category: String,
    val appearance: String,
    val wbcRange: String,
    val pmnPercent: String,
    val isUrgent: Boolean = false
)

object LabsFluidsKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Core Adult Lab Panels
    // ------------------------------------------------------------------------
    val adultPanels: List<AdultLabPanel> = listOf(
        AdultLabPanel(
            id = "cbc_coags",
            label = "CBC & Coags",
            rows = listOf(
                DualLabRow("Hemoglobin (Male)", "", "13.5–17.5", "g/dL", "135–175", "g/L"),
                DualLabRow("Hemoglobin (Female)", "", "12.0–15.5", "g/dL", "120–155", "g/L"),
                DualLabRow("WBC Count", "", "4.0–11.0", "×10³/µL", "4.0–11.0", "×10⁹/L"),
                DualLabRow("Platelets", "", "150–400", "×10³/µL", "150–400", "×10⁹/L"),
                DualLabRow("MCV", "", "80–100", "fL", "80–100", "fL"),
                DualLabRow("Reticulocytes", "", "0.5–2.0", "%", "25–85", "×10⁹/L"),
                DualLabRow("PT / INR", "Non-anticoagulated", "11–13.5 s · 0.8–1.1", "INR", "11–13.5 s · 0.8–1.1", "INR"),
                DualLabRow("aPTT", "", "25–35", "sec", "25–35", "sec"),
                DualLabRow("Fibrinogen", "", "200–400", "mg/dL", "2.0–4.0", "g/L"),
                DualLabRow("D-dimer", "Age-adj >50y: Age×10", "< 500", "ng/mL FEU", "< 0.50", "mg/L FEU")
            )
        ),
        AdultLabPanel(
            id = "rft_lyte",
            label = "RFT & Lyte",
            rows = listOf(
                DualLabRow("Sodium (Na⁺)", "", "135–145", "mEq/L", "135–145", "mmol/L"),
                DualLabRow("Potassium (K⁺)", "", "3.5–5.0", "mEq/L", "3.5–5.0", "mmol/L"),
                DualLabRow("Chloride (Cl⁻)", "", "98–106", "mEq/L", "98–106", "mmol/L"),
                DualLabRow("Bicarbonate (HCO₃⁻)", "", "22–26", "mEq/L", "22–26", "mmol/L"),
                DualLabRow("BUN / Urea", "", "7–20", "mg/dL", "2.5–7.1", "mmol/L"),
                DualLabRow("Creatinine (Male)", "", "0.7–1.3", "mg/dL", "62–115", "µmol/L"),
                DualLabRow("Creatinine (Female)", "", "0.6–1.1", "mg/dL", "53–97", "µmol/L"),
                DualLabRow("Total Calcium", "Correct if low Alb", "8.5–10.5", "mg/dL", "2.12–2.62", "mmol/L"),
                DualLabRow("Magnesium (Mg²⁺)", "", "1.7–2.2", "mg/dL", "0.70–0.90", "mmol/L"),
                DualLabRow("Phosphate (PO₄³⁻)", "", "2.5–4.5", "mg/dL", "0.81–1.45", "mmol/L"),
                DualLabRow("Uric Acid (M / F)", "", "3.5–7.2 / 2.6–6.0", "mg/dL", "210–430 / 155–360", "µmol/L")
            )
        ),
        AdultLabPanel(
            id = "lft",
            label = "LFT",
            rows = listOf(
                DualLabRow("Total Bilirubin", "", "0.1–1.2", "mg/dL", "2–21", "µmol/L"),
                DualLabRow("Direct Bilirubin", "", "< 0.3", "mg/dL", "< 5", "µmol/L"),
                DualLabRow("AST (SGOT)", "", "10–40", "U/L", "10–40", "U/L"),
                DualLabRow("ALT (SGPT)", "", "7–56", "U/L", "7–56", "U/L"),
                DualLabRow("ALP (Adult)", "", "44–147", "U/L", "44–147", "U/L"),
                DualLabRow("GGT", "M ≤60 · F ≤40", "8–60", "U/L", "8–60", "U/L"),
                DualLabRow("Albumin", "", "3.5–5.0", "g/dL", "35–50", "g/L"),
                DualLabRow("Total Protein", "", "6.0–8.3", "g/dL", "60–83", "g/L")
            )
        ),
        AdultLabPanel(
            id = "cardiac_metabolic",
            label = "Cardiac & Metabolic",
            rows = listOf(
                DualLabRow("hs-Troponin I / T", "Check assay 99th %ile", "< 14–19", "ng/L", "< 14–19", "ng/L"),
                DualLabRow("BNP", "HF unlikely <100", "< 100", "pg/mL", "< 100", "ng/L"),
                DualLabRow("Fasting Glucose", "", "70–99", "mg/dL", "3.9–5.5", "mmol/L"),
                DualLabRow("HbA1c", "DM ≥6.5% / ≥48", "< 5.7", "%", "< 39", "mmol/mol"),
                DualLabRow("Total Cholesterol", "Desirable", "< 200", "mg/dL", "< 5.2", "mmol/L"),
                DualLabRow("LDL Cholesterol", "Optimal", "< 100", "mg/dL", "< 2.6", "mmol/L"),
                DualLabRow("HDL Cholesterol", "M >40 · F >50", "> 40 / > 50", "mg/dL", "> 1.0 / > 1.3", "mmol/L"),
                DualLabRow("Triglycerides", "Fasting", "< 150", "mg/dL", "< 1.7", "mmol/L"),
                DualLabRow("Lactate (Venous/Art)", "", "4.5–19.8", "mg/dL", "0.5–2.2", "mmol/L"),
                DualLabRow("Amylase", "", "30–110", "U/L", "30–110", "U/L"),
                DualLabRow("Lipase", "Preferred for pancreatitis", "10–140", "U/L", "10–140", "U/L")
            )
        ),
        AdultLabPanel(
            id = "thyroid_iron",
            label = "Thyroid & Iron",
            rows = listOf(
                DualLabRow("TSH", "", "0.4–4.0", "mIU/L", "0.4–4.0", "mIU/L"),
                DualLabRow("Free T4", "", "0.8–1.8", "ng/dL", "10–23", "pmol/L"),
                DualLabRow("Ferritin (Male)", "Acute phase reactant", "30–400", "ng/mL", "30–400", "µg/L"),
                DualLabRow("Ferritin (Female)", "Acute phase reactant", "15–150", "ng/mL", "15–150", "µg/L"),
                DualLabRow("Serum Iron", "", "60–170", "µg/dL", "11–30", "µmol/L"),
                DualLabRow("TIBC", "", "250–450", "µg/dL", "45–80", "µmol/L"),
                DualLabRow("Transferrin Sat", "Low <20%", "20–50", "%", "20–50", "%")
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: Pediatric Age-Adjusted Labs
    // ------------------------------------------------------------------------
    val pedsLabGroups: List<PedsLabAgeGroup> = listOf(
        PedsLabAgeGroup(
            id = "nb",
            ageLabel = "Newborn",
            wbcShiftNote = "Neutrophil-predominant (~60%) at birth; shifts to lymphocytes by Day 4–7.",
            rows = listOf(
                DualLabRow("Hemoglobin", "Physiologic polycythemia", "14.5–22.5", "g/dL", "145–225", "g/L"),
                DualLabRow("WBC Count", "High in first 48h", "9.0–30.0", "×10³/µL", "9.0–30.0", "×10⁹/L"),
                DualLabRow("Neutrophils / Lymphs", "Birth predominance", "60% N / 30% L", "diff", "60% N / 30% L", "diff"),
                DualLabRow("Platelets", "", "150–450", "×10³/µL", "150–450", "×10⁹/L"),
                DualLabRow("Creatinine", "Reflects maternal Cr d1–3", "0.3–1.0", "mg/dL", "27–88", "µmol/L"),
                DualLabRow("Alkaline Phosphatase", "Physiologically elevated", "100–350", "U/L", "100–350", "U/L")
            )
        ),
        PedsLabAgeGroup(
            id = "1_6m",
            ageLabel = "1–6 mo",
            wbcShiftNote = "Lymphocyte-predominant (~60–70% lymphs vs 25–35% neutrophils).",
            rows = listOf(
                DualLabRow("Hemoglobin", "Physiologic nadir at 2–3 mo", "9.5–13.5", "g/dL", "95–135", "g/L"),
                DualLabRow("WBC Count", "", "6.0–17.5", "×10³/µL", "6.0–17.5", "×10⁹/L"),
                DualLabRow("Neutrophils / Lymphs", "Infant lymphocytosis", "30% N / 60% L", "diff", "30% N / 60% L", "diff"),
                DualLabRow("Platelets", "", "150–450", "×10³/µL", "150–450", "×10⁹/L"),
                DualLabRow("Creatinine", "Low muscle mass", "0.2–0.4", "mg/dL", "18–35", "µmol/L"),
                DualLabRow("Alkaline Phosphatase", "Active bone growth", "150–420", "U/L", "150–420", "U/L")
            )
        ),
        PedsLabAgeGroup(
            id = "1_5y",
            ageLabel = "1–5 yr",
            wbcShiftNote = "Lymphocyte-predominant until ~4–5 yr, then crosses back toward neutrophils.",
            rows = listOf(
                DualLabRow("Hemoglobin", "", "11.0–14.0", "g/dL", "110–140", "g/L"),
                DualLabRow("WBC Count", "", "5.5–15.5", "×10³/µL", "5.5–15.5", "×10⁹/L"),
                DualLabRow("Neutrophils / Lymphs", "Crosses ~50/50 at 4–5 yr", "35–45% N / 50–60% L", "diff", "35–45% N / 50–60% L", "diff"),
                DualLabRow("Platelets", "", "150–450", "×10³/µL", "150–450", "×10⁹/L"),
                DualLabRow("Creatinine", "", "0.3–0.5", "mg/dL", "27–44", "µmol/L"),
                DualLabRow("Alkaline Phosphatase", "2–3× adult upper limit", "140–350", "U/L", "140–350", "U/L")
            )
        ),
        PedsLabAgeGroup(
            id = "6_12y",
            ageLabel = "6–12 yr",
            wbcShiftNote = "Transitions to adult neutrophil predominance (~55–65% N / 30–35% L).",
            rows = listOf(
                DualLabRow("Hemoglobin", "", "11.5–15.5", "g/dL", "115–155", "g/L"),
                DualLabRow("WBC Count", "", "4.5–13.5", "×10³/µL", "4.5–13.5", "×10⁹/L"),
                DualLabRow("Neutrophils / Lymphs", "Adult-like pattern", "55% N / 35% L", "diff", "55% N / 35% L", "diff"),
                DualLabRow("Platelets", "", "150–450", "×10³/µL", "150–450", "×10⁹/L"),
                DualLabRow("Creatinine", "", "0.4–0.8", "mg/dL", "35–71", "µmol/L"),
                DualLabRow("Alkaline Phosphatase", "Peaks in pre-pubertal spurt", "140–400", "U/L", "140–400", "U/L")
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 3: ABG / VBG Rapid Matrix
    // ------------------------------------------------------------------------
    val abgNormals: List<AbgNormalRow> = listOf(
        AbgNormalRow("pH", "7.35–7.45", "7.35–7.45"),
        AbgNormalRow("PaCO₂", "35–45 mmHg", "4.7–6.0 kPa"),
        AbgNormalRow("HCO₃⁻", "22–26 mEq/L", "22–26 mmol/L"),
        AbgNormalRow("PaO₂", "80–100 mmHg", "10.7–13.3 kPa"),
        AbgNormalRow("Base Excess", "-2 to +2 mEq/L", "-2 to +2 mmol/L")
    )

    val abgDisorders: List<AbgDisorderRow> = listOf(
        AbgDisorderRow(
            disorder = "Metabolic Acidosis",
            primaryChange = "↓ pH · ↓ HCO₃⁻",
            compensationFormula = "Winter's: PaCO₂ = (1.5 × HCO₃⁻) + 8 ± 2 mmHg"
        ),
        AbgDisorderRow(
            disorder = "Metabolic Alkalosis",
            primaryChange = "↑ pH · ↑ HCO₃⁻",
            compensationFormula = "Expected ΔPaCO₂ ≈ 0.7 × ΔHCO₃⁻ (or PaCO₂ = 0.7×HCO₃ + 20 ± 5)"
        ),
        AbgDisorderRow(
            disorder = "Respiratory Acidosis",
            primaryChange = "↓ pH · ↑ PaCO₂",
            compensationFormula = "Acute: ↑HCO₃ 1 per 10 ↑PaCO₂ · Chronic: ↑HCO₃ 3.5–4 per 10"
        ),
        AbgDisorderRow(
            disorder = "Respiratory Alkalosis",
            primaryChange = "↑ pH · ↓ PaCO₂",
            compensationFormula = "Acute: ↓HCO₃ 2 per 10 ↓PaCO₂ · Chronic: ↓HCO₃ 4–5 per 10"
        )
    )

    fun vbgOffsets(system: LabUnitSystem): List<Pair<String, String>> = listOf(
        "VBG pH vs ABG" to "0.03 to 0.04 LOWER (ABG ≈ VBG + 0.035)",
        "VBG PCO₂ vs ABG" to if (system == LabUnitSystem.SI) "0.5–0.8 kPa HIGHER (rules out hypercapnia if <6.0)" else "4–6 mmHg HIGHER (rules out hypercapnia if <45)",
        "VBG HCO₃⁻" to "Matches ABG closely (±1–1.5) — reliable for DKA/metabolic",
        "VBG PO₂" to "Not reliable for oxygenation — use SpO₂ or arterial ABG"
    )

    // ------------------------------------------------------------------------
    // CARD 4: Body Fluid Interpreters
    // ------------------------------------------------------------------------
    val csfProfiles: List<CsfProfileRow> = listOf(
        CsfProfileRow(
            condition = "Normal",
            pressureCmH2O = "10–20",
            wbcAndDiff = "< 5 (Lymphs)",
            proteinConv = "15–45 mg/dL",
            proteinSi = "0.15–0.45 g/L",
            glucoseConv = "50–80 (≥60% serum)",
            glucoseSi = "2.8–4.4 (≥0.6 ratio)"
        ),
        CsfProfileRow(
            condition = "Bacterial",
            pressureCmH2O = "↑↑ >25",
            wbcAndDiff = "> 1000 (PMNs)",
            proteinConv = "↑↑ >100 mg/dL",
            proteinSi = "↑↑ >1.0 g/L",
            glucoseConv = "↓↓ <40 (<40% serum)",
            glucoseSi = "↓↓ <2.2 (<0.4 ratio)",
            isUrgent = true
        ),
        CsfProfileRow(
            condition = "Viral",
            pressureCmH2O = "Normal / ↑",
            wbcAndDiff = "10–500 (Lymphs)",
            proteinConv = "Normal–80 mg/dL",
            proteinSi = "0.4–0.8 g/L",
            glucoseConv = "Normal (>50% serum)",
            glucoseSi = "Normal (>0.5 ratio)"
        ),
        CsfProfileRow(
            condition = "TB Meningitis",
            pressureCmH2O = "↑↑ >25",
            wbcAndDiff = "50–500 (Lymphs)",
            proteinConv = "↑↑ 100–500 mg/dL",
            proteinSi = "↑↑ 1.0–5.0 g/L",
            glucoseConv = "↓ <45 (<50% serum)",
            glucoseSi = "↓ <2.5 (<0.5 ratio)",
            isUrgent = true
        ),
        CsfProfileRow(
            condition = "Fungal (Crypto)",
            pressureCmH2O = "↑↑↑ >30",
            wbcAndDiff = "20–200 (Lymphs)",
            proteinConv = "↑ 50–200 mg/dL",
            proteinSi = "↑ 0.5–2.0 g/L",
            glucoseConv = "↓ <45 (<50% serum)",
            glucoseSi = "↓ <2.5 (<0.5 ratio)"
        )
    )

    val lightsCriteria: List<String> = listOf(
        "Pleural Fluid / Serum Protein ratio > 0.5",
        "Pleural Fluid / Serum LDH ratio > 0.6",
        "Pleural Fluid LDH > 2/3 Upper Limit of Normal Serum LDH"
    )

    val pleuralTransudateCauses: List<String> = listOf(
        "Heart failure (most common)",
        "Cirrhosis (hepatic hydrothorax)",
        "Nephrotic syndrome / hypoalbuminemia"
    )

    val pleuralExudateCauses: List<String> = listOf(
        "Parapneumonic effusion / Empyema",
        "Malignancy & Pulmonary embolism",
        "Tuberculosis, Pancreatitis, Autoimmune (RA/SLE)"
    )

    fun empyemaMarkers(system: LabUnitSystem): List<String> = listOf(
        "Pleural pH < 7.20 (strong indication for chest tube drainage)",
        if (system == LabUnitSystem.SI) "Pleural Glucose < 2.2 mmol/L · LDH > 1000 U/L" else "Pleural Glucose < 40 mg/dL · LDH > 1000 U/L",
        "Frank pus, positive Gram stain, or loculated effusion → drain urgently"
    )

    fun asciticSaagHighTitle(system: LabUnitSystem): String =
        if (system == LabUnitSystem.SI) "SAAG ≥ 11 g/L — Portal HTN" else "SAAG ≥ 1.1 g/dL — Portal HTN"

    fun asciticSaagLowTitle(system: LabUnitSystem): String =
        if (system == LabUnitSystem.SI) "SAAG < 11 g/L — Non-Portal" else "SAAG < 1.1 g/dL — Non-Portal"

    val saagHighCauses: List<String> = listOf(
        "Cirrhosis, Alcoholic hepatitis, Massive liver mets",
        "Right heart failure / Constrictive pericarditis",
        "Budd-Chiari syndrome"
    )

    val saagLowCauses: List<String> = listOf(
        "Peritoneal carcinomatosis",
        "Tuberculous peritonitis",
        "Pancreatitis, Nephrotic syndrome, Serositis"
    )

    val synovialProfiles: List<SynovialProfileRow> = listOf(
        SynovialProfileRow("Normal", "Clear, high viscosity", "< 200", "< 25%"),
        SynovialProfileRow("Non-Inflammatory (OA)", "Clear / Straw", "200–2,000", "< 25%"),
        SynovialProfileRow("Inflammatory (RA / Gout)", "Cloudy, yellow", "2,000–50,000", "50–75%"),
        SynovialProfileRow("Septic Arthritis", "Purulent, opaque", "> 50,000", "> 75% PMN", isUrgent = true)
    )
}
