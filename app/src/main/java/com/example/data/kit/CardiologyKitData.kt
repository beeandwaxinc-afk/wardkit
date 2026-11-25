package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: CARDIOLOGY (5 INTERACTIVE REFERENCE CARDS)
// ============================================================================

data class StemiTerritoryRow(
    val territory: String,
    val leads: String,
    val artery: String,
    val pearl: String = "",
    val isRedFlag: Boolean = false
)

data class AvBlockRow(
    val degree: String,
    val ecgPattern: String,
    val management: String,
    val isHighRisk: Boolean = false
)

data class EcgPatternRow(
    val title: String,
    val badge: String,
    val findings: String,
    val isRedFlag: Boolean = false
)

data class MurmurRow(
    val name: String,
    val timing: String,
    val locationRadiation: String,
    val keyFeatures: String
)

data class ManeuverRow(
    val maneuver: String,
    val physiology: String,
    val effect: String
)

data class ClassTierRow(
    val tier: String,
    val description: String,
    val badge: String = ""
)

data class VaughanWilliamsRow(
    val drugClass: String,
    val mechanism: String,
    val examples: String,
    val note: String,
    val isRedFlag: Boolean = false
)

data class DoacRenalRow(
    val drug: String,
    val standardDose: String,
    val reducedDoseRule: String,
    val avoidCutoff: String
)

data class AcuteArrhythmiaRow(
    val scenario: String,
    val badge: String,
    val action: String,
    val isUrgent: Boolean = false
)

object CardiologyKitData {

    // ------------------------------------------------------------------------
    // CARD 1: ECG Quick Sheet & STEMI Map
    // ------------------------------------------------------------------------
    val stemiTerritories: List<StemiTerritoryRow> = listOf(
        StemiTerritoryRow("Septal", "V1–V2", "LAD (septal perforators)"),
        StemiTerritoryRow("Anterior", "V3–V4", "LAD"),
        StemiTerritoryRow("Anterolateral", "V3–V6, I, aVL", "Proximal LAD / LCx"),
        StemiTerritoryRow(
            "High Lateral",
            "I, aVL",
            "LCx / 1st Diagonal",
            pearl = "Look for reciprocal ST↓ in II, III, aVF."
        ),
        StemiTerritoryRow(
            "Inferior",
            "II, III, aVF",
            "RCA (~80%) or LCx (~20%)",
            pearl = "RED FLAG: Always obtain right-sided V4R for RV infarction — AVOID NITRATES!",
            isRedFlag = true
        ),
        StemiTerritoryRow(
            "Posterior",
            "V7–V9",
            "LCx or RCA",
            pearl = "Suspect if ST depression + tall R wave in V1–V3."
        ),
        StemiTerritoryRow(
            "aVR Elevation",
            "aVR ≥1 mm + diffuse ST↓",
            "Left Main / Triple-Vessel",
            pearl = "High-risk proximal coronary occlusion — urgent cath lab.",
            isRedFlag = true
        )
    )

    val normalEcgIntervals: List<Pair<String, String>> = listOf(
        "Heart Rate" to "60–100 bpm",
        "PR Interval" to "120–200 ms (3–5 small boxes)",
        "QRS Duration" to "<120 ms (<3 small boxes)",
        "QTc Limit" to "<450 ms (M) · <460 ms (F)",
        "Normal Axis" to "-30° to +90° (I + & aVF +)"
    )

    const val qtcRedFlag = "QTc >500 ms carries high risk of Torsades de Pointes — stop offending drugs & replete K⁺/Mg²⁺."

