package com.example

import com.example.data.CalculatorRegistry
import com.example.data.DepartmentRegistry
import com.example.data.kit.AdultErAndInfusionData
import com.example.data.kit.CardiologyKitData
import com.example.data.kit.CkdRiskLevel
import com.example.data.kit.DoseEngineMath
import com.example.data.kit.DoseTabId
import com.example.data.kit.EmergencyAnesthesiaKitData
import com.example.data.kit.ErCrashProtocolsData
import com.example.data.kit.GastroKitData
import com.example.data.kit.GeneralSurgeryKitData
import com.example.data.kit.HepBSeverity
import com.example.data.kit.IcuKitData
import com.example.data.kit.InternalMedicineKitData
import com.example.data.kit.IvBloodKitData
import com.example.data.kit.KitBlockId
import com.example.data.kit.KitCardIds
import com.example.data.kit.KitRegistry
import com.example.data.kit.KitSearchHit
import com.example.data.kit.KitTier
import com.example.data.kit.LaAndPeadsCoreData
import com.example.data.kit.LabUnitSystem
import com.example.data.kit.LabsFluidsKitData
import com.example.data.kit.NephrologyKitData
import com.example.data.kit.NeuroKitData
import com.example.data.kit.ObsGynaeKitData
import com.example.data.kit.PediatricsKitData
import com.example.data.kit.WeightUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KitRegistryTest {

    @Test
    fun `existing 10 A-Z departments and 81 calculators remain intact`() {
        assertEquals(10, DepartmentRegistry.all.size)
        assertEquals(81, CalculatorRegistry.all.size)
        val wheelNames = DepartmentRegistry.all.map { it.wheelName }
        assertEquals(wheelNames.sortedBy { it.lowercase() }, wheelNames)
    }

    @Test
    fun `v7 2-tier equal bento grid structure and all 67 kit cards are complete`() {
        // 14 total blocks: 4 Hospital Essentials (2x2 Tier 1) + 10 A-Z Specialties (2x5 Tier 2)
        assertEquals(14, KitRegistry.blocks.size)
        assertEquals(4, KitRegistry.hospitalEssentialBlocks.size)
        assertEquals(10, KitRegistry.specialtyBlocks.size)
        assertEquals(
            listOf(
                KitBlockId.ER_CRASH,
                KitBlockId.DOSE_ENGINE,
                KitBlockId.LABS_FLUIDS,
                KitBlockId.IV_BLOOD
            ),
            KitRegistry.hospitalEssentialBlocks.map { it.id }
        )
        assertEquals(
            listOf(
                KitBlockId.CARDIOLOGY,
                KitBlockId.EMERGENCY_ANESTHESIA,
                KitBlockId.GASTRO_HEPATOLOGY,
                KitBlockId.GENERAL_SURGERY,
                KitBlockId.ICU_CRITICAL_CARE,
                KitBlockId.INTERNAL_MEDICINE,
                KitBlockId.NEPHROLOGY,
                KitBlockId.NEURO_NEUROSURGERY,
                KitBlockId.OBS_GYNAE,
                KitBlockId.PEDIATRICS
            ),
            KitRegistry.specialtyBlocks.map { it.id }
        )
        val specialtyTitles = KitRegistry.specialtyBlocks.map { it.title }
        assertEquals(specialtyTitles.sortedBy { it.lowercase() }, specialtyTitles)
        assertTrue(KitRegistry.specialtyBlocks.all { it.tier == KitTier.SPECIALTIES && it.badgeText == "5 CARDS" })

        // 67 total cards = 17 Tier-1 + 50 Tier-2 (10 specialties × 5 cards)
        assertEquals(67, KitRegistry.allCards.size)
        assertEquals(5, KitRegistry.cardsForBlock(KitBlockId.ER_CRASH).size)
        assertEquals(4, KitRegistry.cardsForBlock(KitBlockId.DOSE_ENGINE).size)
        assertEquals(4, KitRegistry.cardsForBlock(KitBlockId.LABS_FLUIDS).size)
        assertEquals(4, KitRegistry.cardsForBlock(KitBlockId.IV_BLOOD).size)
        KitRegistry.specialtyBlocks.forEach { block ->
            assertEquals(5, KitRegistry.cardsForBlock(block.id).size)
        }

        // Phase 3 clinical datasets untouched
        assertEquals(11, PediatricsKitData.growthVitalsRows.size)
        assertEquals(10, PediatricsKitData.milestoneAges.size)
        assertEquals(3, PediatricsKitData.whoPlans.size)
        assertEquals(5, PediatricsKitData.neonatalDailyFluids.size)
        assertEquals(6, PediatricsKitData.neonatalSpo2Targets.size)
        assertTrue(PediatricsKitData.epiWhoSchedule.size >= 6)
        assertTrue(PediatricsKitData.ukAusNipSchedule.size >= 6)

        val hbRow = LabsFluidsKitData.adultPanels.first().rows.first()
        assertNotEquals(hbRow.rangeFor(LabUnitSystem.CONV), hbRow.rangeFor(LabUnitSystem.SI))
        assertEquals(8, IvBloodKitData.ivFluids.size)
    }

    @Test
    fun `v7 Gastro, Nephrology, Obs & Gynae datasets and Hep B decoder are complete`() {
        // Hepatitis B 4-marker serology decoder (all 7 canonical patterns + discordant)
        val susceptible = GastroKitData.decodeHepB(hbsAg = false, antiHbs = false, totalAntiHbc = false, igmAntiHbc = false)
        assertEquals("Susceptible", susceptible.title)
        assertEquals(HepBSeverity.INFO, susceptible.severity)

        val vaccinated = GastroKitData.decodeHepB(hbsAg = false, antiHbs = true, totalAntiHbc = false, igmAntiHbc = false)
        assertEquals("Immune via Vaccination", vaccinated.title)
        assertEquals(HepBSeverity.SAFE, vaccinated.severity)

        val pastNatural = GastroKitData.decodeHepB(hbsAg = false, antiHbs = true, totalAntiHbc = true, igmAntiHbc = false)
        assertEquals("Immune via Past Natural Infection", pastNatural.title)
        assertEquals(HepBSeverity.SAFE, pastNatural.severity)

        val acuteHbv = GastroKitData.decodeHepB(hbsAg = true, antiHbs = false, totalAntiHbc = true, igmAntiHbc = true)
        assertEquals("Acute Hepatitis B Infection", acuteHbv.title)
        assertEquals(HepBSeverity.CRITICAL, acuteHbv.severity)

        val chronicHbv = GastroKitData.decodeHepB(hbsAg = true, antiHbs = false, totalAntiHbc = true, igmAntiHbc = false)
        assertEquals("Chronic Hepatitis B Infection", chronicHbv.title)
        assertEquals(HepBSeverity.CRITICAL, chronicHbv.severity)

        val windowPeriod = GastroKitData.decodeHepB(hbsAg = false, antiHbs = false, totalAntiHbc = true, igmAntiHbc = true)
        assertEquals("Acute Window Period", windowPeriod.title)
        assertEquals(HepBSeverity.WARNING, windowPeriod.severity)

        val isolatedCore = GastroKitData.decodeHepB(hbsAg = false, antiHbs = false, totalAntiHbc = true, igmAntiHbc = false)
        assertEquals("Isolated Anti-HBc", isolatedCore.title)
        assertEquals(HepBSeverity.WARNING, isolatedCore.severity)

        val discordant = GastroKitData.decodeHepB(hbsAg = true, antiHbs = true, totalAntiHbc = false, igmAntiHbc = true)
        assertEquals("Unusual / Discordant Pattern", discordant.title)
        assertEquals(HepBSeverity.WARNING, discordant.severity)

        // Gastro & Hepatology datasets
        assertEquals(4, GastroKitData.hepCMatrix.size)
        assertEquals(4, GastroKitData.varicealBundleSteps.size)
        assertEquals(2, GastroKitData.nonVaricealSteps.size)
        assertEquals(6, GastroKitData.forrestClasses.size)
        assertEquals(3, GastroKitData.lftPatterns.size)
        assertEquals(2, GastroKitData.jaundiceBuckets.size)
        assertEquals(4, GastroKitData.westHavenGrades.size)
        assertEquals(3, GastroKitData.heManagementBullets.size)
        assertEquals(4, GastroKitData.sbpProtocolBullets.size)
        assertEquals(3, GastroKitData.hrsAkiBullets.size)
        assertEquals(4, GastroKitData.bristolStoolRows.size)
        assertEquals(3, GastroKitData.asucWardBundle.size)
        assertEquals(4, GastroKitData.hPyloriAndIbsBullets.size)

        // Nephrology datasets & KDIGO CKD Heatmap
        assertEquals(3, NephrologyKitData.kdigoAkiStages.size)
        assertEquals(6, NephrologyKitData.akiDiffRows.size)
        assertEquals(6, NephrologyKitData.ckdHeatmapRows.size)
        val g1 = NephrologyKitData.ckdHeatmapRows.first { it.gfrLabel == "G1" }
        assertEquals(CkdRiskLevel.LOW, g1.a1Risk)
        assertEquals(CkdRiskLevel.MODERATE, g1.a2Risk)
        assertEquals(CkdRiskLevel.HIGH, g1.a3Risk)
        val g3a = NephrologyKitData.ckdHeatmapRows.first { it.gfrLabel == "G3a" }
        assertEquals(CkdRiskLevel.MODERATE, g3a.a1Risk)
        assertEquals(CkdRiskLevel.HIGH, g3a.a2Risk)
        assertEquals(CkdRiskLevel.VERY_HIGH, g3a.a3Risk)
        val g5 = NephrologyKitData.ckdHeatmapRows.first { it.gfrLabel == "G5" }
        assertEquals(CkdRiskLevel.VERY_HIGH, g5.a1Risk)
        assertEquals(CkdRiskLevel.VERY_HIGH, g5.a2Risk)
        assertEquals(CkdRiskLevel.VERY_HIGH, g5.a3Risk)
        assertEquals(4, NephrologyKitData.nephrologyReferralTriggers.size)
        assertEquals(3, NephrologyKitData.ckdWardTargets.size)
        assertEquals(6, NephrologyKitData.urinaryCasts.size)
        assertEquals(4, NephrologyKitData.urinaryCrystals.size)
        assertEquals(5, NephrologyKitData.dialysisAeiou.size)
        assertEquals(7, NephrologyKitData.sadmansRules.size)
        assertEquals(10, NephrologyKitData.renalDrugFlags.size)

        // Obs & Gynae datasets
        assertEquals(3, ObsGynaeKitData.trimesters.size)
        assertEquals(3, ObsGynaeKitData.zuspanSteps.size)
        assertEquals(2, ObsGynaeKitData.pritchardSteps.size)
        assertEquals(3, ObsGynaeKitData.mgToxicityChecks.size)
        assertEquals(5, ObsGynaeKitData.uterotonicLadder.size)
        assertEquals(4, ObsGynaeKitData.pphFourTs.size)
        assertEquals(3, ObsGynaeKitData.ctgCoreFeatures.size)
        assertEquals(4, ObsGynaeKitData.ctgDecelerations.size)
        assertEquals(6, ObsGynaeKitData.intrauterineResusSteps.size)
        assertEquals(6, ObsGynaeKitData.safePregnancyDrugs.size)
        assertEquals(8, ObsGynaeKitData.teratogenRedList.size)
    }

    @Test
    fun `v6 Cardiology, Emergency & Anesthesia, and Neuro datasets are complete`() {
        // Cardiology
        assertEquals(7, CardiologyKitData.stemiTerritories.size)
        assertEquals(5, CardiologyKitData.normalEcgIntervals.size)
        assertEquals(4, CardiologyKitData.avBlocks.size)
        assertEquals(5, CardiologyKitData.electrolyteEcgRows.size)
        assertEquals(5, CardiologyKitData.drugSyndromeEcgRows.size)
        assertEquals(7, CardiologyKitData.murmurs.size)
        assertEquals(4, CardiologyKitData.maneuvers.size)
        assertEquals(4, CardiologyKitData.killipClasses.size)
        assertEquals(4, CardiologyKitData.nyhaClasses.size)
        assertEquals(3, CardiologyKitData.hfPhenotypes.size)
        assertEquals(4, CardiologyKitData.hfGdmtPillars.size)
        assertEquals(6, CardiologyKitData.vaughanWilliams.size)
        assertEquals(4, CardiologyKitData.doacRenalMatrix.size)
        assertEquals(3, CardiologyKitData.daptRules.size)
        assertEquals(4, CardiologyKitData.acuteArrhythmiaRows.size)

        // Emergency & Anesthesia
        assertEquals(7, EmergencyAnesthesiaKitData.rsiSevenPs.size)
        assertEquals(3, EmergencyAnesthesiaKitData.difficultAirwayMnemonics.size)
        assertEquals(3, EmergencyAnesthesiaKitData.cicoFailedAirwaySteps.size)
        assertEquals(5, EmergencyAnesthesiaKitData.oxygenDevices.size)
        assertEquals(2, EmergencyAnesthesiaKitData.nivModes.size)
        assertEquals(4, EmergencyAnesthesiaKitData.nivContraindications.size)
        assertEquals(5, EmergencyAnesthesiaKitData.proceduralSedationDrugs.size)
        assertEquals(3, EmergencyAnesthesiaKitData.whoPainLadder.size)
        assertEquals(6, EmergencyAnesthesiaKitData.atlsPrimarySurvey.size)
        assertEquals(3, EmergencyAnesthesiaKitData.canadianCSpineSteps.size)
        assertEquals(3, EmergencyAnesthesiaKitData.ottawaRules.size)
        assertEquals(4, EmergencyAnesthesiaKitData.fastingRules.size)
        assertEquals(8, EmergencyAnesthesiaKitData.preOpHoldList.size)

        // Neuro & Neurosurgery
        assertEquals(12, NeuroKitData.cranialNerves.size)
        assertEquals(5, NeuroKitData.upperLimbDermatomes.size)
        assertEquals(6, NeuroKitData.lowerLimbDermatomes.size)
        assertEquals(4, NeuroKitData.brainstemSyndromes.size)
        assertEquals(3, NeuroKitData.spinalCordSyndromes.size)
        assertEquals(2, NeuroKitData.caudaVsConusBullets.size)
        assertEquals(2, NeuroKitData.thrombolyticDoses.size)
        assertEquals(3, NeuroKitData.strokeMandatoryGates.size)
        assertEquals(5, NeuroKitData.strokeContraindications.size)
        assertEquals(3, NeuroKitData.icpTargets.size)
        assertEquals(4, NeuroKitData.icpLadder.size)
        assertEquals(4, NeuroKitData.herniationSyndromes.size)
    }

    @Test
    fun `v4 and v5 clinical datasets and dose engine math remain intact`() {
        assertEquals(14, ErCrashProtocolsData.antidotes.size)
        assertEquals(10, AdultErAndInfusionData.erRsiDrugs.size)
        assertEquals(5, AdultErAndInfusionData.continuousInfusions.size)
        assertEquals(12, LaAndPeadsCoreData.peadsCoreDrugs.size)
        assertEquals(4, GeneralSurgeryKitData.atlsShockClasses.size)
        assertEquals(11, IcuKitData.fastHugsBid.size)
        assertEquals(6, InternalMedicineKitData.empiricAbxSyndromes.size)

        assertEquals(70.0, WeightUnit.LBS.toKg(154.32358), 0.01)

        val ketamine = AdultErAndInfusionData.erRsiDrugs.first { it.id == "er_ketamine" }
        val ket120 = DoseEngineMath.computeWeightDose(
            120.0,
            ketamine.regimens.first { it.id == "rsi" },
            ketamine.concentrations.first { it.id == "c50" }
        )
        assertTrue(ket120.isSingleCapped)
        assertEquals(200.0, ket120.finalSingleMg, 0.01)
        assertEquals(4.0, ket120.volumeMl, 0.01)

        val norepi = AdultErAndInfusionData.continuousInfusions.first { it.id == "icu_norepi" }
        val norepiMlHr = DoseEngineMath.computeInfusionMlHr(
            doseValue = 0.1,
            weightKg = 70.0,
            dilution = norepi.dilutions.first(),
            weightIndependent = false
        )
        assertNotNull(norepiMlHr)
        assertEquals(26.25, norepiMlHr!!, 0.01)

        val lipid80 = DoseEngineMath.computeIntralipidLast(80.0)
        assertTrue(lipid80.isBolusCapped)
        assertEquals(100.0, lipid80.bolusMl, 0.01)
        assertFalse(DoseEngineMath.computeAdenosineDoses(30.0).isFirstCapped)
    }

    @Test
    fun `unified kit search matches all v7, v6, v5, v4, and Phase 3 clinical keywords`() {
        val expectedCards = mapOf(
            // Gastro & Hepatology (v7)
            "Hepatitis B" to KitCardIds.GASTRO_HEPB_DECODER,
            "Hep B Serology" to KitCardIds.GASTRO_HEPB_DECODER,
            "HBsAg" to KitCardIds.GASTRO_HEPB_DECODER,
            "Hep C" to KitCardIds.GASTRO_HEPB_DECODER,
            "GI Bleed" to KitCardIds.GASTRO_GI_BLEED,
            "UGIB" to KitCardIds.GASTRO_GI_BLEED,
            "Varices" to KitCardIds.GASTRO_GI_BLEED,
            "Terlipressin" to KitCardIds.GASTRO_GI_BLEED,
            "Octreotide" to KitCardIds.GASTRO_GI_BLEED,
            "Forrest" to KitCardIds.GASTRO_GI_BLEED,
            "LFT" to KitCardIds.GASTRO_LFT_JAUNDICE,
            "R-Factor" to KitCardIds.GASTRO_LFT_JAUNDICE,
            "Jaundice" to KitCardIds.GASTRO_LFT_JAUNDICE,
            "West Haven" to KitCardIds.GASTRO_CIRRHOSIS,
            "Encephalopathy" to KitCardIds.GASTRO_CIRRHOSIS,
            "Lactulose" to KitCardIds.GASTRO_CIRRHOSIS,
            "SBP" to KitCardIds.GASTRO_CIRRHOSIS,
            "HRS" to KitCardIds.GASTRO_CIRRHOSIS,
            "Bristol Stool" to KitCardIds.GASTRO_STOOL_IBD,
            "H. pylori" to KitCardIds.GASTRO_STOOL_IBD,
            "Truelove Witts" to KitCardIds.GASTRO_STOOL_IBD,
            "Ulcerative Colitis" to KitCardIds.GASTRO_STOOL_IBD,
            "Rome IV" to KitCardIds.GASTRO_STOOL_IBD,

            // Nephrology (v7)
            "KDIGO" to KitCardIds.NEPHRO_AKI,
            "AKI" to KitCardIds.NEPHRO_AKI,
            "CKD Heatmap" to KitCardIds.NEPHRO_CKD_HEATMAP,
            "Urine Casts" to KitCardIds.NEPHRO_URINALYSIS,
            "Muddy Brown" to KitCardIds.NEPHRO_URINALYSIS,
            "Crystals" to KitCardIds.NEPHRO_URINALYSIS,
            "AEIOU" to KitCardIds.NEPHRO_DIALYSIS_SADMANS,
            "Dialysis" to KitCardIds.NEPHRO_DIALYSIS_SADMANS,
            "SADMANS" to KitCardIds.NEPHRO_DIALYSIS_SADMANS,
            "Sick Day" to KitCardIds.NEPHRO_DIALYSIS_SADMANS,
            "Renal Dosing" to KitCardIds.NEPHRO_RENAL_DRUGS,
            "Metformin eGFR" to KitCardIds.NEPHRO_RENAL_DRUGS,
            "Nephrotoxins" to KitCardIds.NEPHRO_RENAL_DRUGS,

            // Obs & Gynae (v7)
            "Antenatal" to KitCardIds.OBS_ANTENATAL,
            "Anti-D" to KitCardIds.OBS_ANTENATAL,
            "OGTT" to KitCardIds.OBS_ANTENATAL,
            "GBS" to KitCardIds.OBS_ANTENATAL,
            "Pritchard" to KitCardIds.OBS_MGSO4,
            "Zuspan" to KitCardIds.OBS_MGSO4,
            "MgSO4" to KitCardIds.OBS_MGSO4,
            "Eclampsia" to KitCardIds.OBS_MGSO4,
            "Preeclampsia" to KitCardIds.OBS_MGSO4,
            "PPH" to KitCardIds.OBS_PPH,
            "4 Ts" to KitCardIds.OBS_PPH,
            "Oxytocin" to KitCardIds.OBS_PPH,
            "Ergometrine" to KitCardIds.OBS_PPH,
            "Carboprost" to KitCardIds.OBS_PPH,
            "Misoprostol" to KitCardIds.OBS_PPH,
            "CTG" to KitCardIds.OBS_CTG,
            "Decelerations" to KitCardIds.OBS_CTG,
            "FIGO" to KitCardIds.OBS_CTG,
            "Pregnancy Drugs" to KitCardIds.OBS_SAFE_DRUGS,
            "Teratogens" to KitCardIds.OBS_SAFE_DRUGS,

            // Cardiology (v6)
            "ECG" to KitCardIds.CARDIO_ECG_STEMI,
            "STEMI" to KitCardIds.CARDIO_ECG_STEMI,
            "AV Block" to KitCardIds.CARDIO_ECG_STEMI,
            "Hyperkalemia ECG" to KitCardIds.CARDIO_LYTES_DRUGS_ECG,
            "Digoxin" to KitCardIds.CARDIO_LYTES_DRUGS_ECG,
            "Pericarditis" to KitCardIds.CARDIO_LYTES_DRUGS_ECG,
            "Murmurs" to KitCardIds.CARDIO_MURMURS,
            "Valsalva" to KitCardIds.CARDIO_MURMURS,
            "Killip" to KitCardIds.CARDIO_CLASSIFICATIONS,
            "NYHA" to KitCardIds.CARDIO_CLASSIFICATIONS,
            "Vaughan Williams" to KitCardIds.CARDIO_CLASSIFICATIONS,
            "Antiarrhythmics" to KitCardIds.CARDIO_CLASSIFICATIONS,
            "DOAC" to KitCardIds.CARDIO_DRUGS_ANTICOAG,
            "Apixaban" to KitCardIds.CARDIO_DRUGS_ANTICOAG,
            "Rivaroxaban" to KitCardIds.CARDIO_DRUGS_ANTICOAG,
            "DAPT" to KitCardIds.CARDIO_DRUGS_ANTICOAG,

            // Emergency & Anesthesia (v6)
            "RSI" to KitCardIds.EMERG_AIRWAY_RSI,
            "7 Ps" to KitCardIds.EMERG_AIRWAY_RSI,
            "LEMON" to KitCardIds.EMERG_AIRWAY_RSI,
            "MOANS" to KitCardIds.EMERG_AIRWAY_RSI,
            "RODS" to KitCardIds.EMERG_AIRWAY_RSI,
            "Oxygen Delivery" to KitCardIds.EMERG_OXYGEN_NIV,
            "Venturi" to KitCardIds.EMERG_OXYGEN_NIV,
            "HFNC" to KitCardIds.EMERG_OXYGEN_NIV,
            "CPAP" to KitCardIds.EMERG_OXYGEN_NIV,
            "BiPAP" to KitCardIds.EMERG_OXYGEN_NIV,
            "WHO Pain Ladder" to KitCardIds.EMERG_SEDATION_PAIN,
            "Procedural Sedation" to KitCardIds.EMERG_SEDATION_PAIN,
            "ATLS ABCDE" to KitCardIds.EMERG_TRAUMA_IMAGING,
            "Canadian C-Spine" to KitCardIds.EMERG_TRAUMA_IMAGING,
            "Ottawa Ankle" to KitCardIds.EMERG_TRAUMA_IMAGING,
            "Ottawa Knee" to KitCardIds.EMERG_TRAUMA_IMAGING,
            "NPO" to KitCardIds.EMERG_PREOP_FASTING,
            "Fasting" to KitCardIds.EMERG_PREOP_FASTING,
            "Pre-Op Drug Hold" to KitCardIds.EMERG_PREOP_FASTING,
            "SGLT2" to KitCardIds.EMERG_PREOP_FASTING,

            // Neuro & Neurosurgery (v6)
            "Cranial Nerves" to KitCardIds.NEURO_CRANIAL_NERVES,
            "Foramina" to KitCardIds.NEURO_CRANIAL_NERVES,
            "Dermatomes" to KitCardIds.NEURO_DERMATOMES,
            "Myotomes" to KitCardIds.NEURO_DERMATOMES,
            "Reflexes" to KitCardIds.NEURO_DERMATOMES,
            "Weber" to KitCardIds.NEURO_SYNDROMES,
            "Wallenberg" to KitCardIds.NEURO_SYNDROMES,
            "Brown-Sequard" to KitCardIds.NEURO_SYNDROMES,
            "Cauda Equina" to KitCardIds.NEURO_SYNDROMES,
            "Conus Medullaris" to KitCardIds.NEURO_SYNDROMES,
            "Stroke" to KitCardIds.NEURO_STROKE_TPA,
            "Thrombolysis" to KitCardIds.NEURO_STROKE_TPA,
            "tPA" to KitCardIds.NEURO_STROKE_TPA,
            "Tenecteplase" to KitCardIds.NEURO_STROKE_TPA,
            "ICP" to KitCardIds.NEURO_RAISED_ICP,
            "Mannitol" to KitCardIds.NEURO_RAISED_ICP,
            "Hypertonic Saline" to KitCardIds.NEURO_RAISED_ICP,
            "Herniation" to KitCardIds.NEURO_RAISED_ICP,
            "Cushing Triad" to KitCardIds.NEURO_RAISED_ICP,

            // Phase 3–5 preserved
            "ATLS" to KitCardIds.SURG_ATLS_SHOCK,
            "DOPES" to KitCardIds.ICU_PEAK_PLATEAU,
            "Empiric Antibiotics" to KitCardIds.MED_EMPIRIC_ABX,
            "ACLS" to KitCardIds.ER_ACLS_PALS,
            "milestones" to KitCardIds.PEDS_MILESTONES,
            "CSF" to KitCardIds.LABS_BODY_FLUIDS
        )
        expectedCards.forEach { (query, expectedCardId) ->
            val cardHits = KitRegistry.searchUnified(query)
                .filterIsInstance<KitSearchHit.CardHit>()
                .map { it.card.id }
            assertTrue("Query '$query' did not match card $expectedCardId (got $cardHits)", expectedCardId in cardHits)
        }

        val ketamineHits = KitRegistry.searchUnified("Ketamine")
            .filterIsInstance<KitSearchHit.DrugHit>()
        assertTrue(ketamineHits.any { it.entry.tabId == DoseTabId.ER_RSI })
    }
}
