package com.example.data

internal val CalculatorsPart2: List<CalculatorDef> = listOf(
    // =========================================================================
    // 6. NEURO & NEUROSURGERY
    // =========================================================================
    CalculatorDef(
        id = "neuro_gcs",
        departmentId = DepartmentId.NEURO,
        name = "Glasgow Coma Scale",
        shortSubtitle = "Eye, Verbal & Motor consciousness score",
        synonyms = listOf("gcs", "coma", "tbi", "head injury", "intubate", "consciousness"),
        formulaSummary = "GCS = Eye (1–4) + Verbal (1–5) + Motor (1–6) = 3–15\n13–15 Mild | 9–12 Moderate | ≤8 Severe (Consider Airway/Intubation)",
        clinicalPearl = "Always document individual components (e.g., E3 V4 M6 = 13). The Motor subscore is the strongest single predictor of outcome in traumatic brain injury.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "eye",
                label = "Eye Opening (E)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Spontaneous", "Opens eyes spontaneously", "E4", 4),
                    ChoiceOption("3", "To Speech", "Opens to verbal command", "E3", 3),
                    ChoiceOption("2", "To Pressure / Pain", "Opens to painful stimulus", "E2", 2),
                    ChoiceOption("1", "None", "No eye opening", "E1", 1)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "verbal",
                label = "Verbal Response (V)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("5", "Oriented", "Coherent & oriented", "V5", 5),
                    ChoiceOption("4", "Confused", "Converses but disoriented", "V4", 4),
                    ChoiceOption("3", "Words", "Inappropriate single words", "V3", 3),
                    ChoiceOption("2", "Sounds", "Incomprehensible groans", "V2", 2),
                    ChoiceOption("1", "None", "No verbal response", "V1", 1)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "motor",
                label = "Motor Response (M)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("6", "Obeys Commands", "Follows simple motor commands", "M6", 6),
                    ChoiceOption("5", "Localizes Pain", "Purposeful movement to stimulus", "M5", 5),
                    ChoiceOption("4", "Normal Flexion", "Withdraws from pain", "M4", 4),
                    ChoiceOption("3", "Abnormal Flexion", "Decorticate posturing", "M3", 3),
                    ChoiceOption("2", "Extension", "Decerebrate posturing", "M2", 2),
                    ChoiceOption("1", "None", "No motor response", "M1", 1)
                )
            )
        )
    ),
    CalculatorDef(
        id = "neuro_cpp",
        departmentId = DepartmentId.NEURO,
        name = "Cerebral Perfusion (CPP)",
        shortSubtitle = "MAP minus ICP brain perfusion target",
        synonyms = listOf("cpp", "icp", "map", "tbi", "intracranial pressure", "brain"),
        formulaSummary = "CPP = MAP − ICP\nMAP = (SBP + 2 × DBP) / 3\nTarget in severe TBI: 60–70 mmHg",
        clinicalPearl = "Zero the arterial line transducer at the tragus (foramen of Monro) when measuring CPP alongside an EVD/ICP bolt. Avoid aggressive CPP >70 mmHg due to ARDS risk.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "map_mode",
                label = "MAP Entry Method",
                options = listOf(
                    ChoiceOption("direct", "Direct MAP"),
                    ChoiceOption("bp", "From SBP & DBP")
                ),
                defaultOptionId = "direct"
            ),
            CalcInputDef.NumericField(
                id = "map",
                label = "Mean Arterial Pressure (MAP)",
                hint = "mmHg",
                units = listOf("mmHg"),
                visibleWhenChoiceKey = "map_mode",
                visibleWhenChoiceValue = "direct"
            ),
            CalcInputDef.NumericField(
                id = "sbp",
                label = "Systolic BP (SBP)",
                hint = "mmHg",
                units = listOf("mmHg"),
                visibleWhenChoiceKey = "map_mode",
                visibleWhenChoiceValue = "bp"
            ),
            CalcInputDef.NumericField(
                id = "dbp",
                label = "Diastolic BP (DBP)",
                hint = "mmHg",
                units = listOf("mmHg"),
                visibleWhenChoiceKey = "map_mode",
                visibleWhenChoiceValue = "bp"
            ),
            CalcInputDef.NumericField(
                id = "icp",
                label = "Intracranial Pressure (ICP)",
                hint = "mmHg",
                units = listOf("mmHg"),
                allowZero = true
            )
        )
    ),
    CalculatorDef(
        id = "neuro_abc2",
        departmentId = DepartmentId.NEURO,
        name = "ABC/2 Hematoma Volume",
        shortSubtitle = "Intracerebral hemorrhage CT bleed volume",
        synonyms = listOf("ich", "bleed", "hematoma", "ct head", "stroke", "hemorrhage"),
        formulaSummary = "Volume (mL) = (A × B × C) / 2\nA = longest diameter (cm), B = perpendicular diameter (cm)\nC = weighted slice count × slice thickness (cm)",
        clinicalPearl = "For slice count C: slices with >75% of largest area count as 1, 25–75% count as 0.5, and <25% are excluded. Volume ≥30 mL adds +1 to the ICH Score.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "a_cm",
                label = "A — Longest Diameter",
                hint = "cm",
                units = listOf("cm")
            ),
            CalcInputDef.NumericField(
                id = "b_cm",
                label = "B — Perpendicular Width",
                hint = "cm",
                units = listOf("cm")
            ),
            CalcInputDef.NumericField(
                id = "slices",
                label = "Effective Slice Count",
                hint = "Number of slices",
                units = listOf("slices")
            ),
            CalcInputDef.SegmentedChoice(
                id = "thickness_mode",
                label = "CT Slice Thickness",
                options = listOf(
                    ChoiceOption("0.5", "0.5 cm", "5 mm"),
                    ChoiceOption("1.0", "1.0 cm", "10 mm"),
                    ChoiceOption("custom", "Custom", "Enter cm")
                ),
                defaultOptionId = "0.5"
            ),
            CalcInputDef.NumericField(
                id = "custom_thickness",
                label = "Custom Slice Thickness",
                hint = "cm",
                units = listOf("cm"),
                visibleWhenChoiceKey = "thickness_mode",
                visibleWhenChoiceValue = "custom"
            )
        )
    ),
    CalculatorDef(
        id = "neuro_ich",
        departmentId = DepartmentId.NEURO,
        name = "ICH Score",
        shortSubtitle = "30-day intracerebral hemorrhage mortality",
        synonyms = listOf("hemorrhagic stroke", "bleed", "mortality", "ivh", "infratentorial"),
        formulaSummary = "GCS (3–4: +2, 5–12: +1, 13–15: 0) + Vol ≥30 mL (+1) + IVH (+1) + Infratentorial (+1) + Age ≥80 (+1).\n30-day mortality: 0:0%, 1:13%, 2:26%, 3:72%, 4:97%, 5–6:~100%",
        clinicalPearl = "Use for baseline severity grading and communication—never as the sole criterion for early withdrawal of life-sustaining treatment (self-fulfilling prophecy bias).",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "gcs_tier",
                label = "Glasgow Coma Scale (GCS)",
                options = listOf(
                    ChoiceOption("0", "13–15", "", "0", 0),
                    ChoiceOption("1", "5–12", "", "+1", 1),
                    ChoiceOption("2", "3–4", "", "+2", 2)
                )
            ),
            CalcInputDef.ToggleCriterion("vol30", "ICH Volume ≥30 mL", "Measured via ABC/2 on initial CT", "+1", 1),
            CalcInputDef.ToggleCriterion("ivh", "Intraventricular Hemorrhage (IVH)", "Blood extending into ventricles", "+1", 1),
            CalcInputDef.ToggleCriterion("infra", "Infratentorial Origin", "Cerebellum or brainstem bleed", "+1", 1),
            CalcInputDef.ToggleCriterion("age80", "Age ≥80 Years", "Patient age 80 or older", "+1", 1)
        )
    ),

    // =========================================================================
    // 7. ICU & CRITICAL CARE
    // =========================================================================
    CalculatorDef(
        id = "icu_map",
        departmentId = DepartmentId.ICU,
        name = "Mean Arterial Pressure",
        shortSubtitle = "MAP & pulse pressure organ perfusion",
        synonyms = listOf("map", "bp", "blood pressure", "vasopressor", "sepsis", "pulse pressure"),
        formulaSummary = "MAP = (SBP + 2 × DBP) / 3\nPulse Pressure = SBP − DBP\nSepsis / Organ Perfusion Floor: ≥65 mmHg",
        clinicalPearl = "MAP <65 mmHg compromises renal, cerebral, and coronary autoregulation. Narrow pulse pressure (<25% of SBP) suggests low stroke volume or tamponade.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "sbp",
                label = "Systolic BP (SBP)",
                hint = "mmHg",
                units = listOf("mmHg")
            ),
            CalcInputDef.NumericField(
                id = "dbp",
                label = "Diastolic BP (DBP)",
                hint = "mmHg",
                units = listOf("mmHg")
            )
        )
    ),
    CalculatorDef(
        id = "icu_qsofa",
        departmentId = DepartmentId.ICU,
        name = "qSOFA Score",
        shortSubtitle = "Bedside sepsis deterioration screen",
        synonyms = listOf("sepsis", "sofa", "infection", "shock", "icu"),
        formulaSummary = "1 point each: RR ≥22 /min, Altered Mentation (GCS <15), SBP ≤100 mmHg.\n≥2 points: High risk of poor outcome / ICU transfer",
        clinicalPearl = "Surviving Sepsis guidelines advise using qSOFA as a high-specificity risk marker rather than a standalone screening tool to rule out early sepsis.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("rr", "Respiratory Rate ≥22 /min", "Tachypnea", "+1", 1),
            CalcInputDef.ToggleCriterion("gcs", "Altered Mentation (GCS <15)", "Acute confusion or reduced alertness", "+1", 1),
            CalcInputDef.ToggleCriterion("sbp", "Systolic BP ≤100 mmHg", "Hypotension", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "icu_pf_ratio",
        departmentId = DepartmentId.ICU,
        name = "PaO₂/FiO₂ Ratio (ARDS)",
        shortSubtitle = "Berlin ARDS hypoxemia severity index",
        synonyms = listOf("pf ratio", "ards", "horowitz", "abg", "ventilator", "oxygenation"),
        formulaSummary = "P/F Ratio = PaO₂ (mmHg) / (FiO₂ % / 100)\n>300 Normal | 201–300 Mild | 101–200 Moderate | ≤100 Severe ARDS",
        clinicalPearl = "Berlin ARDS classification requires PEEP/CPAP ≥5 cmH₂O, acute onset within 1 week, and bilateral opacities not fully explained by cardiac failure.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "pao2",
                label = "Arterial PaO₂",
                hint = "mmHg",
                units = listOf("mmHg")
            ),
            CalcInputDef.NumericField(
                id = "fio2",
                label = "Inspired FiO₂ (%)",
                hint = "21–100%",
                units = listOf("%")
            )
        )
    ),
    CalculatorDef(
        id = "icu_ardsnet",
        departmentId = DepartmentId.ICU,
        name = "ARDSNet Tidal Volume",
        shortSubtitle = "PBW & lung-protective 6–8 mL/kg Vt",
        synonyms = listOf("ventilator", "pbw", "tidal volume", "tv", "ards", "intubation"),
        formulaSummary = "Male PBW = 50 + 2.3 × (Height in − 60)\nFemale PBW = 45.5 + 2.3 × (Height in − 60)\nLung-protective Tidal Volume: 6 mL/kg PBW (range 4–8 mL/kg)",
        clinicalPearl = "ALWAYS calculate tidal volume from Predicted Body Weight (height & sex)—never actual body weight—to prevent barotrauma in obese patients. Keep plateau pressure ≤30 cmH₂O.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "sex",
                label = "Biological Sex",
                options = listOf(
                    ChoiceOption("male", "Male"),
                    ChoiceOption("female", "Female")
                ),
                defaultOptionId = "male"
            ),
            CalcInputDef.NumericField(
                id = "height",
                label = "Patient Height",
                hint = "Height",
                units = listOf("cm", "in")
            )
        )
    ),

    // =========================================================================
    // 8. NEPHROLOGY
    // =========================================================================
    CalculatorDef(
        id = "nephro_cg",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "Cockcroft-Gault CrCl",
        shortSubtitle = "Renal drug dosing creatinine clearance",
        synonyms = listOf("creatinine clearance", "crcl", "renal dosing", "vancomycin", "doac", "kidney"),
        formulaSummary = "CrCl (mL/min) = [(140 − Age) × Weight (kg)] / (72 × SCr mg/dL)\nMultiply by 0.85 if Female.  (1 mg/dL = 88.4 µmol/L)",
        clinicalPearl = "Most FDA/BNF renal drug-dosing tables still use Cockcroft-Gault. In obesity (BMI ≥30), consider Adjusted Body Weight; in unstable AKI, CrCl lags behind true GFR.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "sex",
                label = "Biological Sex",
                options = listOf(
                    ChoiceOption("male", "Male"),
                    ChoiceOption("female", "Female")
                ),
                defaultOptionId = "male"
            ),
            CalcInputDef.NumericField(
                id = "age",
                label = "Age",
                hint = "Years",
                units = listOf("yrs")
            ),
            CalcInputDef.NumericField(
                id = "weight",
                label = "Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            ),
            CalcInputDef.NumericField(
                id = "scr",
                label = "Serum Creatinine (SCr)",
                hint = "SCr",
                units = listOf("mg/dL", "µmol/L")
            )
        )
    ),
    CalculatorDef(
        id = "nephro_ckdepi",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "CKD-EPI 2021 eGFR",
        shortSubtitle = "Race-free eGFR & KDIGO G1–G5 stage",
        synonyms = listOf("egfr", "gfr", "ckd", "kdigo", "kidney", "renal"),
        formulaSummary = "eGFR = 142 × min(SCr/κ, 1)^α × max(SCr/κ, 1)^(−1.200) × 0.9938^Age × (1.012 if Female)\nFemale: κ=0.7, α=−0.241  |  Male: κ=0.9, α=−0.302",
        clinicalPearl = "The 2021 CKD-EPI creatinine equation removes race adjustment and is recommended by NKF/ASN for CKD staging. Not validated during rapidly changing AKI.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "sex",
                label = "Biological Sex",
                options = listOf(
                    ChoiceOption("male", "Male"),
                    ChoiceOption("female", "Female")
                ),
                defaultOptionId = "male"
            ),
            CalcInputDef.NumericField(
                id = "age",
                label = "Age",
                hint = "Years",
                units = listOf("yrs")
            ),
            CalcInputDef.NumericField(
                id = "scr",
                label = "Serum Creatinine (SCr)",
                hint = "SCr",
                units = listOf("mg/dL", "µmol/L")
            )
        )
    ),
    CalculatorDef(
        id = "nephro_fena",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "FENa",
        shortSubtitle = "Prerenal vs intrinsic AKI differentiation",
        synonyms = listOf("aki", "fractional excretion sodium", "prerenal", "atn", "acute kidney injury"),
        formulaSummary = "FENa (%) = [(Urine Na × Serum Cr) / (Serum Na × Urine Cr)] × 100\n<1% Prerenal | 1–2% Indeterminate | >2% Intrinsic Renal (ATN)",
        clinicalPearl = "Loop/thiazide diuretics falsely elevate FENa (>1% even in prerenal states)—use FEUrea (<35% prerenal) instead if the patient recently received diuretics.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "una",
                label = "Urine Sodium (U_Na)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "sna",
                label = "Serum Sodium (S_Na)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "ucr",
                label = "Urine Creatinine (U_Cr)",
                hint = "mg/dL",
                units = listOf("mg/dL")
            ),
            CalcInputDef.NumericField(
                id = "scr",
                label = "Serum Creatinine (S_Cr)",
                hint = "mg/dL",
                units = listOf("mg/dL")
            )
        )
    ),
    CalculatorDef(
        id = "nephro_fwd",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "Free Water Deficit",
        shortSubtitle = "Hypernatremia total water replacement",
        synonyms = listOf("hypernatremia", "sodium", "water deficit", "tbw", "d5w"),
        formulaSummary = "TBW = Weight (kg) × Factor (0.6 M/Child, 0.5 F/Elderly M, 0.45 Elderly F)\nDeficit (L) = TBW × [(Current Na / Target Na) − 1]",
        clinicalPearl = "In chronic hypernatremia (>48 hrs), lower serum Na by NO MORE than 10–12 mEq/L per 24 hours (~0.5 mEq/L/hr) to prevent cerebral edema. Remember ongoing insensible & urinary losses.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "group",
                label = "Patient TBW Group",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0.60", "Adult Male", "TBW = 0.60"),
                    ChoiceOption("0.50", "Adult Female / Elderly Male", "TBW = 0.50"),
                    ChoiceOption("0.45", "Elderly Female", "TBW = 0.45"),
                    ChoiceOption("0.60_child", "Child", "TBW = 0.60")
                ),
                defaultOptionId = "0.60"
            ),
            CalcInputDef.NumericField(
                id = "weight",
                label = "Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            ),
            CalcInputDef.NumericField(
                id = "current_na",
                label = "Current Sodium (Na⁺)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.NumericField(
                id = "target_na",
                label = "Target Sodium (Optional)",
                hint = "Default 140 mEq/L",
                units = listOf("mEq/L"),
                optional = true
            )
        )
    ),

    // =========================================================================
    // 9. GASTRO & HEPATOLOGY
    // =========================================================================
    CalculatorDef(
        id = "gastro_child_pugh",
        departmentId = DepartmentId.GASTRO,
        name = "Child-Pugh Score",
        shortSubtitle = "Cirrhosis severity & surgical risk class",
        synonyms = listOf("cirrhosis", "liver", "hepatic", "child turcotte", "ascites", "encephalopathy"),
        formulaSummary = "Sum of 5 domains (1–3 pts each) = 5–15:\nClass A (5–6): Well-compensated | Class B (7–9): Significant | Class C (10–15): Decompensated",
        clinicalPearl = "1-year survival: Class A ~100%, Class B ~80%, Class C ~45%. Perioperative abdominal surgery mortality rises sharply from ~10% (A) to ~30% (B) and ~80% (C).",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "bili",
                label = "Total Bilirubin",
                options = listOf(
                    ChoiceOption("1", "<2.0", "mg/dL", "+1", 1),
                    ChoiceOption("2", "2.0–3.0", "mg/dL", "+2", 2),
                    ChoiceOption("3", ">3.0", "mg/dL", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "albumin",
                label = "Serum Albumin",
                options = listOf(
                    ChoiceOption("1", ">3.5", "g/dL", "+1", 1),
                    ChoiceOption("2", "2.8–3.5", "g/dL", "+2", 2),
                    ChoiceOption("3", "<2.8", "g/dL", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "inr",
                label = "INR",
                options = listOf(
                    ChoiceOption("1", "<1.7", "", "+1", 1),
                    ChoiceOption("2", "1.7–2.2", "", "+2", 2),
                    ChoiceOption("3", ">2.2", "", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "ascites",
                label = "Ascites",
                options = listOf(
                    ChoiceOption("1", "None", "", "+1", 1),
                    ChoiceOption("2", "Mild / Mod", "Diuretic resp.", "+2", 2),
                    ChoiceOption("3", "Severe", "Refractory", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "enceph",
                label = "Hepatic Encephalopathy",
                options = listOf(
                    ChoiceOption("1", "None", "Grade 0", "+1", 1),
                    ChoiceOption("2", "Grade 1–2", "Mild–Mod", "+2", 2),
                    ChoiceOption("3", "Grade 3–4", "Severe", "+3", 3)
                )
            )
        )
    ),
    CalculatorDef(
        id = "gastro_meld_na",
        departmentId = DepartmentId.GASTRO,
        name = "MELD-Na Score",
        shortSubtitle = "End-stage liver 90-day mortality score",
        synonyms = listOf("meld", "liver transplant", "cirrhosis", "hepatology", "bilirubin", "inr"),
        formulaSummary = "Clamp Bili, INR, Cr ≥1.0 (Cr ≤4.0; set 4.0 if dialysis ≥2×/wk).\nMELD = 3.78·ln(Bili) + 11.2·ln(INR) + 9.57·ln(Cr) + 6.43\nIf MELD >11: MELD-Na = MELD + 1.32(137−Na) − 0.033·MELD(137−Na)  [Na clamped 125–137]",
        clinicalPearl = "Final MELD-Na is rounded to the nearest integer and bounded between 6 and 40. Scores ≥15 generally warrant liver transplant evaluation.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "bili",
                label = "Total Bilirubin",
                hint = "mg/dL",
                units = listOf("mg/dL")
            ),
            CalcInputDef.NumericField(
                id = "inr",
                label = "INR",
                hint = "Ratio",
                units = listOf("INR")
            ),
            CalcInputDef.NumericField(
                id = "scr",
                label = "Serum Creatinine (SCr)",
                hint = "mg/dL",
                units = listOf("mg/dL")
            ),
            CalcInputDef.NumericField(
                id = "na",
                label = "Serum Sodium (Na⁺)",
                hint = "mEq/L",
                units = listOf("mEq/L")
            ),
            CalcInputDef.ToggleCriterion(
                id = "dialysis",
                label = "Dialysis ≥2× in Past Week",
                detail = "Or 24 hrs of CVVHD (sets SCr to 4.0 mg/dL)",
                pointsBadge = "Cr=4.0",
                points = 0
            )
        )
    ),
    CalculatorDef(
        id = "gastro_maddrey",
        departmentId = DepartmentId.GASTRO,
        name = "Maddrey's DF",
        shortSubtitle = "Alcoholic hepatitis corticosteroid threshold",
        synonyms = listOf("alcoholic hepatitis", "discriminant function", "prednisolone", "liver", "pt"),
        formulaSummary = "DF = 4.6 × (Patient PT − Control PT) + Total Bilirubin (mg/dL)\n≥32: Severe alcoholic hepatitis (consider corticosteroids)",
        clinicalPearl = "Score ≥32 carries ~35–50% 30-day mortality. Screen thoroughly for active infection, GI bleeding, or AKI before initiating prednisolone; reassess response at Day 7 with Lille score.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "pt_patient",
                label = "Patient PT",
                hint = "seconds",
                units = listOf("sec")
            ),
            CalcInputDef.NumericField(
                id = "pt_control",
                label = "Control PT",
                hint = "seconds",
                units = listOf("sec")
            ),
            CalcInputDef.NumericField(
                id = "bili",
                label = "Total Bilirubin",
                hint = "Bilirubin",
                units = listOf("mg/dL", "µmol/L")
            )
        )
    ),
    CalculatorDef(
        id = "gastro_saag",
        departmentId = DepartmentId.GASTRO,
        name = "SAAG",
        shortSubtitle = "Serum-ascites albumin gradient",
        synonyms = listOf("ascites", "paracentesis", "portal hypertension", "cirrhosis", "albumin"),
        formulaSummary = "SAAG = Serum Albumin (g/dL) − Ascitic Fluid Albumin (g/dL)\n≥1.1 g/dL: Portal Hypertension  |  <1.1 g/dL: Non-Portal Hypertension",
        clinicalPearl = "SAAG ≥1.1 g/dL identifies portal hypertension (cirrhosis, heart failure, Budd-Chiari) with ~97% accuracy. Pair with ascitic total protein (>2.5 g/dL points toward cardiac ascites).",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "serum_alb",
                label = "Serum Albumin",
                hint = "g/dL",
                units = listOf("g/dL")
            ),
            CalcInputDef.NumericField(
                id = "ascitic_alb",
                label = "Ascitic Fluid Albumin",
                hint = "g/dL",
                units = listOf("g/dL"),
                allowZero = true
            )
        )
    ),

    // =========================================================================
    // 10. EMERGENCY & ANESTHESIA
    // =========================================================================
    CalculatorDef(
        id = "em_steroids",
        departmentId = DepartmentId.EMERGENCY,
        name = "Steroid Equivalence",
        shortSubtitle = "Simultaneous corticosteroid dose conversion",
        synonyms = listOf("corticosteroid", "prednisolone", "dexamethasone", "hydrocortisone", "methylprednisolone", "triamcinolone"),
        formulaSummary = "Equivalent anti-inflammatory doses:\nHydrocortisone 20 mg = Prednisolone 5 mg = Methylprednisolone 4 mg = Triamcinolone 4 mg = Dexamethasone 0.75 mg",
        clinicalPearl = "Equivalence reflects glucocorticoid potency only—mineralocorticoid activity differs widely (Hydrocortisone highest; Dexamethasone virtually none, making Dex poor for adrenal crisis).",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "source_steroid",
                label = "Source Steroid",
                compactRow = false,
                options = listOf(
                    ChoiceOption("hydro", "Hydrocortisone", "Eq: 20 mg"),
                    ChoiceOption("pred", "Prednisolone / Prednisone", "Eq: 5 mg"),
                    ChoiceOption("methyl", "Methylprednisolone", "Eq: 4 mg"),
                    ChoiceOption("triam", "Triamcinolone", "Eq: 4 mg"),
                    ChoiceOption("dex", "Dexamethasone", "Eq: 0.75 mg")
                ),
                defaultOptionId = "pred"
            ),
            CalcInputDef.NumericField(
                id = "dose_mg",
                label = "Source Dose",
                hint = "mg",
                units = listOf("mg")
            )
        )
    ),
    CalculatorDef(
        id = "em_ibw",
        departmentId = DepartmentId.EMERGENCY,
        name = "Ideal & Adjusted Weight",
        shortSubtitle = "Devine IBW & obese dosing AdjBW",
        synonyms = listOf("ibw", "adjbw", "devine", "ideal body weight", "adjusted weight", "anesthesia", "dosing"),
        formulaSummary = "Devine IBW (kg): Male = 50 + 2.3 × (in − 60)  |  Female = 45.5 + 2.3 × (in − 60)\nAdjBW = IBW + 0.4 × (Actual Weight − IBW)",
        clinicalPearl = "Use IBW for tidal volume and hydrophilic drugs (e.g., rocuronium, acyclovir). Use AdjBW for aminoglycosides and many induction agents when actual weight >120% IBW.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "sex",
                label = "Biological Sex",
                options = listOf(
                    ChoiceOption("male", "Male"),
                    ChoiceOption("female", "Female")
                ),
                defaultOptionId = "male"
            ),
            CalcInputDef.NumericField(
                id = "height",
                label = "Height",
                hint = "Height",
                units = listOf("cm", "in")
            ),
            CalcInputDef.NumericField(
                id = "actual_weight",
                label = "Actual Weight (Optional for AdjBW)",
                hint = "Optional",
                units = listOf("kg", "lbs"),
                optional = true
            )
        )
    ),
    CalculatorDef(
        id = "em_mallampati_asa",
        departmentId = DepartmentId.EMERGENCY,
        name = "Mallampati & ASA Status",
        shortSubtitle = "Pre-op airway & perioperative risk summary",
        synonyms = listOf("airway", "anesthesia", "preop", "intubation", "asa", "mallampati"),
        formulaSummary = "Mallampati I–IV grades oropharyngeal visibility (III–IV = higher difficult intubation risk).\nASA I–V (+E Emergency) grades systemic physical status.",
        clinicalPearl = "Combine Mallampati with thyromental distance, mouth opening, neck mobility, and prior airway history—no single bedside airway test has 100% sensitivity alone.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "mallampati",
                label = "Mallampati Airway Class",
                compactRow = false,
                options = listOf(
                    ChoiceOption("I", "Class I", "Full soft palate, uvula & pillars visible"),
                    ChoiceOption("II", "Class II", "Soft palate & major uvula visible"),
                    ChoiceOption("III", "Class III", "Soft palate & base of uvula only"),
                    ChoiceOption("IV", "Class IV", "Hard palate only visible")
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "asa",
                label = "ASA Physical Status",
                compactRow = false,
                options = listOf(
                    ChoiceOption("I", "ASA I", "Normal healthy patient"),
                    ChoiceOption("II", "ASA II", "Mild systemic disease"),
                    ChoiceOption("III", "ASA III", "Severe systemic disease"),
                    ChoiceOption("IV", "ASA IV", "Severe disease — constant threat to life"),
                    ChoiceOption("V", "ASA V", "Moribund — not expected to survive without op")
                )
            ),
            CalcInputDef.ToggleCriterion(
                id = "emergency_e",
                label = "Emergency Surgery Modifier (E)",
                detail = "Delay would significantly increase threat to life or body part",
                pointsBadge = "+E",
                points = 0
            )
        )
    ),
    CalculatorDef(
        id = "em_resus",
        departmentId = DepartmentId.EMERGENCY,
        name = "Emergency Resus Dosing",
        shortSubtitle = "Adrenaline, defib joules & fluid bolus",
        synonyms = listOf("adrenaline", "epinephrine", "anaphylaxis", "cardiac arrest", "defib", "code", "pals", "acls", "bolus"),
        formulaSummary = "Anaphylaxis IM 1:1,000: 0.01 mg/kg (max 0.5 mg = 0.5 mL)\nArrest IV 1:10,000: 0.01 mg/kg (max 1.0 mg = 10 mL)\nPeads Defib: 2 J/kg → 4 J/kg (max 200 J)  |  Bolus: 20 mL/kg",
        clinicalPearl = "CRITICAL CONCENTRATION CHECK: Give 1:1,000 (1 mg/mL) IM for anaphylaxis in anterolateral thigh. Give 1:10,000 (0.1 mg/mL) IV/IO for cardiac arrest—never confuse the two.",
        inputs = listOf(
            CalcInputDef.NumericField(
                id = "weight",
                label = "Patient Weight",
                hint = "Weight",
                units = listOf("kg", "lbs")
            )
        )
    )
)
