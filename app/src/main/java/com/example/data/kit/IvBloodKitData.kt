package com.example.data.kit

// ============================================================================
// BLOCK 3: IV FLUIDS, LINES & BLOOD (4 INTERACTIVE CARDS)
// ============================================================================

data class IvFluidRow(
    val name: String,
    val tonicity: String,
    val na: String,
    val k: String,
    val cl: String,
    val bufferOrDex: String,
    val osmolality: String,
    val wardUseAndCaution: String
)

data class IvCannulaRow(
    val gauge: String,
    val colorName: String,
    val colorHex: Long,
    val flowRate: String,
    val indications: String
)

data class TubeCatheterAgeGroup(
    val id: String,
    val ageLabel: String,
    val weightHint: String,
    val foleyFr: String,
    val ngTubeFr: String,
    val chestDrainFr: String,
    val lmaSize: String
)

data class BloodProductItem(
    val name: String,
    val volumeAndDose: String,
    val expectedRise: String,
    val infusionRule: String,
    val keyIndication: String
)

object IvBloodKitData {

    // ------------------------------------------------------------------------
    // CARD 1: IV Fluid Composition Table
    // ------------------------------------------------------------------------
    val ivFluids: List<IvFluidRow> = listOf(
        IvFluidRow(
            name = "0.9% Normal Saline",
            tonicity = "Isotonic",
            na = "154",
            k = "0",
            cl = "154",
            bufferOrDex = "None",
            osmolality = "308",
            wardUseAndCaution = "Resuscitation, DKA, blood carrier · Caution: hyperchloremic acidosis in large volumes."
        ),
        IvFluidRow(
            name = "Ringer's Lactate / Hartmann's",
            tonicity = "Balanced Isotonic",
            na = "130–131",
            k = "4–5",
            cl = "109–111",
            bufferOrDex = "Lactate 28–29",
            osmolality = "273–278",
            wardUseAndCaution = "1st-line burns, trauma, sepsis, surgery · Avoid with blood products (Ca²⁺ clots citrate)."
        ),
        IvFluidRow(
            name = "0.45% Saline (Half NS)",
            tonicity = "Hypotonic",
            na = "77",
            k = "0",
            cl = "77",
            bufferOrDex = "None",
            osmolality = "154",
            wardUseAndCaution = "Hypernatremia / free-water replacement · Caution: cerebral edema if rapid."
        ),
        IvFluidRow(
            name = "D5W (5% Dextrose)",
            tonicity = "Physiologically Hypotonic",
            na = "0",
            k = "0",
            cl = "0",
            bufferOrDex = "Dex 50 g/L",
            osmolality = "252",
            wardUseAndCaution = "Hypernatremia, drug diluent, hypoglycemia · Caution: hypotonic hyponatremia & cerebral edema."
        ),
        IvFluidRow(
            name = "D5 0.45% NS",
            tonicity = "Hyperosmolar / Hypotonic Na",
            na = "77",
            k = "0",
            cl = "77",
            bufferOrDex = "Dex 50 g/L",
            osmolality = "406",
            wardUseAndCaution = "Adult maintenance + dextrose (often + KCl) · Monitor Na⁺ in SIADH/post-op."
        ),
        IvFluidRow(
            name = "3% NaCl (Hypertonic)",
            tonicity = "Hypertonic",
            na = "513",
            k = "0",
            cl = "513",
            bufferOrDex = "None",
            osmolality = "1026",
            wardUseAndCaution = "Severe symptomatic hyponatremia / raised ICP · Cap Na⁺ rise ≤8 mEq/L/24h (ODS risk)."
        ),
        IvFluidRow(
            name = "Albumin 5%",
            tonicity = "Iso-oncotic Colloid",
            na = "130–160",
            k = "≤2",
            cl = "~130",
            bufferOrDex = "Albumin 50 g/L",
            osmolality = "~300",
            wardUseAndCaution = "Intravascular volume expansion, plasmapheresis · Caution: volume overload in CHF."
        ),
        IvFluidRow(
            name = "Albumin 20% (or 25%)",
            tonicity = "Hyper-oncotic Colloid",
            na = "130–160",
            k = "≤2",
            cl = "~130",
            bufferOrDex = "Albumin 200 g/L",
            osmolality = "~300",
            wardUseAndCaution = "Post-LVP paracentesis (6–8 g/L >5L), SBP, HRS · Draws 3–4× fluid intravascularly."
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: IV Cannula Sizing Chart
    // ------------------------------------------------------------------------
    val ivCannulas: List<IvCannulaRow> = listOf(
        IvCannulaRow(
            gauge = "14G",
            colorName = "Orange",
            colorHex = 0xFFEA580C,
            flowRate = "~240–300 mL/min",
            indications = "Massive trauma resuscitation, rapid blood/fluid bolus, major surgery."
        ),
        IvCannulaRow(
            gauge = "16G",
            colorName = "Grey",
            colorHex = 0xFF64748B,
            flowRate = "~180–210 mL/min",
            indications = "Shock resuscitation, trauma, rapid PRBC transfusion, high-risk obstetrics."
        ),
        IvCannulaRow(
            gauge = "18G",
            colorName = "Green",
            colorHex = 0xFF16A34A,
            flowRate = "~90–105 mL/min",
            indications = "Standard adult transfusion, CT contrast power-injection, ER/ward workhorse."
        ),
        IvCannulaRow(
            gauge = "20G",
            colorName = "Pink",
            colorHex = 0xFFEC4899,
            flowRate = "~60 mL/min",
            indications = "Routine adult IV antibiotics, maintenance fluids, non-emergent blood."
        ),
        IvCannulaRow(
            gauge = "22G",
            colorName = "Blue",
            colorHex = 0xFF2563EB,
            flowRate = "~36 mL/min",
            indications = "Older children, fragile/elderly veins, routine IV meds."
        ),
        IvCannulaRow(
            gauge = "24G",
            colorName = "Yellow",
            colorHex = 0xFFEAB308,
            flowRate = "~20 mL/min",
            indications = "Neonates, infants, small pediatric or very fragile veins."
        )
    )

    // ------------------------------------------------------------------------
    // CARD 3: Tubes & Catheters by Age
    // ------------------------------------------------------------------------
    val tubeCatheterGroups: List<TubeCatheterAgeGroup> = listOf(
        TubeCatheterAgeGroup(
            id = "neonate",
            ageLabel = "Neonate",
            weightHint = "< 5 kg",
            foleyFr = "5–6 Fr",
            ngTubeFr = "5–8 Fr",
            chestDrainFr = "8–12 Fr",
            lmaSize = "Size 1 (<5 kg)"
        ),
        TubeCatheterAgeGroup(
            id = "infant",
            ageLabel = "Infant",
            weightHint = "5–10 kg",
            foleyFr = "6–8 Fr",
            ngTubeFr = "8 Fr",
            chestDrainFr = "10–14 Fr",
            lmaSize = "Size 1.5 (5–10 kg)"
        ),
        TubeCatheterAgeGroup(
            id = "1_5y",
            ageLabel = "1–5 yr",
            weightHint = "10–20 kg",
            foleyFr = "8–10 Fr",
            ngTubeFr = "8–10 Fr",
            chestDrainFr = "14–20 Fr",
            lmaSize = "Size 2 (10–20 kg)"
        ),
        TubeCatheterAgeGroup(
            id = "6_12y",
            ageLabel = "6–12 yr",
            weightHint = "20–35 kg",
            foleyFr = "10–12 Fr",
            ngTubeFr = "10–14 Fr",
            chestDrainFr = "20–28 Fr",
            lmaSize = "Size 2.5 (20–30 kg) · Size 3 (>30 kg)"
        ),
        TubeCatheterAgeGroup(
            id = "adult",
            ageLabel = "Adult",
            weightHint = "> 50 kg",
            foleyFr = "14–16 Fr (F) · 16–18 Fr (M)",
            ngTubeFr = "14–18 Fr (12 Fr feeding)",
            chestDrainFr = "14 Fr (pneumo) · 28–32 Fr (hemo)",
            lmaSize = "Size 4 (50–70 kg) · Size 5 (>70 kg)"
        )
    )

    // ------------------------------------------------------------------------
    // CARD 4: Blood Products & Transfusion Guide
    // ------------------------------------------------------------------------
    val bloodProducts: List<BloodProductItem> = listOf(
        BloodProductItem(
            name = "PRBC (Packed Red Blood Cells)",
            volumeAndDose = "~300 mL / unit · Peads: 10–15 mL/kg",
            expectedRise = "1 unit raises adult Hb ~1 g/dL (Hct ~3%)",
            infusionRule = "Infuse over 2–3 hrs; MUST complete ≤4 hrs from issue",
            keyIndication = "Symptomatic anemia, Hb <7 g/dL (or <8 in ACS), acute hemorrhage"
        ),
        BloodProductItem(
            name = "FFP (Fresh Frozen Plasma)",
            volumeAndDose = "~250 mL / unit · Dose: 10–15 mL/kg",
            expectedRise = "Raises coagulation factors ~20–30%",
            infusionRule = "Infuse over 30–60 min/unit after thawing",
            keyIndication = "Active bleeding with INR >1.5, massive transfusion, DIC, TTP"
        ),
        BloodProductItem(
            name = "Platelets (Apheresis / Pooled)",
            volumeAndDose = "~250–300 mL (1 adult dose) · Peads: 10 mL/kg",
            expectedRise = "1 dose raises platelets ~30–50 ×10⁹/L",
            infusionRule = "Infuse over 30–60 min (never refrigerate)",
            keyIndication = "Plt <10k prophylaxis, <20k fever/sepsis, <50k active bleed/procedure"
        ),
        BloodProductItem(
            name = "Cryoprecipitate",
            volumeAndDose = "10 units pooled (~100–150 mL) · 1–2 u / 10 kg",
            expectedRise = "10 units raises Fibrinogen ~50–70 mg/dL (0.5–0.7 g/L)",
            infusionRule = "Infuse rapidly over 15–30 min via blood filter",
            keyIndication = "Fibrinogen <100–150 mg/dL (<1.0–1.5 g/L) with bleeding, DIC, PPH"
        )
    )

    val transfusionReactionSteps: List<Pair<String, String>> = listOf(
        "1. STOP" to "Stop the transfusion immediately — disconnect blood tubing.",
        "2. KEEP LINE OPEN" to "Flush/maintain IV access with 0.9% NS using NEW tubing.",
        "3. CHECK & VERIFY" to "Check vitals/ABC and re-verify patient wristband vs bag ID.",
        "4. NOTIFY & WORKUP" to "Call blood bank, return bag/tubing, send DAT, repeat crossmatch & urine Hb."
    )
}
