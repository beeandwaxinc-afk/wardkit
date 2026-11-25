package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val CardiologyPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "cardio_framingham",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "Framingham 10-Year CVD Risk",
        shortSubtitle = "D'Agostino 2008 10-yr general CVD risk",
        synonyms = listOf("framingham", "cvd", "cholesterol", "hdl", "statin", "cardiovascular risk"),
        formulaSummary = "D'Agostino 2008 Cox Equation: Risk% = (1 − S₀^exp(Σ − Mean)) × 100\nMale S₀=0.88936, Mean=23.9802 | Female S₀=0.95012, Mean=26.1931\n<10% Low | 10–20% Intermediate | >20% High Risk",
        clinicalPearl = "Validated for primary prevention in adults aged 30–74 without established CVD. High risk (>20%) or intermediate risk with enhancers warrants statin & aggressive BP control.",
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
                label = "Age (30–74 yrs)",
                hint = "Years",
                units = listOf("yrs")
            ),
            CalcInputDef.NumericField(
                id = "tc",
                label = "Total Cholesterol",
                hint = "Total Chol",
                units = listOf("mg/dL", "mmol/L")
            ),
            CalcInputDef.NumericField(
                id = "hdl",
                label = "HDL Cholesterol",
                hint = "HDL",
                units = listOf("mg/dL", "mmol/L")
            ),
            CalcInputDef.NumericField(
                id = "sbp",
                label = "Systolic BP",
                hint = "mmHg",
                units = listOf("mmHg")
            ),
            CalcInputDef.ToggleCriterion("htn_tx", "Treated for Hypertension", "Currently taking antihypertensive medication", "Tx", 0),
            CalcInputDef.ToggleCriterion("smoker", "Current Smoker", "Active tobacco smoking", "Risk", 0),
            CalcInputDef.ToggleCriterion("diabetes", "Diabetes Mellitus", "Diagnosed diabetes or fasting glucose ≥126", "Risk", 0)
        )
    ),
    CalculatorDef(
        id = "cardio_hasbled",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "HAS-BLED Score",
        shortSubtitle = "Major bleeding risk on anticoagulation",
        synonyms = listOf("bleeding", "afib", "anticoagulation", "warfarin", "doac", "hasbled"),
        formulaSummary = "1 point each (0–9): HTN (SBP >160), Abnormal Renal, Abnormal Liver, Stroke, Bleeding Hx, Labile INR, Elderly (>65), Drugs (Antiplatelets/NSAIDs), Alcohol (≥8/wk).\n0–1 Low | 2 Moderate | ≥3 High Risk",
        clinicalPearl = "A high HAS-BLED score (≥3) is NOT a contraindication to anticoagulation—it flags patients requiring closer follow-up and correction of modifiable bleeding risks (BP, NSAIDs, alcohol).",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("htn", "Hypertension (Uncontrolled)", "Systolic BP >160 mmHg", "+1", 1),
            CalcInputDef.ToggleCriterion("renal", "Abnormal Renal Function", "Dialysis, transplant, or SCr >2.26 mg/dL (200 µmol/L)", "+1", 1),
            CalcInputDef.ToggleCriterion("liver", "Abnormal Liver Function", "Cirrhosis, bilirubin >2× ULN, or AST/ALT/ALP >3× ULN", "+1", 1),
            CalcInputDef.ToggleCriterion("stroke", "Stroke History", "Prior ischemic or hemorrhagic stroke", "+1", 1),
            CalcInputDef.ToggleCriterion("bleeding", "Bleeding History / Predisposition", "Prior major bleed, anemia, or bleeding diathesis", "+1", 1),
            CalcInputDef.ToggleCriterion("inr", "Labile INR", "Unstable/high INRs or Time in Therapeutic Range <60%", "+1", 1),
            CalcInputDef.ToggleCriterion("elderly", "Elderly (Age >65 yrs)", "Age greater than 65 years", "+1", 1),
            CalcInputDef.ToggleCriterion("drugs", "Drugs (Antiplatelets / NSAIDs)", "Concomitant aspirin, clopidogrel, or NSAIDs", "+1", 1),
            CalcInputDef.ToggleCriterion("alcohol", "Alcohol Use (≥8 drinks/wk)", "Heavy alcohol intake", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "cardio_timi",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "TIMI Risk Score (UA/NSTEMI)",
        shortSubtitle = "14-day MACE risk in non-ST ACS",
        synonyms = listOf("timi", "nstemi", "unstable angina", "acs", "chest pain", "mi"),
        formulaSummary = "7 criteria (+1 each = 0–7): Age ≥65, ≥3 CAD risk factors, Known CAD ≥50%, Aspirin past 7d, ≥2 severe angina episodes/24h, ST deviation ≥0.5 mm, Elevated markers.\n14-day MACE: 0–1: 4.7% | 2: 8.3% | 3: 13.2% | 4: 19.9% | 5: 26.2% | 6–7: 40.9%",
        clinicalPearl = "Patients with TIMI ≥3 derive the greatest benefit from early invasive coronary angiography, dual antiplatelet therapy, and anticoagulation.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("age65", "Age ≥65 Years", "Older adult risk factor", "+1", 1),
            CalcInputDef.ToggleCriterion("cad_rf", "≥3 CAD Risk Factors", "Family hx, HTN, hypercholesterolemia, DM, or active smoker", "+1", 1),
            CalcInputDef.ToggleCriterion("known_cad", "Known CAD (Stenosis ≥50%)", "Prior documented coronary artery stenosis ≥50%", "+1", 1),
            CalcInputDef.ToggleCriterion("aspirin", "Aspirin Use in Past 7 Days", "Symptoms despite recent aspirin therapy", "+1", 1),
            CalcInputDef.ToggleCriterion("angina", "Severe Angina (≥2 episodes / 24h)", "Recurrent rest or crescendo angina", "+1", 1),
            CalcInputDef.ToggleCriterion("st_dev", "ST Deviation ≥0.5 mm", "Transient ST elevation or depression on ECG", "+1", 1),
            CalcInputDef.ToggleCriterion("markers", "Elevated Cardiac Markers", "Positive troponin or CK-MB", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "cardio_wells_pe",
        departmentId = DepartmentId.CARDIOLOGY,
        name = "Wells' Criteria for PE",
        shortSubtitle = "Pulmonary embolism pre-test probability",
        synonyms = listOf("pe", "pulmonary embolism", "ctpa", "d-dimer", "vte", "wells"),
        formulaSummary = "DVT signs (+3), PE #1 dx (+3), HR >100 (+1.5), Immob/Surg (+1.5), Prior DVT/PE (+1.5), Hemoptysis (+1), Malignancy (+1).\nTwo-tier: ≤4 PE Unlikely | >4 PE Likely\nThree-tier: <2 Low | 2–6 Moderate | >6 High",
        clinicalPearl = "If PE Unlikely (≤4), combine with PERC (if low risk) or age-adjusted D-dimer to safely avoid unnecessary CTPA. If PE Likely (>4), proceed directly to CTPA.",
        isChecklistZeroValid = true,
        inputs = listOf(
            CalcInputDef.ToggleCriterion("dvt_signs", "Clinical Signs/Symptoms of DVT", "Leg swelling & deep venous tenderness", "+3", 3),
            CalcInputDef.ToggleCriterion("pe_likely", "PE #1 Diagnosis or Equally Likely", "Alternative diagnosis is less likely than PE", "+3", 3),
            CalcInputDef.ToggleCriterion("hr100", "Heart Rate >100 bpm", "Resting tachycardia", "+1.5", 1),
            CalcInputDef.ToggleCriterion("immob_surg", "Immobilization ≥3d or Surgery ≤4wk", "Bed rest or recent operative procedure", "+1.5", 1),
            CalcInputDef.ToggleCriterion("prev_vte", "Previous DVT / PE", "Objectively diagnosed prior VTE", "+1.5", 1),
            CalcInputDef.ToggleCriterion("hemoptysis", "Hemoptysis", "Coughing up blood", "+1", 1),
            CalcInputDef.ToggleCriterion("malignancy", "Active Malignancy", "Treatment within 6 months or palliative", "+1", 1)
        )
    )
)
