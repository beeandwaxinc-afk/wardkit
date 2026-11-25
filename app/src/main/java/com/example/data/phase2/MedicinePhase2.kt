package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val MedicinePhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "im_bmi_bsa",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "BMI & BSA (Mosteller)",
        shortSubtitle = "WHO BMI category & Mosteller BSA m²",
        synonyms = listOf("bmi", "bsa", "mosteller", "body mass index", "body surface area", "chemotherapy", "obesity"),
        formulaSummary = "BMI = Weight (kg) / Height (m)²\nMosteller BSA (m²) = √[(Height cm × Weight kg) / 3600]\nWHO: <18.5 Underweight | 18.5–24.9 Normal | 25–29.9 Overweight | ≥30 Obese I–III",
        clinicalPearl = "Mosteller BSA is the standard bedside formula for chemotherapy dosing, cardiac index normalization, and pediatric drug indexing.",
        inputs = listOf(
            CalcInputDef.NumericField("weight", "Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.NumericField("height", "Height", "Height", listOf("cm", "in"))
        )
    ),
    CalculatorDef(
        id = "im_centor",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "Centor / McIsaac Score",
        shortSubtitle = "Group A Strep pharyngitis probability",
        synonyms = listOf("centor", "mcisaac", "strep", "pharyngitis", "sore throat", "tonsillitis"),
        formulaSummary = "Tonsillar exudates (+1), Tender ant. cervical nodes (+1), Fever >38°C (+1), No cough (+1) + Age (3–14: +1, 15–44: 0, ≥45: −1) = −1 to 5.\n≤1: No test/Abx | 2–3: Rapid Strep / Culture | ≥4: High Probability / Consider Empiric Rx",
        clinicalPearl = "Designed to reduce unnecessary antibiotic prescribing for viral pharyngitis. Confirm with Rapid Antigen Detection Test (RADT) whenever available before starting penicillin/amoxicillin.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "age_group",
                label = "Patient Age",
                options = listOf(
                    ChoiceOption("1", "3–14 yrs", "", "+1", 1),
                    ChoiceOption("0", "15–44 yrs", "", "0", 0),
                    ChoiceOption("-1", "≥45 yrs", "", "−1", -1)
                )
            ),
            CalcInputDef.ToggleCriterion("exudate", "Tonsillar Exudates or Swelling", "White patches or marked tonsillar enlargement", "+1", 1),
            CalcInputDef.ToggleCriterion("nodes", "Tender Anterior Cervical Nodes", "Swollen, tender anterior cervical lymphadenopathy", "+1", 1),
            CalcInputDef.ToggleCriterion("fever", "Fever >38.0°C (>100.4°F)", "Documented or history of fever", "+1", 1),
            CalcInputDef.ToggleCriterion("no_cough", "Absence of Cough", "No cough present", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "im_news2",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "NEWS2 (Early Warning Score)",
        shortSubtitle = "RCP adult deterioration & Red Score triage",
        synonyms = listOf("news", "news2", "national early warning score", "ews", "early warning", "sepsis", "acvpu", "deterioration"),
        formulaSummary = "7 RCP Parameters (0–20): RR, SpO₂ (Scale 1 or Scale 2 COPD), Air/O₂, SBP, Pulse, ACVPU, Temp.\n0–4 Low | Any Single Param = 3 → Red Score (Low-Medium) | 5–6 Medium | ≥7 High Risk Emergency",
        clinicalPearl = "Use SpO₂ Scale 2 ONLY when a clinician has explicitly documented hypercapnic respiratory failure (e.g., COPD) with a target SpO₂ of 88–92%. Any single parameter scoring 3 triggers an urgent Red Score review.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "spo2_scale",
                label = "SpO₂ Scoring Scale",
                options = listOf(
                    ChoiceOption("scale1", "Scale 1", "Standard"),
                    ChoiceOption("scale2", "Scale 2", "COPD 88–92%")
                ),
                defaultOptionId = "scale1"
            ),
            CalcInputDef.SegmentedChoice(
                id = "rr",
                label = "1. Respiration Rate (/min)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("rr_low3", "≤8 /min", "Severe bradypnea", "+3", 3),
                    ChoiceOption("rr_low1", "9–11 /min", "Mild bradypnea", "+1", 1),
                    ChoiceOption("rr_norm", "12–20 /min", "Normal", "0", 0),
                    ChoiceOption("rr_hi2", "21–24 /min", "Tachypnea", "+2", 2),
                    ChoiceOption("rr_hi3", "≥25 /min", "Severe tachypnea", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "spo2_s1",
                label = "2. SpO₂ — Scale 1 (Standard)",
                compactRow = false,
                visibleWhenChoiceKey = "spo2_scale",
                visibleWhenChoiceValue = "scale1",
                options = listOf(
                    ChoiceOption("s1_3", "≤91%", "", "+3", 3),
                    ChoiceOption("s1_2", "92–93%", "", "+2", 2),
                    ChoiceOption("s1_1", "94–95%", "", "+1", 1),
                    ChoiceOption("s1_0", "≥96%", "", "0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "spo2_s2",
                label = "2. SpO₂ — Scale 2 (Hypercapnic / COPD)",
                compactRow = false,
                visibleWhenChoiceKey = "spo2_scale",
                visibleWhenChoiceValue = "scale2",
                options = listOf(
                    ChoiceOption("s2_low3", "≤83%", "", "+3", 3),
                    ChoiceOption("s2_low2", "84–85%", "", "+2", 2),
                    ChoiceOption("s2_low1", "86–87%", "", "+1", 1),
                    ChoiceOption("s2_norm", "88–92% (or ≥93% on Room Air)", "Target range", "0", 0),
                    ChoiceOption("s2_hi1", "93–94% on Supplemental O₂", "", "+1", 1),
                    ChoiceOption("s2_hi2", "95–96% on Supplemental O₂", "", "+2", 2),
                    ChoiceOption("s2_hi3", "≥97% on Supplemental O₂", "Hyperoxia risk", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "oxygen",
                label = "3. Air or Supplemental Oxygen",
                options = listOf(
                    ChoiceOption("0", "Room Air", "", "0", 0),
                    ChoiceOption("2", "Supplemental O₂", "", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "sbp",
                label = "4. Systolic Blood Pressure (mmHg)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("sbp_low3", "≤90 mmHg", "Hypotension", "+3", 3),
                    ChoiceOption("sbp_low2", "91–100 mmHg", "", "+2", 2),
                    ChoiceOption("sbp_low1", "101–110 mmHg", "", "+1", 1),
                    ChoiceOption("sbp_norm", "111–219 mmHg", "Normal range", "0", 0),
                    ChoiceOption("sbp_hi3", "≥220 mmHg", "Hypertensive crisis", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "pulse",
                label = "5. Pulse / Heart Rate (bpm)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("hr_low3", "≤40 bpm", "Severe bradycardia", "+3", 3),
                    ChoiceOption("hr_low1", "41–50 bpm", "", "+1", 1),
                    ChoiceOption("hr_norm", "51–90 bpm", "Normal", "0", 0),
                    ChoiceOption("hr_hi1", "91–110 bpm", "", "+1", 1),
                    ChoiceOption("hr_hi2", "111–130 bpm", "", "+2", 2),
                    ChoiceOption("hr_hi3", "≥131 bpm", "Severe tachycardia", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "acvpu",
                label = "6. Consciousness (ACVPU)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Alert (A)", "Fully awake & oriented", "0", 0),
                    ChoiceOption("3", "New Confusion (C) / V / P / U", "Acute confusion or reduced consciousness", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "temp",
                label = "7. Temperature (°C)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("t_low3", "≤35.0 °C", "Hypothermia", "+3", 3),
                    ChoiceOption("t_low1", "35.1–36.0 °C", "", "+1", 1),
                    ChoiceOption("t_norm", "36.1–38.0 °C", "Normal", "0", 0),
                    ChoiceOption("t_hi1", "38.1–39.0 °C", "Fever", "+1", 1),
                    ChoiceOption("t_hi2", "≥39.1 °C", "High fever", "+2", 2)
                )
            )
        )
    ),
    CalculatorDef(
        id = "im_osmolality",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "Serum Osmolality & Gap",
        shortSubtitle = "Calculated osm & toxic alcohol osmolar gap",
        synonyms = listOf("osmolality", "osmolar gap", "methanol", "ethylene glycol", "ethanol", "toxicology", "hhs"),
        formulaSummary = "Calc Osm = 2×Na + (Glucose mg/dL / 18) + (BUN mg/dL / 2.8) [+ Ethanol mg/dL / 4.6]\nNormal: 275–295 mOsm/kg  |  Osmolar Gap = Measured − Calc (Flag >10 mOsm/kg)",
        clinicalPearl = "An elevated osmolar gap (>10 mOsm/kg) suggests unmeasured osmotically active solutes such as methanol, ethylene glycol, isopropanol, or mannitol—remember late toxic alcohol ingestion may have normalized the osmolar gap as acids form.",
        inputs = listOf(
            CalcInputDef.NumericField("na", "Serum Sodium (Na⁺)", "mEq/L", listOf("mEq/L")),
            CalcInputDef.NumericField("glucose", "Serum Glucose", "Glucose", listOf("mg/dL", "mmol/L")),
            CalcInputDef.NumericField("bun", "BUN / Urea", "BUN or Urea", listOf("mg/dL (BUN)", "mmol/L (Urea)")),
            CalcInputDef.NumericField("ethanol", "Serum Ethanol (Optional)", "Optional mg/dL", listOf("mg/dL"), optional = true),
            CalcInputDef.NumericField("measured_osm", "Measured Osmolality (Optional)", "Optional mOsm/kg", listOf("mOsm/kg"), optional = true)
        )
    )
)
