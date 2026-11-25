package com.example.ui.kit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Biotech
import androidx.compose.material.icons.rounded.Bloodtype
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Healing
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.PregnantWoman
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.kit.KitBlockId
import com.example.data.kit.KitCardIds
import com.example.data.kit.KitRegistry
import com.example.data.kit.LabUnitSystem

/**
 * Full-height Kit Reference Block sub-screen.
 * Routes DOSE_ENGINE directly to DoseEngineScreen (with shared pinned weight bar + 4 sub-tabs),
 * and hosts all other blocks with one-tap connected sub-tabs + ← BACK / Pin bottom dock.
 */
@Composable
fun KitBlockScreen(
    blockId: KitBlockId,
    activeCardId: String,
    focusedDoseDrugId: String?,
    isCardPinned: Boolean,
    labUnitSystem: LabUnitSystem,
    onSelectCard: (String) -> Unit,
    onClearFocusedDoseDrug: () -> Unit,
    onSelectLabUnitSystem: (LabUnitSystem) -> Unit,
    onTogglePinCard: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (blockId == KitBlockId.DOSE_ENGINE) {
        DoseEngineScreen(
            activeTabCardId = activeCardId,
            focusedDrugId = focusedDoseDrugId,
            isPinned = isCardPinned,
            onSelectTabCard = onSelectCard,
            onClearFocusedDrug = onClearFocusedDoseDrug,
            onTogglePin = { onTogglePinCard(activeCardId) },
            onBack = onBack,
            modifier = modifier
        )
        return
    }

    BackHandler(onBack = onBack)

    val block = KitRegistry.getBlock(blockId)
    val cards = block.cards
    val activeCard = cards.firstOrNull { it.id == activeCardId } ?: cards.first()

    val blockIcon = when (blockId) {
        KitBlockId.ER_CRASH -> Icons.Rounded.Emergency
        KitBlockId.DOSE_ENGINE -> Icons.Rounded.Medication
        KitBlockId.CARDIOLOGY -> Icons.Rounded.Favorite
        KitBlockId.EMERGENCY_ANESTHESIA -> Icons.Rounded.LocalHospital
        KitBlockId.GASTRO_HEPATOLOGY -> Icons.Rounded.HealthAndSafety
        KitBlockId.GENERAL_SURGERY -> Icons.Rounded.Healing
        KitBlockId.ICU_CRITICAL_CARE -> Icons.Rounded.MonitorHeart
        KitBlockId.INTERNAL_MEDICINE -> Icons.Rounded.MedicalServices
        KitBlockId.NEPHROLOGY -> Icons.Rounded.WaterDrop
        KitBlockId.NEURO_NEUROSURGERY -> Icons.Rounded.Psychology
        KitBlockId.OBS_GYNAE -> Icons.Rounded.PregnantWoman
        KitBlockId.PEDIATRICS -> Icons.Rounded.ChildCare
        KitBlockId.LABS_FLUIDS -> Icons.Rounded.Biotech
        KitBlockId.IV_BLOOD -> Icons.Rounded.Bloodtype
    }

    Column(modifier = modifier.fillMaxSize()) {
        // ====================================================================
        // TOP BLOCK HEADER + OPTIONAL PERSISTENT SI | CONV TOGGLE (LABS BLOCK)
        // ====================================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        modifier = Modifier.size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = blockIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = block.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = activeCard.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (blockId == KitBlockId.LABS_FLUIDS) {
                Spacer(Modifier.width(10.dp))
                CompactLabUnitToggle(
                    selected = labUnitSystem,
                    onSelect = onSelectLabUnitSystem
                )
            }
        }

        // ====================================================================
        // ONE-TAP CONNECTED SUB-TABS FOR ALL CARDS IN THIS BLOCK
        // ====================================================================
        Spacer(Modifier.height(4.dp))
        KitScrollableChipRow(
            items = cards.map { it.id to it.tabLabel },
            selectedId = activeCard.id,
            onSelect = onSelectCard,
            tagPrefix = "kit_subtab"
        )
        Spacer(Modifier.height(8.dp))

        // ====================================================================
        // ACTIVE REFERENCE CARD CONTENT
        // ====================================================================
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = activeCard.id,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "kit_card_switch"
            ) { cardId ->
                when (cardId) {
                    // Tier 1 · ER & Crash Protocols (5 Cards)
                    KitCardIds.ER_ACLS_PALS -> ErAclsPalsCard()
                    KitCardIds.ER_ANAPHYLAXIS_SEIZURE -> ErAnaphylaxisSeizureCard()
                    KitCardIds.ER_DKA_HYPERKALEMIA -> ErDkaHyperkalemiaCard()
                    KitCardIds.ER_SEPSIS_BUNDLES -> ErSepsisBundlesCard()
                    KitCardIds.ER_TOXICOLOGY -> ErToxicologyCard()

                    // Tier 1 · Normal Labs & Body Fluids (4 Cards — Phase 3)
                    KitCardIds.LABS_ADULT_CORE -> AdultCoreLabsCard(unitSystem = labUnitSystem)
                    KitCardIds.LABS_PEDIATRIC -> PediatricLabsCard(unitSystem = labUnitSystem)
                    KitCardIds.LABS_ABG_VBG -> AbgVbgMatrixCard(unitSystem = labUnitSystem)
                    KitCardIds.LABS_BODY_FLUIDS -> BodyFluidsCard(unitSystem = labUnitSystem)

                    // Tier 1 · IV Fluids, Lines & Blood (4 Cards — Phase 3)
                    KitCardIds.IV_FLUIDS_TABLE -> IvFluidsTableCard()
                    KitCardIds.IV_CANNULAS -> IvCannulasCard()
                    KitCardIds.IV_TUBES_CATHETERS -> TubesCathetersCard()
                    KitCardIds.IV_BLOOD_PRODUCTS -> BloodProductsCard()

                    // Tier 2 · Cardiology (5 Cards — Phase 6)
                    KitCardIds.CARDIO_ECG_STEMI -> CardioEcgStemiCard()
                    KitCardIds.CARDIO_LYTES_DRUGS_ECG -> CardioLytesDrugsEcgCard()
                    KitCardIds.CARDIO_MURMURS -> CardioMurmursManeuversCard()
                    KitCardIds.CARDIO_CLASSIFICATIONS -> CardioClassificationsCard()
                    KitCardIds.CARDIO_DRUGS_ANTICOAG -> CardioDrugsAnticoagCard()

                    // Tier 2 · Emergency & Anesthesia (5 Cards — Phase 6)
                    KitCardIds.EMERG_AIRWAY_RSI -> EmergAirwayRsiCard()
                    KitCardIds.EMERG_OXYGEN_NIV -> EmergOxygenNivCard()
                    KitCardIds.EMERG_SEDATION_PAIN -> EmergSedationPainCard()
                    KitCardIds.EMERG_TRAUMA_IMAGING -> EmergTraumaImagingCard()
                    KitCardIds.EMERG_PREOP_FASTING -> EmergPreOpFastingCard()

                    // Tier 2 · Gastro & Hepatology (5 Cards — Phase 7)
                    KitCardIds.GASTRO_HEPB_DECODER -> GastroHepBDecoderCard()
                    KitCardIds.GASTRO_GI_BLEED -> GastroGiBleedCard()
                    KitCardIds.GASTRO_LFT_JAUNDICE -> GastroLftJaundiceCard()
                    KitCardIds.GASTRO_CIRRHOSIS -> GastroCirrhosisCard()
                    KitCardIds.GASTRO_STOOL_IBD -> GastroStoolIbdCard()

                    // Tier 2 · General Surgery (5 Cards — Phase 5)
                    KitCardIds.SURG_ATLS_SHOCK -> SurgAtlsShockCard()
                    KitCardIds.SURG_BURNS_TBSA -> SurgBurnsTbsaCard()
                    KitCardIds.SURG_SUTURES -> SurgSuturesCard()
                    KitCardIds.SURG_WOUNDS_TETANUS -> SurgWoundsTetanusCard()
                    KitCardIds.SURG_POSTOP_DRAINS -> SurgPostOpDrainsCard()

                    // Tier 2 · ICU & Critical Care (5 Cards — Phase 5)
                    KitCardIds.ICU_VENT_PRESETS -> IcuVentPresetsCard()
                    KitCardIds.ICU_PEAK_PLATEAU -> IcuPeakPlateauCard()
                    KitCardIds.ICU_WEANING_SBT -> IcuWeaningSbtCard()
                    KitCardIds.ICU_SEDATION_DELIRIUM -> IcuSedationDeliriumCard()
                    KitCardIds.ICU_PROPHYLAXIS_LYTES -> IcuProphylaxisLytesCard()

                    // Tier 2 · Internal Medicine (5 Cards — Phase 5)
                    KitCardIds.MED_EMPIRIC_ABX -> MedEmpiricAbxCard()
                    KitCardIds.MED_INSULIN_PK -> MedInsulinPkCard()
                    KitCardIds.MED_HTN_EMERGENCY -> MedHtnEmergencyCard()
                    KitCardIds.MED_ELECTROLYTE_MAPS -> MedElectrolyteMapsCard()
                    KitCardIds.MED_VTE_FUO -> MedVteFuoCard()

                    // Tier 2 · Nephrology (5 Cards — Phase 7)
                    KitCardIds.NEPHRO_AKI -> NephroAkiStagingCard()
                    KitCardIds.NEPHRO_CKD_HEATMAP -> NephroCkdHeatmapCard()
                    KitCardIds.NEPHRO_URINALYSIS -> NephroUrinalysisCard()
                    KitCardIds.NEPHRO_DIALYSIS_SADMANS -> NephroDialysisSickDayCard()
                    KitCardIds.NEPHRO_RENAL_DRUGS -> NephroRenalDrugsCard()

                    // Tier 2 · Neuro & Neurosurgery (5 Cards — Phase 6)
                    KitCardIds.NEURO_CRANIAL_NERVES -> NeuroCranialNervesCard()
                    KitCardIds.NEURO_DERMATOMES -> NeuroDermatomesCard()
                    KitCardIds.NEURO_SYNDROMES -> NeuroSyndromesCard()
                    KitCardIds.NEURO_STROKE_TPA -> NeuroStrokeThrombolysisCard()
                    KitCardIds.NEURO_RAISED_ICP -> NeuroRaisedIcpCard()

                    // Tier 2 · Obs & Gynae (5 Cards — Phase 7)
                    KitCardIds.OBS_ANTENATAL -> ObsAntenatalCard()
                    KitCardIds.OBS_MGSO4 -> ObsMgso4Card()
                    KitCardIds.OBS_PPH -> ObsPphCard()
                    KitCardIds.OBS_CTG -> ObsCtgCard()
                    KitCardIds.OBS_SAFE_DRUGS -> ObsSafeDrugsCard()

                    // Tier 2 · Pediatrics Quick-Ref (5 Cards — Phase 3)
                    KitCardIds.PEDS_GROWTH_VITALS -> PedsGrowthVitalsCard()
                    KitCardIds.PEDS_MILESTONES -> PedsMilestonesCard()
                    KitCardIds.PEDS_WHO_DEHYDRATION -> PedsWhoDehydrationCard()
                    KitCardIds.PEDS_NEONATAL -> PedsNeonatalCard()
                    KitCardIds.PEDS_IMMUNIZATION -> PedsImmunizationCard()
                }
            }
        }

        // ====================================================================
        // BOTTOM ACTION DOCK: ← BACK | Pin / Pinned
        // ====================================================================
        KitBottomActionBar(
            isPinned = isCardPinned,
            onBack = onBack,
            onTogglePin = { onTogglePinCard(activeCard.id) }
        )
    }
}

@Composable
private fun CompactLabUnitToggle(
    selected: LabUnitSystem,
    onSelect: (LabUnitSystem) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        listOf(LabUnitSystem.SI, LabUnitSystem.CONV).forEachIndexed { index, unit ->
            val isSelected = selected == unit
            val shape = if (index == 0) {
                RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 5.dp, bottomEnd = 5.dp)
            } else {
                RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp, topEnd = 16.dp, bottomEnd = 16.dp)
            }
            Surface(
                modifier = Modifier
                    .heightIn(min = 40.dp)
                    .clip(shape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onSelect(unit)
                    }
                    .testTag("lab_unit_${unit.key.lowercase()}"),
                shape = shape,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = unit.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
