package com.example.data

import com.example.data.phase2.CardiologyPhase2Calculators
import com.example.data.phase2.EmergencyPhase2Calculators
import com.example.data.phase2.GastroPhase2Calculators
import com.example.data.phase2.IcuPhase2Calculators
import com.example.data.phase2.MedicinePhase2Calculators
import com.example.data.phase2.NephrologyPhase2Calculators
import com.example.data.phase2.NeuroPhase2Calculators
import com.example.data.phase2.ObsGynaePhase2Calculators
import com.example.data.phase2.PediatricsPhase2Calculators
import com.example.data.phase2.SurgeryPhase2Calculators

object CalculatorRegistry {
    private val phase2All: List<CalculatorDef> =
        CardiologyPhase2Calculators +
            EmergencyPhase2Calculators +
            GastroPhase2Calculators +
            SurgeryPhase2Calculators +
            IcuPhase2Calculators +
            MedicinePhase2Calculators +
            NephrologyPhase2Calculators +
            NeuroPhase2Calculators +
            ObsGynaePhase2Calculators +
            PediatricsPhase2Calculators

    val all: List<CalculatorDef> =
        (CalculatorsPart1 + CalculatorsPart2 + phase2All)
            .sortedBy { it.name.lowercase() }

    private val byId: Map<String, CalculatorDef> = all.associateBy { it.id }

    private val byDepartment: Map<DepartmentId, List<CalculatorDef>> =
        all.groupBy { it.departmentId }
            .mapValues { (_, list) -> list.sortedBy { it.name.lowercase() } }

    fun get(id: String): CalculatorDef? = byId[id]

    fun forDepartment(departmentId: DepartmentId): List<CalculatorDef> =
        byDepartment[departmentId].orEmpty()

    fun countForDepartment(departmentId: DepartmentId): Int =
        byDepartment[departmentId]?.size ?: 0

    fun search(query: String): List<CalculatorDef> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()
        val tokens = trimmed.split(" ").filter { it.isNotBlank() }

        return all
            .filter { calc ->
                val dept = DepartmentRegistry.get(calc.departmentId)
                val searchable = buildString {
                    append(calc.name.lowercase())
                    append(' ')
                    append(calc.shortSubtitle.lowercase())
                    append(' ')
                    append(dept.fullName.lowercase())
                    append(' ')
                    append(dept.wheelName.lowercase())
                    append(' ')
                    append(calc.synonyms.joinToString(" ").lowercase())
                }
                tokens.all { searchable.contains(it) }
            }
            .sortedBy { it.name.lowercase() }
    }
}
