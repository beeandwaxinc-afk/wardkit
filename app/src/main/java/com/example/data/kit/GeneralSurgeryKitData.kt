package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: GENERAL SURGERY (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class AtlsShockClass(
    val id: String,
    val label: String,
    val bloodLossPct: String,
    val bloodLossMl: String,
    val heartRate: String,
    val bloodPressure: String,
    val pulsePressure: String,
    val respRate: String,
    val urineOutput: String,
    val cnsStatus: String,
    val baseDeficit: String,
    val fluidAction: String,
    val isCritical: Boolean = false
)

data class BurnRegionRow(
    val region: String,
    val adultPct: String,
    val pedsPct: String
)

data class SutureRemovalRow(
    val site: String,
    val gauge: String,
    val removalDays: String
)

data class SutureMaterialRow(
    val name: String,
    val type: String,
    val absorptionOrUse: String
)

data class TetanusRuleRow(
    val vaccineHistory: String,
    val action: String,
    val needsTig: Boolean = false
)

data class WoundClassRow(
    val classLabel: String,
    val ssiRate: String,
    val examples: String
)

data class PreOpAbxRow(
    val surgeryType: String,
    val typicalAgent: String
)

data class PostOpFeverStage(
    val window: String,
    val wLabel: String,
    val causes: List<String>
)

data class StomaNormRow(
    val type: String,
    val siteAndAppearance: String,
    val dailyOutput: String,
    val alertNote: String
)

object GeneralSurgeryKitData {

    // ------------------------------------------------------------------------
    // CARD 1: ATLS Hemorrhagic Shock (Classes I–IV · 10th Ed)
    // ------------------------------------------------------------------------
    val atlsShockClasses: List<AtlsShockClass> = listOf(
        AtlsShockClass(
            id = "c1",
            label = "Class I",
            bloodLossPct = "<15%",
            bloodLossMl = "<750 mL",
            heartRate = "<100",
            bloodPressure = "Normal",
            pulsePressure = "Normal / ↑",
            respRate = "14–20",
            urineOutput = ">30 mL/hr",
            cnsStatus = "Slightly anxious",
            baseDeficit = "0 to -2 mEq/L",
            fluidAction = "Monitor / Crystalloid"
        ),
        AtlsShockClass(
            id = "c2",
            label = "Class II",
            bloodLossPct = "15–30%",
            bloodLossMl = "750–1500 mL",
            heartRate = "100–120",
            bloodPressure = "Normal",
            pulsePressure = "↓ Narrowed",
            respRate = "20–30",
            urineOutput = "20–30 mL/hr",
            cnsStatus = "Mildly anxious",
            baseDeficit = "-2 to -6 mEq/L",
            fluidAction = "Isotonic Crystalloid (1 L warmed)"
        ),
        AtlsShockClass(
            id = "c3",
            label = "Class III",
            bloodLossPct = "31–40%",
            bloodLossMl = "1500–2000 mL",
            heartRate = "120–140",
            bloodPressure = "↓ Decreased",
            pulsePressure = "↓ Narrowed",
            respRate = "30–40",
            urineOutput = "5–15 mL/hr",
            cnsStatus = "Anxious / Confused",
            baseDeficit = "-6 to -10 mEq/L",
            fluidAction = "Crystalloid + Blood Products",
            isCritical = true
        ),
        AtlsShockClass(
            id = "c4",
            label = "Class IV",
            bloodLossPct = ">40%",
            bloodLossMl = ">2000 mL",
            heartRate = ">140",
            bloodPressure = "↓↓ Severe",
            pulsePressure = "↓↓ Very Narrow",
            respRate = ">35",
            urineOutput = "Negligible",
            cnsStatus = "Confused / Lethargic",
            baseDeficit = "≤ -10 mEq/L",
            fluidAction = "Activate MTP (1:1:1) + TXA 1g + Control Bleeding",
            isCritical = true
        )
    )

    const val atlsRedFlag =
        "Elderly, beta-blockers, & athletes may mask tachycardia until sudden collapse."

    // ------------------------------------------------------------------------
    // CARD 2: Burn Surface Area Reference (Rule of Nines & ABA Transfer)
    // ------------------------------------------------------------------------
    val burnRegions: List<BurnRegionRow> = listOf(
        BurnRegionRow("Head & Neck", "9%", "18%"),
        BurnRegionRow("Each Upper Limb", "9%", "9%"),
        BurnRegionRow("Anterior Trunk", "18%", "18%"),
        BurnRegionRow("Posterior Trunk", "18%", "18%"),
        BurnRegionRow("Each Lower Limb", "18%", "13.5–14%"),
        BurnRegionRow("Perineum", "1%", "1%")
    )

    const val lundBrowderPearl =
        "Lund-Browder (1–10 yrs): subtract ~1% from head & add 0.5% to each leg per year of age."

    const val burnPalmPearl =
        "Patient's palm + closed fingers ≈ 1% TBSA; count ONLY partial (2nd) & full-thickness (3rd) burns (exclude 1st-degree erythema)."

    val abaTransferCriteria: List<String> = listOf(
        "Partial-thickness burns >10% TBSA.",
        "Burns involving Face, Hands, Feet, Genitalia, Perineum, or Major Joints.",
        "Any Full-Thickness (3rd-degree) burn in any age group.",
        "Electrical (including lightning) or Chemical burns.",
        "Inhalation injury (facial soot, singed nasal hairs, hoarseness, carbonaceous sputum).",
        "Major comorbidities, concomitant trauma, or circumferential limb/chest burns (escharotomy risk)."
    )

    // ------------------------------------------------------------------------
    // CARD 3: Sutures & Removal Timeline
    // ------------------------------------------------------------------------
    val sutureRemovalRows: List<SutureRemovalRow> = listOf(
        SutureRemovalRow("Face / Eyelid", "5-0 to 6-0", "3–5 days"),
        SutureRemovalRow("Scalp", "3-0 to 4-0 / Staples", "7–10 days"),
        SutureRemovalRow("Neck", "4-0 to 5-0", "5–7 days"),
        SutureRemovalRow("Trunk / Abdomen / Back", "3-0 to 4-0", "7–10 d (tension 10–14 d)"),
        SutureRemovalRow("Upper / Lower Extremities", "4-0", "7–10 d (lower leg 10–14 d)"),
        SutureRemovalRow("Over Joints / Palms / Soles", "3-0 to 4-0", "10–14 days")
    )

    const val sutureDelayNote =
        "Add 3–5 days in diabetics, steroid users, malnutrition, or radiation sites."

    val absorbableSutures: List<SutureMaterialRow> = listOf(
        SutureMaterialRow("Plain / Chromic Gut", "Natural twisted", "Fast absorption; high tissue reactivity"),
        SutureMaterialRow("Vicryl (Polyglactin)", "Braided synthetic", "Absorbs ~60–70 days; deep dermis / mucosa"),
        SutureMaterialRow("Monocryl", "Monofilament", "Subcuticular closure; low drag & minimal scar"),
        SutureMaterialRow("PDS (Polydioxanone)", "Slow monofilament", "Absorbs ~180 days; abdominal fascia / high tension")
    )

    val nonAbsorbableSutures: List<SutureMaterialRow> = listOf(
        SutureMaterialRow("Nylon (Ethilon)", "Monofilament", "Routine skin closure; low reactivity"),
        SutureMaterialRow("Polypropylene (Prolene)", "Monofilament", "Vascular, fascia, skin; lowest reactivity"),
        SutureMaterialRow("Silk", "Braided natural", "Drain anchoring & ties; high tissue drag")
    )

    // ------------------------------------------------------------------------
    // CARD 4: Wounds, Tetanus & Surgical Prophylaxis
    // ------------------------------------------------------------------------
    val tetanusCleanMinor: List<TetanusRuleRow> = listOf(
        TetanusRuleRow("≥3 doses & last <10 yrs", "None"),
        TetanusRuleRow("≥3 doses & last ≥10 yrs", "Td / Tdap Booster"),
        TetanusRuleRow("<3 doses or Unknown", "Td / Tdap (No TIG)")
    )

    val tetanusDirtyProne: List<TetanusRuleRow> = listOf(
        TetanusRuleRow("≥3 doses & last <5 yrs", "None"),
        TetanusRuleRow("≥3 doses & last ≥5 yrs", "Td / Tdap Booster"),
        TetanusRuleRow("<3 doses or Unknown", "Td / Tdap + TIG 250 units IM", needsTig = true)
    )

    const val tetanusDirtyExamples =
        "Dirty / Tetanus-prone: soil, feces, puncture, crush, burn, frostbite, or devitalized tissue. Give TIG in a separate syringe & site."

    val cdcWoundClasses: List<WoundClassRow> = listOf(
        WoundClassRow("Class I · Clean", "<2% SSI", "Uninfected elective; no GI/GU/resp tract entry (hernia, thyroid)"),
        WoundClassRow("Class II · Clean-Contaminated", "3–7% SSI", "Controlled GI/GU/biliary/resp tract entry without major spill"),
        WoundClassRow("Class III · Contaminated", "10–17% SSI", "Fresh open trauma (<4h), major sterile break, gross GI spillage"),
        WoundClassRow("Class IV · Dirty / Infected", ">25% SSI", "Old trauma with devitalized tissue, perforated viscus, frank pus")
    )

    val preOpTimingBullets: List<String> = listOf(
        "Give within 60 min BEFORE incision (120 min for Vancomycin / Fluoroquinolones).",
        "Redose intra-op if duration >2 half-lives (Cefazolin q4h) or blood loss >1500 mL.",
        "Discontinue prophylactic antibiotics within 24 hours post-op."
    )

    val preOpTypicalAgents: List<PreOpAbxRow> = listOf(
        PreOpAbxRow("Clean / Ortho / Cardio", "Cefazolin 2g IV (3g if ≥120 kg)"),
        PreOpAbxRow("Colorectal / Biliary / Appendix", "Cefazolin + Metronidazole (or Cefoxitin)")
    )

    // ------------------------------------------------------------------------
    // CARD 5: Post-Op Fever (5 Ws) & Drains / Stomas
    // ------------------------------------------------------------------------
    val postOpFeverTimeline: List<PostOpFeverStage> = listOf(
        PostOpFeverStage(
            window = "DAY 0–1",
            wLabel = "Wind / Inflammatory",
            causes = listOf(
                "Benign surgical cytokine release & atelectasis.",
                "Day 0: Malignant hyperthermia, transfusion, or drug reaction."
            )
        ),
        PostOpFeverStage(
            window = "DAY 2–3",
            wLabel = "Water / Wind",
            causes = listOf(
                "Pneumonia (HAP or aspiration).",
                "Urinary tract infection (catheter-associated)."
            )
        ),
        PostOpFeverStage(
            window = "DAY 3–5",
            wLabel = "Walking / Lines",
            causes = listOf(
                "DVT / Pulmonary embolism (PE).",
                "Thrombophlebitis, IV cannula, or central line sepsis."
            )
        ),
        PostOpFeverStage(
            window = "DAY 5–7+",
            wLabel = "Wound / Abscess",
            causes = listOf(
                "Surgical site infection (SSI) & anastomotic leak (Day 5–7 classic!).",
                "Intra-abdominal abscess or C. difficile colitis."
            )
        ),
        PostOpFeverStage(
            window = "ANY DAY",
            wLabel = "Wonder Drugs",
            causes = listOf(
                "Drug fever, alcohol withdrawal, or adrenal crisis."
            )
        )
    )

    const val postOpRedFlag =
        "Fever <24h with severe pain, crepitus, or shock → rule out Necrotizing Fasciitis / Group A Strep or Clostridial SSI."

    val drainRemovalCriteria: List<String> = listOf(
        "Serous / non-purulent output with volume <20–30 mL/24h for 2 consecutive days.",
        "Inspect character first: check for bile, amylase/pancreatic leak, chyle, or feculent material."
    )

    val stomaNorms: List<StomaNormRow> = listOf(
        StomaNormRow(
            type = "Ileostomy",
            siteAndAppearance = "RLQ · Spouted · Liquid green/brown",
            dailyOutput = "500–1200 mL/d",
            alertNote = "Flag high output >1500–2000 mL/d (AKI & hypomagnesemia risk)."
        ),
        StomaNormRow(
            type = "Colostomy",
            siteAndAppearance = "LLQ · Flush · Formed stool",
            dailyOutput = "200–500 mL/d",
            alertNote = "Slower return of function; watch for parastomal hernia or retraction."
        )
    )

    const val stomaIschemiaWarning =
        "Urgent ischemia sign: dusky, purple, or black stoma mucosa — assess viability immediately."
}
