package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val NeuroPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "neuro_abcd2",
        departmentId = DepartmentId.NEURO,
        name = "ABCD² Score (TIA Risk)",
        shortSubtitle = "Early 2-day stroke risk after TIA",
        synonyms = listOf("abcd2", "tia", "transient ischemic attack", "stroke", "neurology"),
        formulaSummary = "Age ≥60 (+1) + BP ≥140/90 (+1) + Clinical Features (0–2) + Duration (0–2) + Diabetes (+1) = 0–7.\n0–3 Low (1.0% 2-day) | 4–5 Moderate (4.1%) | 6–7 High Risk (8.1% 2-day)",
        clinicalPearl = "Do NOT use a low ABCD² score alone to defer urgent carotid imaging or cardiac workup—patients with symptomatic carotid stenosis or AF still carry high early stroke risk.",
        inputs = listOf(
            CalcInputDef.ToggleCriterion("age60", "Age ≥60 Years", "A — Age", "+1", 1),
            CalcInputDef.ToggleCriterion("bp140", "Initial BP ≥140/90 mmHg", "B — SBP ≥140 or DBP ≥90", "+1", 1),
            CalcInputDef.SegmentedChoice(
                id = "clinical",
                label = "C — Clinical Features of TIA",
                compactRow = false,
                options = listOf(
                    ChoiceOption("2", "Unilateral Weakness", "Focal motor deficit", "+2", 2),
                    ChoiceOption("1", "Speech Disturbance Only", "Without motor weakness", "+1", 1),
                    ChoiceOption("0", "Other Symptoms", "Sensory, visual, or ataxia only", "0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "duration",
                label = "D — Symptom Duration",
                options = listOf(
                    ChoiceOption("0", "<10 min", "", "0", 0),
                    ChoiceOption("1", "10–59 min", "", "+1", 1),
                    ChoiceOption("2", "≥60 min", "", "+2", 2)
                )
            ),
            CalcInputDef.ToggleCriterion("diabetes", "Diabetes Mellitus", "D — History of diabetes", "+1", 1)
        )
    ),
    CalculatorDef(
        id = "neuro_four",
        departmentId = DepartmentId.NEURO,
        name = "FOUR Score (Coma)",
        shortSubtitle = "Intubated-friendly brainstem coma scale",
        synonyms = listOf("four score", "coma", "brainstem", "intubated", "gcs", "neuro icu"),
        formulaSummary = "Eye (0–4) + Motor (0–4) + Brainstem Reflexes (0–4) + Respiration (0–4) = 0–16.\nCaptures locked-in syndrome, herniation reflexes, and ventilator drive without verbal testing.",
        clinicalPearl = "Superior to GCS in intubated ICU patients because it evaluates pupillary/corneal/cough reflexes and respiratory drive directly. Score 0 warrants formal brain-death evaluation.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "eye",
                label = "Eye Response (E)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Eyelids open, tracking or blinking to command", "", "E4", 4),
                    ChoiceOption("3", "Eyelids open, NOT tracking", "", "E3", 3),
                    ChoiceOption("2", "Closed, open to loud voice", "", "E2", 2),
                    ChoiceOption("1", "Closed, open to pain", "", "E1", 1),
                    ChoiceOption("0", "Remain closed with pain", "", "E0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "motor",
                label = "Motor Response (M)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Thumbs-up, fist, or peace sign to command", "", "M4", 4),
                    ChoiceOption("3", "Localizing to pain", "", "M3", 3),
                    ChoiceOption("2", "Flexion response to pain", "", "M2", 2),
                    ChoiceOption("1", "Extension response to pain", "", "M1", 1),
                    ChoiceOption("0", "No response or generalized myoclonus status", "", "M0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "brainstem",
                label = "Brainstem Reflexes (B)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Pupil & corneal reflexes present", "", "B4", 4),
                    ChoiceOption("3", "One pupil wide and fixed", "", "B3", 3),
                    ChoiceOption("2", "Pupil OR corneal reflex absent", "", "B2", 2),
                    ChoiceOption("1", "Pupil AND corneal reflexes absent", "", "B1", 1),
                    ChoiceOption("0", "Absent pupil, corneal & cough reflexes", "", "B0", 0)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "respiration",
                label = "Respiration (R)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Not intubated, regular breathing", "", "R4", 4),
                    ChoiceOption("3", "Not intubated, Cheyne-Stokes pattern", "", "R3", 3),
                    ChoiceOption("2", "Not intubated, irregular breathing", "", "R2", 2),
                    ChoiceOption("1", "Breathes above ventilator rate", "", "R1", 1),
                    ChoiceOption("0", "Breathes at ventilator rate or apnea", "", "R0", 0)
                )
            )
        )
    ),
    CalculatorDef(
        id = "neuro_mod_fisher",
        departmentId = DepartmentId.NEURO,
        name = "Modified Fisher Scale (SAH)",
        shortSubtitle = "Subarachnoid hemorrhage vasospasm risk",
        synonyms = listOf("fisher", "sah", "subarachnoid", "aneurysm", "vasospasm", "nimodipine"),
        formulaSummary = "Grade 0: No SAH/IVH (0%) | Grade 1: Thin SAH, no IVH (24%) | Grade 2: Thin SAH + IVH (33%) | Grade 3: Thick SAH, no IVH (33%) | Grade 4: Thick SAH + IVH (40%)",
        clinicalPearl = "Grades 3–4 carry the highest delayed cerebral ischemia (DCI) and symptomatic vasospasm risk (peak Days 4–14). Ensure oral nimodipine 60 mg q4h and euvolemia are initiated.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "grade",
                label = "Non-Contrast CT Head Appearance",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Grade 0 — No SAH or IVH", "Symptomatic vasospasm: 0%", "0", 0),
                    ChoiceOption("1", "Grade 1 — Thin SAH, No IVH", "Focal or diffuse thin SAH (<1 mm)", "1", 1),
                    ChoiceOption("2", "Grade 2 — Thin SAH + IVH", "Thin SAH with bilateral IVH", "2", 2),
                    ChoiceOption("3", "Grade 3 — Thick SAH, No IVH", "Completely fills ≥1 cistern/fissure", "3", 3),
                    ChoiceOption("4", "Grade 4 — Thick SAH + IVH", "Thick SAH with bilateral IVH", "4", 4)
                )
            )
        )
    ),
    CalculatorDef(
        id = "neuro_mrs",
        departmentId = DepartmentId.NEURO,
        name = "Modified Rankin Scale (mRS)",
        shortSubtitle = "Post-stroke functional disability grade",
        synonyms = listOf("mrs", "rankin", "stroke", "thrombectomy", "disability", "functional"),
        formulaSummary = "0: No symptoms | 1: No significant disability | 2: Slight disability (Independent) | 3: Moderate disability | 4: Moderately severe | 5: Severe (Bedridden) | 6: Dead",
        clinicalPearl = "Baseline pre-stroke mRS 0–2 is a key eligibility criterion for mechanical thrombectomy and stroke outcome trials. Scores 0–2 denote functional independence.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "mrs_grade",
                label = "Functional Status",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "0 — No Symptoms", "Completely asymptomatic", "0", 0),
                    ChoiceOption("1", "1 — No Significant Disability", "Able to carry out all usual activities despite symptoms", "1", 1),
                    ChoiceOption("2", "2 — Slight Disability", "Unable to do all prior activities; looks after own affairs", "2", 2),
                    ChoiceOption("3", "3 — Moderate Disability", "Requires some help, walks unassisted", "3", 3),
                    ChoiceOption("4", "4 — Moderately Severe Disability", "Unable to walk or attend bodily needs unassisted", "4", 4),
                    ChoiceOption("5", "5 — Severe Disability", "Bedridden, incontinent, requires constant nursing care", "5", 5),
                    ChoiceOption("6", "6 — Dead", "Deceased", "6", 6)
                )
            )
        )
    )
)
