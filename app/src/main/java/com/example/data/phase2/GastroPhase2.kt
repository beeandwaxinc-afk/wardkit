package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val GastroPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "gastro_fib4",
        departmentId = DepartmentId.GASTRO,
        name = "FIB-4 Fibrosis Index",
        shortSubtitle = "Non-invasive liver fibrosis screening",
        synonyms = listOf("fib4", "masld", "nafld", "fatty liver", "cirrhosis", "fibrosis", "hepatitis"),
        formulaSummary = "FIB-4 = (Age × AST) / (Platelets [10⁹/L] × √ALT)\n<1.30 Low Risk (<2.0 if Age ≥65) | 1.30–2.67 Indeterminate | >2.67 High Risk (>3.25 HCV cutoff)",
        clinicalPearl = "First-line screening tool for MASLD/NAFLD fibrosis. Indeterminate (1.30–2.67) scores warrant secondary elastography (FibroScan). Less reliable in patients <35 years.",
        inputs = listOf(
            CalcInputDef.NumericField("age", "Age", "Years", listOf("yrs")),
            CalcInputDef.NumericField("ast", "AST", "U/L", listOf("U/L")),
            CalcInputDef.NumericField("alt", "ALT", "U/L", listOf("U/L")),
            CalcInputDef.NumericField("plt", "Platelets", "×10⁹/L (e.g., 180)", listOf("10⁹/L"))
        )
    ),
    CalculatorDef(
        id = "gastro_gbs",
        departmentId = DepartmentId.GASTRO,
        name = "Glasgow-Blatchford (UGIB)",
        shortSubtitle = "Upper GI bleed intervention & discharge",
        synonyms = listOf("gbs", "blatchford", "ugib", "gi bleed", "melena", "hematemesis", "endoscopy"),
        formulaSummary = "Urea/BUN (0–6) + Sex-specific Hb (0–6) + SBP (0–3) + HR ≥100 (+1) + Melena (+1) + Syncope (+2) + Liver (+2) + Heart Failure (+2) = 0–23.\n0: Very Low Risk (Outpatient) | 1–5: Inpatient | ≥6: High Risk Urgent Endoscopy",
        clinicalPearl = "GBS = 0 identifies very-low-risk UGIB patients (<1% need for transfusion or endoscopic intervention) who can safely be managed with urgent outpatient endoscopy.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "sex",
                label = "Biological Sex (for Hb Thresholds)",
                options = listOf(
                    ChoiceOption("male", "Male"),
                    ChoiceOption("female", "Female")
                ),
                defaultOptionId = "male"
            ),
            CalcInputDef.SegmentedChoice(
                id = "urea",
                label = "Blood Urea (mmol/L) / BUN (mg/dL)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "<6.5 mmol/L (<18.2 BUN)", "", "0", 0),
                    ChoiceOption("2", "6.5–7.9 mmol/L (18.2–22.3)", "", "+2", 2),
                    ChoiceOption("3", "8.0–9.9 mmol/L (22.4–27.9)", "", "+3", 3),
                    ChoiceOption("4", "10.0–24.9 mmol/L (28–69.9)", "", "+4", 4),
                    ChoiceOption("6", "≥25.0 mmol/L (≥70 BUN)", "", "+6", 6)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "hb_male",
                label = "Hemoglobin — Male (g/dL)",
                compactRow = false,
                visibleWhenChoiceKey = "sex",
                visibleWhenChoiceValue = "male",
                options = listOf(
                    ChoiceOption("0", "≥13.0 g/dL", "", "0", 0),
                    ChoiceOption("1", "12.0–12.9 g/dL", "", "+1", 1),
                    ChoiceOption("3", "10.0–11.9 g/dL", "", "+3", 3),
                    ChoiceOption("6", "<10.0 g/dL", "", "+6", 6)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "hb_female",
                label = "Hemoglobin — Female (g/dL)",
                compactRow = false,
                visibleWhenChoiceKey = "sex",
                visibleWhenChoiceValue = "female",
                options = listOf(
                    ChoiceOption("0", "≥12.0 g/dL", "", "0", 0),
                    ChoiceOption("1", "10.0–11.9 g/dL", "", "+1", 1),
                    ChoiceOption("6", "<10.0 g/dL", "", "+6", 6)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "sbp",
                label = "Systolic Blood Pressure",
                options = listOf(
                    ChoiceOption("0", "≥110", "", "0", 0),
                    ChoiceOption("1", "100–109", "", "+1", 1),
                    ChoiceOption("2", "90–99", "", "+2", 2),
                    ChoiceOption("3", "<90", "", "+3", 3)
                )
            ),
            CalcInputDef.SectionHeader("gbs_other", "Clinical Features"),
            CalcInputDef.ToggleCriterion("hr100", "Pulse ≥100 bpm", "Resting tachycardia", "+1", 1),
            CalcInputDef.ToggleCriterion("melena", "Presentation with Melena", "Black tarry stools", "+1", 1),
            CalcInputDef.ToggleCriterion("syncope", "Presentation with Syncope", "Transient loss of consciousness", "+2", 2),
            CalcInputDef.ToggleCriterion("hepatic", "Hepatic Disease", "Known history or clinical/lab evidence", "+2", 2),
            CalcInputDef.ToggleCriterion("cardiac", "Cardiac Failure", "Known history or clinical/echo evidence", "+2", 2)
        )
    ),
    CalculatorDef(
        id = "gastro_lille",
        departmentId = DepartmentId.GASTRO,
        name = "Lille Model (Alc. Hepatitis)",
        shortSubtitle = "Day-7 steroid response in severe AH",
        synonyms = listOf("lille", "alcoholic hepatitis", "steroids", "prednisolone", "liver"),
        formulaSummary = "R = 3.19 − 0.101·Age + 0.147·Alb(g/L) + 0.0165·(Bili0 − Bili7 µmol/L) − 0.206·Renal − 0.0065·Bili0 − 0.0096·PT\nLille = exp(−R) / (1 + exp(−R))\n<0.45 Steroid Responder (Continue) | ≥0.45 Non-Responder (Stop Steroids)",
        clinicalPearl = "Calculated at Day 7 of corticosteroid therapy in severe alcoholic hepatitis (Maddrey ≥32). Stopping steroids when Lille ≥0.45 avoids futile infection risk.",
        inputs = listOf(
            CalcInputDef.NumericField("age", "Age", "Years", listOf("yrs")),
            CalcInputDef.NumericField("albumin", "Baseline Albumin", "Albumin", listOf("g/L", "g/dL")),
            CalcInputDef.NumericField("bili0", "Day 0 Bilirubin", "Baseline Bili", listOf("µmol/L", "mg/dL")),
            CalcInputDef.NumericField("bili7", "Day 7 Bilirubin", "Day 7 Bili", listOf("µmol/L", "mg/dL")),
            CalcInputDef.NumericField("pt", "Baseline PT", "seconds", listOf("sec")),
            CalcInputDef.ToggleCriterion("renal", "Renal Insufficiency", "SCr >1.3 mg/dL (>115 µmol/L)", "Renal=1", 1)
        )
    ),
    CalculatorDef(
        id = "gastro_rockall",
        departmentId = DepartmentId.GASTRO,
        name = "Rockall Score (UGIB)",
        shortSubtitle = "Pre- & post-endoscopy UGIB mortality",
        synonyms = listOf("rockall", "ugib", "gi bleed", "rebleeding", "endoscopy", "ulcer"),
        formulaSummary = "Pre-Endoscopy (0–7): Age (0–2) + Shock (0–2) + Comorbidity (0–3)\nOptional Post-Endoscopy (+0–4): Diagnosis (0–2) + Major Stigmata (0–2) → Full Score (0–11)",
        clinicalPearl = "Pre-endoscopy Rockall predicts mortality on admission; Full Rockall incorporates endoscopic diagnosis & bleeding stigmata to predict rebleeding and guide ICU vs ward disposition.",
        inputs = listOf(
            CalcInputDef.SectionHeader("rock_pre", "Pre-Endoscopy Criteria (Required)"),
            CalcInputDef.SegmentedChoice(
                id = "age",
                label = "Age",
                options = listOf(
                    ChoiceOption("0", "<60 yrs", "", "0", 0),
                    ChoiceOption("1", "60–79 yrs", "", "+1", 1),
                    ChoiceOption("2", "≥80 yrs", "", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "shock",
                label = "Shock / Hemodynamics",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "No Shock", "HR <100 & SBP ≥100 mmHg", "0", 0),
                    ChoiceOption("1", "Tachycardia", "HR ≥100 & SBP ≥100 mmHg", "+1", 1),
                    ChoiceOption("2", "Hypotension", "SBP <100 mmHg", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "comorb",
                label = "Comorbidity",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "No Major Comorbidity", "", "0", 0),
                    ChoiceOption("2", "CHF / IHD / Major Comorbidity", "Cardiac or major systemic illness", "+2", 2),
                    ChoiceOption("3", "Renal / Liver Failure / Metastatic CA", "End-organ failure or disseminated malignancy", "+3", 3)
                )
            ),
            CalcInputDef.SectionHeader("rock_post", "Post-Endoscopy Findings (Optional)"),
            CalcInputDef.SegmentedChoice(
                id = "diagnosis",
                label = "Endoscopic Diagnosis",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Mallory-Weiss / No Lesion", "No lesion & no stigmata", "0", 0),
                    ChoiceOption("1", "All Other Diagnoses", "Peptic ulcer, erosions, esophagitis, varices", "+1", 1),
                    ChoiceOption("2", "Upper GI Malignancy", "Confirmed or suspected GI cancer", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "stigmata",
                label = "Major Stigmata of Recent Hemorrhage",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "None or Dark Spot Only", "Clean base or flat pigmented spot", "0", 0),
                    ChoiceOption("2", "Blood / Adherent Clot / Visible Vessel", "Active bleeding or spurting vessel", "+2", 2)
                )
            )
        )
    )
)
