package com.example.data.kit

import java.util.Locale
import kotlin.math.min

// ============================================================================
// BLOCK B (TIER 1): QUICK DRUG & INFUSION ENGINE — MODELS & CALCULATION MATH
// ============================================================================

enum class DoseTabId(val cardId: String, val shortLabel: String) {
    ER_RSI(KitCardIds.DOSE_ER_RSI, "ER & RSI"),
    WARD_ICU(KitCardIds.DOSE_WARD_ICU, "Ward & ICU"),
    LOCAL_ANESTH(KitCardIds.DOSE_LOCAL_ANESTH, "Local Anesth"),
    PEADS_CORE(KitCardIds.DOSE_PEADS_CORE, "Peads Core");

    companion object {
        fun fromCardId(cardId: String): DoseTabId =
            entries.firstOrNull { it.cardId == cardId } ?: ER_RSI
    }
}

enum class WeightUnit(val id: String, val label: String) {
    KG("kg", "kg"),
    LBS("lbs", "lbs");

    fun toKg(raw: Double): Double = if (this == KG) raw else raw / 2.2046226218
}

data class DrugConcentration(
    val id: String,
    val label: String,
    val mgPerMl: Double
)

data class WeightDoseRegimen(
    val id: String,
    val label: String,
    val dosePerKg: Double,
    val isDailyDose: Boolean = false,
    val dosesPerDay: Int = 1,
    val maxSingleMg: Double,
    val maxDailyMg: Double? = null,
    val frequencyLabel: String = "single dose",
    val allowedConcIds: Set<String>? = null
)

enum class SpecialDrugMode {
    STANDARD,
    ADENOSINE_DUAL,
    HEPARIN_BOLUS_DRIP,
    ASTHMA_NEBS_TIER,
    ONDANSETRON_DYNAMIC_CAP,
    PARACETAMOL_PEADS
}

data class WeightBasedDrugDef(
    val id: String,
    val tabId: DoseTabId,
    val name: String,
    val routeTag: String,
    val regimens: List<WeightDoseRegimen>,
    val concentrations: List<DrugConcentration>,
    val pearl: String,
    val isRedFlagPearl: Boolean = false,
    val keywords: List<String> = emptyList(),
    val specialMode: SpecialDrugMode = SpecialDrugMode.STANDARD
)

data class DoseComputation(
    val rawSingleMg: Double,
    val finalSingleMg: Double,
    val volumeMl: Double,
    val isSingleCapped: Boolean,
    val capSingleMg: Double,
    val finalDailyMg: Double? = null,
    val isDailyCapped: Boolean = false,
    val capDailyMg: Double? = null
)

data class InfusionDilution(
    val id: String,
    val label: String,
    val mcgPerMl: Double
)

data class InfusionDrugDef(
    val id: String,
    val name: String,
    val weightIndependent: Boolean,
    val unitLabel: String,
    val dilutions: List<InfusionDilution>,
    val presetDoses: List<Double>,
    val pearl: String,
    val keywords: List<String>
)

data class LocalAnestheticRegimen(
    val id: String,
    val label: String,
    val mgPerKg: Double,
    val maxMg: Double
)

data class LocalAnestheticPercent(
    val id: String,
    val label: String,
    val percent: Double
) {
    val mgPerMl: Double get() = percent * 10.0
}

data class LocalAnestheticDef(
    val id: String,
    val name: String,
    val regimens: List<LocalAnestheticRegimen>,
    val percentOptions: List<LocalAnestheticPercent>,
    val pearl: String,
    val isRedFlagPearl: Boolean = false,
    val keywords: List<String>
)

data class LocalAnestheticResult(
    val maxSafeMg: Double,
    val maxSafeMl: Double,
    val isCapped: Boolean,
    val hardCapMg: Double
)

data class IntralipidLastResult(
    val bolusMl: Double,
    val isBolusCapped: Boolean,
    val infusionMlPerMin: Double,
    val infusionMlPerHr: Double,
    val absoluteMaxMl: Double
)

data class HeparinCalcResult(
    val bolusUnits: Double,
    val isBolusCapped: Boolean,
    val maxBolusUnits: Double,
    val dripUnitsPerHr: Double,
    val isDripCapped: Boolean,
    val maxDripUnitsPerHr: Double?
)

