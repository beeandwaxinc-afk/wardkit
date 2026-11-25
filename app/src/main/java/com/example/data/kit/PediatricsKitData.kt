package com.example.data.kit

// ============================================================================
// BLOCK 1: PEDIATRICS QUICK-REF (5 INTERACTIVE CLINICAL CARDS)
// ============================================================================

data class PedsGrowthVitalsRow(
    val id: String,
    val ageLabel: String,
    val weightKg: String,
    val heightCm: String,
    val ofcCm: String,
    val hrBpm: String,
    val rrPerMin: String,
    val sbpMmHg: String
)

data class PedsFormulaRule(
    val label: String,
    val formula: String
)

data class PedsMilestoneAge(
    val id: String,
    val ageLabel: String,
    val grossMotor: String,
    val fineMotor: String,
    val language: String,
    val socialAdaptive: String,
    val redFlag: String
)

data class WhoSignRow(
    val feature: String,
    val finding: String
)

data class WhoOrsAgeRow(
    val age: String,
    val weight: String,
    val volumeMl: String
)

data class WhoDehydrationPlan(
    val id: String,
    val shortTitle: String,
    val classification: String,
    val severityTag: String,
    val signs: List<WhoSignRow>,
    val managementSteps: List<String>,
    val zincNote: String? = null,
    val showOrsAgeTable: Boolean = false,
    val ivProtocolRows: List<Pair<String, String>> = emptyList(),
    val dangerNote: String
)

data class NeonatalFluidDay(
    val day: String,
    val mlPerKgDay: String
)

data class NeonatalSpo2Target(
    val minute: String,
    val target: String
)

data class ImmunizationAgeRow(
    val age: String,
    val vaccines: List<String>
)

object PediatricsKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Age Weight, Height, OFC & Vitals Lookup
    // ------------------------------------------------------------------------
    val growthVitalsRows: List<PedsGrowthVitalsRow> = listOf(
        PedsGrowthVitalsRow("nb", "Newborn", "3.5 kg", "50 cm", "35 cm", "100–160", "30–60", "60–90"),
        PedsGrowthVitalsRow("3m", "3 mo", "6.0 kg", "60 cm", "40 cm", "100–160", "30–50", "70–100"),
        PedsGrowthVitalsRow("6m", "6 mo", "7.5–8 kg", "67 cm", "43 cm", "100–160", "25–45", "70–100"),
        PedsGrowthVitalsRow("1y", "1 yr", "10 kg", "75 cm", "46 cm", "90–150", "24–40", "72–104"),
        PedsGrowthVitalsRow("2y", "2 yr", "12 kg", "87 cm", "48 cm", "80–140", "22–37", "80–110"),
        PedsGrowthVitalsRow("3y", "3 yr", "14 kg", "95 cm", "49 cm", "80–130", "20–30", "85–112"),
        PedsGrowthVitalsRow("4y", "4 yr", "16 kg", "102 cm", "50 cm", "80–120", "20–28", "86–114"),
        PedsGrowthVitalsRow("5y", "5 yr", "18 kg", "109 cm", "50.5 cm", "75–115", "18–25", "88–115"),
        PedsGrowthVitalsRow("6_8y", "6–8 yr", "20–26 kg", "115–128 cm", "—", "70–110", "18–25", "90–118"),
        PedsGrowthVitalsRow("10y", "10 yr", "32 kg", "138 cm", "—", "65–105", "16–22", "95–122"),
        PedsGrowthVitalsRow("12y", "12 yr", "40 kg", "149 cm", "—", "60–100", "14–20", "100–126")
    )

    val bedsideFormulas: List<PedsFormulaRule> = listOf(
        PedsFormulaRule("Weight (1–5 yr)", "2 × (Age + 4) kg"),
        PedsFormulaRule("Weight (6–12 yr)", "3 × Age + 7 kg"),
        PedsFormulaRule("Height (2–12 yr)", "6 × Age + 77 cm"),
        PedsFormulaRule("Hypotension SBP (1–10 yr)", "< 70 + (2 × Age) mmHg")
    )

    // ------------------------------------------------------------------------
    // CARD 2: Developmental Milestones & Red Flags
    // ------------------------------------------------------------------------
    val milestoneAges: List<PedsMilestoneAge> = listOf(
        PedsMilestoneAge(
            id = "m2",
            ageLabel = "2 mo",
            grossMotor = "Lifts head & chest prone; brief head control upright.",
            fineMotor = "Hands open briefly; tracks objects past midline.",
            language = "Coos vowel sounds; turns toward voice.",
            socialAdaptive = "Social smile; calms when spoken to or held.",
            redFlag = "No social smile, no visual tracking, or no startle to loud sound."
        ),
        PedsMilestoneAge(
            id = "m4",
            ageLabel = "4 mo",
            grossMotor = "Holds head steady; pushes up onto forearms prone.",
            fineMotor = "Brings hands to midline; reaches for and grasps rattle.",
            language = "Laughs aloud; babbles/coos reciprocally.",
            socialAdaptive = "Smiles spontaneously; watches faces intently.",
            redFlag = "Head not steady, no cooing/vocalizing, or fisted hands persistently."
        ),
        PedsMilestoneAge(
            id = "m6",
            ageLabel = "6 mo",
            grossMotor = "Rolls supine-to-prone; sits with tripod hand support.",
            fineMotor = "Transfers cube hand-to-hand; raking palmar grasp.",
            language = "Babbles consonants (ba, da); responds to name.",
            socialAdaptive = "Recognizes familiar faces; enjoys mirror play.",
            redFlag = "Persistent head lag, cannot reach for objects, or no vowel/consonant sounds."
        ),
        PedsMilestoneAge(
            id = "m9",
            ageLabel = "9 mo",
            grossMotor = "Sits independently without support; pulls to stand.",
            fineMotor = "Immature pincer grasp; bangs two cubes together.",
            language = "Nonspecific mama/dada; understands 'no' and gestures.",
            socialAdaptive = "Stranger anxiety; plays peek-a-boo; waves bye-bye.",
            redFlag = "Not sitting unsupported, no babbling, or no response to name."
        ),
        PedsMilestoneAge(
            id = "m12",
            ageLabel = "12 mo",
            grossMotor = "Cruises furniture; stands alone or takes first steps.",
            fineMotor = "Mature pincer grasp; drops blocks into cup.",
            language = "1–2 specific words; follows 1-step command with gesture.",
            socialAdaptive = "Points to show interest; drinks from open cup with help.",
            redFlag = "No standing with support, no pointing/gestures, or no single word."
        ),
        PedsMilestoneAge(
            id = "m18",
            ageLabel = "18 mo",
            grossMotor = "Walks independently well; climbs onto low chair.",
            fineMotor = "Builds 3–4 block tower; scribbles spontaneously.",
            language = "10–25 words; points to ≥1 body part on request.",
            socialAdaptive = "Simple pretend play (feeds doll); uses spoon.",
            redFlag = "Not walking independently, <6 words, or no pretend play / joint attention."
        ),
        PedsMilestoneAge(
            id = "y2",
            ageLabel = "2 yr",
            grossMotor = "Runs well; kicks ball; walks up/down stairs holding rail.",
            fineMotor = "Builds 6-block tower; turns single pages; copies vertical line.",
            language = "50+ words; combines 2-word phrases; 50% intelligible.",
            socialAdaptive = "Parallel play; copies adult tasks; removes simple clothing.",
            redFlag = "No 2-word meaningful phrases, cannot walk up stairs, or poor eye contact."
        ),
        PedsMilestoneAge(
            id = "y3",
            ageLabel = "3 yr",
            grossMotor = "Pedals tricycle; climbs stairs alternating feet.",
            fineMotor = "Copies a circle; stacks 9 cubes; holds crayon with fingers.",
            language = "3-word sentences; ≥75% intelligible; knows name & age.",
            socialAdaptive = "Group play; takes turns; dresses with minimal help.",
            redFlag = "Speech <50% intelligible, cannot pedal or copy circle, or falls frequently."
        ),
        PedsMilestoneAge(
            id = "y4",
            ageLabel = "4 yr",
            grossMotor = "Hops on one foot; catches bounced ball reliably.",
            fineMotor = "Copies a cross/square; uses blunt scissors; draws 3-part person.",
            language = "Tells short stories; ~100% intelligible to strangers.",
            socialAdaptive = "Cooperative imaginative play; prefers peers; toilet trained.",
            redFlag = "Cannot hop on one foot, unclear sentences, or unable to interact with peers."
        ),
        PedsMilestoneAge(
            id = "y5",
            ageLabel = "5 yr",
            grossMotor = "Skips alternating feet; balances on one foot ≥10 sec.",
            fineMotor = "Copies triangle; draws 6-part person; prints some letters.",
            language = "Fluent 5+ word sentences; counts to 10; knows colors.",
            socialAdaptive = "Follows game rules; dresses/undresses independently.",
            redFlag = "Cannot skip or draw person — ANY skill regression at ANY age = urgent red flag."
        )
    )

    // ------------------------------------------------------------------------
    // CARD 3: WHO Dehydration Plans A, B & C
    // ------------------------------------------------------------------------
    val whoPlans: List<WhoDehydrationPlan> = listOf(
        WhoDehydrationPlan(
            id = "plan_a",
            shortTitle = "Plan A (None)",
            classification = "No Dehydration",
            severityTag = "HOME THERAPY",
            signs = listOf(
                WhoSignRow("General Condition", "Well, alert"),
                WhoSignRow("Eyes", "Normal"),
                WhoSignRow("Thirst", "Drinks normally, not thirsty"),
                WhoSignRow("Skin Pinch", "Goes back quickly (<1 sec)")
            ),
            managementSteps = listOf(
                "Give extra home fluids + low-osmolarity ORS after each loose stool:",
                "• < 2 yr: 50–100 mL ORS after each loose stool",
                "• 2–10 yr: 100–200 mL ORS after each loose stool",
                "• > 10 yr: As much ORS as wanted",
                "Continue breastfeeding and age-appropriate feeding."
            ),
            zincNote = "Zinc × 10–14 days: <6 mo = 10 mg/day · ≥6 mo = 20 mg/day",
            dangerNote = "Return urgently: poor drinking, worsening stools, blood in stool, high fever, or repeated vomiting."
        ),
        WhoDehydrationPlan(
            id = "plan_b",
            shortTitle = "Plan B (Some)",
            classification = "Some Dehydration (≥2 signs)",
            severityTag = "CLINIC ORS · 4 HRS",
            signs = listOf(
                WhoSignRow("General Condition", "Restless, irritable"),
                WhoSignRow("Eyes", "Sunken"),
                WhoSignRow("Thirst", "Thirsty, drinks eagerly"),
                WhoSignRow("Skin Pinch", "Goes back slowly (1–2 sec)")
            ),
            managementSteps = listOf(
                "Give 75 mL/kg low-osmolarity ORS orally over 4 hours in clinic.",
                "If child wants more ORS than calculated, give more.",
                "If vomiting: wait 10 min, then continue more slowly (1 tsp q2–3 min).",
                "Continue breastfeeding freely throughout rehydration.",
                "Reassess at 4 hours → switch to Plan A, repeat Plan B, or escalate to Plan C."
            ),
            zincNote = "Start Zinc × 10–14 days once vomiting settles (<6 mo: 10 mg/day · ≥6 mo: 20 mg/day).",
            showOrsAgeTable = true,
            dangerNote = "If eyelid puffiness develops, pause ORS and give plain water/breastmilk."
        ),
        WhoDehydrationPlan(
            id = "plan_c",
            shortTitle = "Plan C (Severe)",
            classification = "Severe Dehydration (≥2 signs)",
            severityTag = "URGENT IV RESUS",
            signs = listOf(
                WhoSignRow("General Condition", "Lethargic or unconscious"),
                WhoSignRow("Eyes", "Sunken"),
                WhoSignRow("Thirst", "Drinks poorly or unable to drink"),
                WhoSignRow("Skin Pinch", "Goes back very slowly (>2 sec)")
            ),
            managementSteps = listOf(
                "Give 100 mL/kg IV Ringer's Lactate (or 0.9% NS if RL unavailable).",
                "Reassess every 15–30 min; repeat 30 mL/kg bolus if radial pulse weak/absent.",
                "Start ORS (5 mL/kg/hr) as soon as child can drink safely.",
                "Reassess hydration at 6 hrs (infants) or 3 hrs (older children)."
            ),
            ivProtocolRows = listOf(
                "< 12 months" to "30 mL/kg in 1 hr  →  then 70 mL/kg in 5 hrs",
                "≥ 12 months" to "30 mL/kg in 30 min  →  then 70 mL/kg in 2.5 hrs"
            ),
            dangerNote = "No IV access? Insert IO needle or NG ORS at 20 mL/kg/hr × 6 hrs (total 120 mL/kg)."
        )
    )

    val whoPlanBAgeTable: List<WhoOrsAgeRow> = listOf(
        WhoOrsAgeRow("< 4 mo", "< 6 kg", "200–450 mL"),
        WhoOrsAgeRow("4–12 mo", "6–<10 kg", "450–800 mL"),
        WhoOrsAgeRow("12 mo–2 yr", "10–<12 kg", "800–960 mL"),
        WhoOrsAgeRow("2–5 yr", "12–19 kg", "960–1600 mL")
    )

    // ------------------------------------------------------------------------
    // CARD 4: Neonatal Quick Card
    // ------------------------------------------------------------------------
    val neonatalDailyFluids: List<NeonatalFluidDay> = listOf(
        NeonatalFluidDay("Day 1", "60"),
        NeonatalFluidDay("Day 2", "80"),
        NeonatalFluidDay("Day 3", "100"),
        NeonatalFluidDay("Day 4", "120"),
        NeonatalFluidDay("Day 5+", "150")
    )

    val neonatalSpo2Targets: List<NeonatalSpo2Target> = listOf(
        NeonatalSpo2Target("1 min", "60–65%"),
        NeonatalSpo2Target("2 min", "65–70%"),
        NeonatalSpo2Target("3 min", "70–75%"),
        NeonatalSpo2Target("4 min", "75–80%"),
        NeonatalSpo2Target("5 min", "80–85%"),
        NeonatalSpo2Target("10 min", "85–95%")
    )

    val jaundiceRedFlags: List<String> = listOf(
        "Visible jaundice <24 hours of life (always pathologic — rule out hemolysis).",
        "Pale/acholic stools or dark urine (conjugated hyperbilirubinemia — biliary atresia).",
        "Rapid TSB rise >5 mg/dL/day (>85 µmol/L/day) or persistent jaundice >14d term / >21d preterm.",
        "Acute bilirubin encephalopathy: lethargy, high-pitched cry, hypotonia, opisthotonos."
    )

    // ------------------------------------------------------------------------
    // CARD 5: Immunization Schedule
    // ------------------------------------------------------------------------
    val epiWhoSchedule: List<ImmunizationAgeRow> = listOf(
        ImmunizationAgeRow("Birth", listOf("BCG", "OPV-0", "HepB-0")),
        ImmunizationAgeRow("6 weeks", listOf("OPV-1", "Penta-1", "PCV-1", "Rota-1")),
        ImmunizationAgeRow("10 weeks", listOf("OPV-2", "Penta-2", "PCV-2", "Rota-2")),
        ImmunizationAgeRow("14 weeks", listOf("OPV-3", "IPV-1", "Penta-3", "PCV-3")),
        ImmunizationAgeRow("9 months", listOf("MR-1", "TCV", "IPV-2")),
        ImmunizationAgeRow("15 months", listOf("MR-2"))
    )

    val ukAusNipSchedule: List<ImmunizationAgeRow> = listOf(
        ImmunizationAgeRow("Birth", listOf("HepB (Aus NIP)")),
        ImmunizationAgeRow("2 months", listOf("6-in-1 (DTaP-HepB-IPV-Hib)", "PCV", "Rota", "MenB")),
        ImmunizationAgeRow("4 months", listOf("6-in-1 (DTaP-HepB-IPV-Hib)", "PCV", "Rota", "MenB")),
        ImmunizationAgeRow("6 months", listOf("6-in-1 (DTaP-HepB-IPV-Hib)", "PCV (high-risk/Aus)")),
        ImmunizationAgeRow("12 months", listOf("MMR", "MenACWY", "PCV booster", "MenB booster")),
        ImmunizationAgeRow("18 months", listOf("MMRV", "Hib", "DTaP")),
        ImmunizationAgeRow("4 years", listOf("DTaP-IPV"))
    )
}
