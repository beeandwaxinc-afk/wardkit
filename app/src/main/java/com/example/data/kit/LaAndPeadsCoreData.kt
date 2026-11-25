package com.example.data.kit

// ============================================================================
// SUB-TAB 3: LOCAL ANESTHETICS & LIVE 20% INTRALIPID LAST (4 AGENTS + RESCUE)
// SUB-TAB 4: PEDIATRIC WARD / OPD CORE (12 CORE DRUGS — SORTED A–Z)
// ============================================================================

data class DoseDrugSearchEntry(
    val drugId: String,
    val title: String,
    val subtitle: String,
    val tabId: DoseTabId,
    val keywords: List<String>
)

object LaAndPeadsCoreData {

    // ------------------------------------------------------------------------
    // SUB-TAB 3: LOCAL ANESTHETICS (4 Agents) + 20% INTRALIPID LAST
    // ------------------------------------------------------------------------
    val localAnesthetics: List<LocalAnestheticDef> = listOf(
        LocalAnestheticDef(
            id = "la_lidocaine_plain",
            name = "Lidocaine (Plain)",
            regimens = listOf(
                LocalAnestheticRegimen("plain", "Plain 4.5 mg/kg (max 300 mg)", 4.5, 300.0)
            ),
            percentOptions = listOf(
                LocalAnestheticPercent("p1", "1% (10 mg/mL)", 1.0),
                LocalAnestheticPercent("p2", "2% (20 mg/mL)", 2.0),
                LocalAnestheticPercent("p05", "0.5% (5 mg/mL)", 0.5)
            ),
            pearl = "Fast onset (2–5 min), duration 1–2 hrs; aspirate before every injection.",
            keywords = listOf("lidocaine", "lignocaine", "local anesthetic", "plain", "suture")
        ),
        LocalAnestheticDef(
            id = "la_lidocaine_epi",
            name = "Lidocaine + Adrenaline",
            regimens = listOf(
                LocalAnestheticRegimen("epi", "+ Adrenaline 7.0 mg/kg (max 500 mg)", 7.0, 500.0)
            ),
            percentOptions = listOf(
                LocalAnestheticPercent("p1", "1% (10 mg/mL)", 1.0),
                LocalAnestheticPercent("p2", "2% (20 mg/mL)", 2.0)
            ),
            pearl = "Vasoconstriction prolongs block 2–4 hrs & reduces bleeding.",
            keywords = listOf("lidocaine adrenaline", "lignocaine epinephrine", "local anesthetic")
        ),
        LocalAnestheticDef(
            id = "la_bupivacaine",
            name = "Bupivacaine",
            regimens = listOf(
                LocalAnestheticRegimen("plain", "Plain 2.0 mg/kg (max 175 mg)", 2.0, 175.0),
                LocalAnestheticRegimen("epi", "+ Adrenaline 2.5 mg/kg (max 225 mg)", 2.5, 225.0)
            ),
            percentOptions = listOf(
                LocalAnestheticPercent("p025", "0.25% (2.5 mg/mL)", 0.25),
                LocalAnestheticPercent("p05", "0.5% (5 mg/mL)", 0.5)
            ),
            pearl = "High cardiotoxicity — aspirate first; NEVER inject IV.",
            isRedFlagPearl = true,
            keywords = listOf("bupivacaine", "marcaine", "local anesthetic", "last", "nerve block")
        ),
        LocalAnestheticDef(
            id = "la_ropivacaine",
            name = "Ropivacaine",
            regimens = listOf(
                LocalAnestheticRegimen("std", "3.0 mg/kg (max 200 mg)", 3.0, 200.0)
            ),
            percentOptions = listOf(
                LocalAnestheticPercent("p02", "0.2% (2 mg/mL)", 0.2),
                LocalAnestheticPercent("p05", "0.5% (5 mg/mL)", 0.5),
                LocalAnestheticPercent("p075", "0.75% (7.5 mg/mL)", 0.75)
            ),
            pearl = "Lower cardiotoxicity & less motor block than Bupivacaine.",
            keywords = listOf("ropivacaine", "naropin", "local anesthetic", "nerve block")
        )
    )

