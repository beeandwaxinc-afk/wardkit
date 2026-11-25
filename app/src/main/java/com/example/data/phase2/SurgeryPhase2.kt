package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val SurgeryPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "surg_fluid_deficit",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Fluid Deficit & Maintenance",
        shortSubtitle = "Dehydration deficit + 4-2-1 8h/16h rates",
        synonyms = listOf("dehydration", "fluid deficit", "rehydration", "maintenance", "iv fluid", "volume"),
        formulaSummary = "Deficit (mL) = Weight (kg) × %Dehydration × 10\n24-hr Maintenance = Holliday-Segar (100/50/20 mL/kg/day)\nFirst 8h Rate = (50% Deficit + 1/3 Maint) / 8  |  Next 16h Rate = (50% Deficit + 2/3 Maint) / 16",
        clinicalPearl = "Replace emergent shock boluses (10–20 mL/kg isotonic crystalloid) first, then subtract bolus volume from the calculated deficit when setting 24-hour rehydration rates.",
        inputs = listOf(
            CalcInputDef.NumericField("weight", "Patient Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.SegmentedChoice(
                id = "dehydration_pct",
                label = "Dehydration Severity",
                options = listOf(
                    ChoiceOption("5", "Mild", "5%"),
                    ChoiceOption("7.5", "Mod", "7.5%"),
                    ChoiceOption("10", "Severe", "10%"),
                    ChoiceOption("custom", "Custom", "Enter %")
                ),
                defaultOptionId = "5"
            ),
            CalcInputDef.NumericField(
                id = "custom_pct",
                label = "Custom Dehydration (%)",
                hint = "e.g., 6",
                units = listOf("%"),
                visibleWhenChoiceKey = "dehydration_pct",
                visibleWhenChoiceValue = "custom"
            )
        )
    ),
    CalculatorDef(
        id = "surg_glasgow_imrie",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Glasgow-Imrie (Pancreatitis)",
        shortSubtitle = "PANCREAS 48-hr acute pancreatitis score",
        synonyms = listOf("pancreatitis", "imrie", "glasgow", "pancreas", "amylase", "lipase"),
        formulaSummary = "PANCREAS (1 pt each within 48h = 0–8): PaO₂ <60 mmHg, Age >55, Neutrophils/WBC >15, Ca <2.0 mmol/L, Urea >16 mmol/L, LDH >600, Albumin <32 g/L, Glucose >10 mmol/L.\n0–2 Mild/Moderate | ≥3 Severe Acute Pancreatitis",
        clinicalPearl = "Applies equally to both gallstone and alcohol-induced acute pancreatitis. Score ≥3 indicates severe disease requiring HDU/ICU review and aggressive goal-directed resuscitation.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("pao2", "PaO₂ <60 mmHg (<8.0 kPa)", "P — Hypoxemia", "+1", 1),
            CalcInputDef.ToggleCriterion("age55", "Age >55 Years", "A — Older age", "+1", 1),
            CalcInputDef.ToggleCriterion("wbc", "Neutrophils / WBC >15 ×10⁹/L", "N — Leukocytosis", "+1", 1),
            CalcInputDef.ToggleCriterion("ca", "Calcium <2.0 mmol/L (<8.0 mg/dL)", "C — Hypocalcemia", "+1", 1),
            CalcInputDef.ToggleCriterion("urea", "Urea >16 mmol/L (BUN >45 mg/dL)", "R — Renal / azotemia", "+1", 1),
            CalcInputDef.ToggleCriterion("ldh", "LDH >600 U/L", "E — Enzymes", "+1", 1),
            CalcInputDef.ToggleCriterion("alb", "Albumin <32 g/L (<3.2 g/dL)", "A — Hypoalbuminemia", "+1", 1),
            CalcInputDef.ToggleCriterion("glu", "Glucose >10 mmol/L (>180 mg/dL)", "S — Sugar (no prior diabetes)", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "surg_ranson",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Ranson's Criteria (Pancreatitis)",
        shortSubtitle = "Admission & 48-hr pancreatitis mortality",
        synonyms = listOf("ranson", "pancreatitis", "gallstone", "mortality", "amylase"),
        formulaSummary = "5 Admission + 6 Forty-Eight-Hour criteria (+1 each = 0–11; thresholds differ for Gallstone vs Non-Gallstone).\n0–2: ~2% | 3–4: ~15% | 5–6: ~40% | ≥7: ~100% mortality",
        clinicalPearl = "Note the higher age, WBC, glucose, and LDH cutoffs for gallstone pancreatitis. Full prognostic accuracy requires reassessment at 48 hours.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "etiology",
                label = "Pancreatitis Etiology",
                options = listOf(
                    ChoiceOption("non_gallstone", "Non-Gallstone", "Alcohol / Other"),
                    ChoiceOption("gallstone", "Gallstone", "Biliary")
                ),
                defaultOptionId = "non_gallstone"
            ),
            // Non-Gallstone Admission
            CalcInputDef.SectionHeader("ran_adm_ng", "At Admission — Non-Gallstone (5 Criteria)", visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_age", "Age >55 Years", "Admission criterion", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_wbc", "WBC >16,000 /µL", "Admission leukocytosis", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_glu", "Glucose >200 mg/dL (>11.1 mmol/L)", "Admission hyperglycemia", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_ldh", "LDH >350 U/L", "Admission LDH", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_ast", "AST >250 U/L", "Admission AST", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),

            // Gallstone Admission
            CalcInputDef.SectionHeader("ran_adm_g", "At Admission — Gallstone (5 Criteria)", visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_age", "Age >70 Years", "Gallstone admission cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_wbc", "WBC >18,000 /µL", "Gallstone leukocytosis cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_glu", "Glucose >220 mg/dL (>12.2 mmol/L)", "Gallstone glucose cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_ldh", "LDH >400 U/L", "Gallstone LDH cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_ast", "AST >250 U/L", "Admission AST", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),

            // 48-Hour Criteria (Non-Gallstone)
            CalcInputDef.SectionHeader("ran_48_ng", "At 48 Hours — Non-Gallstone (Optional, 6 Criteria)", visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_hct", "Hematocrit Drop >10%", "Within 48 hours", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_bun", "BUN Increase >5 mg/dL (>1.8 mmol/L)", "Despite IV fluids", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_ca", "Serum Calcium <8.0 mg/dL (<2.0 mmol/L)", "Hypocalcemia at 48h", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_pao2", "Arterial PaO₂ <60 mmHg", "Hypoxemia at 48h", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_bd", "Base Deficit >4 mEq/L", "Metabolic acidosis at 48h", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),
            CalcInputDef.ToggleCriterion("ng_seq", "Fluid Sequestration >6 L", "Third-spacing within 48h", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "non_gallstone"),

            // 48-Hour Criteria (Gallstone)
            CalcInputDef.SectionHeader("ran_48_g", "At 48 Hours — Gallstone (Optional, 6 Criteria)", visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_hct", "Hematocrit Drop >10%", "Within 48 hours", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_bun", "BUN Increase >2 mg/dL (>0.7 mmol/L)", "Gallstone BUN cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_ca", "Serum Calcium <8.0 mg/dL (<2.0 mmol/L)", "Hypocalcemia at 48h", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_pao2", "Arterial PaO₂ <60 mmHg", "Hypoxemia at 48h (optional in gallstone)", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_bd", "Base Deficit >5 mEq/L", "Gallstone base deficit cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone"),
            CalcInputDef.ToggleCriterion("g_seq", "Fluid Sequestration >4 L", "Gallstone fluid sequestration cutoff", "+1", 1, visibleWhenChoiceKey = "etiology", visibleWhenChoiceValue = "gallstone")
        )
    ),
    CalculatorDef(
        id = "surg_rts",
        departmentId = DepartmentId.GENERAL_SURGERY,
        name = "Revised Trauma Score (RTS)",
        shortSubtitle = "Field trauma triage & coded survival score",
        synonyms = listOf("rts", "trauma", "triage", "atls", "gcs", "injury"),
        formulaSummary = "Triage RTS = GCS(0–4) + SBP(0–4) + RR(0–4) = 0–12 (≤11 → Trauma Center)\nCoded RTS = 0.9368·GCS + 0.7326·SBP + 0.2908·RR  (Max 7.84)",
        clinicalPearl = "Coded RTS weights GCS most heavily to reflect traumatic brain injury severity. Any patient with Triage RTS ≤11 warrants transport to a designated trauma center.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "gcs",
                label = "Glasgow Coma Scale (GCS)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "13–15", "", "4", 4),
                    ChoiceOption("3", "9–12", "", "3", 3),
                    ChoiceOption("2", "6–8", "", "2", 2),
                    ChoiceOption("1", "4–5", "", "1", 1),
                    ChoiceOption("0", "3", "", "0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "sbp",
                label = "Systolic Blood Pressure (mmHg)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", ">89 mmHg", "", "4", 4),
                    ChoiceOption("3", "76–89 mmHg", "", "3", 3),
                    ChoiceOption("2", "50–75 mmHg", "", "2", 2),
                    ChoiceOption("1", "1–49 mmHg", "", "1", 1),
                    ChoiceOption("0", "0 mmHg", "", "0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "rr",
                label = "Respiratory Rate (breaths/min)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "10–29 /min", "", "4", 4),
                    ChoiceOption("3", ">29 /min", "", "3", 3),
                    ChoiceOption("2", "6–9 /min", "", "2", 2),
                    ChoiceOption("1", "1–5 /min", "", "1", 1),
                    ChoiceOption("0", "0 /min", "", "0", 0)
                )
            )
        )
    )
)
