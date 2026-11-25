package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: INTERNAL MEDICINE (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class EmpiricAbxTier(
    val setting: String,
    val regimen: String,
    val isRedFlag: Boolean = false
)

data class EmpiricAbxSyndrome(
    val id: String,
    val chipLabel: String,
    val title: String,
    val badge: String? = null,
    val isUrgent: Boolean = false,
    val tiers: List<EmpiricAbxTier>
)

data class InsulinPkRow(
    val category: String,
    val examples: String,
    val onset: String,
    val peak: String,
    val duration: String,
    val bedsideNote: String
)

data class HtnEmergencyRow(
    val condition: String,
    val targetBadge: String,
    val firstLineAgents: String,
    val cautionNote: String,
    val isHighRisk: Boolean = false
)

data class ElectrolyteWorkupBucket(
    val title: String,
    val badge: String,
    val bullets: List<String>
)

data class FuoBucket(
    val category: String,
    val examples: String
)

object InternalMedicineKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Empiric Antibiotic Syndromic Guide (6 Filterable Syndromes)
    // ------------------------------------------------------------------------
    const val abxHeaderWarning =
        "Protocol Reference — Verify Local Antibiogram & Allergies"

    val empiricAbxSyndromes: List<EmpiricAbxSyndrome> = listOf(
        EmpiricAbxSyndrome(
            id = "cap",
            chipLabel = "CAP",
            title = "CAP (Community-Acquired Pneumonia)",
            tiers = listOf(
                EmpiricAbxTier("Outpatient Healthy", "Amoxicillin OR Doxycycline OR Macrolide."),
                EmpiricAbxTier("Inpatient Non-Severe", "Ceftriaxone 1–2g IV OD + Azithromycin 500mg (or Doxycycline)."),
                EmpiricAbxTier("Severe / ICU", "Ceftriaxone + Azithromycin (or Levofloxacin); add Vancomycin / Pip-Tazo only if MRSA / Pseudomonas risk.")
            )
        ),
        EmpiricAbxSyndrome(
            id = "hap",
            chipLabel = "HAP / VAP",
            title = "HAP / VAP (Hospital / Ventilator-Associated)",
            badge = "RENAL ADJUST ALL",
            tiers = listOf(
                EmpiricAbxTier("Anti-Pseudomonal β-Lactam", "Piperacillin-Tazobactam 4.5g q6h OR Cefepime 2g q8h OR Meropenem 1g q8h."),
                EmpiricAbxTier("MRSA Coverage", "Add Vancomycin 15–20 mg/kg q8–12h if MRSA risk or ICU septic shock.")
            )
        ),
        EmpiricAbxSyndrome(
            id = "uti",
            chipLabel = "UTI / Pyelo",
            title = "UTI & Acute Pyelonephritis",
            tiers = listOf(
                EmpiricAbxTier("Uncomplicated Cystitis", "Nitrofurantoin 100mg BD ×5d (avoid if eGFR <30) OR Fosfomycin 3g single dose."),
                EmpiricAbxTier("Pyelonephritis / Urosepsis", "Ceftriaxone 1–2g IV OD; escalate to Pip-Tazo / Meropenem if ESBL risk or septic shock.")
            )
        ),
        EmpiricAbxSyndrome(
            id = "meningitis",
            chipLabel = "Meningitis",
            title = "Acute Bacterial Meningitis",
            badge = "GIVE STAT — NO LP/CT DELAY",
            isUrgent = true,
            tiers = listOf(
                EmpiricAbxTier("Steroid First", "Dexamethasone 10mg IV before or with 1st antibiotic dose.", isRedFlag = true),
                EmpiricAbxTier("Core Empiric Regimen", "Ceftriaxone 2g IV q12h + Vancomycin 15–20 mg/kg q8–12h."),
                EmpiricAbxTier("Listeria Coverage", "Add Ampicillin 2g IV q4h if Age >50 yrs, immunocompromised, or pregnant.")
            )
        ),
        EmpiricAbxSyndrome(
            id = "cellulitis",
            chipLabel = "Cellulitis",
            title = "Cellulitis / Skin & Soft Tissue",
            tiers = listOf(
                EmpiricAbxTier("Non-Purulent (Strep / MSSA)", "Cephalexin / Cefazolin / Flucloxacillin."),
                EmpiricAbxTier("Purulent / Abscess (MRSA)", "I&D + Doxycycline or TMP-SMX (IV Vancomycin if systemic toxicity)."),
                EmpiricAbxTier("RED FLAG — Necrotizing Fasciitis", "Pip-Tazo/Meropenem + Vancomycin + Clindamycin (antitoxin) + Urgent Surgical Debridement.", isRedFlag = true)
            )
        ),
        EmpiricAbxSyndrome(
            id = "intra_abd",
            chipLabel = "Intra-Abd",
            title = "Intra-Abdominal Sepsis",
            tiers = listOf(
                EmpiricAbxTier("Community-Acquired", "Ceftriaxone + Metronidazole."),
                EmpiricAbxTier("Healthcare-Associated / Severe", "Piperacillin-Tazobactam OR Meropenem (+ source control).")
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: Insulin Pharmacokinetics & Glycemic Regimens
    // ------------------------------------------------------------------------
    val insulinPkRows: List<InsulinPkRow> = listOf(
        InsulinPkRow(
            category = "Rapid-Acting",
            examples = "Lispro · Aspart · Glulisine",
            onset = "10–15 min",
            peak = "1–2 hr",
            duration = "3–5 hr",
            bedsideNote = "Give immediately with meal."
        ),
        InsulinPkRow(
            category = "Short-Acting",
            examples = "Regular / Actrapid",
            onset = "30 min",
            peak = "2–3 hr",
            duration = "5–8 hr",
            bedsideNote = "Give 30 min pre-meal; used for IV drips."
        ),
        InsulinPkRow(
            category = "Intermediate",
            examples = "NPH / Insulatard",
            onset = "2–4 hr",
            peak = "4–10 hr",
            duration = "10–16 hr",
            bedsideNote = "Cloudy suspension; gently roll before drawing."
        ),
        InsulinPkRow(
            category = "Long-Acting",
            examples = "Glargine · Detemir · Degludec",
            onset = "1–2 hr",
            peak = "Peakless",
            duration = "20–24 hr",
            bedsideNote = "Degludec lasts >42 hr; steady basal coverage."
        )
    )

    val inpatientTddTiers: List<Pair<String, String>> = listOf(
        "0.3 U/kg/day" to "Elderly, CKD, or low BMI",
        "0.4 U/kg/day" to "Standard inpatient starting TDD",
        "0.5 U/kg/day" to "Obese, severe hyperglycemia, or steroids"
    )

    val basalBolusRules: List<String> = listOf(
        "Target inpatient glucose: 140–180 mg/dL (7.8–10.0 mmol/L).",
        "Split TDD: 50% Basal (once-daily Glargine) + 50% Prandial Bolus (÷3 meals) + correction scale."
    )

    const val insulinRedFlag =
        "Avoid sliding-scale insulin alone (roller-coaster glucose); NEVER hold basal insulin in Type 1 Diabetes (precipitates DKA) — reduce by 20–30% if NPO."

    // ------------------------------------------------------------------------
    // CARD 3: Hypertensive Emergency Matrix
    // ------------------------------------------------------------------------
    const val htnGoldenRule =
        "Emergency = BP ≥180/120 WITH acute target-organ damage. General target: ↓ MAP ≤20–25% in 1st hour (avoid abrupt drops → watershed stroke)."

    val htnEmergencies: List<HtnEmergencyRow> = listOf(
        HtnEmergencyRow(
            condition = "Aortic Dissection",
            targetBadge = "SBP <120 · HR <60 (<20m)",
            firstLineAgents = "IV Esmolol or Labetalol FIRST → then add Nicardipine / Clevidipine.",
            cautionNote = "Always control HR before starting a vasodilator (prevents reflex shear stress).",
            isHighRisk = true
        ),
        HtnEmergencyRow(
            condition = "Acute Ischemic Stroke",
            targetBadge = "tPA <185/110 · No tPA ≥220/120",
            firstLineAgents = "IV Labetalol or Nicardipine.",
            cautionNote = "tPA/TNK: keep <180/105 post-drug. No thrombolysis: permissive HTN (↓ 15% over 24h only if ≥220/120)."
        ),
        HtnEmergencyRow(
            condition = "Acute Intracerebral Hemorrhage (ICH)",
            targetBadge = "SBP ~140 mmHg",
            firstLineAgents = "IV Nicardipine or Labetalol.",
            cautionNote = "If SBP 150–220, lower smoothly toward ~140; avoid dropping <130 mmHg."
        ),
        HtnEmergencyRow(
            condition = "Acute Pulmonary Edema (SCAPE)",
            targetBadge = "VASODILATE EARLY",
            firstLineAgents = "IV Nitroglycerin + CPAP/BiPAP + IV Furosemide.",
            cautionNote = "Avoid beta-blockers acutely in decompensated pulmonary edema."
        ),
        HtnEmergencyRow(
            condition = "Acute Coronary Syndrome (ACS)",
            targetBadge = "RELIEVE ISCHEMIA",
            firstLineAgents = "IV Nitroglycerin + IV Esmolol / Labetalol.",
            cautionNote = "Avoid Nitroprusside (coronary steal); confirm no recent PDE-5 inhibitor use."
        ),
        HtnEmergencyRow(
            condition = "Severe Preeclampsia / Eclampsia",
            targetBadge = "TARGET <160/110",
            firstLineAgents = "IV Labetalol, IV Hydralazine, or PO Immediate-Release Nifedipine (+ MgSO₄).",
            cautionNote = "Avoid ACEi, ARBs, and Nitroprusside in pregnancy.",
            isHighRisk = true
        ),
        HtnEmergencyRow(
            condition = "Sympathomimetic / Cocaine / Pheo",
            targetBadge = "ALPHA + BENZO",
            firstLineAgents = "Benzodiazepines + IV Phentolamine or Nicardipine.",
            cautionNote = "Avoid unopposed beta-blockade (worsens α-mediated vasoconstriction)."
        )
    )

    // ------------------------------------------------------------------------
    // CARD 4: Electrolyte Workup Rapid Maps
    // ------------------------------------------------------------------------
    val hypoNaStep1Osm: List<Pair<String, String>> = listOf(
        "High (>295)" to "Hyperglycemia or Mannitol (translocational)",
        "Normal (275–295)" to "Pseudohyponatremia (lipids / paraproteins)",
        "Low (<275)" to "True Hypotonic Hyponatremia → Step 2 below"
    )

    val hypoNaStep2Volume: List<ElectrolyteWorkupBucket> = listOf(
        ElectrolyteWorkupBucket(
            title = "Hypovolemic",
            badge = "VOLUME DEPLETED",
            bullets = listOf(
                "U_Na <20: GI loss (vomiting/diarrhea), burns, 3rd spacing.",
                "U_Na >20: Diuretics, Addison's disease, cerebral salt wasting."
            )
        ),
        ElectrolyteWorkupBucket(
            title = "Euvolemic",
            badge = "CHECK U_OSM & U_NA",
            bullets = listOf(
                "U_Osm >100 & U_Na >30–40: SIADH, Hypothyroidism, Adrenal insufficiency.",
                "U_Osm <100: Primary polydipsia, Beer potomania, low-solute diet."
            )
        ),
        ElectrolyteWorkupBucket(
            title = "Hypervolemic",
            badge = "EDEMATOUS",
            bullets = listOf(
                "U_Na <20: Heart failure, Cirrhosis, Nephrotic syndrome.",
                "U_Na >20: Advanced acute or chronic renal failure."
            )
        )
    )

    const val hypoNaSafetyStrip =
        "Severe neuro symptoms (seizure/coma) → 100–150 mL 3% NaCl bolus over 10–20 min; cap chronic rise ≤8 mEq/L per 24h (ODS risk)."

    val potassiumBuckets: List<ElectrolyteWorkupBucket> = listOf(
        ElectrolyteWorkupBucket(
            title = "Hypokalemia (K⁺ < 3.5)",
            badge = "3 MECHANISMS",
            bullets = listOf(
                "GI Loss: Diarrhea, vomiting, NG suction, laxatives.",
                "Renal Loss: Loop/thiazide diuretics, RTA, hyperaldosteronism, low Mg²⁺.",
                "Intracellular Shift: Insulin, β₂-agonists, metabolic alkalosis."
            )
        ),
        ElectrolyteWorkupBucket(
            title = "Hyperkalemia (K⁺ > 5.0)",
            badge = "RULE OUT PSEUDO 1ST",
            bullets = listOf(
                "Check Pseudohyperkalemia first (hemolysis, fist clenching, thrombocytosis).",
                "True Causes: AKI/CKD, RAAS inhibitors / Spironolactone / NSAIDs.",
                "Shift / Release: Acidosis, rhabdomyolysis, tumor lysis (TLS), hypoaldosteronism."
            )
        )
    )

    const val calciumCorrectionBanner =
        "Always correct total Calcium for Albumin OR check Ionized Ca²⁺ directly."

    val calciumBuckets: List<ElectrolyteWorkupBucket> = listOf(
        ElectrolyteWorkupBucket(
            title = "Hypercalcemia — Split by PTH",
            badge = "CHECK PTH FIRST",
            bullets = listOf(
                "High / Inappropriately Normal PTH: Primary Hyperparathyroidism, FHH.",
                "Suppressed PTH: Malignancy (PTHrP / lytic / myeloma), Vitamin D toxicity, Granulomatous (Sarcoidosis), Thiazides."
            )
        ),
        ElectrolyteWorkupBucket(
            title = "Hypocalcemia — Split by PTH",
            badge = "CHECK PTH & MG²⁺",
            bullets = listOf(
                "Low PTH: Post-thyroidectomy or post-parathyroidectomy.",
                "High PTH: Vitamin D deficiency, CKD, acute pancreatitis, massive transfusion (citrate), hypomagnesemia."
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 5: Fever / FUO & Medical VTE Prophylaxis
    // ------------------------------------------------------------------------
    val paduaTriggers: List<Pair<String, String>> = listOf(
        "+3 pts" to "Active cancer · Prior VTE · Reduced mobility ≥3d · Known thrombophilia",
        "+2 pts" to "Recent trauma or surgery (≤1 month)",
        "+1 pt" to "Age ≥70 · HF / Resp failure · Acute infection · BMI ≥30"
    )

    val vtePharmacologicBullets: List<String> = listOf(
        "Enoxaparin 40 mg SC OD (standard medical prophylaxis).",
        "If CrCl <30 mL/min: reduce to Enoxaparin 30 mg SC OD OR switch to UFH 5000 U SC q8–12h."
    )

    val vteContraindications: List<String> = listOf(
        "Active bleeding or Platelets <50,000 /µL → use Mechanical SCDs.",
        "Severe coagulopathy or recent ICH / neurosurgery.",
        "Active HIT → avoid all heparin/LMWH; use Fondaparinux."
    )

    const val fuoDefinition =
        "Temp ≥38.3°C on multiple occasions >3 weeks without diagnosis after structured workup."

    val fuoBigFour: List<FuoBucket> = listOf(
        FuoBucket("Infections", "TB, infective endocarditis, occult abscess, enteric fever, malaria / dengue / brucella."),
        FuoBucket("Malignancy", "Lymphoma, renal cell carcinoma (RCC), leukemia."),
        FuoBucket("Autoimmune / Inflammatory", "Adult Still's disease, GCA, SLE, systemic vasculitis."),
        FuoBucket("Miscellaneous", "Drug fever, DVT / PE, factitious fever.")
    )

    const val fuoCoreWorkupStrip =
        "CBC+Diff · ESR/CRP · LFT/RFT · ≥2 Blood Cultures (pre-Abx) · Urinalysis/Culture · CXR · HIV/Hepatitis/TB screen · Peripheral smear/MP where endemic."
}