    val lastFiveStepChecklist: List<String> = listOf(
        "1. Stop LA injection immediately & call for help / LAST kit.",
        "2. 100% O₂ & secure airway (prevent hypoxia & hypercapnia/acidosis).",
        "3. Seizures: Give Benzodiazepine (AVOID Propofol in unstable patients).",
        "4. Start 20% Intralipid bolus + infusion immediately.",
        "5. Cardiac Arrest: Cut Adrenaline to ≤1 mcg/kg (avoid Vasopressin/CCBs/BBs)."
    )

    // ------------------------------------------------------------------------
    // SUB-TAB 4: PEDIATRIC WARD / OPD CORE (12 Core Drugs — Strictly A–Z)
    // ------------------------------------------------------------------------
    val peadsCoreDrugs: List<WeightBasedDrugDef> = listOf(
        WeightBasedDrugDef(
            id = "peads_amoxicillin",
            tabId = DoseTabId.PEADS_CORE,
            name = "Amoxicillin",
            routeTag = "PO",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "Standard 45 mg/kg/d (÷3 TDS)",
                    dosePerKg = 45.0,
                    isDailyDose = true,
                    dosesPerDay = 3,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 3000.0,
                    frequencyLabel = "q8h (TDS)"
                ),
                WeightDoseRegimen(
                    id = "high",
                    label = "High-Dose AOM/CAP 90 mg/kg/d (÷2 BD)",
                    dosePerKg = 90.0,
                    isDailyDose = true,
                    dosesPerDay = 2,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 3000.0,
                    frequencyLabel = "q12h (BD)"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c125", "125 mg/5 mL", 25.0),
                DrugConcentration("c250", "250 mg/5 mL", 50.0)
            ),
            pearl = "1st-line for AOM & CAP; max 1000 mg/dose, 3000 mg/day.",
            keywords = listOf("amoxicillin", "amoxil", "aom", "cap", "pneumonia", "peads")
        ),
        WeightBasedDrugDef(
            id = "peads_asthma_nebs",
            tabId = DoseTabId.PEADS_CORE,
            name = "Asthma Nebulizers (Salbutamol + Ipratropium)",
            routeTag = "Neb q20m ×3",
            regimens = listOf(
                WeightDoseRegimen("neb", "Auto Weight Tier (<20 kg vs ≥20 kg)", 0.0, maxSingleMg = 5.0)
            ),
            concentrations = listOf(
                DrugConcentration("c5", "5 mg/mL Salb", 5.0)
            ),
            pearl = "Dilute in 2–3 mL 0.9% NS; back-to-back q20min ×3 for acute severe wheeze.",
            keywords = listOf("salbutamol", "albuterol", "ipratropium", "atrovent", "asthma", "nebulizer", "wheeze"),
            specialMode = SpecialDrugMode.ASTHMA_NEBS_TIER
        ),
        WeightBasedDrugDef(
            id = "peads_azithromycin",
            tabId = DoseTabId.PEADS_CORE,
            name = "Azithromycin",
            routeTag = "PO · OD",
            regimens = listOf(
                WeightDoseRegimen("std", "Standard 10 mg/kg OD", 10.0, maxSingleMg = 500.0, frequencyLabel = "once daily"),
                WeightDoseRegimen("enteric", "Enteric Fever 20 mg/kg OD", 20.0, maxSingleMg = 1000.0, frequencyLabel = "once daily")
            ),
            concentrations = listOf(
                DrugConcentration("c200", "200 mg/5 mL", 40.0)
            ),
            pearl = "Standard atypical CAP 3–5d; enteric fever 20 mg/kg/day ×7 days.",
            keywords = listOf("azithromycin", "zithromax", "enteric", "typhoid", "atypical")
        ),
        WeightBasedDrugDef(
            id = "peads_cefixime",
            tabId = DoseTabId.PEADS_CORE,
            name = "Cefixime",
            routeTag = "PO",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "Standard 8 mg/kg/d (OD)",
                    dosePerKg = 8.0,
                    isDailyDose = true,
                    dosesPerDay = 1,
                    maxSingleMg = 400.0,
                    maxDailyMg = 400.0,
                    frequencyLabel = "once daily"
                ),
                WeightDoseRegimen(
                    id = "enteric",
                    label = "Enteric 20 mg/kg/d (÷2 BD)",
                    dosePerKg = 20.0,
                    isDailyDose = true,
                    dosesPerDay = 2,
                    maxSingleMg = 400.0,
                    maxDailyMg = 800.0,
                    frequencyLabel = "q12h (BD)"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c100", "100 mg/5 mL", 20.0),
                DrugConcentration("c200", "200 mg/5 mL", 40.0)
            ),
            pearl = "Oral 3rd-gen cephalosporin for pediatric UTI & enteric fever.",
            keywords = listOf("cefixime", "cefim", "uti", "enteric", "oral cephalosporin")
        ),
        WeightBasedDrugDef(
            id = "peads_cefotaxime",
            tabId = DoseTabId.PEADS_CORE,
            name = "Cefotaxime IV",
            routeTag = "IV",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "Standard 150 mg/kg/d (÷3 q8h)",
                    dosePerKg = 150.0,
                    isDailyDose = true,
                    dosesPerDay = 3,
                    maxSingleMg = 2000.0,
                    maxDailyMg = 12000.0,
                    frequencyLabel = "q8h"
                ),
                WeightDoseRegimen(
                    id = "meningitis",
                    label = "Meningitis 200 mg/kg/d (÷4 q6h)",
                    dosePerKg = 200.0,
                    isDailyDose = true,
                    dosesPerDay = 4,
                    maxSingleMg = 2000.0,
                    maxDailyMg = 12000.0,
                    frequencyLabel = "q6h"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c100", "100 mg/mL", 100.0),
                DrugConcentration("c50", "50 mg/mL", 50.0)
            ),
            pearl = "Preferred 3rd-gen cephalosporin in neonates (safe with bilirubin & Ca²⁺).",
            keywords = listOf("cefotaxime", "claforan", "meningitis", "neonatal sepsis", "iv")
        ),
        WeightBasedDrugDef(
            id = "peads_ceftriaxone",
            tabId = DoseTabId.PEADS_CORE,
            name = "Ceftriaxone IV",
            routeTag = "IV",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "Standard 50 mg/kg OD",
                    dosePerKg = 50.0,
                    isDailyDose = true,
                    dosesPerDay = 1,
                    maxSingleMg = 2000.0,
                    maxDailyMg = 2000.0,
                    frequencyLabel = "q24h (OD)"
                ),
                WeightDoseRegimen(
                    id = "meningitis",
                    label = "Meningitis/Severe 100 mg/kg/d (÷2 q12h)",
                    dosePerKg = 100.0,
                    isDailyDose = true,
                    dosesPerDay = 2,
                    maxSingleMg = 2000.0,
                    maxDailyMg = 4000.0,
                    frequencyLabel = "q12h"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c100", "100 mg/mL", 100.0),
                DrugConcentration("c40", "40 mg/mL", 40.0)
            ),
            pearl = "Avoid in hyperbilirubinemic neonates or with IV calcium solutions.",
            isRedFlagPearl = true,
            keywords = listOf("ceftriaxone", "rocephin", "meningitis", "sepsis", "iv")
        ),
        WeightBasedDrugDef(
            id = "peads_co_amoxiclav",
            tabId = DoseTabId.PEADS_CORE,
            name = "Co-Amoxiclav (Amoxicillin Component)",
            routeTag = "PO",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "r4_1",
                    label = "4:1 TDS (30 mg/kg/d ÷3)",
                    dosePerKg = 30.0,
                    isDailyDose = true,
                    dosesPerDay = 3,
                    maxSingleMg = 500.0,
                    maxDailyMg = 1500.0,
                    frequencyLabel = "q8h (TDS)",
                    allowedConcIds = setOf("c156", "c312")
                ),
                WeightDoseRegimen(
                    id = "r7_1",
                    label = "7:1 BD (45 mg/kg/d ÷2)",
                    dosePerKg = 45.0,
                    isDailyDose = true,
                    dosesPerDay = 2,
                    maxSingleMg = 875.0,
                    maxDailyMg = 1750.0,
                    frequencyLabel = "q12h (BD)",
                    allowedConcIds = setOf("c457")
                )
            ),
            concentrations = listOf(
                DrugConcentration("c156", "156.25 (125 amox)/5 mL", 25.0),
                DrugConcentration("c312", "312.5 (250 amox)/5 mL", 50.0),
                DrugConcentration("c457", "457 (400 amox)/5 mL", 80.0)
            ),
            pearl = "Dose strictly by Amoxicillin component; match 4:1 TDS vs 7:1 BD formulation.",
            keywords = listOf("co-amoxiclav", "augmentin", "amoxicillin clavulanate", "syrup")
        ),
        WeightBasedDrugDef(
            id = "peads_gentamicin",
            tabId = DoseTabId.PEADS_CORE,
            name = "Gentamicin IV",
            routeTag = "IV · q24h OD",
            regimens = listOf(
                WeightDoseRegimen("peds", "Pediatric 7 mg/kg OD", 7.0, maxSingleMg = 500.0, frequencyLabel = "q24h (OD)"),
                WeightDoseRegimen("neo", "Neonatal/Conserv 5 mg/kg OD", 5.0, maxSingleMg = 500.0, frequencyLabel = "q24h (OD)")
            ),
            concentrations = listOf(
                DrugConcentration("c40", "40 mg/mL", 40.0),
                DrugConcentration("c10", "10 mg/mL", 10.0)
            ),
            pearl = "Infuse over 30 min; check trough level & serum creatinine before 2nd/3rd dose.",
            keywords = listOf("gentamicin", "aminoglycoside", "neonatal sepsis", "uti")
        ),
        WeightBasedDrugDef(
            id = "peads_ibuprofen",
            tabId = DoseTabId.PEADS_CORE,
            name = "Ibuprofen",
            routeTag = "PO · q6–8h",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "10 mg/kg/dose (q6–8h)",
                    dosePerKg = 10.0,
                    isDailyDose = false,
                    dosesPerDay = 4,
                    maxSingleMg = 400.0,
                    maxDailyMg = 1600.0,
                    frequencyLabel = "q6–8h PRN"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c100", "100 mg/5 mL", 20.0),
                DrugConcentration("c200", "200 mg/5 mL", 40.0)
            ),
            pearl = "Avoid <6 mo, dehydration, CKD, GI bleed, or suspected dengue.",
            isRedFlagPearl = true,
            keywords = listOf("ibuprofen", "brufen", "nsaid", "fever", "antipyretic", "pain")
        ),
        WeightBasedDrugDef(
            id = "peads_ondansetron",
            tabId = DoseTabId.PEADS_CORE,
            name = "Ondansetron",
            routeTag = "PO / IV · q8h PRN",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "0.15 mg/kg (max 4 mg <40 kg · 8 mg ≥40 kg)",
                    dosePerKg = 0.15,
                    maxSingleMg = 4.0,
                    frequencyLabel = "q8h PRN"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c4_5", "4 mg/5 mL (PO)", 0.8),
                DrugConcentration("c2_iv", "2 mg/mL (IV)", 2.0)
            ),
            pearl = "Single dose often suffices in gastroenteritis; avoid in congenital long QT.",
            keywords = listOf("ondansetron", "zofran", "vomiting", "antiemetic", "gastroenteritis"),
            specialMode = SpecialDrugMode.ONDANSETRON_DYNAMIC_CAP
        ),
        WeightBasedDrugDef(
            id = "peads_paracetamol",
            tabId = DoseTabId.PEADS_CORE,
            name = "Paracetamol (Acetaminophen)",
            routeTag = "PO / IV · q4–6h",
            regimens = listOf(
                WeightDoseRegimen(
                    id = "std",
                    label = "15 mg/kg/dose (max 60 mg/kg/d · cap 4 g/d)",
                    dosePerKg = 15.0,
                    isDailyDose = false,
                    dosesPerDay = 4,
                    maxSingleMg = 1000.0,
                    maxDailyMg = 4000.0,
                    frequencyLabel = "q4–6h PRN"
                )
            ),
            concentrations = listOf(
                DrugConcentration("c120", "120 mg/5 mL", 24.0),
                DrugConcentration("c250", "250 mg/5 mL", 50.0),
                DrugConcentration("c100d", "100 mg/mL Drops", 100.0),
                DrugConcentration("c10iv", "10 mg/mL IV", 10.0)
            ),
            pearl = "Max 4 doses/24h (60 mg/kg/day, hard adult cap 4000 mg/day).",
            keywords = listOf("paracetamol", "acetaminophen", "panadol", "calpol", "fever", "analgesia"),
            specialMode = SpecialDrugMode.PARACETAMOL_PEADS
        ),
        WeightBasedDrugDef(
            id = "peads_prednisolone",
            tabId = DoseTabId.PEADS_CORE,
            name = "Prednisolone Oral",
            routeTag = "PO · OD",
            regimens = listOf(
                WeightDoseRegimen("std1", "1 mg/kg/day (max 40 mg)", 1.0, maxSingleMg = 40.0, frequencyLabel = "once daily"),
                WeightDoseRegimen("std2", "2 mg/kg/day (max 60 mg)", 2.0, maxSingleMg = 60.0, frequencyLabel = "once daily")
            ),
            concentrations = listOf(
                DrugConcentration("c15", "15 mg/5 mL", 3.0),
                DrugConcentration("c5", "5 mg/5 mL", 1.0)
            ),
            pearl = "Acute asthma exacerbation: 1–2 mg/kg OD ×3–5 days (give with food).",
            keywords = listOf("prednisolone", "steroid", "asthma", "croup", "peads")
        )
    ).sortedBy { it.name.lowercase() }

    // ------------------------------------------------------------------------
    // UNIFIED SEARCH INDEX FOR EVERY DRUG / TOOL IN THE DOSE ENGINE
    // ------------------------------------------------------------------------
    val searchableDrugs: List<DoseDrugSearchEntry> = buildList {
        AdultErAndInfusionData.erRsiDrugs.forEach { d ->
            add(
                DoseDrugSearchEntry(
                    drugId = d.id,
                    title = d.name,
                    subtitle = "ER & RSI · ${d.routeTag}",
                    tabId = DoseTabId.ER_RSI,
                    keywords = d.keywords + listOf(d.name, d.routeTag, "er", "rsi")
                )
            )
        }
        AdultErAndInfusionData.continuousInfusions.forEach { inf ->
            add(
                DoseDrugSearchEntry(
                    drugId = inf.id,
                    title = inf.name,
                    subtitle = "Ward & ICU Infusion · ${inf.unitLabel}",
                    tabId = DoseTabId.WARD_ICU,
                    keywords = inf.keywords + listOf(inf.name, "infusion", "icu", "drip")
                )
            )
        }
        add(
            DoseDrugSearchEntry(
                drugId = "icu_insulin_drip",
                title = "IV Regular Insulin Infusion",
                subtitle = "Ward & ICU · DKA 0.1 / 0.05 U/kg/hr",
                tabId = DoseTabId.WARD_ICU,
                keywords = listOf("insulin", "regular insulin", "dka", "infusion", "drip")
            )
        )
        add(
            DoseDrugSearchEntry(
                drugId = "icu_mag_sulfate",
                title = "Magnesium Sulfate (IV Regimens)",
                subtitle = "Ward & ICU · Eclampsia, Asthma, Torsades",
                tabId = DoseTabId.WARD_ICU,
                keywords = listOf("magnesium", "mgso4", "eclampsia", "torsades", "asthma")
            )
        )
        add(
            DoseDrugSearchEntry(
                drugId = "icu_electrolyte_caps",
                title = "Electrolyte IV Replacement Safe Caps",
                subtitle = "Ward & ICU · KCl, MgSO₄, Phosphate limits",
                tabId = DoseTabId.WARD_ICU,
                keywords = listOf("kcl", "potassium", "electrolyte", "phosphate", "magnesium", "safe cap")
            )
        )
        add(
            DoseDrugSearchEntry(
                drugId = "icu_steroid_equiv",
                title = "Ward Steroid Equivalence Strip",
                subtitle = "Ward & ICU · Hydrocort : Pred : Methylpred : Dex",
                tabId = DoseTabId.WARD_ICU,
                keywords = listOf("steroid", "hydrocortisone", "prednisolone", "methylprednisolone", "dexamethasone", "equivalence")
            )
        )
        add(
            DoseDrugSearchEntry(
                drugId = "icu_adult_analgesia",
                title = "Adult Analgesia & Antiemetic Ward Dosing",
                subtitle = "Ward & ICU · Paracetamol, Ketorolac, Morphine, Ondansetron",
                tabId = DoseTabId.WARD_ICU,
                keywords = listOf("ketorolac", "morphine", "metoclopramide", "pantoprazole", "analgesia", "antiemetic")
            )
        )
        localAnesthetics.forEach { la ->
            add(
                DoseDrugSearchEntry(
                    drugId = la.id,
                    title = la.name,
                    subtitle = "Local Anesthetic · Max Safe mg & mL",
                    tabId = DoseTabId.LOCAL_ANESTH,
                    keywords = la.keywords + listOf(la.name, "la", "local anesthetic")
                )
            )
        }
        add(
            DoseDrugSearchEntry(
                drugId = "la_intralipid_last",
                title = "20% Intralipid LAST Rescue",
                subtitle = "Local Anesthetics · Live Bolus & Infusion",
                tabId = DoseTabId.LOCAL_ANESTH,
                keywords = listOf("intralipid", "last", "lipid emulsion", "local anesthetic toxicity", "rescue")
            )
        )
        peadsCoreDrugs.forEach { d ->
            add(
                DoseDrugSearchEntry(
                    drugId = d.id,
                    title = d.name,
                    subtitle = "Peads Core · ${d.routeTag}",
                    tabId = DoseTabId.PEADS_CORE,
                    keywords = d.keywords + listOf(d.name, d.routeTag, "pediatric", "peads", "syrup")
                )
            )
        }
    }

    fun searchDrugs(query: String): List<DoseDrugSearchEntry> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()
        val tokens = trimmed.split(Regex("\\s+")).filter { it.isNotEmpty() }
        return searchableDrugs.filter { entry ->
            val haystack = buildString {
                append(entry.title.lowercase()).append(' ')
                append(entry.subtitle.lowercase()).append(' ')
                entry.keywords.forEach { append(it.lowercase()).append(' ') }
            }
            tokens.all { token -> haystack.contains(token) }
        }.sortedWith(
            compareByDescending<DoseDrugSearchEntry> { it.title.lowercase().startsWith(trimmed) }
                .thenByDescending { it.keywords.any { kw -> kw.lowercase() == trimmed } }
                .thenBy { it.title }
        )
    }
}
