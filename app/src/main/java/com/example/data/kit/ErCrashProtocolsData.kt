package com.example.data.kit

// ============================================================================
// BLOCK A (TIER 1): ER & CRASH PROTOCOLS — 5 INTERACTIVE EMERGENCY CARDS
// ============================================================================

data class ArrestStep(
    val title: String,
    val detail: String
)

data class ArrestAlgorithmData(
    val id: String,
    val label: String,
    val cprMetrics: List<Pair<String, String>>,
    val shockableSteps: List<ArrestStep>,
    val nonShockableSteps: List<ArrestStep>
)

data class TimedLadderStage(
    val timeWindow: String,
    val title: String,
    val bullets: List<String>,
    val isUrgent: Boolean = false
)

data class ProtocolStage(
    val stepTitle: String,
    val badge: String,
    val bullets: List<String>,
    val isRedFlag: Boolean = false
)

data class AntidoteRow(
    val id: String,
    val toxin: String,
    val antidote: String,
    val initialDoseAndPearl: String,
    val isRedFlag: Boolean = false
)

object ErCrashProtocolsData {

    // ------------------------------------------------------------------------
    // CARD 1: ACLS & PALS Arrest Algorithms
    // ------------------------------------------------------------------------
    val adultAcls = ArrestAlgorithmData(
        id = "acls",
        label = "Adult ACLS",
        cprMetrics = listOf(
            "Rate" to "100–120 /min",
            "Depth" to "5–6 cm (2–2.4 in)",
            "Recoil & Pauses" to "Full recoil · <10s pause",
            "Airway & Switch" to "10 breaths/min · Switch q2m"
        ),
        shockableSteps = listOf(
            ArrestStep("1. Immediate Defibrillation", "120–200 J biphasic (manufacturer max) → immediate CPR 2 min."),
            ArrestStep("2. Adrenaline (After 2nd Shock)", "1 mg IV/IO (10 mL of 1:10,000) every 3–5 min."),
            ArrestStep("3. Refractory VF/pVT (After 3rd Shock)", "Amiodarone 300 mg IV/IO (2nd dose 150 mg) OR Lidocaine 1–1.5 mg/kg."),
            ArrestStep("4. Rhythm Check & Airway", "Check rhythm q2min; ETCO₂ waveform capnography (target >10–20 mmHg).")
        ),
        nonShockableSteps = listOf(
            ArrestStep("1. Immediate CPR + Adrenaline ASAP", "Resume CPR 2 min; give Adrenaline 1 mg IV/IO immediately, then q3–5 min."),
            ArrestStep("2. Rhythm Check q2min", "If shockable rhythm develops → shock immediately. If organized → check pulse."),
            ArrestStep("3. Hunt Reversible Causes", "Aggressively treat 5H & 5T (especially hypoxia, hyperkalemia, tension PTX).")
        )
    )

    val pedsPals = ArrestAlgorithmData(
        id = "pals",
        label = "Pediatric PALS",
        cprMetrics = listOf(
            "Rate" to "100–120 /min",
            "Depth" to "1/3 AP diameter (4–5 cm)",
            "Ratio (No Airway)" to "15:2 (2-rescuer) · 30:2 (1)",
            "Advanced Airway" to "1 breath q2–3s (20–30/min)"
        ),
        shockableSteps = listOf(
            ArrestStep("1. Immediate Defibrillation", "2 J/kg → 4 J/kg → up to 10 J/kg (max 200 J) → CPR 2 min."),
            ArrestStep("2. Adrenaline (After 2nd Shock)", "0.01 mg/kg IV/IO (0.1 mL/kg of 1:10,000; max 1 mg) q3–5 min."),
            ArrestStep("3. Refractory VF/pVT (After 3rd Shock)", "Amiodarone 5 mg/kg IV/IO (max 300 mg; repeat ×2) OR Lidocaine 1 mg/kg."),
            ArrestStep("4. Reversible Causes & Glucose", "Check bedside glucose early; correct hypoxia & hypovolemia.")
        ),
        nonShockableSteps = listOf(
            ArrestStep("1. Immediate CPR + Adrenaline ASAP", "Start CPR 2 min; give Adrenaline 0.01 mg/kg IV/IO ASAP, then q3–5 min."),
            ArrestStep("2. Prioritize Airway & Oxygen", "Pediatric arrest is commonly asphyxial — optimize bag-mask ventilation."),
            ArrestStep("3. Rhythm Check q2min", "Switch compressor q2min; evaluate 5H & 5T continuously.")
        )
    )

