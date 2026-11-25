package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: ICU & CRITICAL CARE (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class VentPresetDef(
    val id: String,
    val label: String,
    val mode: String,
    val metrics: List<Pair<String, String>>,
    val hardLimitOrGoal: String,
    val isWarningGoal: Boolean = false
)

data class VentPressurePathway(
    val id: String,
    val toggleLabel: String,
    val headline: String,
    val gradientRule: String,
    val causes: List<String>,
    val actions: List<String>
)

data class DopesItem(
    val letter: String,
    val title: String,
    val detail: String
)

data class SbtChecklistItem(
    val id: String,
    val label: String,
    val detail: String
)

enum class RassTone {
    AGITATED,
    TARGET,
    MODERATE,
    DEEP
}

data class RassRow(
    val score: String,
    val term: String,
    val description: String,
    val tone: RassTone
)

data class CamIcuFeature(
    val number: String,
    val title: String,
    val rule: String,
    val connector: String? = null
)

data class AbcdefItem(
    val letter: String,
    val title: String,
    val detail: String
)

data class FastHugsItem(
    val letter: String,
    val title: String,
    val detail: String
)

data class IcuElectrolyteCapRow(
    val electrolyte: String,
    val limitBadge: String,
    val guidance: String
)

object IcuKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Initial Mechanical Ventilation Presets
    // ------------------------------------------------------------------------
    val ventPresets: List<VentPresetDef> = listOf(
        VentPresetDef(
            id = "ards",
            label = "ARDS",
            mode = "AC-VC · Lung-Protective",
            metrics = listOf(
                "Tidal Volume (Vt)" to "6 mL/kg PBW (4–8)",
                "Resp Rate (RR)" to "16–24 /min",
                "PEEP" to "≥5–10 cmH₂O (ARDSNet)",
                "FiO₂ Target" to "SpO₂ 88–95%",
                "pH / CO₂" to "Permissive hypercapnia ≥7.20",
                "Driving Pressure" to "(Pplat − PEEP) <15"
            ),
            hardLimitOrGoal = "Hard Limit: Keep Pplat ≤30 cmH₂O & Driving Pressure <15 cmH₂O.",
            isWarningGoal = true
        ),
        VentPresetDef(
            id = "copd",
            label = "COPD / Asthma",
            mode = "AC-VC · Obstructive",
            metrics = listOf(
                "Tidal Volume (Vt)" to "6–8 mL/kg PBW",
                "Low Resp Rate" to "10–14 /min (max exp time)",
                "I:E Ratio" to "1:3 to 1:5",
                "Insp Flow" to "60–80 L/min (high)",
                "PEEP" to "0–5 cmH₂O (low)",
                "SpO₂ Target" to "88–92% (COPD)"
            ),
            hardLimitOrGoal = "Check expiratory hold regularly for Auto-PEEP (breath stacking).",
            isWarningGoal = true
        ),
        VentPresetDef(
            id = "normal",
            label = "Normal Lungs",
            mode = "AC-VC · Post-Op / Neuro",
            metrics = listOf(
                "Tidal Volume (Vt)" to "6–8 mL/kg PBW",
                "Resp Rate (RR)" to "12–16 /min",
                "PEEP" to "5 cmH₂O",
                "FiO₂ Wean" to "Rapidly <0.40 (SpO₂ 94–98%)"
            ),
            hardLimitOrGoal = "In TBI: keep PaCO₂ 35–40 mmHg; avoid hyperventilation unless acute herniation.",
            isWarningGoal = false
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: High Peak vs. Plateau Pressure Troubleshooter + DOPES
    // ------------------------------------------------------------------------
    const val inspiratoryHoldStep =
        "Perform an Inspiratory Hold (0.5–1.0 sec) to measure Pplat and calculate Ppeak − Pplat."

    val pressurePathways: List<VentPressurePathway> = listOf(
        VentPressurePathway(
            id = "resistance",
            toggleLabel = "High Peak · Normal Pplat",
            headline = "AIRWAY RESISTANCE PROBLEM",
            gradientRule = "Ppeak − Pplat > 5 cmH₂O",
            causes = listOf(
                "Bronchospasm (asthma / COPD flare).",
                "Mucus plug or thick airway secretions.",
                "Kinked or bitten endotracheal tube (ETT).",
                "Small ETT diameter or HME filter occlusion."
            ),
            actions = listOf(
                "Suction ETT immediately & verify catheter passage.",
                "Inspect circuit/ETT for kinks; insert bite block.",
                "Give nebulized bronchodilators (Salbutamol + Ipratropium)."
            )
        ),
        VentPressurePathway(
            id = "compliance",
            toggleLabel = "High Peak · High Pplat",
            headline = "DECREASED COMPLIANCE PROBLEM",
            gradientRule = "Pplat > 30 cmH₂O · Gradient ≤ 5",
            causes = listOf(
                "ARDS or acute pulmonary edema.",
                "Tension pneumothorax (check unilateral breath sounds!).",
                "Right mainstem intubation.",
                "Auto-PEEP, abdominal compartment, or stiff chest wall."
            ),
            actions = listOf(
                "Auscultate bilateral breath sounds & check ETT depth.",
                "Perform expiratory hold to check for Auto-PEEP.",
                "Get bedside lung ultrasound / CXR; lower Vt (4–6 mL/kg)."
            )
        )
    )

    val dopesChecklist: List<DopesItem> = listOf(
        DopesItem("D", "Displacement", "ETT dislodged or migrated into right mainstem."),
        DopesItem("O", "Obstruction", "Secretions, mucus plug, kinked tube, or biting."),
        DopesItem("P", "Pneumothorax", "Tension PTX: absent unilateral sounds + shock → needle decompression."),
        DopesItem("E", "Equipment Failure", "Disconnect ventilator & bag manually with 100% O₂ immediately!"),
        DopesItem("S", "Stacked Breaths", "Auto-PEEP: disconnect circuit 5–10 sec to let lungs empty.")
    )

    // ------------------------------------------------------------------------
    // CARD 3: Weaning & SBT Readiness Checklist
    // ------------------------------------------------------------------------
    val sbtChecklist: List<SbtChecklistItem> = listOf(
        SbtChecklistItem("cause", "Underlying cause improving", "Primary reason for respiratory failure resolving."),
        SbtChecklistItem("oxy", "Adequate oxygenation", "FiO₂ ≤0.40–0.50 & PEEP ≤5–8 cmH₂O (P/F >150–200)."),
        SbtChecklistItem("hemo", "Hemodynamically stable", "No vasopressors or minimal low-dose infusion."),
        SbtChecklistItem("neuro", "Awake & initiating breaths", "Off heavy sedation; able to follow simple commands."),
        SbtChecklistItem("airway", "Airway protection intact", "Strong cough & minimal secretions.")
    )

    val sbtThresholdTiles: List<Pair<String, String>> = listOf(
        "SBT Protocol" to "30–120 min (PS 5–8 / PEEP 5 or T-piece)",
        "RSBI (RR / Vt L)" to "<105 breaths/min/L predicts success"
    )

    const val cuffLeakPearl =
        "Cuff Leak Test if intubated >6d, traumatic intubation, or airway edema risk (leak <110 mL or <10% → consider IV steroids before extubation)."

    val sbtFailureCriteria: List<String> = listOf(
        "RR >35 /min or SpO₂ <90%.",
        "HR >140 /min or >20% jump from baseline.",
        "SBP >180 mmHg or <90 mmHg.",
        "Agitation, diaphoresis, or paradoxical abdominal breathing."
    )

    // ------------------------------------------------------------------------
    // CARD 4: Sedation & Delirium Scales (RASS, CPOT, CAM-ICU, ABCDEF)
    // ------------------------------------------------------------------------
    val rassRows: List<RassRow> = listOf(
        RassRow("+4", "Combative", "Overtly combative, violent, immediate staff danger", RassTone.AGITATED),
        RassRow("+3", "Very Agitated", "Pulls or removes tubes/catheters; aggressive", RassTone.AGITATED),
        RassRow("+2", "Agitated", "Frequent non-purposeful movement; fights ventilator", RassTone.AGITATED),
        RassRow("+1", "Restless", "Anxious/apprehensive; movements not vigorous", RassTone.AGITATED),
        RassRow("0", "Alert & Calm", "Spontaneously pays attention (Ideal Target)", RassTone.TARGET),
        RassRow("-1", "Drowsy", "Awakens >10s with eye contact to voice (ICU Target)", RassTone.TARGET),
        RassRow("-2", "Light Sedation", "Briefly awakens <10s with eye contact (ICU Target)", RassTone.TARGET),
        RassRow("-3", "Moderate Sedation", "Movement or eye opening to voice (no eye contact)", RassTone.MODERATE),
        RassRow("-4", "Deep Sedation", "No response to voice; moves to physical stimulation", RassTone.DEEP),
        RassRow("-5", "Unarousable", "No response to voice or physical stimulation", RassTone.DEEP)
    )

    val camIcuFeatures: List<CamIcuFeature> = listOf(
        CamIcuFeature(
            number = "F1",
            title = "Acute Onset or Fluctuating Course",
            rule = "Mental status change from baseline OR fluctuated in past 24h.",
            connector = "AND"
        ),
        CamIcuFeature(
            number = "F2",
            title = "Inattention (SAVEAHAART)",
            rule = "Squeeze hand on every letter 'A' → >2 errors = positive.",
            connector = "AND EITHER"
        ),
        CamIcuFeature(
            number = "F3",
            title = "Altered Level of Consciousness",
            rule = "Current RASS ≠ 0.",
            connector = "OR"
        ),
        CamIcuFeature(
            number = "F4",
            title = "Disorganized Thinking",
            rule = ">1 error on 4 simple yes/no questions + 2-step command."
        )
    )

    const val camIcuUtaNote =
        "Skip CAM-ICU if RASS is -4 or -5 (UTA — Unable to Assess)."

    val abcdefBundle: List<AbcdefItem> = listOf(
        AbcdefItem("A", "Assess & Manage Pain", "CPOT ≥3 = significant pain; treat pain BEFORE sedatives."),
        AbcdefItem("B", "Both SAT & SBT Daily", "Daily Spontaneous Awakening Trial paired with SBT."),
        AbcdefItem("C", "Choice of Analgesia / Sedation", "Prefer Propofol or Dexmedetomidine over Benzodiazepines."),
        AbcdefItem("D", "Delirium Management", "Screen routinely (CAM-ICU), reorient, protect sleep."),
        AbcdefItem("E", "Early Mobility & Exercise", "Active PT / mobilization as soon as hemodynamically safe."),
        AbcdefItem("F", "Family Engagement", "Involve family in orientation, communication, & goals.")
    )

    // ------------------------------------------------------------------------
    // CARD 5: ICU Prophylaxis (FAST HUGS BID) & Electrolyte Caps
    // ------------------------------------------------------------------------
    val fastHugsBid: List<FastHugsItem> = listOf(
        FastHugsItem("F", "Feeding", "Start enteral nutrition within 24–48h."),
        FastHugsItem("A", "Analgesia", "Assess & treat pain first (CPOT <3)."),
        FastHugsItem("S", "Sedation", "Target RASS 0 to -2; daily sedation vacation."),
        FastHugsItem("T", "Thromboprophylaxis", "LMWH/UFH daily (or SCDs if bleeding)."),
        FastHugsItem("H", "Head of Bed 30–45°", "Elevate HOB 30–45° (core VAP bundle)."),
        FastHugsItem("U", "Ulcer Prophylaxis", "PPI/H2RA only if vent >48h, coagulopathy, or GI bleed hx."),
        FastHugsItem("G", "Glucose Control", "Target 140–180 mg/dL; avoid tight <110 hypoglycemia."),
        FastHugsItem("S", "Spontaneous Breathing", "Daily SBT readiness screen."),
        FastHugsItem("B", "Bowel Regimen", "Prevent constipation / ileus early."),
        FastHugsItem("I", "Indwelling Catheters", "Review & remove Foley & central lines daily!"),
        FastHugsItem("D", "De-escalate Antibiotics", "Review cultures & narrow coverage at 48–72h.")
    )

    const val vapPreventionStrip =
        "HOB 30–45° · Daily SAT + SBT · Subglottic suctioning · Oral care · Avoid unnecessary circuit changes."

    val icuElectrolyteCaps: List<IcuElectrolyteCapRow> = listOf(
        IcuElectrolyteCapRow(
            electrolyte = "Potassium (KCl)",
            limitBadge = "Periph ≤10 · Cent ≤20 mEq/hr",
            guidance = "Peripheral max 40 mEq/L; central requires continuous ECG. Always check & replace Mg²⁺ in refractory hypoK."
        ),
        IcuElectrolyteCapRow(
            electrolyte = "Magnesium (MgSO₄)",
            limitBadge = "1–2 g IV",
            guidance = "Mild: 1–2 g over 1–2h; Severe/Arrhythmia: 2 g over 10–20 min. Halve dose if eGFR <30."
        ),
        IcuElectrolyteCapRow(
            electrolyte = "Phosphate (KPhos / NaPhos)",
            limitBadge = "15–30 mmol / 4–6h",
            guidance = "Infuse slowly over 4–6h — rapid push causes acute hypocalcemia & tetany."
        ),
        IcuElectrolyteCapRow(
            electrolyte = "Calcium (10% Ca Gluconate)",
            limitBadge = "1–2 g / 10–20 min",
            guidance = "10–20 mL IV; preferred peripherally over CaCl₂ due to extravasation tissue necrosis risk."
        )
    )
}
