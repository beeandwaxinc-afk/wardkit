package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CalculatorRegistry
import com.example.data.DepartmentRegistry
import com.example.data.MedWheelPreferences
import com.example.data.ThemeMode
import com.example.data.kit.KitCardIds
import com.example.data.kit.KitRegistry
import com.example.data.kit.KitSearchHit
import com.example.data.kit.LabUnitSystem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name and offline SharedPreferences persist correctly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("Ward Kit", context.getString(R.string.app_name))

        val prefs = MedWheelPreferences(context)
        prefs.saveThemeMode(ThemeMode.AMOLED_DARK)
        assertEquals(ThemeMode.AMOLED_DARK, prefs.loadThemeMode())

        prefs.savePinnedIds(listOf("surg_parkland", "peads_holliday_segar"))
        assertEquals(listOf("surg_parkland", "peads_holliday_segar"), prefs.loadPinnedIds())

        prefs.savePinnedKitIds(
            listOf(
                KitCardIds.GASTRO_HEPB_DECODER,
                KitCardIds.NEPHRO_CKD_HEATMAP,
                KitCardIds.OBS_MGSO4
            )
        )
        assertEquals(
            listOf(
                KitCardIds.GASTRO_HEPB_DECODER,
                KitCardIds.NEPHRO_CKD_HEATMAP,
                KitCardIds.OBS_MGSO4
            ),
            prefs.loadPinnedKitIds()
        )

        prefs.saveLabUnitSystem(LabUnitSystem.SI)
        assertEquals(LabUnitSystem.SI, prefs.loadLabUnitSystem())

        val counts = prefs.incrementUsageCount("surg_parkland")
        assertEquals(1, counts["surg_parkland"])
    }

    @Test
    fun `all 81 calculators remain intact sorted A-Z and Kit v7 search matches all 10 specialties`() {
        assertEquals(10, DepartmentRegistry.all.size)
        assertEquals(81, CalculatorRegistry.all.size)

        val deptNames = DepartmentRegistry.all.map { it.wheelName }
        assertEquals(deptNames.sortedBy { it.lowercase() }, deptNames)

        // Verify Kit v7 2-Tier Bento structure (4 Essentials + 10 Specialties = 14 blocks, 67 cards)
        assertEquals(14, KitRegistry.blocks.size)
        assertEquals(4, KitRegistry.hospitalEssentialBlocks.size)
        assertEquals(10, KitRegistry.specialtyBlocks.size)
        assertEquals(67, KitRegistry.allCards.size)

        val expectedCardHits = mapOf(
            "Hep B Serology" to KitCardIds.GASTRO_HEPB_DECODER,
            "CKD Heatmap" to KitCardIds.NEPHRO_CKD_HEATMAP,
            "Pritchard" to KitCardIds.OBS_MGSO4,
            "STEMI" to KitCardIds.CARDIO_ECG_STEMI,
            "LEMON" to KitCardIds.EMERG_AIRWAY_RSI,
            "Cauda Equina" to KitCardIds.NEURO_SYNDROMES,
            "ATLS" to KitCardIds.SURG_ATLS_SHOCK,
            "DOPES" to KitCardIds.ICU_PEAK_PLATEAU,
            "Empiric Antibiotics" to KitCardIds.MED_EMPIRIC_ABX
        )
        expectedCardHits.forEach { (query, expectedCardId) ->
            val ids = KitRegistry.search(query).map { it.id }
            assertTrue("Expected '$query' to match $expectedCardId, got $ids", expectedCardId in ids)
        }

        val ketamineHits = KitRegistry.searchUnified("Ketamine").filterIsInstance<KitSearchHit.DrugHit>()
        assertTrue("Expected unified search to index Ketamine in Dose Engine", ketamineHits.isNotEmpty())
    }
}
