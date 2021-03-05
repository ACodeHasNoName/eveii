package opta

import org.optaplanner.core.api.domain.variable.PlanningVariable

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.lookup.PlanningId


@PlanningEntity
data class Ingot(
    @PlanningId
    var id: Int = 0,

    var weight: Int = 0,
    var value: Int = 0,

    @get:PlanningVariable(valueRangeProviderRefs = ["selected"])
    var selected: Boolean? = null
)