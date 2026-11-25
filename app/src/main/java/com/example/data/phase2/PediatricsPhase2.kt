package com.example.data.phase2

import com.example.data.CalcInputDef
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentId

val PediatricsPhase2Calculators: List<CalculatorDef> = listOf(
    CalculatorDef(
        id = "peads_gcs",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Pediatric GCS",
        shortSubtitle = "Infant (<2y) & child (≥2y) coma scale",
        synonyms = listOf("pediatric gcs", "peads gcs", "infant gcs", "coma", "head injury", "tbi"),
        formulaSummary = "Eye (1–4) + Age-Specific Verbal (1–5) + Motor (1–6) = 3–15\n13–15 Mild | 9–12 Moderate | ≤8 Severe (Airway Protection)",
        clinicalPearl = "For infants <2 years, verbal scoring assesses cooing, babbling, consolability, and cry quality rather than orientation. Always document E_ V_ M_ subscores.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "age_group",
                label = "Pediatric Age Group",
                options = listOf(
                    ChoiceOption("infant", "Infant (<2 yrs)"),
                    ChoiceOption("child", "Child (≥2 yrs)")
                ),
                defaultOptionId = "infant"
            ),
            CalcInputDef.SegmentedChoice(
                id = "eye",
                label = "Eye Opening (E)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("4", "Spontaneous", "Opens eyes spontaneously", "E4", 4),
                    ChoiceOption("3", "To Speech / Shout", "Opens to voice", "E3", 3),
                    ChoiceOption("2", "To Pain", "Opens to painful stimulus", "E2", 2),
                    ChoiceOption("1", "None", "No eye opening", "E1", 1)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "verbal_infant",
                label = "Verbal Response — Infant <2 yrs (V)",
                compactRow = false,
                visibleWhenChoiceKey = "age_group",
                visibleWhenChoiceValue = "infant",
                options = listOf(
                    ChoiceOption("5", "Coos & Babbles", "Appropriate smile/interaction", "V5", 5),
                    ChoiceOption("4", "Irritable Cry", "Cries but consolable", "V4", 4),
                    ChoiceOption("3", "Cries to Pain", "Inconsolable / cries to pain", "V3", 3),
                    ChoiceOption("2", "Moans to Pain", "Restless / groans to pain", "V2", 2),
                    ChoiceOption("1", "None", "No vocal response", "V1", 1)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "verbal_child",
                label = "Verbal Response — Child ≥2 yrs (V)",
                compactRow = false,
                visibleWhenChoiceKey = "age_group",
                visibleWhenChoiceValue = "child",
                options = listOf(
                    ChoiceOption("5", "Oriented / Appropriate Words", "Interactive, coherent speech", "V5", 5),
                    ChoiceOption("4", "Confused / Disoriented", "Converses but confused", "V4", 4),
                    ChoiceOption("3", "Inappropriate Words", "Random or exclamatory words", "V3", 3),
                    ChoiceOption("2", "Incomprehensible Sounds", "Moans or groans only", "V2", 2),
                    ChoiceOption("1", "None", "No verbal response", "V1", 1)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "motor",
                label = "Motor Response (M)",
                compactRow = false,
                options = listOf(
                    ChoiceOption("6", "Spontaneous / Obeys Commands", "Normal purposeful movement", "M6", 6),
                    ChoiceOption("5", "Withdraws to Touch / Localizes", "Purposeful localization", "M5", 5),
                    ChoiceOption("4", "Withdraws from Pain", "Normal flexion withdrawal", "M4", 4),
                    ChoiceOption("3", "Abnormal Flexion", "Decorticate posturing", "M3", 3),
                    ChoiceOption("2", "Abnormal Extension", "Decerebrate posturing", "M2", 2),
                    ChoiceOption("1", "None", "Flaccid / no motor response", "M1", 1)
                )
            )
        )
    ),
    CalculatorDef(
        id = "peads_urine_output",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Pediatric Urine Output",
        shortSubtitle = "Oliguria check in mL/kg/hr",
        synonyms = listOf("urine output", "oliguria", "aki", "diaper", "hydration", "fluid balance"),
        formulaSummary = "Urine Output = Volume (mL) / [Weight (kg) × Duration (hrs)]  in mL/kg/hr\n<0.5 Severe Oliguria | 0.5–1.0 Borderline (OK older child; low infant) | >1.0–2.0+ Adequate",
        clinicalPearl = "Minimum adequate urine output targets: Neonates/Infants ≥1.5–2.0 mL/kg/hr, Young Children ≥1.0 mL/kg/hr, Adolescents/Adults ≥0.5 mL/kg/hr.",
        inputs = listOf(
            CalcInputDef.NumericField("weight", "Patient Weight", "Weight", listOf("kg", "lbs")),
            CalcInputDef.NumericField("volume", "Urine Volume", "mL", listOf("mL"), allowZero = true),
            CalcInputDef.NumericField("hours", "Collection Duration", "Hours", listOf("hrs"))
        )
    ),
    CalculatorDef(
        id = "peads_pews",
        departmentId = DepartmentId.PEDIATRICS,
        name = "PEWS (Pediatric Early Warning)",
        shortSubtitle = "Brighton pediatric deterioration score",
        synonyms = listOf("pews", "pediatric early warning score", "brighton pews", "ews", "deterioration"),
        formulaSummary = "Behavior (0–3) + Cardiovascular (0–3) + Respiratory (0–3) + Q15m Nebs (+2) + Persistent Post-Op Vomiting (+2) = 0–13.\n0–2 Low | 3–4 Moderate (Charge RN + ↑Obs) | ≥5 High Risk (Urgent Doctor Review)",
        clinicalPearl = "Children maintain normal blood pressure until late decompensated shock—tachycardia, delayed capillary refill, and lethargy in PEWS catch deterioration far earlier than hypotension.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "behavior",
                label = "Behavior / Neuro",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Playing / Appropriate", "Alert & interactive", "0", 0),
                    ChoiceOption("1", "Sleeping", "Easily rousable", "+1", 1),
                    ChoiceOption("2", "Irritable", "Difficult to console", "+2", 2),
                    ChoiceOption("3", "Lethargic / Confused", "Or reduced response to pain", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "cardio",
                label = "Cardiovascular",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Pink, CRT 1–2 sec", "Normal perfusion", "0", 0),
                    ChoiceOption("1", "Pale or CRT 3 sec", "Mild hypoperfusion", "+1", 1),
                    ChoiceOption("2", "Grey, CRT 4 sec, or HR +20 above normal", "Moderate cardiovascular stress", "+2", 2),
                    ChoiceOption("3", "Grey & Mottled, CRT ≥5s, HR +30 or Brady", "Severe shock / decompensation", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "resp",
                label = "Respiratory",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "Normal RR, No Retractions", "Comfortable breathing", "0", 0),
                    ChoiceOption("1", "RR +10, Accessory Muscles, or FiO₂ ≥30% / 3 L/m", "Mild distress", "+1", 1),
                    ChoiceOption("2", "RR +20, Retractions, or FiO₂ ≥40% / 6 L/m", "Moderate distress", "+2", 2),
                    ChoiceOption("3", "RR ≥5 below norm + Retractions/Grunting or FiO₂ ≥50%", "Severe respiratory distress", "+3", 3)
                )
            ),
            CalcInputDef.SectionHeader("pews_bonus", "Additional Clinical Modifiers (+2 each)"),
            CalcInputDef.ToggleCriterion("nebs", "Every 15-Minute Nebulizers", "Continuous or q15m bronchodilator therapy", "+2", 2),
            CalcInputDef.ToggleCriterion("vomiting", "Persistent Post-Op Vomiting", "Ongoing emesis following surgery", "+2", 2)
        )
    ),
    CalculatorDef(
        id = "peads_westley",
        departmentId = DepartmentId.PEDIATRICS,
        name = "Westley Croup Score",
        shortSubtitle = "Laryngotracheobronchitis severity grading",
        synonyms = listOf("westley", "croup", "stridor", "dexamethasone", "epinephrine", "airway"),
        formulaSummary = "Stridor (0–2) + Retractions (0–3) + Air Entry (0–2) + Cyanosis (0/4/5) + Consciousness (0/5) = 0–17.\n≤2 Mild | 3–7 Moderate | 8–11 Severe | ≥12 Impending Respiratory Failure",
        clinicalPearl = "All croup grades benefit from single-dose dexamethasone (0.15–0.6 mg/kg PO/IM). Moderate-to-severe croup (≥3) with stridor at rest requires nebulized epinephrine and ≥3–4 hrs observation for rebound.",
        inputs = listOf(
            CalcInputDef.SegmentedChoice(
                id = "stridor",
                label = "Inspiratory Stridor",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "None", "", "0", 0),
                    ChoiceOption("1", "When Agitated", "Audible with stethoscope / crying", "+1", 1),
                    ChoiceOption("2", "At Rest", "Audible without stethoscope at rest", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "retractions",
                label = "Chest Wall Retractions",
                options = listOf(
                    ChoiceOption("0", "None", "", "0", 0),
                    ChoiceOption("1", "Mild", "", "+1", 1),
                    ChoiceOption("2", "Mod", "", "+2", 2),
                    ChoiceOption("3", "Severe", "", "+3", 3)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "air_entry",
                label = "Air Entry",
                options = listOf(
                    ChoiceOption("0", "Normal", "", "0", 0),
                    ChoiceOption("1", "Decreased", "", "+1", 1),
                    ChoiceOption("2", "Markedly ↓", "", "+2", 2)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "cyanosis",
                label = "Cyanosis",
                compactRow = false,
                options = listOf(
                    ChoiceOption("0", "None", "", "0", 0),
                    ChoiceOption("4", "With Agitation", "", "+4", 4),
                    ChoiceOption("5", "At Rest", "", "+5", 5)
                )
            ),
            CalcInputDef.SegmentedChoice(
                id = "loc",
                label = "Level of Consciousness",
                options = listOf(
                    ChoiceOption("0", "Normal", "Including sleep", "0", 0),
                    ChoiceOption("5", "Disoriented", "Altered / obtunded", "+5", 5)
                )
            )
        )
    ),
    CalculatorDef(
        id = "peads_wetflag",
        departmentId = DepartmentId.PEDIATRICS,
        name = "WETFLAG Resuscitation",
        shortSubtitle = "APLS pediatric emergency 7-item sheet",
        synonyms = listOf("wetflag", "apls", "pals", "resuscitation", "pediatric arrest", "seizure", "lorazepam", "adrenaline", "defib", "dextrose"),
        formulaSummary = "W: 1–5y (Age+4)×2 kg | 6–12y (Age×3)+7 kg\nE: 4 J/kg (max 200 J) | T: Uncuffed Age/4+4, Cuffed Age/4+3.5 mm\nF: 10 mL/kg (trauma) & 20 mL/kg (sepsis) | L: Lorazepam 0.1 mg/kg (max 4 mg)\nA: Adrenaline 1:10,000 0.1 mL/kg (max 10 mL) | G: 10% Dextrose 2 mL/kg",
        clinicalPearl = "Always use actual measured weight when known (enter in the optional override field). Recheck blood glucose promptly in every collapsed, seizing, or septic child.",
        inputs = listOf(
            CalcInputDef.NumericField("age", "Child Age (1–12 yrs)", "Years", listOf("yrs")),
            CalcInputDef.NumericField("known_weight", "Known Weight Override (Optional)", "Leave blank for APLS est.", listOf("kg", "lbs"), optional = true)
        )
    )
)
