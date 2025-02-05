package com.contractors.app.data.network.model

import com.contractors.app.presentation.ui.model.ObjectTypes
import com.contractors.app.presentation.ui.model.Specialization

data class SpecializationDTO(
    val id: Int,
    val name: String
)
data class ObjectTypesDTO(
    val id: Int,
    val name: String
)
fun SpecializationDTO.toSpecialization(): Specialization = Specialization(id = id, name = name)
fun ObjectTypesDTO.toObjectTypes(): ObjectTypes = ObjectTypes(id = id, name = name)