data class AdenosineCalcResult(
    val firstDoseMg: Double,
    val firstDoseMl: Double,
    val isFirstCapped: Boolean,
    val secondDoseMg: Double,
    val secondDoseMl: Double,
    val isSecondCapped: Boolean
)

data class SteroidEquivRow(
    val id: String,
    val shortName: String,
    val fullName: String,
    val factorMg: Double
)

object DoseEngineMath {

    /**
     * Enforces mandatory hard adult max cap on single AND daily doses,
     * deriving draw-up/syrup volume strictly from the capped dose.
     */
    fun computeWeightDose(
        weightKg: Double,
        regimen: WeightDoseRegimen,
        concentration: DrugConcentration,
        overrideMaxSingleMg: Double? = null
    ): DoseComputation {
        val effectiveMaxSingle = overrideMaxSingleMg ?: regimen.maxSingleMg
        val rawSingle = if (regimen.isDailyDose) {
            (weightKg * regimen.dosePerKg) / regimen.dosesPerDay.coerceAtLeast(1)
        } else {
            weightKg * regimen.dosePerKg
        }

        val cappedSingle = min(rawSingle, effectiveMaxSingle)
        val isSingleCapped = rawSingle > effectiveMaxSingle + 1e-6

        var finalSingle = cappedSingle
        var finalDaily: Double? = null
        var isDailyCapped = false

        if (regimen.maxDailyMg != null) {
            val rawDaily = if (regimen.isDailyDose) {
                weightKg * regimen.dosePerKg
            } else {
                finalSingle * regimen.dosesPerDay.coerceAtLeast(1)
            }
            val cappedDaily = min(rawDaily, regimen.maxDailyMg)
            isDailyCapped = rawDaily > regimen.maxDailyMg + 1e-6
            finalDaily = cappedDaily
            val perDoseFromDailyCap = cappedDaily / regimen.dosesPerDay.coerceAtLeast(1)
            if (perDoseFromDailyCap < finalSingle) {
                finalSingle = perDoseFromDailyCap
            }
        }

        val volumeMl = if (concentration.mgPerMl > 0.0) finalSingle / concentration.mgPerMl else 0.0
        return DoseComputation(
            rawSingleMg = rawSingle,
            finalSingleMg = finalSingle,
            volumeMl = volumeMl,
            isSingleCapped = isSingleCapped,
            capSingleMg = effectiveMaxSingle,
            finalDailyMg = finalDaily,
            isDailyCapped = isDailyCapped,
            capDailyMg = regimen.maxDailyMg
        )
    }

    /**
     * Continuous infusion pump rate in mL/hr:
     * - Weight-based (mcg/kg/min): (dose * weightKg * 60) / concMcgPerMl
     * - Weight-independent (mcg/min, e.g., GTN): (dose * 60) / concMcgPerMl
     */
    fun computeInfusionMlHr(
        doseValue: Double,
        weightKg: Double?,
        dilution: InfusionDilution,
        weightIndependent: Boolean
    ): Double? {
        if (doseValue <= 0.0 || dilution.mcgPerMl <= 0.0) return null
        return if (weightIndependent) {
            (doseValue * 60.0) / dilution.mcgPerMl
        } else {
            val wt = weightKg ?: return null
            if (wt <= 0.0) return null
            (doseValue * wt * 60.0) / dilution.mcgPerMl
        }
    }

    /**
     * Local Anesthetic max safe mg & mL (Max_mg / (Percent * 10.0)) with hard max cap.
     */
    fun computeLocalAnesthetic(
        weightKg: Double,
        regimen: LocalAnestheticRegimen,
        percent: LocalAnestheticPercent
    ): LocalAnestheticResult {
        val rawMg = weightKg * regimen.mgPerKg
        val cappedMg = min(rawMg, regimen.maxMg)
        val isCapped = rawMg > regimen.maxMg + 1e-6
        val maxMl = cappedMg / percent.mgPerMl
        return LocalAnestheticResult(
            maxSafeMg = cappedMg,
            maxSafeMl = maxMl,
            isCapped = isCapped,
            hardCapMg = regimen.maxMg
        )
    }