    val avBlocks: List<AvBlockRow> = listOf(
        AvBlockRow(
            degree = "1st Degree AV Block",
            ecgPattern = "Fixed PR >200 ms; every P conducts a QRS.",
            management = "Usually benign — observe & review AV nodal blockers."
        ),
        AvBlockRow(
            degree = "2nd Degree Mobitz I (Wenckebach)",
            ecgPattern = "Progressive PR lengthening → dropped QRS.",
            management = "Usually AV nodal; monitor or Atropine if symptomatic."
        ),
        AvBlockRow(
            degree = "2nd Degree Mobitz II",
            ecgPattern = "Constant PR interval → sudden dropped QRS.",
            management = "Infranodal (HIGH RISK) → Transcutaneous pads + Pacemaker!",
            isHighRisk = true
        ),
        AvBlockRow(
            degree = "3rd Degree (Complete Heart Block)",
            ecgPattern = "Total AV dissociation (P & QRS march independently).",
            management = "URGENT PACING: Transcutaneous → Transvenous → Permanent.",
            isHighRisk = true
        )
    )

    // ------------------------------------------------------------------------
    // CARD 2: Electrolyte & Drug ECG Changes
    // ------------------------------------------------------------------------
    val electrolyteEcgRows: List<EcgPatternRow> = listOf(
        EcgPatternRow(
            title = "Hyperkalemia (Progressive)",
            badge = "K⁺ >5.5",
            findings = "5.5–6.5: Peaked narrow T → 6.5–7.5: Prolonged PR, flat/lost P → 7.5–8.5: Wide QRS → >8.5: Sine wave → VF/Asystole.",
            isRedFlag = true
        ),
        EcgPatternRow(
            title = "Hypokalemia",
            badge = "K⁺ <3.5",
            findings = "Flattened/inverted T waves, ST depression, prominent U waves, prolonged QU/QT (Torsades risk)."
        ),
        EcgPatternRow(
            title = "Hypercalcemia",
            badge = "SHORT QT",
            findings = "Shortened QT interval; Osborn (J) waves if severe."
        ),
        EcgPatternRow(
            title = "Hypocalcemia",
            badge = "LONG QT",
            findings = "Prolonged QT interval via lengthened ST segment (normal T wave)."
        ),
        EcgPatternRow(
            title = "Hypomagnesemia",
            badge = "TORSADES",
            findings = "Prolonged QT, premature ventricular complexes → Torsades de Pointes."
        )
    )

    val drugSyndromeEcgRows: List<EcgPatternRow> = listOf(
        EcgPatternRow(
            title = "Digoxin Effect vs. Toxicity",
            badge = "SCOOPED ST",
            findings = "Effect = scooped 'Salvador Dalí' ST sagging (not toxic). Toxicity = bidirectional VT, atrial tachycardia with block, frequent PVCs, AV block.",
            isRedFlag = true
        ),
        EcgPatternRow(
            title = "Acute Pericarditis",
            badge = "PR DEPRESSION",
            findings = "Diffuse concave ('smiley') ST elevation + PR depression (reciprocal PR↑ in aVR); no reciprocal ST depression."
        ),
        EcgPatternRow(
            title = "Pulmonary Embolism (PE)",
            badge = "SINUS TACH",
            findings = "Sinus tachycardia (most common), S1Q3T3 (specific, insensitive), new RBBB, T-wave inversions V1–V4."
        ),
        EcgPatternRow(
            title = "Brugada Pattern (Type 1)",
            badge = "V1–V2 COVED",
            findings = "Coved ST elevation ≥2 mm in V1–V2 with negative T wave → SCD risk; avoid Na⁺ blockers & treat fever aggressively.",
            isRedFlag = true
        ),
        EcgPatternRow(
            title = "Hypothermia",
            badge = "OSBORN J WAVE",
            findings = "Prominent Osborn (J) waves, bradycardia, prolonged PR/QRS/QT."
        )
    )

