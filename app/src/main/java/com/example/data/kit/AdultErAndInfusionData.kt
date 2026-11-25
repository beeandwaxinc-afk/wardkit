package com.example.data.kit

// ============================================================================
// SUB-TAB 1: ADULT ER & RSI (10 DRUGS)
// SUB-TAB 2: ADULT WARD & ICU INFUSIONS & QUICK DOSING (10 TOOLS)
// ============================================================================

data class MagSulfateRegimen(
    val id: String,
    val label: String,
    val doseGrams: Double,
    val timingNote: String
)

data class ElectrolyteCapItem(
    val electrolyte: String,
    val routeBadge: String,
    val safeCapRule: String
)

data class AdultWardOrderItem(
    val drug: String,
    val standardOrder: String,
    val maxOrCaution: String
)

object AdultErAndInfusionData {

    // ------------------------------------------------------------------------
    // SUB-TAB 1: ADULT ER & RSI (10 Drugs)
    // ------------------------------------------------------------------------
    val erRsiDrugs: List<WeightBasedDrugDef> = listOf(
        WeightBasedDrugDef(
            id = "er_etomidate",
            tabId = DoseTabId.ER_RSI,
            name = "Etomidate",
            routeTag = "IV · RSI",
            regimens = listOf(
                WeightDoseRegimen("std", "RSI 0.3 mg/kg", 0.3, maxSingleMg = 40.0)
            ),
            concentrations = listOf(
                DrugConcentration("c2", "2 mg/mL", 2.0)
            ),
            pearl = "Hemodynamically neutral induction agent in shock & trauma.",
            keywords = listOf("etomidate", "rsi", "induction", "intubation", "shock")
        ),
        WeightBasedDrugDef(
            id = "er_ketamine",
            tabId = DoseTabId.ER_RSI,
            name = "Ketamine",
            routeTag = "IV / IM",
            regimens = listOf(
                WeightDoseRegimen("rsi", "RSI IV 2 mg/kg", 2.0, maxSingleMg = 200.0),
                WeightDoseRegimen("shock", "Shock RSI 1 mg/kg", 1.0, maxSingleMg = 100.0),
                WeightDoseRegimen("im", "IM 4 mg/kg", 4.0, maxSingleMg = 400.0)
            ),
            concentrations = listOf(
                DrugConcentration("c50", "50 mg/mL", 50.0),
                DrugConcentration("c10", "10 mg/mL", 10.0)
            ),
            pearl = "Preserves BP & bronchodilates; ideal in hypotension & severe asthma.",
            keywords = listOf("ketamine", "rsi", "induction", "sedation", "asthma", "shock")
        ),
        WeightBasedDrugDef(
            id = "er_propofol",
            tabId = DoseTabId.ER_RSI,
            name = "Propofol",
            routeTag = "IV · RSI",
            regimens = listOf(
                WeightDoseRegimen("std", "Standard 2 mg/kg", 2.0, maxSingleMg = 250.0),
                WeightDoseRegimen("shock", "Shock/Elderly 1 mg/kg", 1.0, maxSingleMg = 150.0)
            ),
            concentrations = listOf(
                DrugConcentration("c10", "10 mg/mL (1%)", 10.0)
            ),
            pearl = "Causes hypotension — cut dose 50% in unstable or elderly patients.",
            keywords = listOf("propofol", "rsi", "induction", "sedation")
        ),
        WeightBasedDrugDef(
            id = "er_rocuronium",
            tabId = DoseTabId.ER_RSI,
            name = "Rocuronium",
            routeTag = "IV · Paralytic",
            regimens = listOf(
                WeightDoseRegimen("rsi", "RSI 1.2 mg/kg", 1.2, maxSingleMg = 150.0),
                WeightDoseRegimen("routine", "Routine 0.6 mg/kg", 0.6, maxSingleMg = 100.0)
            ),
            concentrations = listOf(
                DrugConcentration("c10", "10 mg/mL", 10.0)
            ),
            pearl = "Onset 45–60s at 1.2 mg/kg; reversible with Sugammadex.",
            keywords = listOf("rocuronium", "roc", "paralytic", "nmb", "rsi", "sugammadex")
        ),
        WeightBasedDrugDef(
            id = "er_succinylcholine",
            tabId = DoseTabId.ER_RSI,
            name = "Succinylcholine (Suxamethonium)",
            routeTag = "IV · Paralytic",
            regimens = listOf(
                WeightDoseRegimen("rsi", "RSI 1.5 mg/kg", 1.5, maxSingleMg = 150.0)
            ),
            concentrations = listOf(
                DrugConcentration("c50", "50 mg/mL", 50.0),
                DrugConcentration("c20", "20 mg/mL", 20.0)
            ),
            pearl = "RED FLAG — Avoid in K⁺ >5.5, burns/crush >24h, denervation, or MH history.",
            isRedFlagPearl = true,
            keywords = listOf("succinylcholine", "suxamethonium", "sux", "paralytic", "rsi", "hyperkalemia")
        ),
        WeightBasedDrugDef(
            id = "er_adrenaline",
            tabId = DoseTabId.ER_RSI,
            name = "Adrenaline / Epinephrine",
            routeTag = "IM / IV",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "anaph_im",
                    label = "Anaphylaxis IM (1:1,000)",
                    dosePerKg = 0.01,
                    maxSingleMg = 0.5,
                    allowedConcIds = setOf("c1_1000")
                ),
                WeightDoseRegimen(
                    id = "arrest_iv",
                    label = "Arrest IV (1:10,000)",
                    dosePerKg = 0.01,
                    maxSingleMg = 1.0,
                    allowedConcIds = setOf("c1_10000")
                )
            ),
            concentrations = listOf(
                DrugConcentration("c1_1000", "1:1,000 (1 mg/mL IM)", 1.0),
                DrugConcentration("c1_10000", "1:10,000 (0.1 mg/mL IV)", 0.1)
            ),
            pearl = "NEVER inject undiluted 1:1,000 Adrenaline IV push.",
            isRedFlagPearl = true,
            keywords = listOf("adrenaline", "epinephrine", "anaphylaxis", "arrest", "im", "code")
        ),
        WeightBasedDrugDef(
            id = "er_adenosine",
            tabId = DoseTabId.ER_RSI,
            name = "Adenosine (SVT)",
            routeTag = "Rapid IV Push",
            regimens = listOf(
                WeightDoseRegimen("svt", "1st: 0.1 mg/kg (max 6) · 2nd: 0.2 mg/kg (max 12)", 0.1, maxSingleMg = 6.0)
            ),
            concentrations = listOf(
                DrugConcentration("c3", "3 mg/mL", 3.0)
            ),
            pearl = "Large proximal IV + immediate rapid NS flush; record continuous ECG strip.",
            keywords = listOf("adenosine", "svt", "arrhythmia", "tachycardia"),
            specialMode = SpecialDrugMode.ADENOSINE_DUAL
        ),
        WeightBasedDrugDef(
            id = "er_amiodarone",
            tabId = DoseTabId.ER_RSI,
            name = "Amiodarone (Refractory VF/pVT)",
            routeTag = "IV",
            regimens = listOf(
                WeightDoseRegimen("vf", "5 mg/kg (2nd dose 150 mg)", 5.0, maxSingleMg = 300.0)
            ),
            concentrations = listOf(
                DrugConcentration("c50", "50 mg/mL", 50.0)
            ),
            pearl = "Give after 3rd shock in VF/pVT (2nd dose 150 mg); dilute in D5W for perfusing rhythms.",
            keywords = listOf("amiodarone", "vf", "vt", "acls", "antiarrhythmic")
        ),
        WeightBasedDrugDef(
            id = "er_txa",
            tabId = DoseTabId.ER_RSI,
            name = "Tranexamic Acid (TXA)",
            routeTag = "IV over 10m",
            regimens = listOf(
                WeightDoseRegimen("txa", "15 mg/kg", 15.0, maxSingleMg = 1000.0)
            ),
            concentrations = listOf(
                DrugConcentration("c100", "100 mg/mL", 100.0)
            ),
            pearl = "Administer within 3 hours of trauma or PPH onset.",
            keywords = listOf("txa", "tranexamic", "bleeding", "trauma", "pph", "hemorrhage")
        ),
        WeightBasedDrugDef(
            id = "er_heparin",
            tabId = DoseTabId.ER_RSI,
            name = "Unfractionated Heparin",
            routeTag = "IV Bolus + Drip",
            regimens = listOf(
                WeightDoseRegimen("vte", "VTE / PE (80 U/kg + 18 U/kg/hr)", 80.0, maxSingleMg = 10000.0),
                WeightDoseRegimen("acs", "ACS (60 U/kg + 12 U/kg/hr)", 60.0, maxSingleMg = 4000.0)
            ),
            concentrations = listOf(
                DrugConcentration("u1000", "1000 U/mL", 1000.0)
            ),
            pearl = "Check 6-hr aPTT to titrate; antidote = Protamine sulfate.",
            keywords = listOf("heparin", "ufh", "pe", "dvt", "vte", "acs", "anticoagulant"),
            specialMode = SpecialDrugMode.HEPARIN_BOLUS_DRIP
        )
    )

    // ------------------------------------------------------------------------
    // SUB-TAB 2: ADULT WARD & ICU INFUSIONS (#1–#5 Continuous Pump Infusions)
    // ------------------------------------------------------------------------
    val continuousInfusions: List<InfusionDrugDef> = listOf(
        InfusionDrugDef(
            id = "icu_norepi",
            name = "Norepinephrine (Noradrenaline)",
            weightIndependent = false,
            unitLabel = "mcg/kg/min",
            dilutions = listOf(
                InfusionDilution("d16", "4 mg/250 mL (16 mcg/mL)", 16.0),
                InfusionDilution("d32", "8 mg/250 mL (32 mcg/mL)", 32.0),
                InfusionDilution("d64", "16 mg/250 mL (64 mcg/mL)", 64.0)
            ),
            presetDoses = listOf(0.05, 0.1, 0.2, 0.5),
            pearl = "1st-line pressor in septic shock; target MAP ≥65 mmHg.",
            keywords = listOf("norepinephrine", "noradrenaline", "levophed", "pressor", "sepsis", "shock", "drip")
        ),
        InfusionDrugDef(
            id = "icu_epi",
            name = "Epinephrine Infusion",
            weightIndependent = false,
            unitLabel = "mcg/kg/min",
            dilutions = listOf(
                InfusionDilution("d16", "4 mg/250 mL (16 mcg/mL)", 16.0),
                InfusionDilution("d32", "8 mg/250 mL (32 mcg/mL)", 32.0)
            ),
            presetDoses = listOf(0.02, 0.05, 0.1, 0.2),
            pearl = "2nd-line pressor, anaphylactic shock, & symptomatic bradycardia.",
            keywords = listOf("epinephrine infusion", "adrenaline infusion", "pressor", "bradycardia", "anaphylaxis")
        ),
        InfusionDrugDef(
            id = "icu_dopamine",
            name = "Dopamine Infusion",
            weightIndependent = false,
            unitLabel = "mcg/kg/min",
            dilutions = listOf(
                InfusionDilution("d1600", "400 mg/250 mL (1600 mcg/mL)", 1600.0),
                InfusionDilution("d800", "200 mg/250 mL (800 mcg/mL)", 800.0)
            ),
            presetDoses = listOf(5.0, 10.0, 15.0, 20.0),
            pearl = "Higher arrhythmia risk than Norepinephrine; avoid 'renal dose'.",
            keywords = listOf("dopamine", "pressor", "inotrope", "drip", "icu")
        ),
        InfusionDrugDef(
            id = "icu_dobutamine",
            name = "Dobutamine Infusion",
            weightIndependent = false,
            unitLabel = "mcg/kg/min",
            dilutions = listOf(
                InfusionDilution("d1000", "250 mg/250 mL (1000 mcg/mL)", 1000.0),
                InfusionDilution("d2000", "500 mg/250 mL (2000 mcg/mL)", 2000.0)
            ),
            presetDoses = listOf(2.5, 5.0, 10.0, 15.0),
            pearl = "Inotrope for cardiogenic shock; watch for hypotension & tachycardia.",
            keywords = listOf("dobutamine", "inotrope", "cardiogenic shock", "heart failure", "drip")
        ),
        InfusionDrugDef(
            id = "icu_gtn",
            name = "Nitroglycerin (GTN) Infusion",
            weightIndependent = true,
            unitLabel = "mcg/min",
            dilutions = listOf(
                InfusionDilution("d200", "50 mg/250 mL (200 mcg/mL)", 200.0),
                InfusionDilution("d100", "25 mg/250 mL (100 mcg/mL)", 100.0)
            ),
            presetDoses = listOf(10.0, 20.0, 50.0, 100.0, 200.0),
            pearl = "Titrate for SCAPE/ACS/HTN crisis; contraindicated with PDE-5 inhibitors (Sildenafil).",
            keywords = listOf("nitroglycerin", "gtn", "ntg", "scape", "pulmonary edema", "acs", "vasodilator")
        )
    )

    // Tool #7: Magnesium Sulfate Regimens
    val magSulfateRegimens: List<MagSulfateRegimen> = listOf(
        MagSulfateRegimen("ecl_load", "Eclampsia Load (4 g)", 4.0, "4 g IV over 20 min"),
        MagSulfateRegimen("ecl_maint", "Eclampsia Maint (1 g/hr)", 1.0, "1 g/hr continuous IV"),
        MagSulfateRegimen("asthma_tdp", "Asthma / Torsades (2 g)", 2.0, "2 g IV over 20 min"),
        MagSulfateRegimen("hypomag", "Mild Hypomag (2 g / 2h)", 2.0, "2 g IV over 2 hours")
    )

    // Tool #8: Electrolyte IV Replacement Safe Caps
    val electrolyteCaps: List<ElectrolyteCapItem> = listOf(
        ElectrolyteCapItem("KCl — Peripheral IV", "MAX 10 mEq/hr", "Max conc 40 mEq/L · Painful/phlebitis if higher."),
        ElectrolyteCapItem("KCl — Central Line", "MAX 20 mEq/hr", "Continuous ECG telemetry required during infusion."),
        ElectrolyteCapItem("IV Magnesium Sulfate", "1–2 g / 15–60m", "Infuse slower (1–2h) if non-emergent; check DTRs."),
        ElectrolyteCapItem("IV Phosphate", "15–30 mmol / 4–6h", "Rapid infusion causes hypocalcemia & hypotension.")
    )

    // Tool #10: Quick Adult Analgesia & Antiemetic Ward Dosing
    val adultWardOrders: List<AdultWardOrderItem> = listOf(
        AdultWardOrderItem("Paracetamol", "1 g IV/PO q6h", "Max 4 g/day (2 g/day in liver disease / <50 kg)."),
        AdultWardOrderItem("Ketorolac", "15–30 mg IV/IM q6h", "Max 5 days · Avoid in CKD, elderly, or GI bleed."),
        AdultWardOrderItem("Morphine IV", "2–5 mg IV q4h PRN", "Titrate slowly; monitor RR & sedation score."),
        AdultWardOrderItem("Ondansetron", "4–8 mg IV/PO q8h", "Watch QTc prolongation with other QT drugs."),
        AdultWardOrderItem("Metoclopramide", "10 mg IV/PO q8h", "Watch EPS/dystonia; avoid in GI obstruction."),
        AdultWardOrderItem("Pantoprazole", "40 mg IV/PO OD", "UGIB: 80 mg IV bolus + 8 mg/hr infusion ×72h.")
    )
}