    /**
     * Live 20% Intralipid LAST rescue dosing.
     * Initial bolus: 1.5 mL/kg (capped at 100 mL for >70 kg).
     * Infusion: 0.25 mL/kg/min (and mL/hr).
     * Absolute max: 12 mL/kg total.
     */
    fun computeIntralipidLast(weightKg: Double): IntralipidLastResult {
        val rawBolus = weightKg * 1.5
        val cappedBolus = min(rawBolus, 100.0)
        val isBolusCapped = weightKg > 70.0 || rawBolus > 100.0 + 1e-6
        val infMlMin = weightKg * 0.25
        val infMlHr = infMlMin * 60.0
        val absMax = weightKg * 12.0
        return IntralipidLastResult(
            bolusMl = cappedBolus,
            isBolusCapped = isBolusCapped,
            infusionMlPerMin = infMlMin,
            infusionMlPerHr = infMlHr,
            absoluteMaxMl = absMax
        )
    }

    /**
     * Adenosine dual output: 1st dose 0.1 mg/kg (max 6 mg) & 2nd dose 0.2 mg/kg (max 12 mg) at 3 mg/mL.
     */
    fun computeAdenosineDoses(weightKg: Double, concMgPerMl: Double = 3.0): AdenosineCalcResult {
        val raw1 = weightKg * 0.1
        val cap1 = min(raw1, 6.0)
        val raw2 = weightKg * 0.2
        val cap2 = min(raw2, 12.0)
        return AdenosineCalcResult(
            firstDoseMg = cap1,
            firstDoseMl = cap1 / concMgPerMl,
            isFirstCapped = raw1 > 6.0 + 1e-6,
            secondDoseMg = cap2,
            secondDoseMl = cap2 / concMgPerMl,
            isSecondCapped = raw2 > 12.0 + 1e-6
        )
    }

    /**
     * Unfractionated Heparin bolus + maintenance drip for VTE/PE vs ACS.
     */
    fun computeHeparin(weightKg: Double, isAcs: Boolean): HeparinCalcResult {
        return if (isAcs) {
            val rawBolus = weightKg * 60.0
            val capBolus = min(rawBolus, 4000.0)
            val rawDrip = weightKg * 12.0
            val capDrip = min(rawDrip, 1000.0)
            HeparinCalcResult(
                bolusUnits = capBolus,
                isBolusCapped = rawBolus > 4000.0 + 1e-6,
                maxBolusUnits = 4000.0,
                dripUnitsPerHr = capDrip,
                isDripCapped = rawDrip > 1000.0 + 1e-6,
                maxDripUnitsPerHr = 1000.0
            )
        } else {
            val rawBolus = weightKg * 80.0
            val capBolus = min(rawBolus, 10000.0)
            val rawDrip = weightKg * 18.0
            HeparinCalcResult(
                bolusUnits = capBolus,
                isBolusCapped = rawBolus > 10000.0 + 1e-6,
                maxBolusUnits = 10000.0,
                dripUnitsPerHr = rawDrip,
                isDripCapped = false,
                maxDripUnitsPerHr = null
            )
        }
    }

    val steroidEquivalenceTable: List<SteroidEquivRow> = listOf(
        SteroidEquivRow("hydrocort", "Hydrocort", "Hydrocortisone", 20.0),
        SteroidEquivRow("pred", "Pred", "Prednisolone", 5.0),
        SteroidEquivRow("methylpred", "Methylpred", "Methylprednisolone", 4.0),
        SteroidEquivRow("dex", "Dex", "Dexamethasone", 0.75)
    )

    fun computeSteroidEquivalents(sourceId: String, amountMg: Double): List<Pair<SteroidEquivRow, Double>> {
        val source = steroidEquivalenceTable.firstOrNull { it.id == sourceId }
            ?: steroidEquivalenceTable.first()
        val ratio = amountMg / source.factorMg
        return steroidEquivalenceTable.map { row -> row to (ratio * row.factorMg) }
    }

    fun fmt(value: Double, decimals: Int = 1): String {
        val rounded = String.format(Locale.US, "%.${decimals}f", value)
        return if (decimals > 0 && rounded.endsWith(".0")) {
            rounded.dropLast(2)
        } else {
            rounded
        }
    }

    fun fmtDose(mg: Double): String = when {
        mg >= 100.0 -> fmt(mg, 0)
        mg >= 10.0 -> fmt(mg, 1)
        else -> fmt(mg, 2)
    }

    fun fmtVol(ml: Double): String = when {
        ml >= 10.0 -> fmt(ml, 1)
        else -> fmt(ml, 2)
    }
}