    // ------------------------------------------------------------------------
    // CARD 3: Murmurs & Dynamic Maneuvers
    // ------------------------------------------------------------------------
    val murmurs: List<MurmurRow> = listOf(
        MurmurRow("Aortic Stenosis (AS)", "Crescendo-decrescendo systolic", "Right 2nd ICS → Carotids", "Pulsus parvus et tardus, soft S2."),
        MurmurRow("Mitral Regurgitation (MR)", "Holosystolic blowing", "Apex → Axilla", "Displaced PMI, S3 gallop."),
        MurmurRow("Tricuspid Regurgitation (TR)", "Holosystolic", "Left lower sternal border", "Louder on inspiration (Carvallo sign), large V-waves in JVP."),
        MurmurRow("VSD", "Harsh holosystolic", "Left lower sternal border", "Palpable thrill (smaller VSD = louder murmur)."),
        MurmurRow("HOCM", "Harsh systolic ejection", "Left sternal border (no carotids)", "Louder with Valsalva / Standing."),
        MurmurRow("Aortic Regurgitation (AR)", "High-pitched early diastolic", "Left 3rd ICS (Erb's; lean forward)", "Wide pulse pressure, bounding pulses."),
        MurmurRow("Mitral Stenosis (MS)", "Low-pitched mid-diastolic rumble", "Apex (left lateral, bell)", "Opening Snap, loud S1, high AFib risk.")
    )

    val maneuvers: List<ManeuverRow> = listOf(
        ManeuverRow("Inspiration", "↑ Venous return to right heart", "↑ Right-sided murmurs (TR, TS, PR, PS); ↓ Left-sided murmurs."),
        ManeuverRow("Valsalva / Standing", "↓ Preload", "↓ Almost ALL murmurs — EXCEPT ↑ HOCM & earlier MVP click."),
        ManeuverRow("Squatting / Leg Raise", "↑ Preload", "↑ Most murmurs — EXCEPT ↓ HOCM & later MVP click."),
        ManeuverRow("Sustained Handgrip", "↑ Afterload", "↑ MR, AR, VSD (regurgitant/shunt) | ↓ AS & HOCM (outflow obstruction).")
    )

    // ------------------------------------------------------------------------
    // CARD 4: Cardiac Classifications (Killip, NYHA, LVEF/GDMT, Vaughan-Williams)
    // ------------------------------------------------------------------------
    val killipClasses: List<ClassTierRow> = listOf(
        ClassTierRow("Class I", "No rales / no S3", "~6% mort"),
        ClassTierRow("Class II", "Rales <50% lung fields + S3 / ↑JVP", "~17% mort"),
        ClassTierRow("Class III", "Frank acute pulmonary edema", "~38% mort"),
        ClassTierRow("Class IV", "Cardiogenic shock (SBP <90 + hypoperfusion)", "~81% mort")
    )

    val nyhaClasses: List<ClassTierRow> = listOf(
        ClassTierRow("NYHA I", "No physical activity limitation."),
        ClassTierRow("NYHA II", "Slight limitation on ordinary activity."),
        ClassTierRow("NYHA III", "Marked limitation on less-than-ordinary activity."),
        ClassTierRow("NYHA IV", "Symptoms even at rest.")
    )

    val hfPhenotypes: List<Pair<String, String>> = listOf(
        "HFrEF" to "LVEF ≤40%",
        "HFmrEF" to "LVEF 41–49%",
        "HFpEF" to "LVEF ≥50%"
    )

    val hfGdmtPillars: List<String> = listOf(
        "1. ARNI (Sacubitril/Valsartan) / ACEi / ARB.",
        "2. Evidence-based Beta-Blocker (Bisoprolol, Carvedilol, or Metoprolol Succinate).",
        "3. MRA (Spironolactone or Eplerenone).",
        "4. SGLT2 Inhibitor (Dapagliflozin or Empagliflozin)."
    )

    val vaughanWilliams: List<VaughanWilliamsRow> = listOf(
        VaughanWilliamsRow("Class IA", "Na⁺ blocker (↑APD)", "Procainamide, Quinidine", "Watch QTc prolongation & Torsades."),
        VaughanWilliamsRow("Class IB", "Na⁺ blocker (↓APD)", "Lidocaine, Mexiletine", "Preferred for ischemic ventricular tachycardia."),
        VaughanWilliamsRow("Class IC", "Na⁺ blocker (no ΔAPD)", "Flecainide, Propafenone", "RED FLAG: Avoid in structural heart disease or CAD!", isRedFlag = true),
        VaughanWilliamsRow("Class II", "Beta-Blockers", "Metoprolol, Esmolol, Bisoprolol", "AV node rate control & post-MI mortality benefit."),
        VaughanWilliamsRow("Class III", "K⁺ blocker (↑QT)", "Amiodarone, Sotalol, Dofetilide", "Rhythm control; check baseline & serial QTc."),
        VaughanWilliamsRow("Class IV", "Non-DHP CCBs", "Diltiazem, Verapamil", "AV node rate control; avoid in HFrEF & pre-excited AFib/WPW.", isRedFlag = true)
    )

