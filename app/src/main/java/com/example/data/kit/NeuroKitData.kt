package com.example.data.kit

// ============================================================================
// TIER 2 SPECIALTY BLOCK: NEURO & NEUROSURGERY (5 INTERACTIVE CARDS)
// ============================================================================

data class CranialNerveRow(
    val number: Int,
    val cnLabel: String,
    val nameAndForamen: String,
    val bedsideTest: String,
    val lesionDeficit: String,
    val isRedFlag: Boolean = false
)

data class DermatomeRow(
    val level: String,
    val dermatome: String,
    val myotome: String,
    val reflex: String
)

data class NeuroSyndromeRow(
    val title: String,
    val territoryBadge: String,
    val presentation: String,
    val isEmergency: Boolean = false
)

data class StrokeGateItem(
    val id: String,
    val title: String,
    val detail: String
)

data class IcpTierItem(
    val tierBadge: String,
    val title: String,
    val bullets: List<String>,
    val isUrgent: Boolean = false
)

data class HerniationRow(
    val type: String,
    val siteBadge: String,
    val signs: String
)

object NeuroKitData {

    // ------------------------------------------------------------------------
    // CARD 1: Cranial Nerves I–XII Exam & Lesions
    // ------------------------------------------------------------------------
    val cranialNerves: List<CranialNerveRow> = listOf(
        CranialNerveRow(1, "CN I", "Olfactory · Cribriform Plate", "Non-irritant smell test", "Anosmia (anterior fossa fracture / meningioma)."),
        CranialNerveRow(2, "CN II", "Optic · Optic Canal", "Acuity, fields, fundoscopy, RAPD", "Monocular blindness, bitemporal hemianopia (chiasm), RAPD."),
        CranialNerveRow(
            3,
            "CN III",
            "Oculomotor · Sup. Orbital Fissure",
            "EOMs, eyelids & pupils",
            "'Down & Out' eye, ptosis, blown pupil — RED FLAG: PCom aneurysm / uncal herniation!",
            isRedFlag = true
        ),
        CranialNerveRow(4, "CN IV", "Trochlear · Sup. Orbital Fissure", "Look down & inward", "Vertical diplopia worse downstairs; compensatory contralateral head tilt."),
        CranialNerveRow(5, "CN V", "Trigeminal · SOF / Rotundum / Ovale", "V1–V3 sensation, corneal afferent, jaw clench", "Ipsilateral facial sensory loss; jaw deviates TOWARD lesion."),
        CranialNerveRow(6, "CN VI", "Abducens · Sup. Orbital Fissure", "Lateral gaze abduction", "Impaired abduction, horizontal diplopia (false localizing sign in raised ICP!)."),
        CranialNerveRow(7, "CN VII", "Facial · IAM → Stylomastoid", "Forehead wrinkle, eye closure, smile", "UMN spares forehead; LMN (Bell's) paralyzes entire ipsilateral face."),
        CranialNerveRow(8, "CN VIII", "Vestibulocochlear · IAM", "Whisper, Weber/Rinne, HIT", "Sensorineural hearing loss, vertigo, nystagmus (CPA tumor)."),
        CranialNerveRow(9, "CN IX", "Glossopharyngeal · Jugular Foramen", "Gag afferent, posterior 1/3 taste", "Absent gag reflex, dysphagia."),
        CranialNerveRow(10, "CN X", "Vagus · Jugular Foramen", "Say 'Ahh' (palate rise), cough/voice", "Uvula deviates AWAY from lesion, hoarseness."),
        CranialNerveRow(11, "CN XI", "Accessory · Jugular Foramen", "Shoulder shrug (trapezius), head turn (SCM)", "Weak ipsilateral shrug & contralateral head turn."),
        CranialNerveRow(12, "CN XII", "Hypoglossal · Hypoglossal Canal", "Stick out tongue", "Tongue deviates TOWARD LMN lesion ('licks the lesion') + fasciculations.")
    )

    // ------------------------------------------------------------------------
    // CARD 2: Interactive Dermatomes, Myotomes & Reflexes
    // ------------------------------------------------------------------------
    const val dermatomeLandmarks =
        "Key Landmarks: C4 Clavicle · T4 Nipple · T10 Umbilicus · L1 Inguinal · S2–S4 Perianal Saddle."

    val upperLimbDermatomes: List<DermatomeRow> = listOf(
        DermatomeRow("C5", "Lateral upper arm (regimental badge)", "Deltoid (shoulder abduction) & Elbow flexion", "Biceps (C5–C6)"),
        DermatomeRow("C6", "Lateral forearm & Thumb", "Wrist extension & Elbow flexion", "Brachioradialis (C5–C6)"),
        DermatomeRow("C7", "Middle finger", "Elbow extension (Triceps) & Wrist flexion", "Triceps (C7)"),
        DermatomeRow("C8", "Medial forearm & Little finger", "Finger flexion (FDP)", "—"),
        DermatomeRow("T1", "Medial elbow / arm", "Finger abduction (Interossei)", "—")
    )

