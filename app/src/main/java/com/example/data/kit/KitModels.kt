package com.example.data.kit

import com.example.ui.theme.DepartmentPalette
import com.example.ui.theme.DeptAmber
import com.example.ui.theme.DeptCobalt
import com.example.ui.theme.DeptCoralRed
import com.example.ui.theme.DeptCrimson
import com.example.ui.theme.DeptEmerald
import com.example.ui.theme.DeptIndigo
import com.example.ui.theme.DeptMagenta
import com.example.ui.theme.DeptOchre
import com.example.ui.theme.DeptTeal
import com.example.ui.theme.DeptVermilion

enum class KitTier {
    HOSPITAL_ESSENTIALS,
    SPECIALTIES
}

enum class KitBlockId {
    ER_CRASH,
    DOSE_ENGINE,
    LABS_FLUIDS,
    IV_BLOOD,
    CARDIOLOGY,
    EMERGENCY_ANESTHESIA,
    GASTRO_HEPATOLOGY,
    GENERAL_SURGERY,
    ICU_CRITICAL_CARE,
    INTERNAL_MEDICINE,
    NEPHROLOGY,
    NEURO_NEUROSURGERY,
    OBS_GYNAE,
    PEDIATRICS
}

enum class LabUnitSystem(val key: String, val label: String) {
    SI("SI", "SI"),
    CONV("CONV", "Conv");

    companion object {
        fun fromKey(key: String?): LabUnitSystem =
            entries.firstOrNull { it.key == key } ?: CONV
    }
}

enum class SearchFilter(val id: String) {
    ALL("all"),
    CALC("calc"),
    KIT("kit")
}

object KitCardIds {
    // Tier 1 · Block A: ER & Crash Protocols (5 Cards)
    const val ER_ACLS_PALS = "kit_er_acls_pals"
    const val ER_ANAPHYLAXIS_SEIZURE = "kit_er_anaphylaxis_seizure"
    const val ER_DKA_HYPERKALEMIA = "kit_er_dka_hyperkalemia"
    const val ER_SEPSIS_BUNDLES = "kit_er_sepsis_bundles"
    const val ER_TOXICOLOGY = "kit_er_toxicology"

    // Tier 1 · Block B: Quick Drug & Infusion Engine (4 Sub-Tabs)
    const val DOSE_ER_RSI = "kit_dose_er_rsi"
    const val DOSE_WARD_ICU = "kit_dose_ward_icu"
    const val DOSE_LOCAL_ANESTH = "kit_dose_local_anesth"
    const val DOSE_PEADS_CORE = "kit_dose_peads_core"

    // Tier 1 · Block C: Normal Labs & Body Fluids (4 Cards — Phase 3)
    const val LABS_ADULT_CORE = "kit_labs_adult_core"
    const val LABS_PEDIATRIC = "kit_labs_pediatric"
    const val LABS_ABG_VBG = "kit_labs_abg_vbg"
    const val LABS_BODY_FLUIDS = "kit_labs_body_fluids"

    // Tier 1 · Block D: IV Fluids, Lines & Blood (4 Cards — Phase 3)
    const val IV_FLUIDS_TABLE = "kit_iv_fluids_table"
    const val IV_CANNULAS = "kit_iv_cannulas"
    const val IV_TUBES_CATHETERS = "kit_iv_tubes_catheters"
    const val IV_BLOOD_PRODUCTS = "kit_iv_blood_products"

    // Tier 2 · Specialty: Cardiology (5 Cards — Phase 6)
    const val CARDIO_ECG_STEMI = "kit_cardio_ecg_stemi"
    const val CARDIO_LYTES_DRUGS_ECG = "kit_cardio_lytes_drugs_ecg"
    const val CARDIO_MURMURS = "kit_cardio_murmurs"
    const val CARDIO_CLASSIFICATIONS = "kit_cardio_classifications"
    const val CARDIO_DRUGS_ANTICOAG = "kit_cardio_drugs_anticoag"

    // Tier 2 · Specialty: Emergency & Anesthesia (5 Cards — Phase 6)
    const val EMERG_AIRWAY_RSI = "kit_emerg_airway_rsi"
    const val EMERG_OXYGEN_NIV = "kit_emerg_oxygen_niv"
    const val EMERG_SEDATION_PAIN = "kit_emerg_sedation_pain"
    const val EMERG_TRAUMA_IMAGING = "kit_emerg_trauma_imaging"
    const val EMERG_PREOP_FASTING = "kit_emerg_preop_fasting"

    // Tier 2 · Specialty: Gastro & Hepatology (5 Cards — Phase 7)
    const val GASTRO_HEPB_DECODER = "kit_gastro_hepb_decoder"
    const val GASTRO_GI_BLEED = "kit_gastro_gi_bleed"
    const val GASTRO_LFT_JAUNDICE = "kit_gastro_lft_jaundice"
    const val GASTRO_CIRRHOSIS = "kit_gastro_cirrhosis"
    const val GASTRO_STOOL_IBD = "kit_gastro_stool_ibd"

    // Tier 2 · Specialty: General Surgery (5 Cards — Phase 5)
    const val SURG_ATLS_SHOCK = "kit_surg_atls_shock"
    const val SURG_BURNS_TBSA = "kit_surg_burns_tbsa"
    const val SURG_SUTURES = "kit_surg_sutures"
    const val SURG_WOUNDS_TETANUS = "kit_surg_wounds_tetanus"
    const val SURG_POSTOP_DRAINS = "kit_surg_postop_drains"

    // Tier 2 · Specialty: ICU & Critical Care (5 Cards — Phase 5)
    const val ICU_VENT_PRESETS = "kit_icu_vent_presets"
    const val ICU_PEAK_PLATEAU = "kit_icu_peak_plateau"
    const val ICU_WEANING_SBT = "kit_icu_weaning_sbt"
    const val ICU_SEDATION_DELIRIUM = "kit_icu_sedation_delirium"
    const val ICU_PROPHYLAXIS_LYTES = "kit_icu_prophylaxis_lytes"

    // Tier 2 · Specialty: Internal Medicine (5 Cards — Phase 5)
    const val MED_EMPIRIC_ABX = "kit_med_empiric_abx"
    const val MED_INSULIN_PK = "kit_med_insulin_pk"
    const val MED_HTN_EMERGENCY = "kit_med_htn_emergency"
    const val MED_ELECTROLYTE_MAPS = "kit_med_electrolyte_maps"
    const val MED_VTE_FUO = "kit_med_vte_fuo"

