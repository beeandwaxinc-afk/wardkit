package com.example

import com.example.data.CalculatorInputState
import com.example.data.CalculatorRegistry
import com.example.data.DepartmentId
import com.example.data.DepartmentRegistry
import com.example.domain.FormulaEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormulaEngineTest {

    @Test
    fun registryHasExactly10DepartmentsAnd81CalculatorsSortedAZ() {
        val depts = DepartmentRegistry.all
        assertEquals(10, depts.size)
        assertEquals(81, CalculatorRegistry.all.size)
        assertEquals(81, CalculatorRegistry.all.map { it.id }.distinct().size)

        // Exact A-Z department order
        val expectedOrder = listOf(
            DepartmentId.CARDIOLOGY,
            DepartmentId.EMERGENCY,
            DepartmentId.GASTRO,
            DepartmentId.GENERAL_SURGERY,
            DepartmentId.ICU,
            DepartmentId.INTERNAL_MEDICINE,
            DepartmentId.NEPHROLOGY,
            DepartmentId.NEURO,
            DepartmentId.OBS_GYNAE,
            DepartmentId.PEDIATRICS
        )
        assertEquals(expectedOrder, depts.map { it.id })

        // Dynamic tool counts: 8 for first 9 departments, 9 for Pediatrics
        depts.forEach { dept ->
            val expectedCount = if (dept.id == DepartmentId.PEDIATRICS) 9 else 8
            val deptCalcs = CalculatorRegistry.forDepartment(dept.id)
            assertEquals(
                "Department ${dept.fullName} count mismatch",
                expectedCount,
                deptCalcs.size
            )
            // Strictly A-Z inside each department
            val names = deptCalcs.map { it.name.lowercase() }
            assertEquals("Department ${dept.fullName} calculators not sorted A-Z", names.sorted(), names)
        }

        // Global list strictly A-Z
        val allNames = CalculatorRegistry.all.map { it.name.lowercase() }
        assertEquals(allNames.sorted(), allNames)
    }

    @Test
    fun searchAliasesAndAZOrderingWork() {
        assertTrue(CalculatorRegistry.search("NEWS").any { it.id == "im_news2" })
        assertTrue(CalculatorRegistry.search("NEWS2").any { it.id == "im_news2" })
        assertTrue(CalculatorRegistry.search("National Early Warning Score").any { it.id == "im_news2" })
        assertTrue(CalculatorRegistry.search("EWS").any { it.id == "im_news2" })
        assertTrue(CalculatorRegistry.search("PEWS").any { it.id == "peads_pews" })
        assertTrue(CalculatorRegistry.search("SOFA").any { it.id == "icu_sofa" })
        assertTrue(CalculatorRegistry.search("VBAC").any { it.id == "obgyn_vbac" })
        assertTrue(CalculatorRegistry.search("WETFLAG").any { it.id == "peads_wetflag" })
        assertTrue(CalculatorRegistry.search("ROX").any { it.id == "icu_rox" })

        val ewsResults = CalculatorRegistry.search("score").map { it.name.lowercase() }
        assertEquals(ewsResults.sorted(), ewsResults)
    }

    @Test
    fun nonChecklistCalculatorsStartStrictlyBlankAndEmpty() {
        val nonChecklists = CalculatorRegistry.all.filter { !it.isChecklistZeroValid }
        nonChecklists.forEach { calc ->
            val res = FormulaEngine.evaluate(calc.id, CalculatorInputState())
            assertFalse("Calculator ${calc.id} should not be ready with blank inputs", res.isReady)
        }
    }

    @Test
    fun verifyPhase1FormulasPreserved() {
        val hs = FormulaEngine.evaluate("peads_holliday_segar", CalculatorInputState(numericValues = mapOf("weight" to "25")))
        assertEquals("65.0", hs.primaryValue)

        val park = FormulaEngine.evaluate("surg_parkland", CalculatorInputState(numericValues = mapOf("weight" to "70", "tbsa" to "40")))
        assertEquals("11200", park.primaryValue)

        val st = FormulaEngine.evaluate(
            "em_steroids",
            CalculatorInputState(numericValues = mapOf("dose_mg" to "20"), selectedChoices = mapOf("source_steroid" to "hydro"))
        )
        assertEquals("5", st.primaryValue)
    }

    @Test
    fun verifyPhase2CardiologyEmergencyGastroSurgeryIcu() {
        // Framingham Male 55y, TC 213, HDL 50, SBP 120, untreated, non-smoker, non-DM -> ~9.9%–11%
        val fram = FormulaEngine.evaluate(
            "cardio_framingham",
            CalculatorInputState(
                numericValues = mapOf("age" to "55", "tc" to "213", "hdl" to "50", "sbp" to "120"),
                selectedChoices = mapOf("sex" to "male")
            )
        )
        assertTrue(fram.isReady)
        assertTrue(fram.primaryValue.endsWith("%"))

        // Wells PE: DVT (+3) + HR>100 (+1.5) = 4.5 -> PE Likely
        val wpe = FormulaEngine.evaluate(
            "cardio_wells_pe",
            CalculatorInputState(toggleValues = mapOf("dvt_signs" to true, "hr100" to true))
        )
        assertEquals("4.5", wpe.primaryValue)
        assertTrue(wpe.tierLabel!!.contains("Likely"))

        // Paracetamol Rumack-Matthew: 70 kg, 15 g, 4h, 160 mcg/mL -> ABOVE 150-line -> START NAC
        val para = FormulaEngine.evaluate(
            "em_paracetamol",
            CalculatorInputState(
                numericValues = mapOf("weight" to "70", "dose" to "15", "hours" to "4", "level" to "160"),
                selectedUnits = mapOf("dose" to 0, "level" to 0)
            )
        )
        assertEquals("START NAC", para.primaryValue)

        // FIB-4: Age 50, AST 40, ALT 25, Plt 200 -> (50*40)/(200*5) = 2.00
        val fib4 = FormulaEngine.evaluate(
            "gastro_fib4",
            CalculatorInputState(numericValues = mapOf("age" to "50", "ast" to "40", "alt" to "25", "plt" to "200"))
        )
        assertEquals("2.00", fib4.primaryValue)

        // Lille Model: verify valid 0.00..1.00 output
        val lille = FormulaEngine.evaluate(
            "gastro_lille",
            CalculatorInputState(
                numericValues = mapOf("age" to "50", "albumin" to "30", "bili0" to "200", "bili7" to "100", "pt" to "18"),
                selectedUnits = mapOf("albumin" to 0, "bili0" to 0, "bili7" to 0)
            )
        )
        assertTrue(lille.isReady)
        assertTrue(lille.primaryValue.toDouble() in 0.0..1.0)

        // ROX Index: SpO2 95, FiO2 50%, RR 20 -> (95/0.5)/20 = 9.50
        val rox = FormulaEngine.evaluate(
            "icu_rox",
            CalculatorInputState(numericValues = mapOf("spo2" to "95", "fio2" to "50", "rr" to "20"))
        )
        assertEquals("9.50", rox.primaryValue)

        // Vasopressor: Norepi 4mg/250mL (16 mcg/mL), 80 kg, 0.1 mcg/kg/min -> (0.1*80*60)/16 = 30.0 mL/hr
        val vaso = FormulaEngine.evaluate(
            "icu_vasopressor",
            CalculatorInputState(
                numericValues = mapOf("weight" to "80", "target_dose" to "0.1"),
                selectedChoices = mapOf("mode" to "dose_to_rate", "preset" to "norepi", "dose_unit" to "mcg_kg_min")
            )
        )
        assertEquals("30.0", vaso.primaryValue)
    }

    @Test
    fun verifyNews2WetflagVbacNephroNeuroPeads() {
        // NEWS2 Red Score: total 3 from single parameter (ACVPU = 3, all others 0)
        val newsRed = FormulaEngine.evaluate(
            "im_news2",
            CalculatorInputState(
                selectedChoices = mapOf(
                    "spo2_scale" to "scale1",
                    "rr" to "rr_norm",
                    "spo2_s1" to "s1_0",
                    "oxygen" to "0",
                    "sbp" to "sbp_norm",
                    "pulse" to "hr_norm",
                    "acvpu" to "3",
                    "temp" to "t_norm"
                )
            )
        )
        assertEquals("3", newsRed.primaryValue)
        assertTrue(newsRed.tierLabel!!.contains("RED SCORE"))

        // NEWS2 Scale 2 (COPD): 97% on O2 (+3) + O2 (+2) + RR >=25 (+3) = 8 -> HIGH RISK
        val newsHigh = FormulaEngine.evaluate(
            "im_news2",
            CalculatorInputState(
                selectedChoices = mapOf(
                    "spo2_scale" to "scale2",
                    "rr" to "rr_hi3",
                    "spo2_s2" to "s2_hi3",
                    "oxygen" to "2",
                    "sbp" to "sbp_norm",
                    "pulse" to "hr_norm",
                    "acvpu" to "0",
                    "temp" to "t_norm"
                )
            )
        )
        assertEquals("8", newsHigh.primaryValue)
        assertTrue(newsHigh.tierLabel!!.contains("HIGH RISK"))

        // Adrogue-Madias: Male 0.6, 70 kg (TBW 42), Na 120, 3% NaCl (513) -> (513-120)/43 = +9.14
        val am = FormulaEngine.evaluate(
            "nephro_adrogue_madias",
            CalculatorInputState(
                numericValues = mapOf("weight" to "70", "serum_na" to "120"),
                selectedChoices = mapOf("group" to "0.60", "infusate" to "513")
            )
        )
        assertEquals("+9.14", am.primaryValue)

        // VBAC Grobman 2021: Age 30, 70 kg, 165 cm, Prior VBAC -> high probability
        val vbac = FormulaEngine.evaluate(
            "obgyn_vbac",
            CalculatorInputState(
                numericValues = mapOf("age" to "30", "weight" to "70", "height" to "165"),
                selectedChoices = mapOf("obstetric_hx" to "prior_vbac")
            )
        )
        assertTrue(vbac.isReady)
        assertTrue(vbac.primaryValue.removeSuffix("%").toDouble() > 80.0)

        // WETFLAG: Age 4 -> W = (4+4)*2 = 16.0 kg, E = 64 J, T = 4.5 / 5.0 mm, F = 160 / 320 mL, L = 1.60 mg, A = 1.6 mL, G = 32 mL
        val wetflag = FormulaEngine.evaluate(
            "peads_wetflag",
            CalculatorInputState(numericValues = mapOf("age" to "4"))
        )
        assertEquals("16.0", wetflag.primaryValue)
        assertEquals(7, wetflag.secondaryOutputs.size)
        assertTrue(wetflag.secondaryOutputs.any { it.value.contains("64 J") })
        assertTrue(wetflag.secondaryOutputs.any { it.value.contains("1.60 mg") })
        assertTrue(wetflag.secondaryOutputs.any { it.value.contains("32 mL") })
    }
}