    val lowerLimbDermatomes: List<DermatomeRow> = listOf(
        DermatomeRow("L2", "Anterior upper thigh", "Hip flexion (Iliopsoas)", "—"),
        DermatomeRow("L3", "Anterior lower thigh & Medial knee", "Knee extension (Quadriceps)", "Patellar (L3–L4)"),
        DermatomeRow("L4", "Medial shin & Medial malleolus", "Ankle dorsiflexion (Tibialis ant.)", "Patellar (L3–L4)"),
        DermatomeRow("L5", "Lateral shin, Dorsum of foot & Great toe", "Great toe extension (EHL) & Hip abduction", "—"),
        DermatomeRow("S1", "Lateral foot, Sole & Little toe", "Ankle plantarflexion (Gastrocnemius)", "Achilles (S1)"),
        DermatomeRow("S2–S4", "Perianal / Saddle region", "Anal sphincter tone & bladder control", "Bulbocavernosus / Anal wink")
    )

    // ------------------------------------------------------------------------
    // CARD 3: Brainstem & Spinal Cord Syndromes
    // ------------------------------------------------------------------------
    const val ruleOfFoursPearl =
        "Rule of 4s: 4 Midline 'M's (Motor pathway, Medial lemniscus, MLF, Motor CNs 3/4/6/12) · 4 Side 'S's (Spinocerebellar, Spinothalamic, Sensory V, Sympathetic/Horner's)."

    val brainstemSyndromes: List<NeuroSyndromeRow> = listOf(
        NeuroSyndromeRow(
            title = "Weber Syndrome",
            territoryBadge = "VENTRAL MIDBRAIN · PCA",
            presentation = "Ipsilateral CN III palsy + Contralateral hemiparesis."
        ),
        NeuroSyndromeRow(
            title = "Millard-Gubler Syndrome",
            territoryBadge = "VENTRAL PONS · BASILAR",
            presentation = "Ipsilateral CN VI & VII (LMN) palsy + Contralateral hemiparesis."
        ),
        NeuroSyndromeRow(
            title = "Wallenberg (Lateral Medullary)",
            territoryBadge = "PICA / VERTEBRAL",
            presentation = "Ipsilateral Horner's, facial pain/temp loss, ataxia, dysphagia/hoarseness (CN IX/X) + Contralateral body pain/temp loss (NO limb weakness!)."
        ),
        NeuroSyndromeRow(
            title = "Medial Medullary Syndrome",
            territoryBadge = "ASA",
            presentation = "Ipsilateral CN XII (tongue toward lesion) + Contralateral hemiparesis & vibration/proprioception loss."
        )
    )

    val spinalCordSyndromes: List<NeuroSyndromeRow> = listOf(
        NeuroSyndromeRow(
            title = "Brown-Séquard (Hemisection)",
            territoryBadge = "HEMICORD",
            presentation = "Ipsilateral UMN weakness + vibration/proprioception loss below level; Contralateral pain/temp loss starting 1–2 levels below."
        ),
        NeuroSyndromeRow(
            title = "Anterior Cord Syndrome",
            territoryBadge = "ASA OCCLUSION",
            presentation = "Bilateral motor + pain/temp loss below lesion; Dorsal columns (vibration/proprioception) SPARED."
        ),
        NeuroSyndromeRow(
            title = "Central Cord Syndrome",
            territoryBadge = "ELDERLY HYPEREXTENSION",
            presentation = "Upper limb weakness > Lower limb weakness ('cape-like' sensory loss)."
        )
    )

    val caudaVsConusBullets: List<String> = listOf(
        "Cauda Equina (L2–S5 roots): Severe asymmetric radicular pain, saddle anesthesia, LMN areflexic weakness, acute urinary retention/overflow → URGENT MRI + Surgical Decompression (<24–48h)!",
        "Conus Medullaris (T12–L2): Symmetric milder pain, early severe bowel/bladder dysfunction, mixed UMN + LMN signs."
    )

    // ------------------------------------------------------------------------
    // CARD 4: Acute Stroke Thrombolysis Checklist
    // ------------------------------------------------------------------------
    val strokeWindowSummary =
        "Standard IV window: ≤4.5 hrs from Last Known Well (LKW) · Extended up to 9h / wake-up only with perfusion/MRI mismatch · Mechanical Thrombectomy up to 24h for LVO."

    val thrombolyticDoses: List<Pair<String, String>> = listOf(
        "Tenecteplase (TNK)" to "0.25 mg/kg (max 25 mg) single IV bolus over 5 sec",
        "Alteplase (tPA)" to "0.9 mg/kg (max 90 mg): 10% IV bolus (1m) + 90% over 60m"
    )

