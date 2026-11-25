package com.example.data

internal val CalculatorsPart1: List<CalculatorDef> = listOf(
    // =========================================================================
    // 1. PEDIATRICS (PEADS)
    // =========================================================================
    CalculatorDef(
        id = "peads_holliday_segar",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Holliday-Segar Fluids",
        shortSubtitle = "4-2-1 maintenance IV fluid rate",
        synonyms = listOf("4-2-1", "maintenance fluids", "pediatric iv", "daily fluid", "peads"),
        formulaSummary = "1st 10 kg: 4 mL/kg/hr (100 mL/kg/day)\nNext 10 kg: 2 mL/kg/hr (50 mL/kg/day)\nEach kg >20 kg: 1 mL/kg/hr (20 mL/kg/day)",
        clinicalPearl = "Use isotonic fluid (0.9% NaCl or balanced crystalloid with dextrose) for routine pediatric maintenance to prevent hospital-acquired hyponatremia. Cap routine adult-sized maintenance at ~2,400–2,500 mL/day unless high losses exist.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            )
        )
    ),
    CalculatorDef(
        id = "peads_drug_dosing",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Weight-Based Drug & Syrup",
        shortSubtitle = "Dose per administration in mL & mg",
        synonyms = listOf("syrup", "suspension", "mg/kg/day", "pediatric dose", "antibiotic", "paracetamol", "amoxicillin"),
        formulaSummary = "Daily Dose (mg) = Weight (kg) × Dose (mg/kg/day)\nSingle Dose (mg) = Daily Dose / Frequency\nVolume per Dose (mL) = Single Dose × (Conc mL / Conc mg)",
        clinicalPearl = "Always verify the total daily dose never exceeds the maximum adult ceiling dose. Double-check suspension strength (e.g., 125 mg/5 mL vs 250 mg/5 mL) before dispensing.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            ),
            CalcInputDef.NumericField(
                id = "dose_per_kg",
                label = "Daily Dose",
                hint = "mg/kg/day",
                units = listOf("mg/kg/day")
            ),
            CalcInputDef.SegmentedChoice(
                id = "frequency",
                label = "Dosing Frequency",
                options = listOf(
                    ChoiceOption("1", "OD", "1×/day"),
                    ChoiceOption("2", "BD", "2×/day"),
                    ChoiceOption("3", "TDS", "3×/day"),
                    ChoiceOption("4", "QID", "4×/day")
                ),
                defaultOptionId = "2"
            ),
            CalcInputDef.NumericField(
                id = "conc_mg",
                label = "Syrup Concentration (mg)",
                hint = "e.g., 125 or 250",
                units = listOf("mg")
            ),
            CalcInputDef.NumericField(
                id = "conc_ml",
                label = "Per Volume (mL)",
                hint = "e.g., 5",
                units = listOf("mL")
            )
        )
    ),
    CalculatorDef(
        id = "peads_ett",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Pediatric ETT Size & Depth",
        shortSubtitle = "Cuffed, uncuffed ID & insertion depth",
        synonyms = listOf("endotracheal tube", "intubation", "airway", "cuffed", "uncuffed", "depth"),
        formulaSummary = "Uncuffed ID = (Age / 4) + 4.0 mm\nCuffed ID = (Age / 4) + 3.5 mm\nOral Depth = 3 × ETT ID cm  |  Age formula: (Age / 2) + 12 cm",
        clinicalPearl = "Modern PALS guidelines favor cuffed ETTs in infants and children (monitor cuff pressure <20–25 cmH₂O). Always have 0.5 mm smaller and larger tubes immediately ready at the bedside.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "age_years",
                label = "Child Age (1–16 yrs)",
                hint = "Years",
                units = listOf("yrs")
            )
        )
    ),
    CalculatorDef(
        id = "peads_apgar",
        departmentId = DepartmentId.PEDIATRICS,
        name = "APGAR Score",
        shortSubtitle = "Newborn 1 & 5 min vitality assessment",
        synonyms = listOf("newborn", "neonate", "delivery", "resuscitation", "appearance pulse grimace"),
        formulaSummary = "Sum of 5 domains (0–2 each) = 0–10:\n7–10 Reassuring | 4–6 Moderately Abnormal | 0–3 Immediate Resuscitation",
        clinicalPearl = "Do NOT delay neonatal resuscitation waiting for the 1-minute APGAR score. Repeat every 5 minutes up to 20 minutes if the 5-minute score is <7.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "appearance",
                label = "Appearance (Skin Color)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Blue / Pale", "Central cyanosis", "0", 0),
                    ChoiceOption("1", "Acrocyanosis", "Pink body, blue extremities", "+1", 1),
                    ChoiceOption("2", "Completely Pink", "Pink all over", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "pulse",
                label = "Pulse (Heart Rate)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Absent", "No pulse", "0", 0),
                    ChoiceOption("1", "<100 bpm", "Slow heart rate", "+1", 1),
                    ChoiceOption("2", "≥100 bpm", "Vigorous rate", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "grimace",
                label = "Grimace (Reflex Irritability)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "No Response", "Flaccid to stimulation", "0", 0),
                    ChoiceOption("1", "Grimace", "Weak facial response", "+1", 1),
                    ChoiceOption("2", "Cry / Pull Away", "Active cough, sneeze, cry", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "activity",
                label = "Activity (Muscle Tone)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Limp", "Flaccid extremities", "0", 0),
                    ChoiceOption("1", "Some Flexion", "Weak limb movement", "+1", 1),
                    ChoiceOption("2", "Active Motion", "Well-flexed, vigorous", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "respiration",
                label = "Respiration (Breathing)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Absent", "Apneic", "0", 0),
                    ChoiceOption("1", "Slow / Irregular", "Weak cry or gasping", "+1", 1),
                    ChoiceOption("2", "Good, Crying", "Strong regular cry", "+2", 2)
                )
            )
        )
    ),

    // =========================================================================
    // 2. INTERNAL MEDICINE
    // =========================================================================
    CalculatorDef(
        id = "im_corrected_ca",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "Corrected Calcium",
        shortSubtitle = "Albumin-adjusted total serum calcium",
        synonyms = listOf("calcium", "albumin", "payne", "hypocalcemia", "hypercalcemia"),
        formulaSummary = "Corrected Ca (mg/dL) = Measured Ca + 0.8 × (4.0 − Albumin g/dL)\nConversion: 1 mmol/L Ca = 4.0 mg/dL  |  10 g/L Alb = 1.0 g/dL",
        clinicalPearl = "In critically ill, acidotic, or severely hypoalbuminemic patients, directly measure ionized calcium instead of relying on Payne's correction.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "calcium",
                label = "Serum Calcium",
                hint = "Calcium",
                units = listOf("mg/dL", "mmol/L")
            ),
            CalcInputDef.NumericField(
                id = "albumin",
                label = "Serum Albumin",
                hint = "Albumin",
                units = listOf("g/dL", "g/L")
            )
        )
    ),
    CalculatorDef(
        id = "im_anion_gap",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "Anion Gap & Winter's",
        shortSubtitle = "HAGMA & respiratory compensation check",
        synonyms = listOf("winters", "acidosis", "abg", "hagma", "paco2", "bicarbonate", "dka"),
        formulaSummary = "AG = Na − (Cl + HCO₃)  [Normal: 8–12 mEq/L]\nWinter's Expected PaCO₂ = (1.5 × HCO₃) + 8 ± 2 mmHg",
        clinicalPearl = "Always adjust AG for hypoalbuminemia (+2.5 mEq/L per 1 g/dL drop in albumin below 4.0). Winter's formula applies specifically to metabolic acidosis.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "na",
                label = "Sodium (Na⁺)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "cl",
                label = "Chloride (Cl⁻)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "hco3",
                label = "Bicarbonate (HCO₃⁻)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "paco2",
                label = "Measured PaCO₂ (Optional)",
                hint = "Optional ABG PaCO₂",
                units = listOf("mmHg"),
                optional = true
            )
        )
    ),
    CalculatorDef(
        id = "im_corrected_na",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "Corrected Sodium",
        shortSubtitle = "Hyperglycemia-adjusted Na (Katz / Hillier)",
        synonyms = listOf("sodium", "katz", "hillier", "dka", "hhs", "hyperglycemia", "hyponatremia"),
        formulaSummary = "Katz: Na + 0.016 × (Glucose mg/dL − 100)\nHillier (Glu >400 mg/dL): Na + 0.024 × (Glucose mg/dL − 100)",
        clinicalPearl = "Hyperglycemia draws water out of cells, causing translocational hyponatremia. When glucose exceeds 400 mg/dL, Hillier's factor (0.024) better predicts true serum sodium.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "na",
                label = "Measured Sodium (Na⁺)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "glucose",
                label = "Serum Glucose",
                hint = "Glucose",
                units = listOf("mg/dL", "mmol/L")
            )
        )
    ),
    CalculatorDef(
        id = "im_curb65",
        departmentId = DepartmentId.INTERNAL_MEDICINE,
        name = "CURB-65 Score",
        shortSubtitle = "Community-acquired pneumonia severity",
        synonyms = listOf("pneumonia", "cap", "curb", "icu", "mortality"),
        formulaSummary = "1 point each: Confusion, Urea >7 mmol/L (BUN >19), RR ≥30, BP <90/60, Age ≥65.\n0–1: Outpatient | 2: Inpatient | 3–5: ICU Evaluation",
        clinicalPearl = "CURB-65 complements but never replaces clinical assessment of hypoxia, multilobar infiltrates, social support, or ability to tolerate oral therapy.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("confusion", "Confusion", "New disorientation to person, place, or time (AMT ≤8)", "+1", 1),
            CalcInputDef.ToggleCriterion("urea", "Urea >7 mmol/L (BUN >19 mg/dL)", "Elevated blood urea nitrogen", "+1", 1),
            CalcInputDef.ToggleCriterion("rr", "Respiratory Rate ≥30 /min", "Tachypnea on presentation", "+1", 1),
            CalcInputDef.ToggleCriterion("bp", "Blood Pressure <90/60 mmHg", "SBP <90 mmHg or DBP ≤60 mmHg", "+1", 1),
            CalcInputDef.ToggleCriterion("age", "Age ≥65 Years", "Older adult risk factor", "+1", 1)
        )
    ),

    // =========================================================================
    // 3. CARDIOLOGY
    // =========================================================================
    CalculatorDef(
        id = "cardio_qtc",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "QTc Interval",
        shortSubtitle = "Bazett & Fridericia heart-rate corrected QT",
        synonyms = listOf("bazett", "fridericia", "ecg", "ekg", "torsades", "arrhythmia", "long qt"),
        formulaSummary = "RR (sec) = 60 / HR\nBazett QTc = QT / √RR\nFridericia QTc = QT / ∛RR\n>450 ms Prolonged | >500 ms Critical Torsades Risk",
        clinicalPearl = "Bazett overcorrects at high heart rates (>100 bpm) and undercorrects in bradycardia (<60 bpm). Fridericia is more accurate at heart rate extremes.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "qt",
                label = "Measured QT Interval",
                hint = "ms",
                units = listOf("ms")
            ),
            CalcInputDef.NumericField(
                id = "hr",
                label = "Heart Rate",
                hint = "bpm",
                units = listOf("bpm")
            )
        )
    ),
    CalculatorDef(
        id = "cardio_chads2vasc",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "CHA₂DS₂-VASc Score",
        shortSubtitle = "Atrial fibrillation stroke risk & OAC",
        synonyms = listOf("afib", "atrial fibrillation", "stroke", "anticoagulation", "doac", "warfarin", "chads"),
        formulaSummary = "CHF (+1), HTN (+1), Age ≥75 (+2), Diabetes (+1), Stroke/TIA/TE (+2), Vascular (+1), Age 65–74 (+1), Female (+1).\nOAC recommended if ≥2 (Men) or ≥3 (Women).",
        clinicalPearl = "Female sex is a risk modifier rather than an independent risk factor: a score of 1 from female sex alone carries low stroke risk and does not require anticoagulation.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("chf", "CHF / LV Dysfunction", "Clinical heart failure or LVEF ≤40%", "+1", 1),
            CalcInputDef.ToggleCriterion("htn", "Hypertension", "Resting BP >140/90 or on antihypertensives", "+1", 1),
            CalcInputDef.ToggleCriterion("age75", "Age ≥75 Years", "Mutually exclusive with Age 65–74", "+2", 2, exclusiveGroupWithId = "age65"),
            CalcInputDef.ToggleCriterion("dm", "Diabetes Mellitus", "Fasting glucose >125 mg/dL or on therapy", "+1", 1),
            CalcInputDef.ToggleCriterion("stroke", "Prior Stroke / TIA / TE", "History of ischemic stroke, TIA, or thromboembolism", "+2", 2),
            CalcInputDef.ToggleCriterion("vasc", "Vascular Disease", "Prior MI, PAD, or aortic plaque", "+1", 1),
            CalcInputDef.ToggleCriterion("age65", "Age 65–74 Years", "Mutually exclusive with Age ≥75", "+1", 1, exclusiveGroupWithId = "age75"),
            CalcInputDef.ToggleCriterion("female", "Female Sex", "Sex category modifier", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "cardio_heart",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "HEART Score",
        shortSubtitle = "6-week MACE risk in chest pain",
        synonyms = listOf("chest pain", "acs", "mi", "troponin", "mace", "angina"),
        formulaSummary = "History + ECG + Age + Risk Factors + Troponin (0–2 each = 0–10).\n0–3 Low (0.9–1.7%) | 4–6 Moderate (12–16.6%) | 7–10 High (50–65%)",
        clinicalPearl = "Designed for ED chest pain without STEMI. Pair a Low HEART score (0–3) with serial high-sensitivity troponins for safe early discharge.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "history",
                label = "History",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Slightly Suspicious", "Low-risk chest pain features", "0", 0),
                    ChoiceOption("1", "Moderately Suspicious", "Mixed typical & atypical features", "+1", 1),
                    ChoiceOption("2", "Highly Suspicious", "Classic ischemic angina features", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "ecg",
                label = "ECG Findings",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Normal", "No ischemic changes", "0", 0),
                    ChoiceOption("1", "Non-Specific Repol.", "LBBB, LVH, or non-specific ST-T", "+1", 1),
                    ChoiceOption("2", "Significant ST Deviation", "ST depression/elevation not due to LBBB/LVH", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "age",
                label = "Age",
                options = listOf(
                    ChoiceOption("0", "<45 yrs", "", "0", 0),
                    ChoiceOption("1", "45–64 yrs", "", "+1", 1),
                    ChoiceOption("2", "≥65 yrs", "", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "risk",
                label = "Risk Factors",
                subtitle = "HTN, DM, hyperlipidemia, smoking, obesity, family hx, or known atherosclerosis",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "None", "0 known risk factors", "0", 0),
                    ChoiceOption("1", "1–2 Risk Factors", "Moderate risk factor burden", "+1", 1),
                    ChoiceOption("2", "≥3 or Atherosclerosis", "3+ factors or known CAD/PAD/CVA", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "troponin",
                label = "Initial Troponin",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "≤ Normal Limit", "Within lab reference range", "0", 0),
                    ChoiceOption("1", "1–3× Normal Limit", "Mildly elevated", "+1", 1),
                    ChoiceOption("2", ">3× Normal Limit", "Significantly elevated", "+2", 2)
                )
            )
        )
    ),
    CalculatorDef(
        id = "cardio_wells_dvt",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "Wells' Criteria for DVT",
        shortSubtitle = "Pre-test probability of deep vein thrombosis",
        synonyms = listOf("dvt", "deep vein thrombosis", "leg swelling", "vte", "d-dimer", "ultrasound"),
        formulaSummary = "9 clinical criteria (+1 each) + Alternative diagnosis as likely (−2).\nTwo-tier: ≤1 DVT Unlikely | ≥2 DVT Likely\nThree-tier: ≤0 Low | 1–2 Moderate | ≥3 High",
        clinicalPearl = "If DVT Unlikely (≤1), a negative high-sensitivity D-dimer safely excludes DVT. If DVT Likely (≥2), proceed directly to compression venous ultrasound.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("cancer", "Active Cancer", "Treatment ongoing, within 6 months, or palliative", "+1", 1),
            CalcInputDef.ToggleCriterion("paralysis", "Paralysis / Paresis / Cast", "Recent plaster immobilization of lower extremity", "+1", 1),
            CalcInputDef.ToggleCriterion("bedridden", "Bedridden ≥3 Days or Major Surgery", "Major surgery within 12 weeks", "+1", 1),
            CalcInputDef.ToggleCriterion("tenderness", "Deep Venous Tenderness", "Localized tenderness along deep venous system", "+1", 1),
            CalcInputDef.ToggleCriterion("entire_leg", "Entire Leg Swollen", "Diffuse swelling of whole lower limb", "+1", 1),
            CalcInputDef.ToggleCriterion("calf", "Calf Swelling >3 cm", "Measured 10 cm below tibial tuberosity vs other leg", "+1", 1),
            CalcInputDef.ToggleCriterion("pitting", "Pitting Edema", "Confined to the symptomatic leg", "+1", 1),
            CalcInputDef.ToggleCriterion("collateral", "Collateral Superficial Veins", "Non-varicose collateral veins", "+1", 1),
            CalcInputDef.ToggleCriterion("prev_dvt", "Previously Documented DVT", "Prior confirmed deep vein thrombosis", "+1", 1),
            CalcInputDef.ToggleCriterion("alt_dx", "Alternative Diagnosis ≥ as Likely", "Cellulitis, Baker's cyst, muscle strain, etc.", "−2", -2)
        )
    ),

    // =========================================================================
    // 4. GENERAL SURGERY
    // =========================================================================
    CalculatorDef(
        id = "surg_parkland",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Parkland Formula",
        shortSubtitle = "24-hour burn resuscitation LR volume",
        synonyms = listOf("burns", "tbsa", "ringer lactate", "fluid resuscitation", "baxter"),
        formulaSummary = "24-hr LR Total = 4 mL × Weight (kg) × %TBSA\nGive 50% over First 8 hours (from time of burn!)\nGive remaining 50% over Next 16 hours",
        clinicalPearl = "The first 8 hours is timed from the MOMENT OF INJURY, not hospital arrival. Titrate hourly fluid rate to urine output (0.5 mL/kg/hr adults; 1 mL/kg/hr children).",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            ),
            CalcInputDef.NumericField(
                id = "tbsa",
                label = "Burn Area (%TBSA)",
                hint = "1–100%",
                units = listOf("%")
            )
        )
    ),
    CalculatorDef(
        id = "surg_alvarado",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Alvarado Score",
        shortSubtitle = "MANTRELS acute appendicitis risk",
        synonyms = listOf("appendicitis", "mantrels", "rlq pain", "rif pain", "abdomen"),
        formulaSummary = "MANTRELS (0–10): RLQ tenderness (+2) & Leukocytosis (+2); 6 others (+1 each).\n1–4 Unlikely | 5–6 Observe/Imaging | 7–8 Probable | 9–10 Surgery",
        clinicalPearl = "Alvarado score has higher specificity in men; in women of childbearing age, pair with pregnancy test and pelvic ultrasound/CT to rule out gynecologic mimics.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("migration", "Migration of Pain to RLQ", "Periumbilical pain shifting to right lower quadrant", "+1", 1),
            CalcInputDef.ToggleCriterion("anorexia", "Anorexia", "Loss of appetite", "+1", 1),
            CalcInputDef.ToggleCriterion("nausea", "Nausea / Vomiting", "Associated GI symptoms", "+1", 1),
            CalcInputDef.ToggleCriterion("rlq", "RLQ Tenderness", "Tenderness in right iliac fossa", "+2", 2),
            CalcInputDef.ToggleCriterion("rebound", "Rebound Tenderness", "Peritoneal irritation sign", "+1", 1),
            CalcInputDef.ToggleCriterion("temp", "Elevated Temperature >37.3°C", "Fever (>99.1°F)", "+1", 1),
            CalcInputDef.ToggleCriterion("wbc", "Leukocytosis (>10,000 /µL)", "Elevated white blood cell count", "+2", 2),
            CalcInputDef.ToggleCriterion("shift", "Left Shift (Neutrophilia >75%)", "Elevated segmented neutrophils", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "surg_shock_index",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Shock Index",
        shortSubtitle = "Occult hemorrhage & trauma triage ratio",
        synonyms = listOf("trauma", "hemorrhage", "bleeding", "hr/sbp", "hypovolemia"),
        formulaSummary = "Shock Index = Heart Rate (bpm) / Systolic BP (mmHg)\n0.5–0.7 Normal | 0.8–0.99 Elevated | ≥1.0 Hemodynamically Unstable",
        clinicalPearl = "Shock Index detects occult blood loss earlier than hypotension alone. Note that beta-blockers or pacemakers can mask compensatory tachycardia.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "hr",
                label = "Heart Rate",
                hint = "bpm",
                units = listOf("bpm")
            ),
            CalcInputDef.NumericField(
                id = "sbp",
                label = "Systolic BP",
                hint = "mmHg",
                units = listOf("mmHg")
            )
        )
    ),
    CalculatorDef(
        id = "surg_iv_drip",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "IV Drip Rate",
        shortSubtitle = "Gravity infusion drops/min & mL/hr",
        synonyms = listOf("infusion", "drops per minute", "gtt/min", "macrodrip", "microdrip", "iv fluid"),
        formulaSummary = "Drip Rate (gtt/min) = (Volume mL × Drop Factor) / Duration (min)\nPump Rate (mL/hr) = Volume mL / Duration (hr)",
        clinicalPearl = "Standard macrodrip sets deliver 10, 15, or 20 gtt/mL; microdrip (burette/pediatric) sets deliver 60 gtt/mL where gtt/min equals mL/hr.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "volume",
                label = "Total Volume",
                hint = "mL",
                units = listOf("mL")
            ),
            CalcInputDef.NumericField(
                id = "duration",
                label = "Infusion Duration",
                hint = "Time",
                units = listOf("Hours", "Minutes")
            ),
            CalcInputDef.SegmentedChoice(
                id = "drop_factor",
                label = "Drop Factor (gtt/mL)",
                options = listOf(
                    ChoiceOption("10", "10", "Macro"),
                    ChoiceOption("15", "15", "Macro"),
                    ChoiceOption("20", "20", "Macro"),
                    ChoiceOption("60", "60", "Micro")
                ),
                defaultOptionId = "20"
            )
        )
    ),

    // =========================================================================
    // 5. OBS & GYNAE
    // =========================================================================
    CalculatorDef(
        id = "obgyn_naegele",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Naegele's Rule (EDD)",
        shortSubtitle = "Due date, gestational age & trimester",
        synonyms = listOf("edd", "lmp", "pregnancy", "gestational age", "due date", "trimester"),
        formulaSummary = "EDD = LMP + 280 days + (Cycle Length − 28) days\nGestational Age = Today − LMP (adjusted for cycle offset)",
        clinicalPearl = "First-trimester crown-rump length (CRL) ultrasound is the gold standard for dating and supersedes LMP if discrepancy exceeds 5–7 days.",
        inputs = listOf(
            CalcInputDef.DateInput(
                id = "lmp",
                label = "First Day of Last Menstrual Period (LMP)",
                hint = "Select LMP date"
            ),
            CalcInputDef.NumericField(
                id = "cycle",
                label = "Cycle Length (Optional)",
                hint = "Default 28 days",
                units = listOf("days"),
                optional = true
            )
        )
    ),
    CalculatorDef(
        id = "obgyn_bishop",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Bishop Score",
        shortSubtitle = "Cervical readiness for labor induction",
        synonyms = listOf("induction", "cervix", "labor", "dilation", "effacement", "station"),
        formulaSummary = "Dilation (0–3) + Effacement (0–3) + Station (0–3) + Consistency (0–2) + Position (0–2) = 0–13.\n≤5 Unfavorable | 6–7 Intermediate | ≥8 Favorable",
        clinicalPearl = "A Bishop score ≤5 indicates an unfavorable cervix where cervical ripening (prostaglandins or Foley balloon) is recommended before oxytocin.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "dilation",
                label = "Cervical Dilation",
                options = listOf(
                    ChoiceOption("0", "Closed", "", "0", 0),
                    ChoiceOption("1", "1–2 cm", "", "+1", 1),
                    ChoiceOption("2", "3–4 cm", "", "+2", 2),
                    ChoiceOption("3", "≥5 cm", "", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "effacement",
                label = "Cervical Effacement",
                options = listOf(
                    ChoiceOption("0", "0–30%", "", "0", 0),
                    ChoiceOption("1", "40–50%", "", "+1", 1),
                    ChoiceOption("2", "60–70%", "", "+2", 2),
                    ChoiceOption("3", "≥80%", "", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "station",
                label = "Fetal Station",
                options = listOf(
                    ChoiceOption("0", "−3", "", "0", 0),
                    ChoiceOption("1", "−2", "", "+1", 1),
                    ChoiceOption("2", "−1 / 0", "", "+2", 2),
                    ChoiceOption("3", "+1 / +2", "", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "consistency",
                label = "Cervical Consistency",
                options = listOf(
                    ChoiceOption("0", "Firm", "", "0", 0),
                    ChoiceOption("1", "Medium", "", "+1", 1),
                    ChoiceOption("2", "Soft", "", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "position",
                label = "Cervical Position",
                options = listOf(
                    ChoiceOption("0", "Posterior", "", "0", 0),
                    ChoiceOption("1", "Mid", "", "+1", 1),
                    ChoiceOption("2", "Anterior", "", "+2", 2)
                )
            )
        )
    ),
    CalculatorDef(
        id = "obgyn_shock_index",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Obstetric Shock Index",
        shortSubtitle = "Postpartum hemorrhage (PPH) escalation",
        synonyms = listOf("pph", "postpartum hemorrhage", "maternal", "mtp", "bleeding"),
        formulaSummary = "Obstetric SI = Maternal HR (bpm) / Maternal SBP (mmHg)\n<0.9 Normal | 0.9–1.09 Alert | ≥1.1 Severe PPH / Activate MTP",
        clinicalPearl = "Physiological hypervolemia in pregnancy masks hypotension until >30% blood volume is lost. SI ≥0.9 predicts need for blood transfusion and senior obstetric escalation.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "hr",
                label = "Maternal Heart Rate",
                hint = "bpm",
                units = listOf("bpm")
            ),
            CalcInputDef.NumericField(
                id = "sbp",
                label = "Maternal Systolic BP",
                hint = "mmHg",
                units = listOf("mmHg")
            )
        )
    ),
    CalculatorDef(
        id = "obgyn_johnson",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Johnson's Formula (EFW)",
        shortSubtitle = "Estimated fetal weight from fundal height",
        synonyms = listOf("efw", "fetal weight", "sfh", "fundal height", "symphysis"),
        formulaSummary = "EFW (g) = (Adjusted SFH cm − n) × 155\nn = 12 if head above ischial spines; n = 11 if at/below spines.\nSubtract 1 cm from SFH if maternal weight >91 kg.",
        clinicalPearl = "Clinical bedside estimate when ultrasound biometry is unavailable; typical margin of error is ±350–400 g. Ensure bladder is empty before measuring SFH.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "sfh",
                label = "Symphysis-Fundal Height (SFH)",
                hint = "cm",
                units = listOf("cm")
            ),
            CalcInputDef.SegmentedChoice(
                id = "station_n",
                label = "Presenting Part Station",
                compactRow = false,
                options = listOf(
                    ChoiceOption("12", "Above Ischial Spines", "Unengaged (n = 12)"),
                    ChoiceOption("11", "At / Below Ischial Spines", "Engaged (n = 11)")
                ),
                defaultOptionId = "12"
            ),
            CalcInputDef.ToggleCriterion(
                id = "maternal_gt_91",
                label = "Maternal Weight >91 kg (200 lbs)",
                detail = "Subtracts 1 cm from measured SFH",
                pointsBadge = "−1 cm",
                points = 0
            )
        )
    )
)
