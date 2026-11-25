package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val IcuPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "icu_aa_gradient",
        departmentId = DepartmentId.ICU,
        name = "A-a Oxygen Gradient",
        shortSubtitle = "Alveolar-arterial hypoxemia etiology",
        synonyms = listOf("a-a gradient", "aa gradient", "abg", "hypoxemia", "shunt", "vq mismatch", "pao2"),
        formulaSummary = "PAO₂ = [FiO₂/100 × (Patm − 47)] − (PaCO₂ / 0.8)\nA-a Gradient = PAO₂ − PaO₂  |  Expected Normal = (Age / 4) + 4 mmHg",
        clinicalPearl = "Normal A-a gradient in hypoxemia points to hypoventilation or low inspired O₂ (altitude). Elevated A-a gradient indicates V/Q mismatch, right-to-left shunt, or diffusion impairment.",
        inputs = listOf(
            CalcInputDef.NumericField("age", "Age", "Years", listOf("yrs")),
            CalcInputDef.NumericField("fio2", "Inspired FiO₂ (%)", "21–100%", listOf("%")),
            CalcInputDef.NumericField("pao2", "Arterial PaO₂", "mmHg", listOf("mmHg")),
            CalcInputDef.NumericField("paco2", "Arterial PaCO₂", "mmHg", listOf("mmHg")),
            CalcInputDef.NumericField("patm", "Barometric Pressure (Optional)", "Default 760 mmHg", listOf("mmHg"), optional = true)
        )
    ),
    CalculatorDef(
        id = "icu_sofa",
        departmentId = DepartmentId.ICU,
        name = "Full SOFA Score",
        shortSubtitle = "6-organ ICU sepsis & mortality grading",
        synonyms = listOf("sofa", "sepsis", "sepsis-3", "organ failure", "icu", "mortality"),
        formulaSummary = "Sum of 6 organ systems (0–4 each = 0–24): Respiration, Coagulation, Liver, Cardiovascular, CNS, Renal.\nAcute Δ ≥2 defines Sepsis-3 organ dysfunction.\n0–6: <10% | 7–9: 15–20% | 10–12: 40–50% | 13–14: 50–60% | ≥15: >80% mortality",
        clinicalPearl = "Calculate baseline SOFA and repeat daily—a rising SOFA score during the first 48 hours of ICU care predicts mortality ≥50% regardless of initial score.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "resp",
                label = "Respiration (PaO₂/FiO₂ mmHg)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "≥400", "", "0", 0),
                    ChoiceOption("1", "<400", "", "+1", 1),
                    ChoiceOption("2", "<300", "", "+2", 2),
                    ChoiceOption("3", "<200 with Resp. Support", "", "+3", 3),
                    ChoiceOption("4", "<100 with Resp. Support", "", "+4", 4)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "coag",
                label = "Coagulation (Platelets ×10³/µL)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "≥150", "", "0", 0),
                    ChoiceOption("1", "<150", "", "+1", 1),
                    ChoiceOption("2", "<100", "", "+2", 2),
                    ChoiceOption("3", "<50", "", "+3", 3),
                    ChoiceOption("4", "<20", "", "+4", 4)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "liver",
                label = "Liver (Bilirubin mg/dL)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "<1.2 (<20 µmol/L)", "", "0", 0),
                    ChoiceOption("1", "1.2–1.9 (20–32 µmol/L)", "", "+1", 1),
                    ChoiceOption("2", "2.0–5.9 (33–101 µmol/L)", "", "+2", 2),
                    ChoiceOption("3", "6.0–11.9 (102–204 µmol/L)", "", "+3", 3),
                    ChoiceOption("4", "≥12.0 (>204 µmol/L)", "", "+4", 4)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "cardio",
                label = "Cardiovascular (MAP / Vasopressors)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "MAP ≥70 mmHg", "No vasopressors", "0", 0),
                    ChoiceOption("1", "MAP <70 mmHg", "Hypotension", "+1", 1),
                    ChoiceOption("2", "Dopamine ≤5 or Dobutamine (any)", "Low-dose inotrope/pressor", "+2", 2),
                    ChoiceOption("3", "Dopa >5 or Epi/Norepi ≤0.1", "mcg/kg/min", "+3", 3),
                    ChoiceOption("4", "Dopa >15 or Epi/Norepi >0.1", "High-dose vasopressor", "+4", 4)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "cns",
                label = "CNS (Glasgow Coma Scale)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "GCS 15", "", "0", 0),
                    ChoiceOption("1", "GCS 13–14", "", "+1", 1),
                    ChoiceOption("2", "GCS 10–12", "", "+2", 2),
                    ChoiceOption("3", "GCS 6–9", "", "+3", 3),
                    ChoiceOption("4", "GCS <6", "", "+4", 4)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "renal",
                label = "Renal (SCr mg/dL or Urine Output)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "SCr <1.2", "", "0", 0),
                    ChoiceOption("1", "SCr 1.2–1.9", "", "+1", 1),
                    ChoiceOption("2", "SCr 2.0–3.4", "", "+2", 2),
                    ChoiceOption("3", "SCr 3.5–4.9 or UO <500 mL/d", "", "+3", 3),
                    ChoiceOption("4", "SCr ≥5.0 or UO <200 mL/d", "", "+4", 4)
                )
            )
        )
    ),
    CalculatorDef(
        id = "icu_rox",
        departmentId = DepartmentId.ICU,
        name = "ROX Index (HFNC Failure)",
        shortSubtitle = "High-flow nasal cannula intubation risk",
        synonyms = listOf("rox", "hfnc", "optiflow", "high flow", "intubation", "hypoxemia"),
        formulaSummary = "ROX Index = (SpO₂ % / (FiO₂ % / 100)) / Respiratory Rate\n≥4.88 Low Intubation Risk | 3.85–4.87 Close Reassessment | <3.85 High Risk HFNC Failure",
        clinicalPearl = "Validate at 2, 6, and 12 hours after starting HFNC. A falling ROX trend or ROX <3.85 at 12 hours strongly predicts need for endotracheal intubation—avoid delayed intubation.",
        inputs = listOf(
            CalcInputDef.NumericField("spo2", "Oxygen Saturation (SpO₂)", "50–100%", listOf("%")),
            CalcInputDef.NumericField("fio2", "Inspired FiO₂ (%)", "21–100%", listOf("%")),
            CalcInputDef.NumericField("rr", "Respiratory Rate", "breaths/min", listOf("/min"))
        )
    ),
    CalculatorDef(
        id = "icu_vasopressor",
        departmentId = DepartmentId.ICU,
        name = "Vasopressor Infusion Calculator",
        shortSubtitle = "Dose ↔ pump mL/hr for ICU pressors",
        synonyms = listOf("vasopressor", "norepinephrine", "noradrenaline", "epinephrine", "dopamine", "dobutamine", "infusion", "pump rate", "mcg/kg/min"),
        formulaSummary = "Conc (mcg/mL) = (Drug mg × 1000) / Volume mL\nIf mcg/kg/min: Rate (mL/hr) = (Dose × Weight kg × 60) / Conc\nIf mcg/min: Rate (mL/hr) = (Dose × 60) / Conc",
        clinicalPearl = "Always confirm institutional standard bag concentration (e.g., 4 mg/250 mL = 16 mcg/mL vs double-strength 8 mg/250 mL) and whether your ICU doses norepinephrine in mcg/kg/min or flat mcg/min.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "mode",
                label = "Calculation Direction",
                options = listOf(
                    ChoiceOption("dose_to_rate", "Dose → mL/hr"),
                    ChoiceOption("rate_to_dose", "mL/hr → Dose")
                ),
                defaultOptionId = "dose_to_rate"
            ),
            CalcInputDef.SegmentedChoice(
                id = "preset",
                label = "Drug & Bag Concentration",
                compactRow = false,
                options = listOf(
                    ChoiceOption("norepi", "Norepinephrine", "4 mg in 250 mL (16 mcg/mL)"),
                    ChoiceOption("epi", "Epinephrine", "4 mg in 250 mL (16 mcg/mL)"),
                    ChoiceOption("dopa", "Dopamine", "400 mg in 250 mL (1,600 mcg/mL)"),
                    ChoiceOption("dobut", "Dobutamine", "250 mg in 250 mL (1,000 mcg/mL)"),
                    ChoiceOption("custom", "Custom Concentration", "Enter mg and mL below")
                ),
                defaultOptionId = "norepi"
            ),
            CalcInputDef.NumericField(
                id = "custom_mg",
                label = "Drug Amount (mg)",
                hint = "mg",
                units = listOf("mg"),
                visibleWhenChoiceKey = "preset",
                visibleWhenChoiceValue = "custom"
            ),
            CalcInputDef.NumericField(
                id = "custom_ml",
                label = "Total Bag Volume (mL)",
                hint = "mL",
                units = listOf("mL"),
                visibleWhenChoiceKey = "preset",
                visibleWhenChoiceValue = "custom"
            ),
            CalcInputDef.SegmentedChoice(
                id = "dose_unit",
                label = "Dosing Units",
                options = listOf(
                    ChoiceOption("mcg_kg_min", "mcg/kg/min"),
                    ChoiceOption("mcg_min", "mcg/min")
                ),
                defaultOptionId = "mcg_kg_min"
            ),
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs"),
                visibleWhenChoiceKey = "dose_unit",
                visibleWhenChoiceValue = "mcg_kg_min"
            ),
            CalcInputDef.NumericField(
                id = "target_dose",
                label = "Target Dose",
                hint = "Dose",
                units = listOf("mcg/kg/min or mcg/min"),
                visibleWhenChoiceKey = "mode",
                visibleWhenChoiceValue = "dose_to_rate"
            ),
            CalcInputDef.NumericField(
                id = "pump_rate",
                label = "Current Pump Rate",
                hint = "mL/hr",
                units = listOf("mL/hr"),
                visibleWhenChoiceKey = "mode",
                visibleWhenChoiceValue = "rate_to_dose"
            )
        )
    )
)