    val strokeMandatoryGates: List<StrokeGateItem> = listOf(
        StrokeGateItem("ct", "Non-Contrast Head CT Cleared", "Strictly excludes acute intracranial hemorrhage (ICH / SAH)."),
        StrokeGateItem("glucose", "Capillary Glucose Checked", "If <50 mg/dL, treat hypoglycemia FIRST (common stroke mimic!)."),
        StrokeGateItem("bp", "Blood Pressure <185/110 mmHg", "Use IV Labetalol 10–20 mg or Nicardipine drip; keep <180/105 for 24h post-drug.")
    )

    val strokeContraindications: List<String> = listOf(
        "Any acute intracranial hemorrhage on CT or history of prior ICH.",
        "Ischemic stroke, severe head trauma, or intracranial/intraspinal surgery within 3 months.",
        "Active internal bleeding or GI/GU malignancy bleed within 21 days.",
        "Suspected aortic arch dissection or infective endocarditis.",
        "Coagulopathy: Platelets <100,000 · INR >1.7 · elevated aPTT · therapeutic LMWH <24h · DOAC taken <48h (unless specific reversal/normal assay)."
    )

    // ------------------------------------------------------------------------
    // CARD 5: Raised ICP Escalation Ladder & Herniation
    // ------------------------------------------------------------------------
    val icpTargets: List<Pair<String, String>> = listOf(
        "Normal ICP" to "5–15 mmHg",
        "Treat Threshold" to ">20–22 mmHg (>5m)",
        "Target CPP" to "60–70 mmHg (MAP − ICP)"
    )

    val icpLadder: List<IcpTierItem> = listOf(
        IcpTierItem(
            tierBadge = "TIER 0",
            title = "Baseline Neuroprotection (All Patients)",
            bullets = listOf(
                "HOB 30° & neutral midline neck (loosen tight ETT ties/collars).",
                "Avoid hypoxia (SpO₂ ≥94%), keep PaCO₂ 35–40 mmHg, normothermia (<37.5°C).",
                "Keep Na⁺ 140–145 mEq/L, analgesia + sedation, 7d Levetiracetam in severe TBI."
            )
        ),
        IcpTierItem(
            tierBadge = "TIER 1",
            title = "First-Line Active ICP Reduction",
            bullets = listOf(
                "Drain CSF via External Ventricular Drain (EVD) if present.",
                "3% NaCl (250 mL bolus or 2–5 mL/kg; keep Na⁺ <155–160) OR 20% Mannitol (0.25–1.0 g/kg IV over 15–20m; avoid if hypotensive or Osm >320).",
                "Brief mild hyperventilation (PaCO₂ 32–35) ONLY as temporary bridge."
            )
        ),
        IcpTierItem(
            tierBadge = "TIER 2",
            title = "Refractory ICP Escalation",
            bullets = listOf(
                "Deepen sedation (Propofol infusion) & brief neuromuscular blockade trial.",
                "Raise MAP (+10 mmHg challenge) to test pressure autoregulation."
            )
        ),
        IcpTierItem(
            tierBadge = "TIER 3",
            title = "Salvage Neurosurgery",
            bullets = listOf(
                "Decompressive Craniectomy.",
                "Pentobarbital coma (EEG burst suppression) or targeted hypothermia (35–36°C)."
            ),
            isUrgent = true
        )
    )

    val herniationSyndromes: List<HerniationRow> = listOf(
        HerniationRow(
            type = "Uncal (Transtentorial Lateral)",
            siteBadge = "BLOWN PUPIL",
            signs = "Ipsilateral fixed dilated pupil (CN III) + Contralateral hemiparesis (or ipsilateral via Kernohan's notch) + rapid GCS drop."
        ),
        HerniationRow(
            type = "Tonsillar (Foramen Magnum)",
            siteBadge = "CUSHING'S TRIAD",
            signs = "Hypertension + Bradycardia + Irregular Respirations → rapid respiratory arrest & pinpoint/fixed pupils."
        ),
        HerniationRow(
            type = "Central (Diencephalic → Brainstem)",
            siteBadge = "POSTURING",
            signs = "Progressive stupor/coma, Cheyne-Stokes → bilateral small then mid-position fixed pupils, decorticate → decerebrate posturing."
        ),
        HerniationRow(
            type = "Subfalcine (Cingulate)",
            siteBadge = "ACA COMPRESSION",
            signs = "Contralateral leg weakness and abulia."
        )
    )

    const val herniationRescueBridge =
        "Elevate head 30° + 100% O₂ + Brief hyperventilation (PaCO₂ 30–35) + STAT Hyperosmolar Bolus (23.4% NaCl 30 mL central OR 3% NaCl 250 mL OR Mannitol 1 g/kg) + Call Neurosurgery STAT."
}
