package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: GASTRO & HEPATOLOGY (5 INTERACTIVE CARDS)
// ============================================================================

enum class HepBSeverity {
    SAFE,
    INFO,
    WARNING,
    CRITICAL
}

data class HepBInterpretation(
    val title: String,
    val detail: String,
    val badge: String,
    val severity: HepBSeverity
)

data class HepCMatrixRow(
    val pattern: String,
    val meaning: String,
    val action: String,
    val isActive: Boolean = false
)

data class ForrestClassRow(
    val code: String,
    val appearance: String,
    val rebleedRisk: String,
    val isHighRisk: Boolean = false
)

data class LftPatternRow(
    val pattern: String,
    val rFactor: String,
    val bullets: List<String>
)

data class BristolStoolRow(
    val range: String,
    val description: String,
    val clinicalLabel: String,
    val isNormal: Boolean = false
)

object GastroKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Interactive Hepatitis B & C Serology Decoder
    // ------------------------------------------------------------------------
    fun decodeHepB(
        hbsAg: Boolean,
        antiHbs: Boolean,
        totalAntiHbc: Boolean,
        igmAntiHbc: Boolean
    ): HepBInterpretation {
        return when {
            !hbsAg && !antiHbs && !totalAntiHbc && !igmAntiHbc -> HepBInterpretation(
                title = "Susceptible",
                detail = "Not infected and no immunity — offer Hepatitis B vaccination if at risk.",
                badge = "VACCINATE",
                severity = HepBSeverity.INFO
            )
            !hbsAg && antiHbs && !totalAntiHbc && !igmAntiHbc -> HepBInterpretation(
                title = "Immune via Vaccination",
                detail = "Protective Anti-HBs antibodies from vaccination (no past infection).",
                badge = "IMMUNE",
                severity = HepBSeverity.SAFE
            )
            !hbsAg && antiHbs && totalAntiHbc && !igmAntiHbc -> HepBInterpretation(
                title = "Immune via Past Natural Infection",
                detail = "Resolved HBV. Note: Reactivation risk on Rituximab or heavy chemotherapy!",
                badge = "RESOLVED HBV",
                severity = HepBSeverity.SAFE
            )
            hbsAg && !antiHbs && totalAntiHbc && igmAntiHbc -> HepBInterpretation(
                title = "Acute Hepatitis B Infection",
                detail = "Active acute infection with high infectivity — supportive care, monitor INR/ALF.",
                badge = "ACUTE HBV",
                severity = HepBSeverity.CRITICAL
            )
            hbsAg && !antiHbs && totalAntiHbc && !igmAntiHbc -> HepBInterpretation(
                title = "Chronic Hepatitis B Infection",
                detail = "Check HBeAg, Anti-HBe, quantitative HBV DNA, ALT, & fibrosis/HCC surveillance.",
                badge = "CHRONIC HBV",
                severity = HepBSeverity.CRITICAL
            )
            !hbsAg && !antiHbs && totalAntiHbc && igmAntiHbc -> HepBInterpretation(
                title = "Acute Window Period",
                detail = "HBsAg has cleared while Anti-HBs has not yet risen; IgM Anti-HBc is sole acute marker.",
                badge = "WINDOW PERIOD",
                severity = HepBSeverity.WARNING
            )
            !hbsAg && !antiHbs && totalAntiHbc && !igmAntiHbc -> HepBInterpretation(
                title = "Isolated Anti-HBc",
                detail = "Resolved past infection (waned Anti-HBs), false positive, or occult HBV — check HBV DNA.",
                badge = "CHECK HBV DNA",
                severity = HepBSeverity.WARNING
            )
            else -> HepBInterpretation(
                title = "Unusual / Discordant Pattern",
                detail = "Repeat serology panel & order quantitative HBV DNA to clarify.",
                badge = "DISCORDANT",
                severity = HepBSeverity.WARNING
            )
        }
    }

    val hepCMatrix: List<HepCMatrixRow> = listOf(
        HepCMatrixRow(
            pattern = "Anti-HCV (−) · HCV RNA (−)",
            meaning = "No Infection",
            action = "No further HCV follow-up unless recent high-risk exposure."
        ),
        HepCMatrixRow(
            pattern = "Anti-HCV (+) · HCV RNA (+)",
            meaning = "Active Current Infection",
            action = "Assess fibrosis (FIB-4 / Elastography) → Pan-genotypic DAA therapy.",
            isActive = true
        ),
        HepCMatrixRow(
            pattern = "Anti-HCV (+) · HCV RNA (−)",
            meaning = "Resolved / Cured Infection",
            action = "Cleared spontaneously or cured; Anti-HCV is NOT protective against reinfection."
        ),
        HepCMatrixRow(
            pattern = "Anti-HCV (−) · HCV RNA (+)",
            meaning = "Early Acute or Immunosuppressed",
            action = "Acute window before seroconversion or severe immunosuppression — link to care.",
            isActive = true
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: Acute GI Bleed Bundles (Variceal vs. Non-Variceal)
    // ------------------------------------------------------------------------
    const val giBleedResusStrip =
        "2 large-bore IVs (16–18G) · Type & Crossmatch · Restrictive Transfusion Target: Hb 7–8 g/dL (avoid over-transfusion in cirrhosis → ↑ portal pressure!) · Intubate early if hematemesis + GCS <8."

    val varicealBundleSteps: List<Pair<String, String>> = listOf(
        "1. Vasoactive Drug STAT (Pre-Scope)" to "Terlipressin 2 mg IV q4h (then 1 mg q4h; avoid in CAD/ischemia) OR Octreotide 50 mcg IV bolus → 50 mcg/hr infusion ×3–5d.",
        "2. Prophylactic IV Antibiotic STAT" to "Ceftriaxone 1 g IV OD ×5–7 days (proven to reduce mortality & SBP).",
        "3. Urgent Endoscopy (<12 Hours)" to "Esophageal variceal band ligation OR Cyanoacrylate glue for gastric varices.",
        "4. Refractory Bleeding Rescue" to "Sengstaken-Blakemore tube / Danis stent bridge → Salvage TIPS."
    )

    val nonVaricealSteps: List<Pair<String, String>> = listOf(
        "1. IV PPI + Pre-Scope Prokinetic" to "Pantoprazole 80 mg IV bolus → 8 mg/hr drip (or 40 mg IV q12h) + IV Erythromycin 250 mg 30m pre-scope to clear stomach.",
        "2. Urgent Endoscopy (<24 Hours)" to "Dual endoscopic therapy (adrenaline + clip/thermal) for high-risk stigmata."
    )

    val forrestClasses: List<ForrestClassRow> = listOf(
        ForrestClassRow("Ia", "Active spurting hemorrhage", "~90% rebleed", isHighRisk = true),
        ForrestClassRow("Ib", "Active oozing hemorrhage", "~50% rebleed", isHighRisk = true),
        ForrestClassRow("IIa", "Non-bleeding visible vessel", "~43% rebleed", isHighRisk = true),
        ForrestClassRow("IIb", "Adherent clot", "~22% rebleed"),
        ForrestClassRow("IIc", "Flat pigmented hematin spot", "<10% rebleed"),
        ForrestClassRow("III", "Clean-based ulcer", "<5% rebleed")
    )

    // ------------------------------------------------------------------------
    // CARD 3: LFT Patterns & Jaundice Matrix
    // ------------------------------------------------------------------------
    const val rFactorFormula = "R-Factor = (ALT ÷ ULN) ÷ (ALP ÷ ULN)"

    val lftPatterns: List<LftPatternRow> = listOf(
        LftPatternRow(
            pattern = "Hepatocellular (↑↑ AST / ALT)",
            rFactor = "R ≥ 5",
            bullets = listOf(
                "AST/ALT >1000: Ischemic hepatitis ('shock liver'), Paracetamol toxicity, Acute viral hepatitis.",
                "AST:ALT ≥2 (both <300–500 + ↑GGT): Alcoholic liver disease.",
                "Mild–Moderate (<300): MASLD/NAFLD, chronic viral, autoimmune, Wilson's, hemochromatosis, DILI."
            )
        ),
        LftPatternRow(
            pattern = "Cholestatic (↑↑ ALP & GGT)",
            rFactor = "R ≤ 2",
            bullets = listOf(
                "Confirm hepatic origin with GGT (normal GGT = bone or placenta).",
                "RUQ Ultrasound STAT → Dilated ducts = Extrahepatic (stone, pancreatic/biliary tumor, stricture).",
                "Non-dilated ducts = Intrahepatic (PBC, PSC, sepsis, drugs: Co-Amoxiclav / steroids)."
            )
        ),
        LftPatternRow(
            pattern = "Mixed Pattern",
            rFactor = "R = 2–5",
            bullets = listOf(
                "Drug-induced liver injury (DILI), infiltrating hepatic disease, or early biliary obstruction."
            )
        )
    )

    val jaundiceBuckets: List<Pair<String, String>> = listOf(
        "Unconjugated (Indirect) · No Bilirubinuria" to "Hemolysis (↑LDH, ↑Retics, ↓Haptoglobin), Gilbert syndrome (benign; ↑ with fasting/stress), Crigler-Najjar.",
        "Conjugated (Direct) · Dark Urine ± Pale Stools" to "Hepatocellular injury, biliary obstruction, sepsis, Dubin-Johnson / Rotor syndromes."
    )

    // ------------------------------------------------------------------------
    // CARD 4: Cirrhosis Complication Protocols
    // ------------------------------------------------------------------------
    val westHavenGrades: List<Pair<String, String>> = listOf(
        "Grade 1" to "Sleep-wake inversion, mild confusion, shortened attention.",
        "Grade 2" to "Lethargy, disorientation, asterixis (flapping tremor).",
        "Grade 3" to "Somnolent / arousable, gross disorientation, bizarre behavior.",
        "Grade 4" to "Coma — protect airway immediately!"
    )

    val heManagementBullets: List<String> = listOf(
        "Hunt Precipitants First: Infection/SBP, GI bleed, constipation, hypokalemia, sedatives, dehydration/diuretics.",
        "Lactulose: 20–30 mL PO q1–2h until stool, then titrate to 2–3 soft stools/day (retention enema if NPO).",
        "Rifaximin: 550 mg PO BD added for recurrent or refractory hepatic encephalopathy."
    )

    val sbpProtocolBullets: List<String> = listOf(
        "Diagnostic Tap Cutoff: Ascitic PMN ≥ 250 cells/mm³ (tap EVERY admitted cirrhotic with ascites!).",
        "Empiric IV Abx: Ceftriaxone 2 g IV OD (or Cefotaxime 2 g q8h) × 5 days.",
        "IV Albumin Mandatory: 1.5 g/kg Day 1 + 1.0 g/kg Day 3 (prevents HRS & cuts mortality).",
        "Secondary Prophylaxis: Ciprofloxacin / Norfloxacin or TMP-SMX post-episode."
    )

    val hrsAkiBullets: List<String> = listOf(
        "Diagnosis: Cirrhosis + ascites + AKI without shock, nephrotoxins, or structural kidney disease, failing 48h diuretic withdrawal + IV Albumin (1 g/kg/day, max 100 g).",
        "Treatment: IV Albumin (20–40 g/day) + Vasoconstrictor: Terlipressin (monitor for respiratory failure) OR Norepinephrine in ICU.",
        "Definitive Cure: Liver transplantation evaluation."
    )

    // ------------------------------------------------------------------------
    // CARD 5: Stool, H. pylori & IBD / IBS Key
    // ------------------------------------------------------------------------
    val bristolStoolRows: List<BristolStoolRow> = listOf(
        BristolStoolRow("Types 1–2", "Separate hard lumps or lumpy sausage", "Constipation"),
        BristolStoolRow("Types 3–4", "Cracked or smooth soft sausage", "Ideal Normal", isNormal = true),
        BristolStoolRow("Type 5", "Soft blobs with clear-cut edges", "Lacking fiber"),
        BristolStoolRow("Types 6–7", "Fluffy mushy pieces or entirely watery", "Diarrhea")
    )

    val trueloveWittsCriteria: List<String> = listOf(
        "≥6 bloody stools/day PLUS ≥1 Systemic Sign: Temp >37.8°C, HR >90 bpm, Hb <10.5 g/dL, ESR >30 or CRP >30 mg/L."
    )

    val asucWardBundle: List<String> = listOf(
        "Admit + Stool C&S / C. diff + AXR (exclude Toxic Megacolon >6 cm).",
        "IV Hydrocortisone 100 mg q6h + LMWH VTE prophylaxis.",
        "Early GI + Surgical co-management; assess Day 3 Oxford criteria for Infliximab / Cyclosporine rescue."
    )

    val hPyloriAndIbsBullets: List<String> = listOf(
        "1st-Line H. pylori (14d): Bismuth Quadruple (PPI + Bismuth + Tetracycline + Metronidazole) where Clarithromycin resistance >15%.",
        "Alternative: Clarithromycin Triple (PPI + Clarithromycin + Amoxicillin). Confirm cure ≥4 wks post-Rx (off PPI ≥2 wks).",
        "Rome IV IBS: Recurrent abdominal pain ≥1 day/wk × 3 mo linked to defecation + stool frequency/form change.",
        "IBS Red Flags (Scope!): Age >50, weight loss, rectal bleeding, nocturnal pain, or anemia."
    )
}
