package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: EMERGENCY & ANESTHESIA (5 INTERACTIVE CARDS)
// ============================================================================

data class RsiStepItem(
    val stepNumber: String,
    val timing: String,
    val title: String,
    val detail: String,
    val isCritical: Boolean = false
)

data class AirwayMnemonicCard(
    val acronym: String,
    val purpose: String,
    val letters: List<Pair<String, String>>
)

data class OxygenDeviceRow(
    val device: String,
    val flowRate: String,
    val fio2: String,
    val note: String
)

data class NivModeRow(
    val mode: String,
    val initialSettings: String,
    val bestFor: String,
    val pearl: String
)

data class ProceduralSedationRow(
    val drug: String,
    val ivDose: String,
    val onsetDur: String,
    val caution: String
)

data class TraumaSurveyRow(
    val letter: String,
    val domain: String,
    val actions: String,
    val isUrgent: Boolean = false
)

data class FastingRuleRow(
    val hours: String,
    val category: String,
    val examples: String
)

data class PreOpHoldRow(
    val drug: String,
    val holdWindow: String,
    val rationale: String,
    val isHighRisk: Boolean = false
)

object EmergencyAnesthesiaKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Airway & RSI 7 Ps
    // ------------------------------------------------------------------------
    val rsiSevenPs: List<RsiStepItem> = listOf(
        RsiStepItem("1", "-10m", "Preparation (SOAPME)", "Suction, Oxygen, Airway/backup blades/bougie, Pharmacology drawn, Monitoring + Waveform ETCO₂, Equipment/2 IVs."),
        RsiStepItem("2", "-5m", "Preoxygenation", "100% FiO₂ NRB/BVM tight seal 3–5 min (or 8 VC breaths) + Apneic O₂ (NC 15 L/min); head up 20–30°."),
        RsiStepItem("3", "-3m", "Pretreatment / Optimization", "Resuscitate BEFORE intubating — correct hypotension & hypoxia first ('HOP killers').", isCritical = true),
        RsiStepItem("4", "0m", "Paralysis with Induction", "Push induction agent + immediate paralytic (Ketamine/Etomidate + Rocuronium/Suxamethonium)."),
        RsiStepItem("5", "+30s", "Protection & Positioning", "Ear-to-sternal-notch ramping; avoid routine cricoid pressure if it worsens glottic view."),
        RsiStepItem("6", "+45–60s", "Placement with Proof", "Confirm ETT strictly by continuous Waveform ETCO₂ + bilateral air entry + CXR depth (21 cm F / 23 cm M).", isCritical = true),
        RsiStepItem("7", "Post", "Post-Intubation Management", "Secure tube, start analgesia + sedation (never leave paralyzed awake!), set lung-protective vent, recheck BP.")
    )

    val difficultAirwayMnemonics: List<AirwayMnemonicCard> = listOf(
        AirwayMnemonicCard(
            acronym = "LEMON",
            purpose = "Difficult Laryngoscopy",
            letters = listOf(
                "L" to "Look externally (trauma, beard, small mandible)",
                "E" to "Evaluate 3-3-2 rule (mouth, hyoid-mentum, thyroid-floor)",
                "M" to "Mallampati score III–IV",
                "O" to "Obstruction or Obesity",
                "N" to "Neck mobility restriction (C-collar / arthritis)"
            )
        ),
        AirwayMnemonicCard(
            acronym = "MOANS",
            purpose = "Difficult BVM Ventilation",
            letters = listOf(
                "M" to "Mask seal difficulty (beard, facial trauma)",
                "O" to "Obesity or Obstruction",
                "A" to "Age >55 years",
                "N" to "No teeth (edentulous)",
                "S" to "Stiff lungs (ARDS, severe asthma, edema)"
            )
        ),
        AirwayMnemonicCard(
            acronym = "RODS",
            purpose = "Difficult SGA / LMA",
            letters = listOf(
                "R" to "Restricted mouth opening",
                "O" to "Obstruction (upper airway mass/edema)",
                "D" to "Distorted anatomy",
                "S" to "Stiff lungs or stiff C-spine"
            )
        )
    )

    val cicoFailedAirwaySteps: List<String> = listOf(
        "Max 3 laryngoscopy attempts (optimize blade, operator, bougie, or video laryngoscope).",
        "Rescue immediately with 2nd-Generation Supraglottic Airway (i-gel / LMA).",
        "CICO (Can't Intubate, Can't Oxygenate): Immediate Front-of-Neck Access — Scalpel-Bougie-Tube Cricothyroidotomy."
    )

    // ------------------------------------------------------------------------
    // CARD 2: Oxygen Delivery & NIV Guide
    // ------------------------------------------------------------------------
    val oxygenDevices: List<OxygenDeviceRow> = listOf(
        OxygenDeviceRow("Nasal Cannula (NC)", "1–6 L/min", "~24–44%", "Add ~4% FiO₂ per 1 L/min."),
        OxygenDeviceRow("Simple Face Mask", "6–10 L/min", "~35–55%", "NEVER run <5 L/min (causes CO₂ rebreathing!)."),
        OxygenDeviceRow("Venturi Mask", "Fixed valve", "24%–60%", "Blue 24% · White 28% · Yellow 35% · Red 40% · Green 60% — Best for COPD (88–92%)."),
        OxygenDeviceRow("Non-Rebreather (NRB)", "10–15 L/min", "~80–90%", "Inflate reservoir bag completely before placing on patient."),
        OxygenDeviceRow("HFNC", "30–60 L/min", "21–100%", "Heated/humidified + PEEP 2–5 cmH₂O + dead-space washout; track ROX Index at 2h/6h/12h.")
    )

    val nivModes: List<NivModeRow> = listOf(
        NivModeRow(
            mode = "CPAP",
            initialSettings = "5–10 cmH₂O",
            bestFor = "Acute Cardiogenic Pulmonary Edema (SCAPE) & OSA",
            pearl = "Recruits alveoli & rapidly lowers preload and afterload."
        ),
        NivModeRow(
            mode = "BiPAP",
            initialSettings = "IPAP 10–12 / EPAP 5 cmH₂O (keep Δ ≥5)",
            bestFor = "COPD Exacerbation with Hypercapnic Acidosis (pH 7.25–7.35)",
            pearl = "↑IPAP blows off CO₂; ↑EPAP/FiO₂ improves oxygenation."
        )
    )

    val nivContraindications: List<String> = listOf(
        "Cardiac or respiratory arrest; inability to protect airway / GCS <8.",
        "Copious secretions, active vomiting, or upper GI bleeding.",
        "Facial trauma / burns, undrained pneumothorax, or hemodynamic shock.",
        "Reassess ABG & work of breathing within 30–60 min — NEVER delay intubation if worsening!"
    )

    // ------------------------------------------------------------------------
    // CARD 3: Sedation & Analgesia Ladder
    // ------------------------------------------------------------------------
    val proceduralSedationDrugs: List<ProceduralSedationRow> = listOf(
        ProceduralSedationRow("Propofol", "0.5–1 mg/kg load, then 0.25–0.5 mg/kg q3m", "Onset <1m · Dur 5–10m", "Hypotension, apnea, zero analgesia."),
        ProceduralSedationRow("Ketamine", "1–1.5 mg/kg IV (over 60s) or 4 mg/kg IM", "Onset 1m IV · Dur 15–20m", "Emergence agitation, laryngospasm, ↑BP/HR; preserves airway."),
        ProceduralSedationRow("Etomidate", "0.1–0.15 mg/kg IV", "Onset <1m · Dur 5–15m", "Myoclonus, zero analgesia; hemodynamically neutral."),
        ProceduralSedationRow("Midazolam", "1–2 mg slow IV titrate", "Onset 2–3m · Dur 30–60m", "Respiratory depression — synergistic with opioids!"),
        ProceduralSedationRow("Fentanyl", "0.5–1 mcg/kg (25–50 mcg) IV", "Onset 1–2m · Dur 30–45m", "Chest wall rigidity if pushed too rapidly.")
    )

    const val sedationMonitoringStrip =
        "Mandatory Monitoring: Continuous SpO₂, ECG, NIBP q5m, Waveform ETCO₂, dedicated airway rescuer + suction + BVM."

    val whoPainLadder: List<Pair<String, String>> = listOf(
        "Step 1 · Mild" to "Paracetamol + NSAID (avoid NSAID in CKD, GI bleed, elderly) ± adjuvants.",
        "Step 2 · Moderate" to "Add weak opioid (Tramadol / Codeine) or low-dose strong opioid.",
        "Step 3 · Severe" to "Strong opioid (Morphine / Fentanyl / Oxycodone / Hydromorphone) + non-opioid + bowel regimen."
    )

    val reversalAgents: List<String> = listOf(
        "Opioid Reversal: Naloxone 0.04–0.4 mg IV titrated to RR ≥12.",
        "Benzodiazepine Reversal: Flumazenil 0.2 mg IV (AVOID in chronic benzo use, TCA co-ingestion, or seizure history!)."
    )

    // ------------------------------------------------------------------------
    // CARD 4: Trauma Survey & Clinical Imaging Rules
    // ------------------------------------------------------------------------
    val atlsPrimarySurvey: List<TraumaSurveyRow> = listOf(
        TraumaSurveyRow("x", "Exsanguinating Hemorrhage", "Direct pressure, tourniquet, or pelvic binder FIRST.", isUrgent = true),
        TraumaSurveyRow("A", "Airway + C-Spine", "Patent airway, jaw thrust, inline cervical stabilization."),
        TraumaSurveyRow("B", "Breathing", "Bilateral air entry, SpO₂, rule out tension/open PTX & flail chest."),
        TraumaSurveyRow("C", "Circulation", "2 large-bore IVs (16–18G), warm crystalloid / 1:1:1 blood, eFAST, TXA 1g <3h."),
        TraumaSurveyRow("D", "Disability", "GCS, pupil symmetry/reactivity, bedside capillary glucose."),
        TraumaSurveyRow("E", "Exposure + Environment", "Log-roll & full head-to-toe exam + prevent hypothermia (warm blankets/fluids).")
    )

    val canadianCSpineSteps: List<Pair<String, String>> = listOf(
        "Step 1 · High-Risk Factor?" to "Age ≥65, Dangerous mechanism, or Paresthesias → IMAGE C-SPINE.",
        "Step 2 · Low-Risk Factor?" to "Simple rear-end MVC, sitting in ED, ambulatory at any time, delayed neck pain, or no midline tenderness → If NONE, IMAGE.",
        "Step 3 · Active Rotation 45°?" to "Able to actively rotate neck 45° left & right? YES → No Imaging Needed · NO → IMAGE."
    )

    val ottawaRules: List<Pair<String, String>> = listOf(
        "Ankle X-Ray" to "Malleolar pain + tenderness along posterior 6 cm / tip of lateral OR medial malleolus, OR unable to bear weight 4 steps.",
        "Midfoot X-Ray" to "Midfoot pain + tenderness at Navicular OR Base of 5th Metatarsal, OR unable to bear weight 4 steps.",
        "Knee X-Ray (Any 1)" to "Age ≥55 · Isolated patellar tenderness · Fibular head tenderness · Cannot flex knee 90° · Unable to bear weight 4 steps."
    )

    // ------------------------------------------------------------------------
    // CARD 5: Pre-Op Fasting & Drug Hold Matrix
    // ------------------------------------------------------------------------
    val fastingRules: List<FastingRuleRow> = listOf(
        FastingRuleRow("2 Hours", "Clear Liquids", "Water, black tea/coffee without milk, pulp-free juice."),
        FastingRuleRow("4 Hours", "Breast Milk", "Human breast milk for infants."),
        FastingRuleRow("6 Hours", "Formula / Light Meal", "Infant formula, non-human milk, toast & clear liquid."),
        FastingRuleRow("8 Hours", "Full / Fatty Meal", "Fried or fatty foods, meat, heavy full meal.")
    )

    const val fastingExceptionNote =
        "Always confirm local institutional exceptions (emergency surgery, gastroparesis, or GLP-1 agonist users)."

    const val preOpContinueList =
        "Beta-blockers · Statins · CCBs · Antiepileptics · Bronchodilators/Inhalers · Chronic Steroids (consider IV Hydrocortisone stress dose) · Thyroid meds · Aspirin (high CV risk/stents — confirm in neuro/eye cases)."

    val preOpHoldList: List<PreOpHoldRow> = listOf(
        PreOpHoldRow("SGLT2 Inhibitors", "Hold 3–4 days", "Empagliflozin/Dapagliflozin — euglycemic DKA risk!", isHighRisk = true),
        PreOpHoldRow("Metformin", "Hold morning of", "Resume once eGFR stable & tolerating PO."),
        PreOpHoldRow("ACEi / ARBs", "Hold morning of", "Prevents refractory intra-operative hypotension."),
        PreOpHoldRow("Warfarin", "Hold 5 days", "Check INR <1.5; bridge only if high VTE / mechanical valve risk."),
        PreOpHoldRow("DOACs", "Hold 24–48 hrs", "Extend window if low CrCl or high-bleed (neuro/spinal) surgery."),
        PreOpHoldRow("Clopidogrel / Ticagrelor", "Hold 5–7 days", "NEVER stop inside stent critical window without Cardiology sign-off!", isHighRisk = true),
        PreOpHoldRow("NSAIDs", "Hold 3–7 days", "Platelet dysfunction / surgical bleeding risk."),
        PreOpHoldRow("GLP-1 Agonists", "Day of / 1 wk prior", "Hold day of (daily) or 1 week prior (weekly) per aspiration guidance.")
    )
}
