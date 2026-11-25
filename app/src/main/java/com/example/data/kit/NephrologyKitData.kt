package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: NEPHROLOGY (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class KdigoAkiStageRow(
    val stage: String,
    val scrCriteria: String,
    val uoCriteria: String,
    val isSevere: Boolean = false
)

data class AkiDiffRow(
    val parameter: String,
    val prerenal: String,
    val intrinsicAtn: String
)

enum class CkdRiskLevel(val shortLabel: String) {
    LOW("Low"),
    MODERATE("Mod"),
    HIGH("High"),
    VERY_HIGH("V.High")
}

data class CkdHeatmapRow(
    val gfrLabel: String,
    val gfrRange: String,
    val a1Risk: CkdRiskLevel,
    val a2Risk: CkdRiskLevel,
    val a3Risk: CkdRiskLevel
)

data class UrinalysisItemRow(
    val name: String,
    val badge: String,
    val significance: String,
    val isUrgent: Boolean = false
)

data class DialysisAeiouItem(
    val letter: String,
    val title: String,
    val detail: String
)

data class SadmansDrugItem(
    val letter: String,
    val drugClass: String,
    val riskReason: String
)

data class RenalDrugFlagRow(
    val id: String,
    val drug: String,
    val cutoffBadge: String,
    val rule: String,
    val isStopAlert: Boolean = false
)

object NephrologyKitData {

    // ------------------------------------------------------------------------
    // CARD 1: KDIGO AKI Staging & Differentiation
    // ------------------------------------------------------------------------
    val kdigoAkiStages: List<KdigoAkiStageRow> = listOf(
        KdigoAkiStageRow(
            stage = "Stage 1",
            scrCriteria = "1.5–1.9× baseline (7d) OR ↑ ≥0.3 mg/dL (26.5 µmol/L) in 48h",
            uoCriteria = "<0.5 mL/kg/hr for 6–12 hrs"
        ),
        KdigoAkiStageRow(
            stage = "Stage 2",
            scrCriteria = "2.0–2.9× baseline",
            uoCriteria = "<0.5 mL/kg/hr for ≥12 hrs"
        ),
        KdigoAkiStageRow(
            stage = "Stage 3",
            scrCriteria = "≥3.0× baseline OR SCr ≥4.0 mg/dL (353.6 µmol/L) OR RRT started",
            uoCriteria = "<0.3 mL/kg/hr for ≥24 hrs OR Anuria ≥12 hrs",
            isSevere = true
        )
    )

    val akiDiffRows: List<AkiDiffRow> = listOf(
        AkiDiffRow("FENa", "<1%", ">2%"),
        AkiDiffRow("FEUrea (on diuretics)", "≤35%", ">50%"),
        AkiDiffRow("Urine Na⁺", "<20 mEq/L", ">40 mEq/L"),
        AkiDiffRow("Urine Osm", ">500 mOsm/kg", "<350 mOsm/kg"),
        AkiDiffRow("BUN : Cr Ratio", ">20 : 1", "<15 : 1"),
        AkiDiffRow("Urine Sediment", "Bland / Hyaline casts", "Muddy brown granular casts")
    )

    const val postrenalPearl =
        "Postrenal (Obstruction): Perform Bedside Bladder Scan / Renal Ultrasound STAT (hydronephrosis / high PVR → Foley catheter / Urology)."

    // ------------------------------------------------------------------------
    // CARD 2: CKD G1–G5 / A1–A3 Risk Heatmap
    // ------------------------------------------------------------------------
    val ckdHeatmapRows: List<CkdHeatmapRow> = listOf(
        CkdHeatmapRow("G1", "≥90", CkdRiskLevel.LOW, CkdRiskLevel.MODERATE, CkdRiskLevel.HIGH),
        CkdHeatmapRow("G2", "60–89", CkdRiskLevel.LOW, CkdRiskLevel.MODERATE, CkdRiskLevel.HIGH),
        CkdHeatmapRow("G3a", "45–59", CkdRiskLevel.MODERATE, CkdRiskLevel.HIGH, CkdRiskLevel.VERY_HIGH),
        CkdHeatmapRow("G3b", "30–44", CkdRiskLevel.HIGH, CkdRiskLevel.VERY_HIGH, CkdRiskLevel.VERY_HIGH),
        CkdHeatmapRow("G4", "15–29", CkdRiskLevel.VERY_HIGH, CkdRiskLevel.VERY_HIGH, CkdRiskLevel.VERY_HIGH),
        CkdHeatmapRow("G5", "<15", CkdRiskLevel.VERY_HIGH, CkdRiskLevel.VERY_HIGH, CkdRiskLevel.VERY_HIGH)
    )

    val nephrologyReferralTriggers: List<String> = listOf(
        "eGFR <30 mL/min/1.73m² (G4–G5) or persistent ACR ≥300 mg/g.",
        "Rapid progression (>5 mL/min/yr eGFR drop).",
        "Refractory hypertension (≥4 drugs) or persistent K⁺ abnormality.",
        "Unexplained RBC casts / active urinary sediment, hereditary kidney disease, or recurrent stones."
    )

    val ckdWardTargets: List<String> = listOf(
        "BP <120–130/80 mmHg (ACEi/ARB + SGLT2i for proteinuric CKD).",
        "Anemia: Target Hb 10–11.5 g/dL (always replete iron BEFORE ESA).",
        "Acidosis: Oral NaHCO₃ if HCO₃⁻ <22 mEq/L · CKD-MBD: control PO₄³⁻, Ca²⁺, PTH, & Vit D."
    )

    // ------------------------------------------------------------------------
    // CARD 3: Urinalysis Casts & Crystals Key
    // ------------------------------------------------------------------------
    val urinaryCasts: List<UrinalysisItemRow> = listOf(
        UrinalysisItemRow("Hyaline Casts", "BENIGN / PRERENAL", "Normal, concentrated urine, exercise, or prerenal dehydration."),
        UrinalysisItemRow("RBC Casts (Dysmorphic RBCs)", "NEPHRITIC / GN", "Glomerulonephritis — urgent vasculitis / GN workup!", isUrgent = true),
        UrinalysisItemRow("WBC Casts", "PYELO / AIN", "Acute Pyelonephritis OR Acute Interstitial Nephritis (eosinophils/rash/drug)."),
        UrinalysisItemRow("Muddy Brown Granular Casts", "ATN", "Acute Tubular Necrosis (ischemic or nephrotoxic).", isUrgent = true),
        UrinalysisItemRow("Fatty Casts / Oval Fat Bodies", "NEPHROTIC", "'Maltese Cross' under polarized light — Nephrotic syndrome (>3.5 g/d protein)."),
        UrinalysisItemRow("Broad Waxy Casts", "ADVANCED CKD", "Dilated, atrophic tubules in chronic kidney disease.")
    )

    val urinaryCrystals: List<UrinalysisItemRow> = listOf(
        UrinalysisItemRow("Calcium Oxalate", "ENVELOPE / DUMBBELL", "Most common kidney stone; also Ethylene Glycol poisoning!", isUrgent = true),
        UrinalysisItemRow("Uric Acid", "RHOMBOID / DIAMOND", "Radiolucent on X-ray; gout, tumor lysis syndrome (TLS), acidic urine pH <5.5."),
        UrinalysisItemRow("Struvite (MgNH₄PO₄)", "COFFIN-LID", "Staghorn calculi; urease-splitting Proteus / Klebsiella, alkaline urine pH >7.5."),
        UrinalysisItemRow("Cystine", "HEXAGONAL PLATES", "Cystinuria (genetic defect; presents in children / young adults).")
    )

    // ------------------------------------------------------------------------
    // CARD 4: Dialysis Emergencies (AEIOU) & Sick-Day Rules (SADMANS)
    // ------------------------------------------------------------------------
    val dialysisAeiou: List<DialysisAeiouItem> = listOf(
        DialysisAeiouItem("A", "Acidosis", "Severe refractory metabolic acidosis (pH <7.1 despite bicarbonate)."),
        DialysisAeiouItem("E", "Electrolytes", "Refractory Hyperkalemia (K⁺ >6.5 or persistent ECG changes) failing medical shift."),
        DialysisAeiouItem("I", "Intoxications (SLIME)", "Salicylates, Lithium, Isopropanol, Methanol / Ethylene glycol (+ Metformin lactic acidosis)."),
        DialysisAeiouItem("O", "Overload", "Diuretic-refractory acute pulmonary edema / severe hypoxia."),
        DialysisAeiouItem("U", "Uremia", "Uremic pericarditis (friction rub — absolute!), uremic encephalopathy/seizures, or bleeding.")
    )

    val sadmansRules: List<SadmansDrugItem> = listOf(
        SadmansDrugItem("S", "Sulfonylureas", "Prolonged severe hypoglycemia in AKI."),
        SadmansDrugItem("A", "ACE Inhibitors", "Worsen efferent GFR drop & hyperkalemia."),
        SadmansDrugItem("D", "Diuretics", "Worsen hypovolemia & prerenal AKI."),
        SadmansDrugItem("M", "Metformin", "Metformin-associated lactic acidosis (MALA) risk."),
        SadmansDrugItem("A", "ARBs", "Worsen efferent GFR drop & hyperkalemia."),
        SadmansDrugItem("N", "NSAIDs", "Afferent vasoconstriction → severe AKI."),
        SadmansDrugItem("S", "SGLT2 Inhibitors", "Volume depletion & euglycemic DKA.")
    )

    // ------------------------------------------------------------------------
    // CARD 5: Renal Drug Dosing Adjustment Flags (Filterable)
    // ------------------------------------------------------------------------
    val renalDrugFlags: List<RenalDrugFlagRow> = listOf(
        RenalDrugFlagRow("metformin", "Metformin", "STOP <30", "eGFR 30–45: max 1000 mg/d (do not initiate) · eGFR <30: STOP (lactic acidosis).", isStopAlert = true),
        RenalDrugFlagRow("nitrofurantoin", "Nitrofurantoin", "AVOID <30", "eGFR <30: AVOID — fails to concentrate in urine + pulmonary/neuropathy toxicity.", isStopAlert = true),
        RenalDrugFlagRow("enoxaparin", "Enoxaparin (LMWH)", "CrCl <30", "Reduce to 30 mg OD (prophylaxis) or 1 mg/kg OD (treatment) — or switch to UFH."),
        RenalDrugFlagRow("doacs", "DOACs", "CUTOFF 15–30", "Dabigatran avoid <30; Rivaroxaban/Apixaban dose-reduce per renal/age/wt; avoid <15."),
        RenalDrugFlagRow("opioids", "Opioids (Morphine / Codeine)", "AVOID <30", "AVOID Morphine & Codeine in eGFR <30 (neurotoxic metabolites!) → Prefer Fentanyl or cautious Hydromorphone.", isStopAlert = true),
        RenalDrugFlagRow("vanc_ag", "Vancomycin & Aminoglycosides", "LEVELS", "Full loading dose OK → strictly extend interval & monitor trough / AUC levels."),
        RenalDrugFlagRow("betalactams", "Cefepime / Meropenem / Pip-Tazo", "CrCl <50", "Adjust maintenance dose at CrCl <50 (Cefepime neurotoxicity/seizures in unadjusted AKI!).", isStopAlert = true),
        RenalDrugFlagRow("gabapentin", "Gabapentin / Pregabalin", "CrCl <60", "Cut dose 50–75% at CrCl <60 (causes profound sedation & myoclonus)."),
        RenalDrugFlagRow("gout", "Colchicine & Allopurinol", "CrCl <30", "Avoid/markedly cut Colchicine <30 (myopathy/cytopenia); start Allopurinol low (50–100 mg)."),
        RenalDrugFlagRow("contrast", "IV Iodinated Contrast", "eGFR <30–45", "Pre-hydrate with isotonic 0.9% NS / NaHCO₃; NEVER withhold life-saving CT angio (PE/stroke/dissection).")
    )
}