    val fiveHs: List<String> = listOf(
        "Hypovolemia",
        "Hypoxia",
        "Hydrogen ion (Acidosis)",
        "Hypo / Hyperkalemia",
        "Hypothermia"
    )

    val fiveTs: List<String> = listOf(
        "Tension pneumothorax",
        "Tamponade (cardiac)",
        "Toxins",
        "Thrombosis (pulmonary)",
        "Thrombosis (coronary)"
    )

    // ------------------------------------------------------------------------
    // CARD 2: Anaphylaxis & Status Epilepticus Ladders
    // ------------------------------------------------------------------------
    val anaphylaxisStages: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "1st Line — IM Adrenaline (Immediate)",
            badge = "PRIORITY #1",
            bullets = listOf(
                "IM Adrenaline 1:1,000 into anterolateral thigh (vastus lateralis).",
                "Adult: 0.5 mg (0.5 mL) · Child: 0.01 mg/kg (max 0.5 mg).",
                "Repeat IM every 5 min if airway/BP/breathing not improving."
            ),
            isRedFlag = true
        ),
        ProtocolStage(
            stepTitle = "Positioning, O₂ & IV Fluid Bolus",
            badge = "SUPPORTIVE",
            bullets = listOf(
                "Remove trigger immediately; lay flat with legs elevated (NEVER stand up).",
                "High-flow O₂ (10–15 L/min non-rebreather); prepare early airway if stridor.",
                "IV 0.9% NS rapid bolus: Adult 500–1000 mL · Child 20 mL/kg."
            )
        ),
        ProtocolStage(
            stepTitle = "Refractory Shock / Persistent Hypotension",
            badge = "ESCALATE",
            bullets = listOf(
                "Start IV Adrenaline infusion + call ICU / Anesthesia early.",
                "On beta-blockers with refractory shock? Give IV Glucagon 1–5 mg."
            )
        ),
        ProtocolStage(
            stepTitle = "Adjuncts (Never Delay Adrenaline)",
            badge = "SECONDARY",
            bullets = listOf(
                "Wheeze/bronchospasm: Nebulized Salbutamol 5 mg.",
                "Antihistamine (Cetirizine) / Hydrocortisone are secondary only.",
                "Observe ≥4–6 hours (longer if severe) for biphasic reaction."
            )
        )
    )

    val statusEpilepticusLadder: List<TimedLadderStage> = listOf(
        TimedLadderStage(
            timeWindow = "0–5 MIN",
            title = "Stabilize + 1st-Line Benzodiazepine",
            bullets = listOf(
                "Airway/O₂/IV access + CHECK CAPILLARY GLUCOSE ASAP.",
                "If glucose <70 mg/dL: IV Dextrose + Thiamine 100 mg IV (adults).",
                "Pick ONE: IV Lorazepam 4 mg (0.1 mg/kg) OR IM Midazolam 10 mg (0.2 mg/kg) OR IV Diazepam 10 mg (0.2 mg/kg).",
                "May repeat 1st-line benzo ONCE after 5 min if still seizing."
            ),
            isUrgent = true
        ),
        TimedLadderStage(
            timeWindow = "5–20 MIN",
            title = "2nd-Line Anti-Seizure Load (Pick One)",
            bullets = listOf(
                "IV Levetiracetam 60 mg/kg (max 4500 mg) over 10–15 min.",
                "OR IV Valproate 40 mg/kg (max 3000 mg) — avoid in pregnancy/liver disease.",
                "OR IV Fosphenytoin 20 mg PE/kg (max 1500 mg PE) — continuous ECG & BP monitor."
            )
        ),
        TimedLadderStage(
            timeWindow = "20–40 MIN",
            title = "Refractory Status — ICU & RSI",
            bullets = listOf(
                "Prepare RSI intubation + ICU transfer.",
                "Start continuous anesthetic infusion: Midazolam, Propofol, or Ketamine.",
                "Arrange continuous EEG monitoring & treat underlying cause."
            ),
            isUrgent = true
        )
    )

    // ------------------------------------------------------------------------
    // CARD 3: DKA / HHS & Hyperkalemia Cocktails
    // ------------------------------------------------------------------------
    val dkaDiagnosticBullets: List<String> = listOf(
        "DKA: Glucose >250 mg/dL · pH <7.30 · HCO₃ <18 · Ketones (+) · High Anion Gap.",
        "HHS: Glucose >600 mg/dL · Effective Osm >320 mOsm/kg · Minimal ketones."
    )

    val dkaStages: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "Step 1 — IV Fluids First",
            badge = "HOUR 1",
            bullets = listOf(
                "0.9% NS 1 L IV over 1st hour (caution in CHF/CKD/elderly).",
                "Then 250–500 mL/hr: switch to 0.45% NS if corrected Na⁺ is normal/high."
            )
        ),
        ProtocolStage(
            stepTitle = "Step 2 — Potassium Gate (BEFORE Insulin!)",
            badge = "CRITICAL GATE",
            bullets = listOf(
                "K⁺ < 3.3 mEq/L: HOLD INSULIN! Give 20–40 mEq/hr KCl until K⁺ ≥ 3.3.",
                "K⁺ 3.3–5.2 mEq/L: Add 20–30 mEq KCl per L IVF + start insulin.",
                "K⁺ > 5.2 mEq/L: Start insulin, hold K⁺, recheck K⁺ every 2 hours."
            ),
            isRedFlag = true
        ),
        ProtocolStage(
            stepTitle = "Step 3 — IV Regular Insulin & Closure",
            badge = "0.1 U/kg/hr",
            bullets = listOf(
                "Start IV Regular Insulin 0.1 units/kg/hr (target glucose drop 50–75 mg/dL/hr).",
                "When glucose <200–250 mg/dL: add D5 0.45% NS & cut insulin to 0.05 U/kg/hr.",
                "Continue IV drip until Anion Gap ≤12 AND HCO₃ ≥15–18.",
                "Give SC basal insulin 2 HOURS BEFORE stopping IV insulin drip."
            )
        )
    )

    val hyperkalemiaStages: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "1. Stabilize Myocardium (ECG Changes or K⁺ ≥6.5)",
            badge = "ONSET 1–3 MIN",
            bullets = listOf(
                "IV 10% Calcium Gluconate 10 mL (1 g) over 2–5 min.",
                "Lasts 30–60 min; repeat q5–10 min if peaked T / wide QRS persists.",
                "Does NOT lower serum K⁺ — immediately start shift measures."
            ),
            isRedFlag = true
        ),
        ProtocolStage(
            stepTitle = "2. Shift K⁺ Intracellularly",
            badge = "ONSET 15–30 MIN",
            bullets = listOf(
                "IV Regular Insulin 10 U (5 U if severe CKD/low glucose) + 25 g Dextrose (50 mL D50W or 250 mL D10W).",
                "Monitor capillary glucose q30–60 min × 4 hours.",
                "Nebulized Salbutamol 10–20 mg over 10 min.",
                "IV NaHCO₃ 50 mEq ONLY if concurrent severe metabolic acidosis."
            )
        ),
        ProtocolStage(
            stepTitle = "3. Eliminate K⁺ From Body",
            badge = "DEFINITIVE",
            bullets = listOf(
                "IV Furosemide 40–80 mg (only if patient is making urine).",
                "Oral K⁺ binder: Sodium Zirconium Cyclosilicate (SZC) or Patiromer.",
                "Urgent Hemodialysis if anuric, refractory, or ongoing rhabdomyolysis."
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 4: Sepsis 1-Hour & Acute Emergency Bundles
    // ------------------------------------------------------------------------
    val sepsisBundleSteps: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "1. Measure Serum Lactate",
            badge = "0–60 MIN",
            bullets = listOf("Send venous/arterial lactate immediately; remeasure in 2–4h if >2 mmol/L.")
        ),
        ProtocolStage(
            stepTitle = "2. Blood Cultures Before Antibiotics",
            badge = "≥2 SETS",
            bullets = listOf("Obtain ≥2 sets (aerobic + anaerobic) before Abx — NEVER delay Abx >45 min.")
        ),
        ProtocolStage(
            stepTitle = "3. Broad-Spectrum IV Antibiotics",
            badge = "STAT",
            bullets = listOf("Administer empiric IV anti-infectives targeting likely source within 1 hour.")
        ),
        ProtocolStage(
            stepTitle = "4. Rapid Balanced Crystalloid (30 mL/kg)",
            badge = "HYPOTENSION / LACTATE ≥4",
            bullets = listOf("Give 30 mL/kg Ringer's/Hartmann's for hypotension or lactate ≥4 mmol/L; reassess volume status.")
        ),
        ProtocolStage(
            stepTitle = "5. Vasopressors for MAP < 65 mmHg",
            badge = "1ST LINE: NOREPI",
            bullets = listOf("Start Norepinephrine (1st-line) during/after fluids to maintain MAP ≥65 mmHg."),
            isRedFlag = true
        )
    )

    val scapeBundleSteps: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "1. Sit Upright + High-Flow O₂ + Early NIV",
            badge = "CPAP / BiPAP",
            bullets = listOf(
                "Sit upright with legs dependent; apply CPAP (5–10 cmH₂O) or BiPAP early.",
                "NIV rapidly reduces preload, afterload, and intubation rates."
            ),
            isRedFlag = true
        ),
        ProtocolStage(
            stepTitle = "2. High-Dose IV Nitroglycerin (Vasodilator First)",
            badge = "SBP ≥110–160",
            bullets = listOf(
                "Start high-dose IV GTN if SBP ≥110–160 mmHg (sublingual GTN while starting IV).",
                "Avoid if recent PDE-5 inhibitor (Sildenafil) or severe aortic stenosis."
            )
        ),
        ProtocolStage(
            stepTitle = "3. IV Loop Diuretic + Treat Trigger",
            badge = "FUROSEMIDE",
            bullets = listOf(
                "IV Furosemide 40–80 mg (or 1–2× chronic daily oral dose).",
                "Rule out & treat trigger: ACS (ECG/Troponin), rapid AFib, HTN crisis."
            )
        )
    )

    val massiveBleedSteps: List<ProtocolStage> = listOf(
        ProtocolStage(
            stepTitle = "1. Activate MTP Early · Balanced 1:1:1",
            badge = "1:1:1 RATIO",
            bullets = listOf(
                "Transfuse PRBC : FFP : Platelets in 1:1:1 ratio.",
                "Minimize crystalloid dilution; control hemorrhage source urgently."
            ),
            isRedFlag = true
        ),
        ProtocolStage(
            stepTitle = "2. Early IV Tranexamic Acid (TXA)",
            badge = "WITHIN 3 HRS",
            bullets = listOf("IV TXA 1 g over 10 min within 3 hrs of trauma or PPH onset (then 1 g over 8h).")
        ),
        ProtocolStage(
            stepTitle = "3. Prevent Lethal Diamond (Temp · Ca²⁺ · Fib)",
            badge = "TARGETS",
            bullets = listOf(
                "Keep Temperature >36°C (active warming + fluid warmer).",
                "Keep Ionized Ca²⁺ >1.1 mmol/L: give 1 g IV Ca Gluconate every 4 units PRBC.",
                "Keep Fibrinogen >150–200 mg/dL (give Cryoprecipitate if low)."
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 5: Toxicology Antidotes Table (14 Verified Rows)
    // ------------------------------------------------------------------------
    val antidotes: List<AntidoteRow> = listOf(
        AntidoteRow(
            id = "paracetamol",
            toxin = "Paracetamol (Acetaminophen)",
            antidote = "N-Acetylcysteine (NAC)",
            initialDoseAndPearl = "150 mg/kg IV over 1h → 50 mg/kg over 4h → 100 mg/kg over 16h (or 2-bag SNAP)."
        ),
        AntidoteRow(
            id = "opioids",
            toxin = "Opioids",
            antidote = "Naloxone",
            initialDoseAndPearl = "0.04–0.4 mg IV/IM/IN titrate to RR ≥12 · Short half-life: watch for renarcotization."
        ),
        AntidoteRow(
            id = "benzos",
            toxin = "Benzodiazepines",
            antidote = "Flumazenil",
            initialDoseAndPearl = "0.2 mg IV slowly · RED FLAG: Contraindicated in chronic benzo use or TCA co-ingestion (seizures).",
            isRedFlag = true
        ),
        AntidoteRow(
            id = "organophosphates",
            toxin = "Organophosphates / Carbamates",
            antidote = "Atropine + Pralidoxime",
            initialDoseAndPearl = "Atropine 1–2 mg IV doubling q5min until lungs clear (dry secretions) + Pralidoxime 30 mg/kg IV load."
        ),
        AntidoteRow(
            id = "heparin",
            toxin = "Unfractionated Heparin",
            antidote = "Protamine Sulfate",
            initialDoseAndPearl = "1 mg IV per 100 units recent heparin (max 50 mg) · Infuse slowly (hypotension/anaphylaxis risk)."
        ),
        AntidoteRow(
            id = "warfarin",
            toxin = "Warfarin / VKA",
            antidote = "Vitamin K + 4-Factor PCC",
            initialDoseAndPearl = "10 mg IV Vitamin K over 30 min + 25–50 U/kg 4-Factor PCC for major bleeding."
        ),
        AntidoteRow(
            id = "beta_blockers",
            toxin = "Beta-Blockers",
            antidote = "Glucagon + High-Dose Insulin (HIE)",
            initialDoseAndPearl = "Glucagon 3–5 mg IV bolus (antiemetic ready) · HIE 1 U/kg bolus + 1 U/kg/hr + Dextrose."
        ),
        AntidoteRow(
            id = "ccb",
            toxin = "Calcium Channel Blockers",
            antidote = "IV Calcium + HIE + Norepi",
            initialDoseAndPearl = "10% Ca Gluconate 30 mL (or 10% CaCl₂ 10 mL central) + HIE 1 U/kg/hr + Norepinephrine."
        ),
        AntidoteRow(
            id = "tca",
            toxin = "Tricyclic Antidepressants (QRS >100 ms)",
            antidote = "Sodium Bicarbonate",
            initialDoseAndPearl = "1–2 mEq/kg IV bolus · Repeat to narrow QRS and target serum pH 7.50–7.55.",
            isRedFlag = true
        ),
        AntidoteRow(
            id = "last",
            toxin = "Local Anesthetics (LAST)",
            antidote = "20% Intralipid",
            initialDoseAndPearl = "1.5 mL/kg IV bolus over 2–3 min + 0.25 mL/kg/min infusion (max 12 mL/kg total).",
            isRedFlag = true
        ),
        AntidoteRow(
            id = "methb",
            toxin = "Methemoglobinemia",
            antidote = "Methylene Blue",
            initialDoseAndPearl = "1–2 mg/kg IV over 5 min · Caution in G6PD deficiency & serotonergic drugs (SSRIs)."
        ),
        AntidoteRow(
            id = "cyanide",
            toxin = "Cyanide",
            antidote = "Hydroxocobalamin",
            initialDoseAndPearl = "5 g IV over 15 min (may repeat once) · Turns urine and skin harmlessly red."
        ),
        AntidoteRow(
            id = "toxic_alcohols",
            toxin = "Methanol / Ethylene Glycol",
            antidote = "Fomepizole (or IV Ethanol)",
            initialDoseAndPearl = "Fomepizole 15 mg/kg IV load, then 10 mg/kg q12h (+ Hemodialysis if severe acidosis/visual loss)."
        ),
        AntidoteRow(
            id = "iron",
            toxin = "Iron Overdose",
            antidote = "Deferoxamine",
            initialDoseAndPearl = "15 mg/kg/hr IV infusion · Vin rosé urine color; monitor closely for hypotension."
        )
    )
}
