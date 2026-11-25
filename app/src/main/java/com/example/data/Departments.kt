package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Healing
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.PregnantWoman
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.WaterDrop
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

object DepartmentRegistry {
    val all: List<Department> = listOf(
        Department(
            id = DepartmentId.PEDIATRICS,
            wheelName = "Pediatrics",
            fullName = "Pediatrics (Peads)",
            subtitle = "Fluids · Dosing · Airway · APGAR",
            palette = DeptTeal,
            icon = Icons.Rounded.ChildCare
        ),
        Department(
            id = DepartmentId.INTERNAL_MEDICINE,
            wheelName = "Internal Med",
            fullName = "Internal Medicine",
            subtitle = "Electrolytes · Acid-Base · Pneumonia",
            palette = DeptCobalt,
            icon = Icons.Rounded.MedicalServices
        ),
        Department(
            id = DepartmentId.CARDIOLOGY,
            wheelName = "Cardiology",
            fullName = "Cardiology",
            subtitle = "QTc · Stroke Risk · Chest Pain · DVT",
            palette = DeptCrimson,
            icon = Icons.Rounded.Favorite
        ),
        Department(
            id = DepartmentId.GENERAL_SURGERY,
            wheelName = "Gen Surgery",
            fullName = "General Surgery",
            subtitle = "Burns · Appendicitis · Shock · Drips",
            palette = DeptAmber,
            icon = Icons.Rounded.Healing
        ),
        Department(
            id = DepartmentId.OBS_GYNAE,
            wheelName = "Obs & Gynae",
            fullName = "Obs & Gynae",
            subtitle = "EDD · Bishop · PPH Shock · EFW",
            palette = DeptMagenta,
            icon = Icons.Rounded.PregnantWoman
        ),
        Department(
            id = DepartmentId.NEURO,
            wheelName = "Neurology",
            fullName = "Neuro & Neurosurgery",
            subtitle = "GCS · CPP · ABC/2 · ICH Score",
            palette = DeptIndigo,
            icon = Icons.Rounded.Psychology
        ),
        Department(
            id = DepartmentId.ICU,
            wheelName = "ICU & Critical",
            fullName = "ICU & Critical Care",
            subtitle = "MAP · qSOFA · P/F Ratio · ARDSNet",
            palette = DeptCoralRed,
            icon = Icons.Rounded.MonitorHeart
        ),
        Department(
            id = DepartmentId.NEPHROLOGY,
            wheelName = "Nephrology",
            fullName = "Nephrology",
            subtitle = "CrCl · CKD-EPI · FENa · Water Deficit",
            palette = DeptEmerald,
            icon = Icons.Rounded.WaterDrop
        ),
        Department(
            id = DepartmentId.GASTRO,
            wheelName = "Gastro & Hep",
            fullName = "Gastro & Hepatology",
            subtitle = "Child-Pugh · MELD-Na · Maddrey · SAAG",
            palette = DeptOchre,
            icon = Icons.Rounded.LocalHospital
        ),
        Department(
            id = DepartmentId.EMERGENCY,
            wheelName = "Emergency",
            fullName = "Emergency & Anesthesia",
            subtitle = "Caprini · MME · NEXUS · Overdose · Resus",
            palette = DeptVermilion,
            icon = Icons.Rounded.Emergency
        )
    ).sortedBy { it.fullName.lowercase() }

    private val byId: Map<DepartmentId, Department> = all.associateBy { it.id }

    fun get(id: DepartmentId): Department = byId.getValue(id)

    fun indexOf(id: DepartmentId): Int = all.indexOfFirst { it.id == id }.coerceAtLeast(0)
}
