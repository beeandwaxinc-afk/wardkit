package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val NephrologyPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "nephro_adrogue_madias",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "Adrogué-Madias Na Correction",
        shortSubtitle = "Predicted ΔNa per 1 L of IV infusate",
        synonyms = listOf("adrogue", "madias", "hyponatremia", "hypernatremia", "3% nacl", "hypertonic saline", "ods", "sodium"),
        formulaSummary = "ΔNa per 1 L Infusate = (Infusate Na + Infusate K − Serum Na) / (TBW + 1)\nTBW = Weight (kg) × Factor (0.6 Male/Child, 0.5 Female/Elderly Male, 0.45 Elderly Female)",
        clinicalPearl = "Limit chronic sodium correction to ≤8 mEq/L per 24 hours (max 10 mEq/L; ≤6–8 in high-ODS-risk patients with alcoholism, malnutrition, or hypokalemia). Always recheck serum Na every 4–6 hours.",
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
            CalcInputDef.NumericField("weight", "Patient Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.NumericField("serum_na", "Current Serum Na⁺", "mEq/L", listOf("mEq/L")),
            CalcInputDef.SegmentedChoice(
                id = "infusate",
                label = "IV Infusate",
                compactRow = false,
                options = listOf(
                    ChoiceOption("513", "3% Hypertonic Saline", "Na = 513 mEq/L"),
                    ChoiceOption("154", "0.9% Normal Saline", "Na = 154 mEq/L"),
                    ChoiceOption("130", "Ringer's Lactate", "Na = 130 mEq/L"),
                    ChoiceOption("77", "0.45% Half-Normal Saline", "Na = 77 mEq/L"),
                    ChoiceOption("0", "5% Dextrose (D5W)", "Na = 0 mEq/L")
                ),
                defaultOptionId = "154"
            ),
            CalcInputDef.NumericField("infusate_k", "Added Infusate K⁺ (Optional)", "Default 0 mEq/L", listOf("mEq/L"), optional = true, allowZero = true)
        )
    ),
    CalculatorDef(
        id = "nephro_feurea",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "FEUrea (AKI on Diuretics)",
        shortSubtitle = "Fractional excretion of urea for AKI",
        synonyms = listOf("feurea", "urea", "aki", "diuretics", "furosemide", "prerenal", "atn"),
        formulaSummary = "FEUrea (%) = [(Urine Urea × Serum Cr) / (Serum Urea × Urine Cr)] × 100\n≤35% Prerenal AKI (Valid on Diuretics) | 36–50% Indeterminate | >50% Intrinsic AKI",
        clinicalPearl = "Unlike FENa, FEUrea remains reliable in patients receiving loop or thiazide diuretics because proximal tubular urea reabsorption is unaffected by distal diuretic blockade.",
        inputs = listOf(
            CalcInputDef.NumericField("u_urea", "Urine Urea / BUN", "Match serum unit", listOf("mg/dL or mmol/L")),
            CalcInputDef.NumericField("s_urea", "Serum Urea / BUN", "Match urine unit", listOf("mg/dL or mmol/L")),
            CalcInputDef.NumericField("u_cr", "Urine Creatinine", "Match serum unit", listOf("mg/dL or µmol/L")),
            CalcInputDef.NumericField("s_cr", "Serum Creatinine", "Match urine unit", listOf("mg/dL or µmol/L"))
        )
    ),
    CalculatorDef(
        id = "nephro_k_replace",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "Potassium Replacement",
        shortSubtitle = "Hypokalemia deficit & safe IV max rates",
        synonyms = listOf("potassium", "hypokalemia", "kcl", "electrolyte", "magnesium"),
        formulaSummary = "Estimated Total Body Deficit ≈ ((Target K − Current K) / 0.3) × 100 mEq (for K⁺ 2.0–4.0)\nMax Safe Rates: Peripheral IV ≤10 mEq/hr | Central Line ≤20 mEq/hr (Continuous ECG)",
        clinicalPearl = "MANDATORY MAGNESIUM CHECK: Refractory hypokalemia will NOT correct until hypomagnesemia is repleted. Prefer oral KCl whenever the gut works; avoid rapid IV push (fatal arrhythmia).",
        inputs = listOf(
            CalcInputDef.NumericField("current_k", "Current Serum K⁺ (<4.0)", "mEq/L", listOf("mEq/L")),
            CalcInputDef.NumericField("target_k", "Target Serum K⁺ (Optional)", "Default 4.0 mEq/L", listOf("mEq/L"), optional = true),
            CalcInputDef.SegmentedChoice(
                id = "route",
                label = "Replacement Route",
                options = listOf(
                    ChoiceOption("peripheral", "Oral / Peripheral IV", "≤10 mEq/hr"),
                    ChoiceOption("central", "Central Line", "≤20 mEq/hr + ECG")
                ),
                defaultOptionId = "peripheral"
            )
        )
    ),
    CalculatorDef(
        id = "nephro_uag",
        departmentId = DepartmentId.NEPHROLOGY,
        name = "Urine Anion Gap",
        shortSubtitle = "NAGMA differentiation: GI loss vs RTA",
        synonyms = listOf("uag", "urine anion gap", "nagma", "rta", "renal tubular acidosis", "diarrhea"),
        formulaSummary = "Urine Anion Gap = (Urine Na + Urine K) − Urine Cl\n<0 Negative: GI HCO₃⁻ Loss / Diarrhea (Intact NH₄⁺)\n>0 Positive: Renal Tubular Acidosis (Impaired NH₄⁺ Excretion)",
        clinicalPearl = "Mnemonic: 'neGUTive' = GI loss (diarrhea); 'PosiTive' = Tubular (RTA). Only interpret in Normal Anion Gap Metabolic Acidosis (NAGMA) when urine Na >20 mEq/L and no unmeasured urine anions (ketones, hippurate) are present.",
        inputs = listOf(
            CalcInputDef.NumericField("una", "Urine Sodium (U_Na)", "mEq/L", listOf("mEq/L"), allowZero = true),
            CalcInputDef.NumericField("uk", "Urine Potassium (U_K)", "mEq/L", listOf("mEq/L"), allowZero = true),
            CalcInputDef.NumericField("ucl", "Urine Chloride (U_Cl)", "mEq/L", listOf("mEq/L"), allowZero = true)
        )
    )
)