    // Tier 2 · Specialty: Nephrology (5 Cards — Phase 7)
    const val NEPHRO_AKI = "kit_nephro_aki"
    const val NEPHRO_CKD_HEATMAP = "kit_nephro_ckd_heatmap"
    const val NEPHRO_URINALYSIS = "kit_nephro_urinalysis"
    const val NEPHRO_DIALYSIS_SADMANS = "kit_nephro_dialysis_sadmans"
    const val NEPHRO_RENAL_DRUGS = "kit_nephro_renal_drugs"

    // Tier 2 · Specialty: Neuro & Neurosurgery (5 Cards — Phase 6)
    const val NEURO_CRANIAL_NERVES = "kit_neuro_cranial_nerves"
    const val NEURO_DERMATOMES = "kit_neuro_dermatomes"
    const val NEURO_SYNDROMES = "kit_neuro_syndromes"
    const val NEURO_STROKE_TPA = "kit_neuro_stroke_tpa"
    const val NEURO_RAISED_ICP = "kit_neuro_raised_icp"

    // Tier 2 · Specialty: Obs & Gynae (5 Cards — Phase 7)
    const val OBS_ANTENATAL = "kit_obs_antenatal"
    const val OBS_MGSO4 = "kit_obs_mgso4"
    const val OBS_PPH = "kit_obs_pph"
    const val OBS_CTG = "kit_obs_ctg"
    const val OBS_SAFE_DRUGS = "kit_obs_safe_drugs"

    // Tier 2 · Specialty: Pediatrics Quick-Ref (5 Cards — Phase 3)
    const val PEDS_GROWTH_VITALS = "kit_peds_growth_vitals"
    const val PEDS_MILESTONES = "kit_peds_milestones"
    const val PEDS_WHO_DEHYDRATION = "kit_peds_who_dehydration"
    const val PEDS_NEONATAL = "kit_peds_neonatal"
    const val PEDS_IMMUNIZATION = "kit_peds_immunization"
}

data class KitCardDef(
    val id: String,
    val blockId: KitBlockId,
    val title: String,
    val tabLabel: String,
    val shortSubtitle: String,
    val keywords: List<String>
)

data class KitBlockDef(
    val id: KitBlockId,
    val tier: KitTier,
    val title: String,
    val subtitle: String,
    val badgeText: String,
    val palette: DepartmentPalette,
    val cards: List<KitCardDef>
)

sealed interface KitSearchHit {
    val key: String
    val title: String
    val subtitle: String
    val blockId: KitBlockId

    data class CardHit(val card: KitCardDef) : KitSearchHit {
        override val key: String get() = "kit_card_${card.id}"
        override val title: String get() = card.title
        override val subtitle: String get() = "${KitRegistry.getBlock(card.blockId).title} · ${card.shortSubtitle}"
        override val blockId: KitBlockId get() = card.blockId
    }

    data class DrugHit(val entry: DoseDrugSearchEntry) : KitSearchHit {
        override val key: String get() = "kit_drug_${entry.drugId}"
        override val title: String get() = entry.title
        override val subtitle: String get() = "Dose Engine · ${entry.subtitle}"
        override val blockId: KitBlockId get() = KitBlockId.DOSE_ENGINE
    }
}

