package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val EmergencyPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "em_caprini",
        departmentId = DepartmentId.EMERGENCY,
        name = "Caprini VTE Risk Score",
        shortSubtitle = "Surgical VTE prophylaxis stratification",
        synonyms = listOf("caprini", "vte", "dvt", "pe", "prophylaxis", "enoxaparin", "heparin", "surgery"),
        formulaSummary = "Weighted sum of +1, +2, +3, and +5 surgical VTE risk factors (Age brackets mutually exclusive).\n0 Lowest | 1–2 Low | 3–4 Moderate | ≥5 High Risk (Pharmacologic + Mechanical Prophylaxis)",
        clinicalPearl = "Widely endorsed by ACCP guidelines for non-orthopedic surgical patients. Patients with Caprini ≥5 benefit from LMWH/UFH plus IPC; extended 4-week prophylaxis is considered in major abdominal/pelvic cancer surgery.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.SectionHeader("cap_h1", "1-Point Risk Factors (+1 each)"),
            CalcInputDef.ToggleCriterion("age_41_60", "Age 41–60 Years", "Mutually exclusive age bracket", "+1", 1, exclusiveGroupKey = "caprini_age"),
            CalcInputDef.ToggleCriterion("minor_surg", "Minor Surgery Planned", "Procedure <45 minutes", "+1", 1),
            CalcInputDef.ToggleCriterion("bmi_25", "BMI >25 kg/m²", "Overweight or obese", "+1", 1),
            CalcInputDef.ToggleCriterion("swollen_legs", "Swollen Legs (Current)", "Edema of lower extremities", "+1", 1),
            CalcInputDef.ToggleCriterion("varicose", "Varicose Veins", "Visible varicosities", "+1", 1),
            CalcInputDef.ToggleCriterion("preg", "Pregnancy or Postpartum (<1 mo)", "Obstetric hypercoagulable state", "+1", 1),
            CalcInputDef.ToggleCriterion("ocp_hrt", "OCP or Hormone Replacement", "Estrogen-containing therapy", "+1", 1),
            CalcInputDef.ToggleCriterion("sepsis", "Sepsis (<1 month)", "Recent systemic infection", "+1", 1),
            CalcInputDef.ToggleCriterion("lung", "Serious Lung Disease / Pneumonia (<1 mo)", "Includes COPD or acute pneumonia", "+1", 1),
            CalcInputDef.ToggleCriterion("mi_chf", "Acute MI or CHF (<1 month)", "Recent myocardial infarction or heart failure", "+1", 1),
            CalcInputDef.ToggleCriterion("med_bed", "Medical Patient on Bed Rest", "Currently immobilized medical admission", "+1", 1),
            CalcInputDef.ToggleCriterion("ibd", "History of Inflammatory Bowel Disease", "Crohn's or ulcerative colitis", "+1", 1),

            CalcInputDef.SectionHeader("cap_h2", "2-Point Risk Factors (+2 each)"),
            CalcInputDef.ToggleCriterion("age_61_74", "Age 61–74 Years", "Mutually exclusive age bracket", "+2", 2, exclusiveGroupKey = "caprini_age"),
            CalcInputDef.ToggleCriterion("major_surg", "Major Open / Laparoscopic Surgery", "Procedure >45 minutes", "+2", 2),
            CalcInputDef.ToggleCriterion("malignancy", "Present or Previous Malignancy", "Active or history of cancer", "+2", 2),
            CalcInputDef.ToggleCriterion("bed_72", "Confined to Bed >72 Hours", "Prolonged immobility", "+2", 2),
            CalcInputDef.ToggleCriterion("cast", "Immobilizing Plaster Cast (<1 mo)", "Lower limb cast immobilization", "+2", 2),
            CalcInputDef.ToggleCriterion("cvc", "Central Venous Access", "CVC, PICC, or port in place", "+2", 2),

            CalcInputDef.SectionHeader("cap_h3", "3-Point Risk Factors (+3 each)"),
            CalcInputDef.ToggleCriterion("age_75", "Age ≥75 Years", "Mutually exclusive age bracket", "+3", 3, exclusiveGroupKey = "caprini_age"),
            CalcInputDef.ToggleCriterion("hx_vte", "Personal History of DVT / PE", "Prior thromboembolism", "+3", 3),
            CalcInputDef.ToggleCriterion("fhx_vte", "Family History of VTE", "First-degree relative with VTE", "+3", 3),
            CalcInputDef.ToggleCriterion("thrombophilia", "Known Thrombophilia", "Factor V Leiden, Prothrombin 20210A, Lupus/APS, etc.", "+3", 3),

            CalcInputDef.SectionHeader("cap_h5", "5-Point Risk Factors (+5 each)"),
            CalcInputDef.ToggleCriterion("stroke_1m", "Stroke (<1 month)", "Acute ischemic or hemorrhagic stroke", "+5", 5),
            CalcInputDef.ToggleCriterion("arthroplasty", "Elective Major Arthroplasty", "Hip or knee replacement", "+5", 5),
            CalcInputDef.ToggleCriterion("fracture", "Hip, Pelvis, or Leg Fracture (<1 mo)", "Major lower extremity trauma", "+5", 5),
            CalcInputDef.ToggleCriterion("sci", "Acute Spinal Cord Injury (<1 mo)", "Paralysis / acute SCI", "+5", 5)
        )
    ),
    CalculatorDef(
        id = "em_mme",
        departmentId = DepartmentId.EMERGENCY,
        name = "Morphine Milligram Equivalents",
        shortSubtitle = "CDC daily opioid MME overdose screen",
        synonyms = listOf("mme", "opioid", "morphine", "oxycodone", "fentanyl", "hydromorphone", "tramadol", "naloxone"),
        formulaSummary = "Daily MME = 24-hr Dose × CDC Factor:\nMorphine 1.0 | Codeine 0.15 | Tramadol 0.2 | Hydrocodone 1.0 | Oxycodone 1.5 | Hydromorphone 5.0 | Tapentadol 0.4 | Fentanyl Patch (mcg/hr) × 2.4\n<50 Standard | 50–89 Caution/Naloxone | ≥90 High Overdose Risk",
        clinicalPearl = "CDC MME factors estimate overdose risk ONLY—do NOT use directly for equianalgesic opioid rotation without reducing the calculated new opioid dose by 25–50% for incomplete cross-tolerance.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "opioid",
                label = "Opioid Medication",
                compactRow = false,
                options = listOf(
                    ChoiceOption("morphine", "Morphine (Oral)", "Factor × 1.0"),
                    ChoiceOption("codeine", "Codeine", "Factor × 0.15"),
                    ChoiceOption("tramadol", "Tramadol", "Factor × 0.2"),
                    ChoiceOption("hydrocodone", "Hydrocodone", "Factor × 1.0"),
                    ChoiceOption("oxycodone", "Oxycodone", "Factor × 1.5"),
                    ChoiceOption("hydromorphone", "Hydromorphone (Oral)", "Factor × 5.0"),
                    ChoiceOption("tapentadol", "Tapentadol", "Factor × 0.4"),
                    ChoiceOption("fentanyl_patch", "Fentanyl Transdermal Patch", "mcg/hr × 2.4")
                ),
                defaultOptionId = "morphine"
            ),
            CalcInputDef.NumericField(
                id = "daily_dose",
                label = "24-Hour Total Dose (mg/day)",
                hint = "mg / 24h (or mcg/hr if Fentanyl)",
                units = listOf("mg/day", "mcg/hr")
            )
        )
    ),
    CalculatorDef(
        id = "em_nexus",
        departmentId = DepartmentId.EMERGENCY,
        name = "NEXUS C-Spine Criteria",
        shortSubtitle = "Cervical spine trauma imaging rule-out",
        synonyms = listOf("nexus", "c-spine", "cervical spine", "trauma", "collar", "ct neck"),
        formulaSummary = "5 Criteria: Midline tenderness, Focal neuro deficit, Altered alertness, Intoxication, Distracting injury.\nAll 5 Absent → Low Risk (No imaging by NEXUS) | Any ≥1 Present → C-Spine Imaging Indicated",
        clinicalPearl = "NEXUS has ~99% sensitivity for clinically significant C-spine injury. Use caution in patients >65 years where the Canadian C-Spine Rule may offer higher specificity.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("midline", "Posterior Midline C-Spine Tenderness", "Palpation tenderness along spinous processes C1–T1", "High Risk", 1),
            CalcInputDef.ToggleCriterion("neuro", "Focal Neurologic Deficit", "Any motor or sensory deficit on exam", "High Risk", 1),
            CalcInputDef.ToggleCriterion("alertness", "Altered Level of Alertness", "GCS <15, disorientation, or delayed response", "High Risk", 1),
            CalcInputDef.ToggleCriterion("intox", "Evidence of Intoxication", "Alcohol, sedatives, or illicit substance impairment", "High Risk", 1),
            CalcInputDef.ToggleCriterion("distracting", "Painful Distracting Injury", "Long-bone fracture, visceral injury, large burn, etc.", "High Risk", 1)
        )
    ),
    CalculatorDef(
        id = "em_paracetamol",
        departmentId = DepartmentId.EMERGENCY,
        name = "Paracetamol Overdose",
        shortSubtitle = "Rumack-Matthew 150-line NAC nomogram",
        synonyms = listOf("acetaminophen", "paracetamol", "tylenol", "rumack", "matthew", "nac", "overdose", "toxicology"),
        formulaSummary = "Dose/kg = Ingested mg / Weight kg (Toxic ≥150 mg/kg or ≥10 g)\nRumack-Matthew 150-line (4–24h): 150 × 2^(−(t − 4) / 4) mcg/mL\nUK 100-line: 100 × 2^(−(t − 4) / 4) mcg/mL  [1 mcg/mL = 6.614 µmol/L]",
        clinicalPearl = "Nomogram applies ONLY to acute single ingestions with known time between 4 and 24 hours post-ingestion. Do NOT use for staggered, repeated supratherapeutic, or unknown-time ingestions—start NAC empirically.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            ),
            CalcInputDef.NumericField(
                id = "dose",
                label = "Acute Single Ingested Dose",
                hint = "Dose",
                units = listOf("g", "mg")
            ),
            CalcInputDef.NumericField(
                id = "hours",
                label = "Hours Post-Ingestion (4–24 hrs)",
                hint = "Hours",
                units = listOf("hrs")
            ),
            CalcInputDef.NumericField(
                id = "level",
                label = "Serum Paracetamol Level",
                hint = "Serum level",
                units = listOf("mcg/mL", "µmol/L")
            )
        )
    )
)