    // ------------------------------------------------------------------------
    // CARD 5: Cardiac Drugs & Anticoagulation (DOACs, DAPT, Acute Arrhythmia)
    // ------------------------------------------------------------------------
    val doacRenalMatrix: List<DoacRenalRow> = listOf(
        DoacRenalRow(
            drug = "Apixaban",
            standardDose = "5 mg BD",
            reducedDoseRule = "2.5 mg BD if ≥2 of: Age ≥80, Wt ≤60 kg, SCr ≥1.5 mg/dL (133 µmol/L).",
            avoidCutoff = "Avoid / caution if CrCl <15 mL/min"
        ),
        DoacRenalRow(
            drug = "Rivaroxaban",
            standardDose = "20 mg OD (with dinner)",
            reducedDoseRule = "15 mg OD if CrCl 15–50 mL/min (must take with food).",
            avoidCutoff = "Avoid if CrCl <15 mL/min"
        ),
        DoacRenalRow(
            drug = "Dabigatran",
            standardDose = "150 mg BD",
            reducedDoseRule = "110 mg BD if Age ≥80 or high bleeding risk.",
            avoidCutoff = "AVOID if CrCl <30 mL/min"
        ),
        DoacRenalRow(
            drug = "Edoxaban",
            standardDose = "60 mg OD",
            reducedDoseRule = "30 mg OD if CrCl 15–50 mL/min or Wt ≤60 kg.",
            avoidCutoff = "Avoid if CrCl <15 or >95 mL/min (reduced efficacy)"
        )
    )

    const val warfarinOnlyStrip = "Mechanical Heart Valve or Moderate–Severe Mitral Stenosis: WARFARIN ONLY — DOACs Strictly Contraindicated."

    val daptRules: List<String> = listOf(
        "Post-ACS (STEMI / NSTEMI): Aspirin 75–100 mg + P2Y12 (Ticagrelor or Prasugrel preferred over Clopidogrel) typically 12 months (shorten to 3–6m if high bleeding risk).",
        "Stable CAD Elective DES: Typically 6 months DAPT → lifelong single antiplatelet (SAPT).",
        "GI Bleed Risk: Add PPI (Pantoprazole preferred — avoid Omeprazole with Clopidogrel)."
    )

    val acuteArrhythmiaRows: List<AcuteArrhythmiaRow> = listOf(
        AcuteArrhythmiaRow(
            scenario = "Unstable Tachycardia",
            badge = "SYNC CARDIOVERT",
            action = "Shock, chest pain, AMS, or pulm edema → Immediate Synchronized Cardioversion (AFib 120–200 J · Flutter/SVT 50–100 J · Monomorphic VT 100 J).",
            isUrgent = true
        ),
        AcuteArrhythmiaRow(
            scenario = "Stable Regular Narrow (SVT)",
            badge = "ADENOSINE",
            action = "Modified Valsalva → Adenosine 6 mg → 12 mg rapid IV push with flush."
        ),
        AcuteArrhythmiaRow(
            scenario = "Stable AFib Rate Control",
            badge = "CHECK WPW / EF",
            action = "IV Metoprolol or Diltiazem (Digoxin/Amiodarone if HFrEF). NEVER give AV nodal blockers in WPW pre-excited AFib — use Procainamide or cardiovert!",
            isUrgent = true
        ),
        AcuteArrhythmiaRow(
            scenario = "Symptomatic Bradycardia",
            badge = "ATROPINE / PACE",
            action = "Atropine 1 mg IV q3–5 min (max 3 mg) → Transcutaneous pacing OR Epinephrine / Dopamine infusion."
        )
    )
}