object KitRegistry {
    val blocks: List<KitBlockDef> = listOf(
        // ====================================================================
        // TIER 1: HOSPITAL ESSENTIALS (SYMMETRICAL 2x2 EQUAL BENTO GRID)
        // ====================================================================
        KitBlockDef(
            id = KitBlockId.ER_CRASH,
            tier = KitTier.HOSPITAL_ESSENTIALS,
            title = "ER & Crash Protocols",
            subtitle = "ACLS, DKA, Antidotes",
            badgeText = "5 CARDS",
            palette = DeptCrimson,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.ER_ACLS_PALS,
                    blockId = KitBlockId.ER_CRASH,
                    title = "ACLS & PALS Arrest Algorithms",
                    tabLabel = "ACLS / PALS",
                    shortSubtitle = "CPR, VF/pVT, Asystole/PEA & 5H/5T",
                    keywords = listOf(
                        "acls", "pals", "cardiac arrest", "code blue", "cpr", "vf", "vt", "pvt",
                        "asystole", "pea", "defib", "defibrillation", "5h 5t", "5h", "5t",
                        "amiodarone", "adrenaline", "crash"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ER_ANAPHYLAXIS_SEIZURE,
                    blockId = KitBlockId.ER_CRASH,
                    title = "Anaphylaxis & Status Epilepticus",
                    tabLabel = "Anaphylaxis / Seizure",
                    shortSubtitle = "IM Adrenaline & 0–40m seizure ladder",
                    keywords = listOf(
                        "anaphylaxis", "allergy", "adrenaline", "epinephrine", "im adrenaline",
                        "status epilepticus", "seizure", "lorazepam", "midazolam", "diazepam",
                        "levetiracetam", "valproate", "fosphenytoin", "convulsion"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ER_DKA_HYPERKALEMIA,
                    blockId = KitBlockId.ER_CRASH,
                    title = "DKA / HHS & Hyperkalemia Cocktails",
                    tabLabel = "DKA / HyperK",
                    shortSubtitle = "K+ gate, insulin drip & Ca gluconate",
                    keywords = listOf(
                        "dka", "hhs", "diabetic ketoacidosis", "hyperkalemia", "hyperkalaemia",
                        "potassium", "calcium gluconate", "insulin", "salbutamol", "peaked t",
                        "cocktail", "anion gap"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ER_SEPSIS_BUNDLES,
                    blockId = KitBlockId.ER_CRASH,
                    title = "Sepsis 1-Hour & Emergency Bundles",
                    tabLabel = "Sepsis / Bundles",
                    shortSubtitle = "Sepsis 1-hr, SCAPE & Massive Bleed MTP",
                    keywords = listOf(
                        "sepsis", "sepsis 1-hour", "septic shock", "bundle", "lactate",
                        "scape", "pulmonary edema", "acute pulmonary edema", "niv", "cpap", "bipap",
                        "massive bleed", "mtp", "massive hemorrhage", "txa", "1:1:1"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ER_TOXICOLOGY,
                    blockId = KitBlockId.ER_CRASH,
                    title = "Toxicology Antidotes Table",
                    tabLabel = "Antidotes",
                    shortSubtitle = "14 core toxins, antidotes & pearls",
                    keywords = listOf(
                        "toxicology", "antidote", "antidotes", "poisoning", "overdose",
                        "nac", "n-acetylcysteine", "naloxone", "flumazenil", "atropine",
                        "pralidoxime", "protamine", "pcc", "glucagon", "methylene blue",
                        "hydroxocobalamin", "fomepizole", "deferoxamine", "organophosphate"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.DOSE_ENGINE,
            tier = KitTier.HOSPITAL_ESSENTIALS,
            title = "Quick Drug & Infusion Engine",
            subtitle = "ER/RSI, ICU, LA & Peads",
            badgeText = "4 TABS",
            palette = DeptIndigo,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.DOSE_ER_RSI,
                    blockId = KitBlockId.DOSE_ENGINE,
                    title = "Adult ER & RSI Dosing",
                    tabLabel = "ER & RSI",
                    shortSubtitle = "10 RSI, arrest & acute ER weight-capped drugs",
                    keywords = listOf(
                        "er", "rsi", "intubation", "induction", "paralytic", "drug engine",
                        "dose engine", "etomidate", "ketamine", "propofol", "rocuronium",
                        "succinylcholine", "suxamethonium", "adenosine", "amiodarone", "txa", "heparin"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.DOSE_WARD_ICU,
                    blockId = KitBlockId.DOSE_ENGINE,
                    title = "Adult Ward & ICU Infusions",
                    tabLabel = "Ward & ICU",
                    shortSubtitle = "Pressor mL/hr, insulin, MgSO₄ & steroids",
                    keywords = listOf(
                        "icu", "ward", "infusion", "drip", "vasopressor", "pump rate",
                        "norepinephrine", "noradrenaline", "epinephrine", "dopamine", "dobutamine",
                        "nitroglycerin", "gtn", "insulin drip", "magnesium", "kcl", "steroid equivalence"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.DOSE_LOCAL_ANESTH,
                    blockId = KitBlockId.DOSE_ENGINE,
                    title = "Local Anesthetics & Intralipid LAST",
                    tabLabel = "Local Anesth",
                    shortSubtitle = "Max safe mg/mL + live 20% Intralipid rescue",
                    keywords = listOf(
                        "local anesthetic", "lidocaine", "lignocaine", "bupivacaine", "ropivacaine",
                        "intralipid", "last", "lipid rescue", "ibw"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.DOSE_PEADS_CORE,
                    blockId = KitBlockId.DOSE_ENGINE,
                    title = "Pediatric Ward / OPD Core Drugs",
                    tabLabel = "Peads Core",
                    shortSubtitle = "12 A–Z weight-capped pediatric syrups & IVs",
                    keywords = listOf(
                        "peads core", "pediatric drugs", "pediatric dosing", "syrup",
                        "amoxicillin", "augmentin", "co-amoxiclav", "salbutamol", "azithromycin",
                        "cefixime", "cefotaxime", "ceftriaxone", "gentamicin", "ibuprofen",
                        "ondansetron", "paracetamol", "prednisolone"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.LABS_FLUIDS,
            tier = KitTier.HOSPITAL_ESSENTIALS,
            title = "Normal Labs & Body Fluids",
            subtitle = "SI/Conv labs, ABG & taps",
            badgeText = "4 CARDS",
            palette = DeptCobalt,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.LABS_ADULT_CORE,
                    blockId = KitBlockId.LABS_FLUIDS,
                    title = "Core Adult Lab Panels",
                    tabLabel = "Core Labs",
                    shortSubtitle = "CBC, coags, RFT, LFT, cardiac, thyroid",
                    keywords = listOf(
                        "labs", "normal labs", "cbc", "coags", "rft", "lft", "electrolytes",
                        "lyte", "hb", "wbc", "platelets", "inr", "aptt", "fibrinogen", "ddimer",
                        "creatinine", "bun", "urea", "bilirubin", "ast", "alt", "alp", "ggt",
                        "troponin", "bnp", "hba1c", "cholesterol", "lactate", "lipase", "tsh",
                        "t4", "ferritin", "iron", "tibc", "si", "conventional"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.LABS_PEDIATRIC,
                    blockId = KitBlockId.LABS_FLUIDS,
                    title = "Pediatric Age-Adjusted Labs",
                    tabLabel = "Peads Labs",
                    shortSubtitle = "Age-specific Hb, WBC shift, Cr & ALP",
                    keywords = listOf(
                        "pediatric labs", "peads labs", "child labs", "newborn hb", "alp",
                        "alkaline phosphatase", "lymphocyte", "neutrophil", "creatinine",
                        "wbc", "platelets"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.LABS_ABG_VBG,
                    blockId = KitBlockId.LABS_FLUIDS,
                    title = "ABG / VBG Rapid Matrix",
                    tabLabel = "ABG / VBG",
                    shortSubtitle = "Normal ABG, compensation & VBG offsets",
                    keywords = listOf(
                        "abg", "vbg", "acid base", "acidosis", "alkalosis", "winters",
                        "winter's", "compensation", "paco2", "hco3", "ph", "base excess",
                        "respiratory", "metabolic"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.LABS_BODY_FLUIDS,
                    blockId = KitBlockId.LABS_FLUIDS,
                    title = "Body Fluid Interpreters",
                    tabLabel = "Body Fluids",
                    shortSubtitle = "CSF, Light's, SAAG/SBP & Synovial",
                    keywords = listOf(
                        "body fluids", "csf", "lumbar puncture", "meningitis", "bacterial",
                        "viral", "tb", "fungal", "pleural", "light's", "lights", "exudate",
                        "transudate", "empyema", "ascitic", "ascites", "saag", "sbp",
                        "paracentesis", "synovial", "joint", "septic arthritis", "gout",
                        "pseudogout", "crystals"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.IV_BLOOD,
            tier = KitTier.HOSPITAL_ESSENTIALS,
            title = "IV Fluids, Lines & Blood",
            subtitle = "Fluids, cannulas & blood",
            badgeText = "4 CARDS",
            palette = DeptTeal,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.IV_FLUIDS_TABLE,
                    blockId = KitBlockId.IV_BLOOD,
                    title = "IV Fluid Composition Table",
                    tabLabel = "IV Fluids",
                    shortSubtitle = "NS, RL/Hartmann's, D5W, 3% NaCl, Albumin",
                    keywords = listOf(
                        "iv fluids", "fluids", "normal saline", "0.9% ns", "ns", "ringer",
                        "ringer's", "rl", "hartmann", "hartmann's", "d5w", "0.45% ns",
                        "3% nacl", "hypertonic", "albumin", "osmolality", "crystalloid",
                        "colloid"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.IV_CANNULAS,
                    blockId = KitBlockId.IV_BLOOD,
                    title = "IV Cannula Sizing Chart",
                    tabLabel = "IV Cannulas",
                    shortSubtitle = "14G–24G color codes, flow rates & uses",
                    keywords = listOf(
                        "cannula", "iv cannula", "branula", "gauge", "14g", "16g", "18g",
                        "20g", "22g", "24g", "orange", "grey", "green", "pink", "blue",
                        "yellow", "flow rate", "iv access"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.IV_TUBES_CATHETERS,
                    blockId = KitBlockId.IV_BLOOD,
                    title = "Tubes & Catheters by Age",
                    tabLabel = "Tubes & Lines",
                    shortSubtitle = "Foley, NG tube, chest drain & LMA sizing",
                    keywords = listOf(
                        "tubes", "catheters", "foley", "urinary catheter", "ng tube",
                        "nasogastric", "chest drain", "chest tube", "thoracostomy", "lma",
                        "french", "fr", "sizing"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.IV_BLOOD_PRODUCTS,
                    blockId = KitBlockId.IV_BLOOD,
                    title = "Blood Products & Transfusion Guide",
                    tabLabel = "Blood Products",
                    shortSubtitle = "PRBC, FFP, Platelets, Cryo & reaction steps",
                    keywords = listOf(
                        "blood", "blood products", "transfusion", "prbc", "packed red cells",
                        "ffp", "fresh frozen plasma", "platelets", "cryoprecipitate", "cryo",
                        "fibrinogen", "reaction", "transfusion reaction"
                    )
                )
            )
        ),

        // ====================================================================
        // TIER 2: SPECIALTIES (STRICTLY A–Z EQUAL 2-COLUMN BENTO GRID)
        // ====================================================================
        KitBlockDef(
            id = KitBlockId.CARDIOLOGY,
            tier = KitTier.SPECIALTIES,
            title = "Cardiology",
            subtitle = "ECG, STEMI, murmurs & DOACs",
            badgeText = "5 CARDS",
            palette = DeptCrimson,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.CARDIO_ECG_STEMI,
                    blockId = KitBlockId.CARDIOLOGY,
                    title = "ECG Quick Sheet & STEMI Map",
                    tabLabel = "ECG & STEMI",
                    shortSubtitle = "Coronary territories, normal intervals & AV blocks",
                    keywords = listOf(
                        "ecg", "ekg", "stemi", "av block", "mobitz", "wenckebach",
                        "complete heart block", "v4r", "avr", "qtc", "axis", "cardiology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.CARDIO_LYTES_DRUGS_ECG,
                    blockId = KitBlockId.CARDIOLOGY,
                    title = "Electrolyte & Drug ECG Changes",
                    tabLabel = "Lyte / Drug ECG",
                    shortSubtitle = "Hyperkalemia ECG, Digoxin, Pericarditis, PE & Brugada",
                    keywords = listOf(
                        "hyperkalemia ecg", "hypokalemia ecg", "u wave", "digoxin", "pericarditis",
                        "brugada", "osborn", "s1q3t3", "torsades", "ecg", "cardiology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.CARDIO_MURMURS,
                    blockId = KitBlockId.CARDIOLOGY,
                    title = "Murmurs & Dynamic Maneuvers",
                    tabLabel = "Murmurs",
                    shortSubtitle = "AS, MR, TR, VSD, HOCM, AR, MS & Valsalva/Handgrip",
                    keywords = listOf(
                        "murmurs", "murmur", "valsalva", "handgrip", "squatting", "hocm",
                        "aortic stenosis", "mitral regurgitation", "carvallo", "opening snap", "cardiology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.CARDIO_CLASSIFICATIONS,
                    blockId = KitBlockId.CARDIOLOGY,
                    title = "Cardiac Classifications & Antiarrhythmics",
                    tabLabel = "Classifications",
                    shortSubtitle = "Killip, NYHA, HF GDMT pillars & Vaughan-Williams",
                    keywords = listOf(
                        "killip", "nyha", "hfref", "gdmt", "vaughan williams", "antiarrhythmics",
                        "flecainide", "amiodarone", "diltiazem", "heart failure", "cardiology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.CARDIO_DRUGS_ANTICOAG,
                    blockId = KitBlockId.CARDIOLOGY,
                    title = "Cardiac Drugs & Anticoagulation",
                    tabLabel = "DOAC & DAPT",
                    shortSubtitle = "DOAC renal cutoffs, DAPT rules & acute arrhythmia Rx",
                    keywords = listOf(
                        "doac", "apixaban", "rivaroxaban", "dabigatran", "edoxaban", "dapt",
                        "clopidogrel", "ticagrelor", "warfarin", "cardioversion", "wpw", "svt", "cardiology"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.EMERGENCY_ANESTHESIA,
            tier = KitTier.SPECIALTIES,
            title = "Emergency & Anesthesia",
            subtitle = "RSI 7 Ps, O2/NIV, trauma & NPO",
            badgeText = "5 CARDS",
            palette = DeptVermilion,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.EMERG_AIRWAY_RSI,
                    blockId = KitBlockId.EMERGENCY_ANESTHESIA,
                    title = "Airway & RSI 7 Ps",
                    tabLabel = "RSI 7 Ps",
                    shortSubtitle = "SOAPME, LEMON/MOANS/RODS & CICO failed airway",
                    keywords = listOf(
                        "rsi", "7 ps", "soapme", "lemon", "moans", "rods", "cico",
                        "cricothyroidotomy", "intubation", "airway", "anesthesia", "emergency"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.EMERG_OXYGEN_NIV,
                    blockId = KitBlockId.EMERGENCY_ANESTHESIA,
                    title = "Oxygen Delivery & NIV Guide",
                    tabLabel = "O₂ & NIV",
                    shortSubtitle = "NC, Venturi, NRB, HFNC, CPAP & BiPAP settings",
                    keywords = listOf(
                        "oxygen delivery", "oxygen", "venturi", "hfnc", "rox index", "cpap",
                        "bipap", "niv", "non-rebreather", "nrb", "emergency", "anesthesia"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.EMERG_SEDATION_PAIN,
                    blockId = KitBlockId.EMERGENCY_ANESTHESIA,
                    title = "Sedation & Analgesia Ladder",
                    tabLabel = "Sedation & Pain",
                    shortSubtitle = "Procedural sedation IV doses, WHO ladder & reversals",
                    keywords = listOf(
                        "who pain ladder", "pain ladder", "procedural sedation", "propofol",
                        "ketamine sedation", "etomidate", "fentanyl", "naloxone", "flumazenil",
                        "analgesia", "anesthesia", "emergency"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.EMERG_TRAUMA_IMAGING,
                    blockId = KitBlockId.EMERGENCY_ANESTHESIA,
                    title = "Trauma Survey & Imaging Rules",
                    tabLabel = "Trauma & Rules",
                    shortSubtitle = "ATLS xABCDE, Canadian C-Spine & Ottawa Ankle/Knee",
                    keywords = listOf(
                        "atls abcde", "xabcde", "primary survey", "canadian c-spine", "c-spine",
                        "ottawa ankle", "ottawa knee", "efast", "trauma", "emergency"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.EMERG_PREOP_FASTING,
                    blockId = KitBlockId.EMERGENCY_ANESTHESIA,
                    title = "Pre-Op Fasting & Drug Hold Matrix",
                    tabLabel = "NPO & Drug Hold",
                    shortSubtitle = "ASA 2-4-6-8 fasting & SGLT2/anticoagulant hold windows",
                    keywords = listOf(
                        "npo", "fasting", "2-4-6-8", "pre-op drug hold", "drug hold", "sglt2",
                        "glp-1", "metformin", "warfarin hold", "doac hold", "anesthesia"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.GASTRO_HEPATOLOGY,
            tier = KitTier.SPECIALTIES,
            title = "Gastro & Hepatology",
            subtitle = "Hep B/C, GI bleed, LFT & SBP",
            badgeText = "5 CARDS",
            palette = DeptOchre,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.GASTRO_HEPB_DECODER,
                    blockId = KitBlockId.GASTRO_HEPATOLOGY,
                    title = "Hepatitis B & C Serology Decoder",
                    tabLabel = "Hep B / C",
                    shortSubtitle = "Interactive 4-marker Hep B tap-decoder & Hep C matrix",
                    keywords = listOf(
                        "hepatitis b", "hep b serology", "hbsag", "anti-hbs", "anti-hbc",
                        "hep c", "hepatitis c", "serology", "window period", "gastro", "hepatology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.GASTRO_GI_BLEED,
                    blockId = KitBlockId.GASTRO_HEPATOLOGY,
                    title = "Acute GI Bleed Bundles",
                    tabLabel = "GI Bleed",
                    shortSubtitle = "Variceal vs. Non-Variceal UGIB & Forrest Ia–III",
                    keywords = listOf(
                        "gi bleed", "ugib", "varices", "variceal", "terlipressin", "octreotide",
                        "forrest", "peptic ulcer", "pantoprazole", "hematemesis", "gastro"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.GASTRO_LFT_JAUNDICE,
                    blockId = KitBlockId.GASTRO_HEPATOLOGY,
                    title = "LFT Patterns & Jaundice Matrix",
                    tabLabel = "LFT & Jaundice",
                    shortSubtitle = "R-Factor hepatocellular/cholestatic & direct/indirect",
                    keywords = listOf(
                        "lft", "r-factor", "r factor", "jaundice", "hepatocellular",
                        "cholestatic", "bilirubin", "gilbert", "ggt", "gastro", "hepatology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.GASTRO_CIRRHOSIS,
                    blockId = KitBlockId.GASTRO_HEPATOLOGY,
                    title = "Cirrhosis Complication Protocols",
                    tabLabel = "Cirrhosis",
                    shortSubtitle = "West Haven HE, SBP albumin & HRS-AKI protocols",
                    keywords = listOf(
                        "west haven", "encephalopathy", "hepatic encephalopathy", "lactulose",
                        "rifaximin", "sbp", "hrs", "hrs-aki", "cirrhosis", "ascites",
                        "gastro", "hepatology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.GASTRO_STOOL_IBD,
                    blockId = KitBlockId.GASTRO_HEPATOLOGY,
                    title = "Stool, H. pylori & IBD / IBS Key",
                    tabLabel = "Stool & IBD",
                    shortSubtitle = "Bristol 1–7, Truelove & Witts ASUC, H. pylori & Rome IV",
                    keywords = listOf(
                        "bristol stool", "bristol", "h. pylori", "h pylori", "truelove witts",
                        "truelove & witts", "ulcerative colitis", "asuc", "ibd", "ibs",
                        "rome iv", "gastro"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.GENERAL_SURGERY,
            tier = KitTier.SPECIALTIES,
            title = "General Surgery",
            subtitle = "ATLS, burns, sutures & post-op",
            badgeText = "5 CARDS",
            palette = DeptAmber,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.SURG_ATLS_SHOCK,
                    blockId = KitBlockId.GENERAL_SURGERY,
                    title = "ATLS Hemorrhagic Shock (Classes I–IV)",
                    tabLabel = "ATLS Shock",
                    shortSubtitle = "Classes I–IV blood loss, vitals, base deficit & fluids",
                    keywords = listOf(
                        "atls", "hemorrhagic shock", "shock", "class i", "class ii", "class iii",
                        "class iv", "trauma", "blood loss", "base deficit", "mtp", "txa", "surgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.SURG_BURNS_TBSA,
                    blockId = KitBlockId.GENERAL_SURGERY,
                    title = "Burn Surface Area Reference",
                    tabLabel = "Burns TBSA",
                    shortSubtitle = "Rule of Nines (Adult/Peads), Lund-Browder & ABA criteria",
                    keywords = listOf(
                        "rule of nines", "lund browder", "burns", "burn", "tbsa", "aba",
                        "transfer criteria", "escharotomy", "inhalation injury", "palm", "surgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.SURG_SUTURES,
                    blockId = KitBlockId.GENERAL_SURGERY,
                    title = "Sutures & Removal Timeline",
                    tabLabel = "Sutures",
                    shortSubtitle = "Gauge & removal days by site + absorbable/non-absorbable",
                    keywords = listOf(
                        "sutures", "suture", "suture removal", "stitches", "vicryl", "monocryl",
                        "pds", "nylon", "ethilon", "prolene", "silk", "absorbable", "gauge", "surgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.SURG_WOUNDS_TETANUS,
                    blockId = KitBlockId.GENERAL_SURGERY,
                    title = "Wounds, Tetanus & Surgical Prophylaxis",
                    tabLabel = "Tetanus & Abx",
                    shortSubtitle = "Tetanus/TIG matrix, CDC wound classes & pre-op Abx",
                    keywords = listOf(
                        "tetanus", "tig", "tdap", "wound", "wound classes", "cdc", "ssi",
                        "surgical prophylaxis", "pre-op abx", "cefazolin", "dirty wound", "surgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.SURG_POSTOP_DRAINS,
                    blockId = KitBlockId.GENERAL_SURGERY,
                    title = "Post-Op Fever (5 Ws) & Drains / Stomas",
                    tabLabel = "Post-Op & Drains",
                    shortSubtitle = "5 Ws fever timeline, drain removal & stoma norms",
                    keywords = listOf(
                        "post-op fever", "postop fever", "5 ws", "5ws", "wind water walking wound",
                        "drains", "drain removal", "stoma", "ileostomy", "colostomy", "anastomotic leak",
                        "necrotizing fasciitis", "surgery"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.ICU_CRITICAL_CARE,
            tier = KitTier.SPECIALTIES,
            title = "ICU & Critical Care",
            subtitle = "Vent presets, DOPES, RASS & SBT",
            badgeText = "5 CARDS",
            palette = DeptCoralRed,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.ICU_VENT_PRESETS,
                    blockId = KitBlockId.ICU_CRITICAL_CARE,
                    title = "Initial Mechanical Ventilation Presets",
                    tabLabel = "Vent Presets",
                    shortSubtitle = "ARDS lung-protective, COPD/Asthma & Normal lungs",
                    keywords = listOf(
                        "ventilator", "ventilation", "vent", "ards", "copd vent", "asthma vent",
                        "peep", "tidal volume", "pbw", "driving pressure", "auto-peep", "icu"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ICU_PEAK_PLATEAU,
                    blockId = KitBlockId.ICU_CRITICAL_CARE,
                    title = "High Peak vs. Plateau Pressure & DOPES",
                    tabLabel = "Peak / Pplat",
                    shortSubtitle = "Inspiratory hold, resistance vs compliance & DOPES",
                    keywords = listOf(
                        "peak pressure", "plateau pressure", "ppeak", "pplat", "dopes",
                        "airway resistance", "compliance", "ventilator alarm", "desaturation",
                        "pneumothorax", "mucus plug", "icu"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ICU_WEANING_SBT,
                    blockId = KitBlockId.ICU_CRITICAL_CARE,
                    title = "Weaning & SBT Readiness Checklist",
                    tabLabel = "Weaning / SBT",
                    shortSubtitle = "Interactive readiness checks, RSBI <105 & cuff leak",
                    keywords = listOf(
                        "sbt", "rsbi", "weaning", "extubation", "spontaneous breathing trial",
                        "cuff leak", "ventilator weaning", "readiness", "icu"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ICU_SEDATION_DELIRIUM,
                    blockId = KitBlockId.ICU_CRITICAL_CARE,
                    title = "Sedation & Delirium (RASS, CPOT, CAM-ICU)",
                    tabLabel = "RASS / CAM-ICU",
                    shortSubtitle = "RASS +4 to -5, CAM-ICU flow & ABCDEF bundle",
                    keywords = listOf(
                        "rass", "cpot", "cam-icu", "cam icu", "delirium", "sedation", "abcdef",
                        "saveahaart", "icu liberation", "pain", "icu"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.ICU_PROPHYLAXIS_LYTES,
                    blockId = KitBlockId.ICU_CRITICAL_CARE,
                    title = "FAST HUGS BID, VAP & Electrolyte Caps",
                    tabLabel = "FAST HUGS BID",
                    shortSubtitle = "Daily ICU rounding checklist, VAP bundle & IV caps",
                    keywords = listOf(
                        "fast hugs bid", "fast hugs", "vap bundle", "vap", "electrolyte caps",
                        "kcl cap", "magnesium", "phosphate", "calcium gluconate", "prophylaxis", "icu"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.INTERNAL_MEDICINE,
            tier = KitTier.SPECIALTIES,
            title = "Internal Medicine",
            subtitle = "Abx, insulin, HTN & lytes",
            badgeText = "5 CARDS",
            palette = DeptCobalt,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.MED_EMPIRIC_ABX,
                    blockId = KitBlockId.INTERNAL_MEDICINE,
                    title = "Empiric Antibiotic Syndromic Guide",
                    tabLabel = "Empiric Abx",
                    shortSubtitle = "CAP, HAP/VAP, UTI/Pyelo, Meningitis, Cellulitis, Intra-Abd",
                    keywords = listOf(
                        "empiric antibiotics", "antibiotics", "abx", "cap", "hap", "vap",
                        "pyelonephritis", "uti", "meningitis", "cellulitis", "intra-abdominal",
                        "necrotizing fasciitis", "ceftriaxone", "vancomycin", "medicine"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.MED_INSULIN_PK,
                    blockId = KitBlockId.INTERNAL_MEDICINE,
                    title = "Insulin PK & Inpatient Glycemic Regimens",
                    tabLabel = "Insulin PK",
                    shortSubtitle = "Onset, peak, duration & basal-bolus TDD rules",
                    keywords = listOf(
                        "insulin", "onset peak duration", "basal bolus", "glargine", "lispro",
                        "aspart", "nph", "regular insulin", "tdd", "sliding scale", "diabetes", "medicine"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.MED_HTN_EMERGENCY,
                    blockId = KitBlockId.INTERNAL_MEDICINE,
                    title = "Hypertensive Emergency Matrix",
                    tabLabel = "HTN Emergency",
                    shortSubtitle = "Aortic dissection, stroke, ICH, SCAPE, ACS & eclampsia",
                    keywords = listOf(
                        "hypertensive emergency", "htn emergency", "aortic dissection", "stroke",
                        "ich", "scape", "preeclampsia", "eclampsia", "labetalol", "nicardipine",
                        "esmolol", "pheochromocytoma", "cocaine", "medicine"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.MED_ELECTROLYTE_MAPS,
                    blockId = KitBlockId.INTERNAL_MEDICINE,
                    title = "Electrolyte Workup Rapid Maps",
                    tabLabel = "Electrolyte Maps",
                    shortSubtitle = "Hyponatremia, Potassium & Calcium algorithmic maps",
                    keywords = listOf(
                        "hyponatremia", "siadh", "hyperkalemia", "hypokalemia", "hypercalcemia",
                        "hypocalcemia", "pth", "electrolyte workup", "ods", "3% nacl", "medicine"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.MED_VTE_FUO,
                    blockId = KitBlockId.INTERNAL_MEDICINE,
                    title = "Fever / FUO & Medical VTE Prophylaxis",
                    tabLabel = "VTE & FUO",
                    shortSubtitle = "Padua triggers, Enoxaparin/UFH & FUO core workup",
                    keywords = listOf(
                        "fuo", "fever of unknown origin", "inpatient fever", "vte prophylaxis",
                        "padua", "enoxaparin", "dvt prophylaxis", "scd", "hit", "medicine"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.NEPHROLOGY,
            tier = KitTier.SPECIALTIES,
            title = "Nephrology",
            subtitle = "AKI, CKD heatmap, casts & AEIOU",
            badgeText = "5 CARDS",
            palette = DeptEmerald,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.NEPHRO_AKI,
                    blockId = KitBlockId.NEPHROLOGY,
                    title = "KDIGO AKI Staging & Differentiation",
                    tabLabel = "KDIGO AKI",
                    shortSubtitle = "Stages 1–3 SCr/UO & Prerenal vs. Intrinsic ATN",
                    keywords = listOf(
                        "kdigo", "aki", "acute kidney injury", "fena", "feurea",
                        "prerenal", "atn", "postrenal", "nephrology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEPHRO_CKD_HEATMAP,
                    blockId = KitBlockId.NEPHROLOGY,
                    title = "CKD G1–G5 / A1–A3 Risk Heatmap",
                    tabLabel = "CKD Heatmap",
                    shortSubtitle = "Color-coded KDIGO heatmap, referral & ward targets",
                    keywords = listOf(
                        "kdigo", "ckd", "ckd heatmap", "g1-g5", "egfr", "albuminuria",
                        "acr", "nephrology referral", "nephrology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEPHRO_URINALYSIS,
                    blockId = KitBlockId.NEPHROLOGY,
                    title = "Urinalysis Casts & Crystals Key",
                    tabLabel = "Casts & Crystals",
                    shortSubtitle = "RBC, WBC, Muddy Brown casts & 4 renal crystals",
                    keywords = listOf(
                        "urine casts", "urinalysis", "casts", "muddy brown", "rbc casts",
                        "wbc casts", "crystals", "calcium oxalate", "uric acid", "struvite",
                        "cystine", "nephrology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEPHRO_DIALYSIS_SADMANS,
                    blockId = KitBlockId.NEPHROLOGY,
                    title = "Dialysis (AEIOU) & Sick-Day (SADMANS)",
                    tabLabel = "AEIOU & SADMANS",
                    shortSubtitle = "Emergent RRT triggers & acute illness drug hold list",
                    keywords = listOf(
                        "aeiou", "dialysis", "rrt", "slime", "sadmans", "sick day",
                        "sick-day", "uremic pericarditis", "nephrology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEPHRO_RENAL_DRUGS,
                    blockId = KitBlockId.NEPHROLOGY,
                    title = "Renal Drug Dosing Adjustment Flags",
                    tabLabel = "Renal Dosing",
                    shortSubtitle = "Filterable high-risk eGFR/CrCl stop & adjust list",
                    keywords = listOf(
                        "renal dosing", "metformin egfr", "metformin", "nephrotoxins",
                        "nitrofurantoin", "cefepime", "enoxaparin", "gabapentin",
                        "contrast", "nephrology"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.NEURO_NEUROSURGERY,
            tier = KitTier.SPECIALTIES,
            title = "Neuro & Neurosurgery",
            subtitle = "CN I–XII, dermatomes, tPA & ICP",
            badgeText = "5 CARDS",
            palette = DeptIndigo,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.NEURO_CRANIAL_NERVES,
                    blockId = KitBlockId.NEURO_NEUROSURGERY,
                    title = "Cranial Nerves I–XII Exam & Lesions",
                    tabLabel = "CN I–XII",
                    shortSubtitle = "Foramina, rapid bedside tests & classic lesion deficits",
                    keywords = listOf(
                        "cranial nerves", "cn", "foramina", "bell palsy", "oculomotor",
                        "abducens", "trigeminal", "hypoglossal", "neurology", "neurosurgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEURO_DERMATOMES,
                    blockId = KitBlockId.NEURO_NEUROSURGERY,
                    title = "Dermatomes, Myotomes & Reflexes",
                    tabLabel = "Dermatomes",
                    shortSubtitle = "C5–T1 upper limb, L2–S2 lower limb & DTR map",
                    keywords = listOf(
                        "dermatomes", "myotomes", "reflexes", "dtr", "c5", "c6", "c7", "l4",
                        "l5", "s1", "saddle", "neurology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEURO_SYNDROMES,
                    blockId = KitBlockId.NEURO_NEUROSURGERY,
                    title = "Brainstem & Spinal Cord Syndromes",
                    tabLabel = "Syndromes",
                    shortSubtitle = "Weber, Wallenberg, Brown-Séquard & Cauda Equina",
                    keywords = listOf(
                        "weber", "millard-gubler", "wallenberg", "lateral medullary",
                        "brown-sequard", "brown sequard", "anterior cord", "central cord",
                        "cauda equina", "conus medullaris", "rule of 4s", "neurology", "neurosurgery"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEURO_STROKE_TPA,
                    blockId = KitBlockId.NEURO_NEUROSURGERY,
                    title = "Acute Stroke Thrombolysis Checklist",
                    tabLabel = "Stroke tPA/TNK",
                    shortSubtitle = "4.5h window, TNK/tPA dosing, gates & contraindications",
                    keywords = listOf(
                        "stroke", "thrombolysis", "tpa", "alteplase", "tenecteplase", "tnk",
                        "thrombectomy", "lkw", "neurology"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.NEURO_RAISED_ICP,
                    blockId = KitBlockId.NEURO_NEUROSURGERY,
                    title = "Raised ICP Ladder & Herniation",
                    tabLabel = "ICP & Herniation",
                    shortSubtitle = "Tier 0→3, Mannitol/3% NaCl, Cushing Triad & rescue",
                    keywords = listOf(
                        "icp", "raised icp", "cpp", "mannitol", "hypertonic saline", "3% nacl",
                        "herniation", "uncal", "tonsillar", "cushing triad", "cushing's triad",
                        "evd", "craniectomy", "neurosurgery", "neurology"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.OBS_GYNAE,
            tier = KitTier.SPECIALTIES,
            title = "Obs & Gynae",
            subtitle = "Antenatal, MgSO4, PPH & CTG",
            badgeText = "5 CARDS",
            palette = DeptMagenta,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.OBS_ANTENATAL,
                    blockId = KitBlockId.OBS_GYNAE,
                    title = "Antenatal Care & Screening Timeline",
                    tabLabel = "Antenatal",
                    shortSubtitle = "1st / 2nd / 3rd trimester scans, OGTT, Anti-D & GBS",
                    keywords = listOf(
                        "antenatal", "anti-d", "ogtt", "gdm", "gbs", "tdap",
                        "folic acid", "trimester", "obs", "gynae", "obstetrics"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.OBS_MGSO4,
                    blockId = KitBlockId.OBS_GYNAE,
                    title = "Preeclampsia & Eclampsia MgSO₄",
                    tabLabel = "MgSO₄",
                    shortSubtitle = "Zuspan IV, Pritchard IM, toxicity checks & Ca antidote",
                    keywords = listOf(
                        "pritchard", "zuspan", "mgso4", "magnesium sulfate", "eclampsia",
                        "preeclampsia", "calcium gluconate", "obs", "obstetrics"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.OBS_PPH,
                    blockId = KitBlockId.OBS_GYNAE,
                    title = "PPH (4 Ts) & Uterotonic Ladder",
                    tabLabel = "PPH & 4 Ts",
                    shortSubtitle = "Oxytocin, TXA, Ergometrine, Carboprost, Misoprostol",
                    keywords = listOf(
                        "pph", "postpartum hemorrhage", "4 ts", "oxytocin", "ergometrine",
                        "carboprost", "misoprostol", "txa", "bakri", "obs", "obstetrics"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.OBS_CTG,
                    blockId = KitBlockId.OBS_GYNAE,
                    title = "CTG Interpretation Matrix (FIGO / NICE)",
                    tabLabel = "CTG Matrix",
                    shortSubtitle = "Baseline, variability, decelerations & 6-step resus",
                    keywords = listOf(
                        "ctg", "decelerations", "figo", "nice", "fhr", "variability",
                        "late decelerations", "intrauterine resuscitation", "obs", "obstetrics"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.OBS_SAFE_DRUGS,
                    blockId = KitBlockId.OBS_GYNAE,
                    title = "Safe Drugs in Pregnancy & Lactation",
                    tabLabel = "Pregnancy Drugs",
                    shortSubtitle = "1st-line safe choices, lactation & teratogen red list",
                    keywords = listOf(
                        "pregnancy drugs", "safe drugs", "lactation", "breastfeeding",
                        "teratogens", "valproate", "labetalol", "methyldopa", "obs", "gynae", "obstetrics"
                    )
                )
            )
        ),
        KitBlockDef(
            id = KitBlockId.PEDIATRICS,
            tier = KitTier.SPECIALTIES,
            title = "Pediatrics (Peads)",
            subtitle = "Vitals, milestones, ORS & NICU",
            badgeText = "5 CARDS",
            palette = DeptTeal,
            cards = listOf(
                KitCardDef(
                    id = KitCardIds.PEDS_GROWTH_VITALS,
                    blockId = KitBlockId.PEDIATRICS,
                    title = "Age Weight, Height, OFC & Vitals",
                    tabLabel = "Growth & Vitals",
                    shortSubtitle = "Expected weight, height, OFC, HR, RR, SBP",
                    keywords = listOf(
                        "pediatrics", "peads", "weight", "height", "ofc", "head circumference",
                        "vitals", "hr", "rr", "sbp", "bp", "hypotension", "newborn", "infant",
                        "growth", "expected weight", "apls"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.PEDS_MILESTONES,
                    blockId = KitBlockId.PEDIATRICS,
                    title = "Developmental Milestones & Red Flags",
                    tabLabel = "Milestones",
                    shortSubtitle = "Gross/fine motor, speech, social & red flags",
                    keywords = listOf(
                        "milestones", "development", "developmental", "red flags", "motor",
                        "gross motor", "fine motor", "language", "speech", "social", "smile",
                        "walking", "babbling", "regression", "pediatrics", "peads"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.PEDS_WHO_DEHYDRATION,
                    blockId = KitBlockId.PEDIATRICS,
                    title = "WHO Dehydration Plans A, B & C",
                    tabLabel = "WHO Dehydration",
                    shortSubtitle = "ORS, zinc & IV Ringer's resuscitation",
                    keywords = listOf(
                        "who", "dehydration", "plan a", "plan b", "plan c", "ors", "zinc",
                        "diarrhea", "diarrhoea", "gastroenteritis", "ringer", "rl", "skin pinch",
                        "pediatrics", "peads", "rehydration"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.PEDS_NEONATAL,
                    blockId = KitBlockId.PEDIATRICS,
                    title = "Neonatal Quick Card",
                    tabLabel = "Neonatal",
                    shortSubtitle = "Fluids, hypoglycemia, SpO2 & jaundice",
                    keywords = listOf(
                        "neonatal", "neonate", "nicu", "newborn", "fluids", "hypoglycemia",
                        "dextrose", "d10", "gir", "resuscitation", "spo2", "ppv", "jaundice",
                        "bilirubin", "phototherapy", "kernicterus"
                    )
                ),
                KitCardDef(
                    id = KitCardIds.PEDS_IMMUNIZATION,
                    blockId = KitBlockId.PEDIATRICS,
                    title = "Immunization Schedule",
                    tabLabel = "Vaccines",
                    shortSubtitle = "EPI / WHO & UK / Aus NIP reference",
                    keywords = listOf(
                        "immunization", "vaccines", "vaccination", "epi", "who", "nip", "uk",
                        "aus", "bcg", "opv", "ipv", "penta", "pcv", "rota", "mmr", "mr",
                        "tcv", "hepb", "menb", "dtap"
                    )
                )
            )
        )
    )

    val hospitalEssentialBlocks: List<KitBlockDef> =
        blocks.filter { it.tier == KitTier.HOSPITAL_ESSENTIALS }

    val specialtyBlocks: List<KitBlockDef> =
        blocks.filter { it.tier == KitTier.SPECIALTIES }.sortedBy { it.title.lowercase() }

    val allCards: List<KitCardDef> = blocks.flatMap { it.cards }

    private val blockMap: Map<KitBlockId, KitBlockDef> = blocks.associateBy { it.id }
    private val cardMap: Map<String, KitCardDef> = allCards.associateBy { it.id }

    fun getBlock(id: KitBlockId): KitBlockDef = blockMap.getValue(id)

    fun getCard(id: String): KitCardDef? = cardMap[id]

    fun cardsForBlock(id: KitBlockId): List<KitCardDef> = getBlock(id).cards

    fun search(query: String): List<KitCardDef> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()
        val tokens = trimmed.split(Regex("\\s+")).filter { it.isNotEmpty() }
        return allCards.filter { card ->
            val block = getBlock(card.blockId)
            val haystack = buildString {
                append(card.title.lowercase()).append(' ')
                append(card.tabLabel.lowercase()).append(' ')
                append(card.shortSubtitle.lowercase()).append(' ')
                append(block.title.lowercase()).append(' ')
                card.keywords.forEach { append(it.lowercase()).append(' ') }
            }
            tokens.all { token -> haystack.contains(token) }
        }.sortedWith(
            compareByDescending<KitCardDef> { it.title.lowercase().startsWith(trimmed) }
                .thenByDescending { it.tabLabel.lowercase().startsWith(trimmed) }
                .thenByDescending { it.keywords.any { kw -> kw.lowercase() == trimmed } }
                .thenBy { it.title }
        )
    }

    fun searchUnified(query: String): List<KitSearchHit> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()
        val drugHits = LaAndPeadsCoreData.searchDrugs(trimmed).map { KitSearchHit.DrugHit(it) }
        val cardHits = search(trimmed).map { KitSearchHit.CardHit(it) }
        // Prioritize exact drug title/keyword matches first, followed by Kit cards
        return (drugHits + cardHits).sortedWith(
            compareByDescending<KitSearchHit> { it.title.lowercase().startsWith(trimmed) }
                .thenByDescending { it is KitSearchHit.DrugHit && it.entry.keywords.any { kw -> kw.lowercase() == trimmed } }
                .thenByDescending { it is KitSearchHit.CardHit && it.card.keywords.any { kw -> kw.lowercase() == trimmed } }
                .thenBy { it.title }
        )
    }
}
