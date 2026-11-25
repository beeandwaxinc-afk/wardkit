package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val ObsGynaePhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "obgyn_ebl",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Estimated Blood Loss (EBL)",
        shortSubtitle = "Quantitative gravimetric PPH blood loss",
        synonyms = listOf("ebl", "qbl", "blood loss", "pph", "sponges", "pads", "postpartum hemorrhage"),
        formulaSummary = "Quantitative Blood Loss (mL) = max(0, Wet Weight g − Dry Weight g) + Net Suction Volume (mL)\n(1 gram blood ≈ 1 mL)  |  <500 Normal | 500–999 Vaginal PPH Alert | ≥1,000 Major PPH (ACOG)",
        clinicalPearl = "Visual estimation systematically underestimates heavy obstetric hemorrhage by 30–50%. Remember to subtract amniotic fluid and irrigation fluid volumes from the suction canister.",
        inputs = listOf(
            CalcInputDef.NumericField("wet_g", "Wet Pads / Sponges Weight", "grams", listOf("g"), allowZero = true),
            CalcInputDef.NumericField("dry_g", "Dry Pads / Sponges Weight", "grams", listOf("g"), allowZero = true),
            CalcInputDef.NumericField("suction_ml", "Net Suction Blood Volume (Optional)", "Default 0 mL (net of irrigation)", listOf("mL"), optional = true, allowZero = true)
        )
    ),
    CalculatorDef(
        id = "obgyn_gwg",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Gestational Weight Gain (IOM)",
        shortSubtitle = "BMI-based pregnancy weight gain target",
        synonyms = listOf("gwg", "iom", "weight gain", "pregnancy", "bmi", "twin", "antenatal"),
        formulaSummary = "Pre-Pregnancy BMI = Weight (kg) / Height (m)²\nSingleton: <18.5 → 12.5–18 kg | 18.5–24.9 → 11.5–16 kg | 25–29.9 → 7–11.5 kg | ≥30 → 5–9 kg\nTwin: Normal 17–25 kg | Overweight 14–23 kg | Obese 11–19 kg",
        clinicalPearl = "First-trimester gain averages 0.5–2.0 kg total; linear weekly rates apply during the 2nd and 3rd trimesters. Both insufficient and excessive gain increase adverse perinatal outcomes.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "gestation",
                label = "Gestation Type",
                options = listOf(
                    ChoiceOption("singleton", "Singleton"),
                    ChoiceOption("twin", "Twin")
                ),
                defaultOptionId = "singleton"
            ),
            CalcInputDef.NumericField("weight", "Pre-Pregnancy Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.NumericField("height", "Height", "Height", listOf("cm", "in"))
        )
    ),
    CalculatorDef(
        id = "obgyn_preeclampsia",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "Preeclampsia Severe Triage",
        shortSubtitle = "ACOG severe-features & MgSO₄ checklist",
        synonyms = listOf("preeclampsia", "eclampsia", "hellp", "magnesium", "mgso4", "bp", "hypertension"),
        formulaSummary = "6 ACOG Severe Features: BP ≥160/110, Platelets <100k, SCr >1.1 or 2× baseline, AST/ALT ≥2× ULN or RUQ pain, Pulmonary Edema, Refractory Headache/Visual Changes.\n0 Checked: Without Severe Features | ≥1 Checked: WITH SEVERE FEATURES",
        clinicalPearl = "Any single severe feature triggers IV MgSO₄ seizure prophylaxis (4–6 g load → 1–2 g/hr), urgent IV labetalol/hydralazine or oral nifedipine for BP ≥160/110, and expedited delivery planning.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("bp", "Severe Blood Pressure (≥160/110)", "SBP ≥160 or DBP ≥110 on two occasions (or immediately if severe)", "Severe", 1),
            CalcInputDef.ToggleCriterion("plt", "Thrombocytopenia (Platelets <100k)", "Platelet count <100 ×10⁹/L", "Severe", 1),
            CalcInputDef.ToggleCriterion("renal", "Renal Insufficiency", "SCr >1.1 mg/dL or doubling of baseline SCr", "Severe", 1),
            CalcInputDef.ToggleCriterion("liver", "Impaired Liver Function", "AST/ALT ≥2× ULN or severe persistent RUQ/epigastric pain", "Severe", 1),
            CalcInputDef.ToggleCriterion("edema", "Pulmonary Edema", "Crackles, hypoxia, or CXR pulmonary edema", "Severe", 1),
            CalcInputDef.ToggleCriterion("neuro", "Refractory Headache or Visual Symptoms", "New-onset headache unresponsive to meds or scotomata/blurring", "Severe", 1)
        )
    ),
    CalculatorDef(
        id = "obgyn_vbac",
        departmentId = DepartmentId.OBS_GYNAE,
        name = "VBAC Success (Grobman 2021)",
        shortSubtitle = "Race-free TOLAC vaginal delivery chance",
        synonyms = listOf("vbac", "tolac", "grobman", "c-section", "cesarean", "trial of labor"),
        formulaSummary = "w = −5.952 − 0.023·Age − 0.024·Wt(kg) + 0.056·Ht(cm) − 0.597·Arrest + 0.868·PriorVag + 1.869·PriorVBAC − 0.966·TreatedHTN\nProbability (%) = 100 / (1 + exp(−w))  |  ≥70% High | 60–69% Mod | <60% Lower",
        clinicalPearl = "The 2021 MFMU Grobman model removes race and ethnicity variables. Predicted success ≥60–70% is associated with lower maternal morbidity than repeat elective cesarean.",
        inputs = listOf(
            CalcInputDef.NumericField("age", "Maternal Age", "Years", listOf("yrs")),
            CalcInputDef.NumericField("weight", "Pre-Pregnancy Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.NumericField("height", "Maternal Height", "Height", listOf("cm", "in")),
            CalcInputDef.SegmentedChoice(
                id = "obstetric_hx",
                label = "Obstetric Vaginal Delivery History",
                compactRow = false,
                options = listOf(
                    ChoiceOption("none", "No Prior Vaginal Delivery", "Prior CS only"),
                    ChoiceOption("prior_vag", "Prior Vaginal Delivery Before CS", "Vaginal birth before cesarean"),
                    ChoiceOption("prior_vbac", "Prior Successful VBAC", "Vaginal birth after cesarean")
                ),
                defaultOptionId = "none"
            ),
            CalcInputDef.ToggleCriterion("arrest", "Arrest Disorder Indication for Prior CS", "Prior CS for arrest of dilation or descent", "Risk", 0),
            CalcInputDef.ToggleCriterion("htn", "Treated Chronic Hypertension", "Pre-existing hypertension on medication", "Risk", 0)
        )
    )
)
