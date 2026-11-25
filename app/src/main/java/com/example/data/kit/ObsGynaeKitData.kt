package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: OBS & GYNAE (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class TrimesterBlock(
    val id: String,
    val label: String,
    val rangeSubtitle: String,
    val items: List<Pair<String, String>>
)

data class UterotonicStepRow(
    val line: String,
    val drugAndDose: String,
    val warning: String,
    val isRedFlag: Boolean = false
)

data class CtgFeatureRow(
    val feature: String,
    val normalBadge: String,
    val abnormalNote: String,
    val isUrgent: Boolean = false
)

data class PregnancyDrugRow(
    val title: String,
    val detail: String,
    val badge: String = ""
)

object ObsGynaeKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Antenatal Care & Screening Timeline
    // ------------------------------------------------------------------------
    val trimesters: List<TrimesterBlock> = listOf(
        TrimesterBlock(
            id = "t1",
            label = "1st Tri (0–13w)",
            rangeSubtitle = "Booking – 13+6 Weeks",
            items = listOf(
                "Supplements" to "Folic Acid 400 mcg/d (5 mg/d if prior NTD, DM, BMI >30, or AEDs) until 12w + Vitamin D.",
                "Booking Labs" to "Blood Group & Rh/Antibody screen, CBC, HIV, HBsAg, Anti-HCV, Syphilis, Urine C&S (treat asymptomatic bacteriuria!), HbA1c if high risk.",
                "Dating & NT Scan" to "11–13+6 weeks (Dating + Nuchal Translucency). Start Aspirin 75–150 mg from 12–16w if high preeclampsia risk."
            )
        ),
        TrimesterBlock(
            id = "t2",
            label = "2nd Tri (14–27w)",
            rangeSubtitle = "14 – 27+6 Weeks",
            items = listOf(
                "Detailed Anomaly Scan" to "18–22 weeks — full fetal anatomy & placental localization.",
                "GDM Screening (75 g OGTT)" to "24–28 weeks (perform earlier if prior GDM or high-risk profile).",
                "Anemia Check" to "Repeat CBC; start iron supplementation if Hb <11 g/dL."
            )
        ),
        TrimesterBlock(
            id = "t3",
            label = "3rd Tri (28–40w)",
            rangeSubtitle = "28 – 40 Weeks",
            items = listOf(
                "Anti-D Ig (300 mcg IM)" to "Give at 28 weeks in all unsensitized Rh-Negative mothers (+ within 72h of any sensitizing event or Rh+ delivery).",
                "Tdap Vaccine" to "27–36 weeks EVERY pregnancy (passive pertussis immunity for newborn).",
                "GBS Rectovaginal Swab" to "35–37 weeks (give intrapartum IV Penicillin G / Ampicillin if positive).",
                "36-Week Check" to "Confirm cephalic lie (offer ECV if breech at 36–37w); monitor BP, urine protein, & fetal movements every visit."
            )
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: Preeclampsia & Eclampsia MgSO₄ Protocols
    // ------------------------------------------------------------------------
    const val mgso4IndicationStrip =
        "Eclamptic seizure control + seizure prophylaxis in Preeclampsia with Severe Features (continue 24 hours post-delivery or post-last seizure)."

    val zuspanSteps: List<Pair<String, String>> = listOf(
        "Loading Dose" to "4 g IV diluted slowly over 15–20 min (8 mL of 50% MgSO₄ in 100 mL NS OR 20 mL of 20%).",
        "Maintenance Infusion" to "1 g/hr IV continuous pump infusion (1–2 g/hr per institutional protocol).",
        "Recurrent Seizure (>15m post-load)" to "Additional 2 g IV bolus slowly over 5–10 minutes."
    )

    val pritchardSteps: List<Pair<String, String>> = listOf(
        "Loading Dose (IV + IM)" to "4 g IV slowly over 15–20 min PLUS 10 g IM (5 g [10 mL of 50%] deep IM into EACH buttock with 1 mL 2% Lidocaine).",
        "Maintenance IM" to "5 g IM every 4 hours into alternating buttocks (ideal when IV infusion pumps are unavailable)."
    )

    val mgToxicityChecks: List<String> = listOf(
        "1. Patellar (DTR) Reflex PRESENT — 1st toxicity sign is loss of reflex (7–10 mEq/L).",
        "2. Respiratory Rate ≥12–16 /min — respiratory depression occurs at 10–13 mEq/L.",
        "3. Urine Output ≥30 mL/hr (≥100 mL in past 4h — Mg²⁺ is renally excreted!)."
    )

    const val mgAntidoteLine =
        "ANTIDOTE: Stop MgSO₄ immediately → Give 10% Calcium Gluconate 10 mL (1 g) IV slowly over 3–5 min + support airway/O₂."

    // ------------------------------------------------------------------------
    // CARD 3: PPH (4 Ts) & Uterotonic Ladder
    // ------------------------------------------------------------------------
    const val pphDefinitionStrip =
        "PPH = Blood loss ≥500 mL (vaginal) / ≥1000 mL (CS) OR any loss with instability (Obstetric Shock Index ≥0.9)."

    val uterotonicLadder: List<UterotonicStepRow> = listOf(
        UterotonicStepRow(
            line = "1st Line",
            drugAndDose = "Oxytocin — 10 U IM OR 40 U in 1 L crystalloid IV (125–250 mL/hr)",
            warning = "NEVER give rapid undiluted IV push (causes severe hypotension!)."
        ),
        UterotonicStepRow(
            line = "Antifibrinolytic",
            drugAndDose = "Tranexamic Acid (TXA) 1 g IV over 10 min within 3 hrs of birth",
            warning = "Repeat 1 g IV if bleeding persists >30 min or restarts within 24h."
        ),
        UterotonicStepRow(
            line = "2nd Line A",
            drugAndDose = "Ergometrine / Methylergonovine 0.2 mg IM (repeat q2–4h)",
            warning = "CONTRAINDICATED in Hypertension, Preeclampsia, or Cardiac Disease!",
            isRedFlag = true
        ),
        UterotonicStepRow(
            line = "2nd Line B",
            drugAndDose = "Carboprost (PGF2α) 250 mcg IM q15–90m (max 8 doses)",
            warning = "CONTRAINDICATED in Asthma (severe bronchospasm)!",
            isRedFlag = true
        ),
        UterotonicStepRow(
            line = "2nd Line C",
            drugAndDose = "Misoprostol 800–1000 mcg sublingual or rectal",
            warning = "Safe in HTN & asthma; causes transient fever / shivering."
        )
    )

    const val pphSurgicalEscalation =
        "Refractory Escalation: Uterine massage / Bimanual compression → Activate MTP (1:1:1) → Bakri Balloon Tamponade → B-Lynch Suture / Uterine Artery Ligation → Emergency Hysterectomy."

    val pphFourTs: List<Pair<String, String>> = listOf(
        "Tone (~70%)" to "Uterine atony (boggy uterus — check bladder & empty with Foley!).",
        "Trauma (~20%)" to "Cervical, vaginal, or perineal lacerations; uterine rupture; uterine inversion.",
        "Tissue (~10%)" to "Retained placenta / cotyledons; placenta accreta spectrum.",
        "Thrombin (<1%)" to "Coagulopathy / DIC (abruption, AFE, severe preeclampsia, massive bleed)."
    )

    // ------------------------------------------------------------------------
    // CARD 4: CTG Interpretation Matrix (FIGO / NICE)
    // ------------------------------------------------------------------------
    val ctgCoreFeatures: List<CtgFeatureRow> = listOf(
        CtgFeatureRow(
            feature = "Baseline FHR",
            normalBadge = "110–160 bpm",
            abnormalNote = "Tachycardia >160 (fever, chorioamnionitis, hypoxia, dehydration) · Bradycardia <110 (<100 >3m = emergency).",
            isUrgent = false
        ),
        CtgFeatureRow(
            feature = "Baseline Variability",
            normalBadge = "5–25 bpm",
            abnormalNote = "Reduced <5 bpm (sleep <40m, opioids/MgSO₄, or hypoxia if >50m) · Sinusoidal = severe fetal anemia (EMERGENCY!).",
            isUrgent = true
        ),
        CtgFeatureRow(
            feature = "Accelerations",
            normalBadge = "↑ ≥15 bpm × ≥15s",
            abnormalNote = "Reassuring sign indicating absence of fetal acidemia."
        )
    )

    val ctgDecelerations: List<CtgFeatureRow> = listOf(
        CtgFeatureRow("Early Decelerations", "BENIGN", "Gradual, mirrors contraction (nadir = peak) → Fetal head compression."),
        CtgFeatureRow("Variable Decelerations", "CORD COMPRESSION", "Abrupt V/W shape, variable timing. Watch for abnormal features: lost variability, >60s, slow recovery."),
        CtgFeatureRow("Late Decelerations", "ABNORMAL · HYPOXIA", "Gradual onset & nadir AFTER contraction peak → Uteroplacental insufficiency!", isUrgent = true),
        CtgFeatureRow("Prolonged Deceleration", "EMERGENCY (>3 MIN)", "Drop >15 bpm lasting >3 min — rule out cord prolapse, abruption, or uterine rupture!", isUrgent = true)
    )

    val intrauterineResusSteps: List<String> = listOf(
        "1. STOP Oxytocin infusion immediately.",
        "2. Turn mother to LEFT LATERAL position (relieve aortocaval compression).",
        "3. Give IV Crystalloid bolus (500 mL).",
        "4. Check maternal BP/Pulse & Vaginal Exam (exclude cord prolapse).",
        "5. Consider Terbutaline 250 mcg SC if uterine tachysystole.",
        "6. Expedite delivery immediately if pathological CTG persists."
    )

    // ------------------------------------------------------------------------
    // CARD 5: Safe Drugs in Pregnancy & Lactation
    // ------------------------------------------------------------------------
    val safePregnancyDrugs: List<PregnancyDrugRow> = listOf(
        PregnancyDrugRow("Hypertension", "Labetalol, Nifedipine (ER/IR), Methyldopa (IV Hydralazine for acute crisis).", "1ST LINE"),
        PregnancyDrugRow("Antibiotics", "Penicillins (Amoxicillin, Co-Amoxiclav), Cephalosporins (Cephalexin, Ceftriaxone), Azithromycin, Clindamycin, Nitrofurantoin (avoid >36w & G6PD).", "SAFE"),
        PregnancyDrugRow("Pain & Fever", "Paracetamol (first-line throughout all trimesters).", "1ST LINE"),
        PregnancyDrugRow("Nausea / Vomiting", "Pyridoxine (Vit B6) + Doxylamine (1st-line), Metoclopramide, Ondansetron (2nd-line).", "ANTIEMETIC"),
        PregnancyDrugRow("Anticoagulation", "LMWH (Enoxaparin) or UFH — do NOT cross placenta.", "SAFE"),
        PregnancyDrugRow("Diabetes / Thyroid", "Insulin (1st-line; Metformin acceptable) · Hyperthyroid: PTU (1st Tri) → Carbimazole/Methimazole (2nd–3rd Tri) · Hypothyroid: ↑ Levothyroxine ~25–30%.", "ENDOCRINE")
    )

    const val lactationPearl =
        "Lactation Quick Pearl: Most safe pregnancy drugs are safe in breastfeeding. Avoid Lithium, Amiodarone, Codeine/Tramadol, Chemotherapy, & Radioactive Iodine."

    val teratogenRedList: List<PregnancyDrugRow> = listOf(
        PregnancyDrugRow("ACE Inhibitors & ARBs", "Fetal renal dysgenesis, oligohydramnios, skull defects — STOP immediately!", "CONTRAINDICATED"),
        PregnancyDrugRow("Warfarin", "Nasal hypoplasia, chondrodysplasia punctata, fetal ICH.", "TERATOGEN"),
        PregnancyDrugRow("Sodium Valproate", "High risk neural tube defects & neurodevelopmental delay — avoid in women of childbearing potential.", "HIGH RISK"),
        PregnancyDrugRow("NSAIDs (Ibuprofen / Diclofenac / Naproxen)", "AVOID >20–28 weeks: premature ductus arteriosus closure & oligohydramnios.", "AVOID >20W"),
        PregnancyDrugRow("Tetracyclines / Doxycycline", "Fetal tooth discoloration & bone growth inhibition.", "AVOID"),
        PregnancyDrugRow("Fluoroquinolones", "Fetal cartilage arthropathy concern.", "AVOID"),
        PregnancyDrugRow("Isotretinoin / Methotrexate / Thalidomide", "Absolute severe major teratogens.", "ABSOLUTE"),
        PregnancyDrugRow("TMP-SMX (Co-Trimoxazole)", "Avoid 1st trimester (folate antagonist) & near term (kernicterus).", "AVOID 1ST/3RD")
    )
}
