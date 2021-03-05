package opta

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.ProblemFactProperty
import org.optaplanner.core.api.domain.valuerange.CountableValueRange
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider


@PlanningSolution
class KnapsackSolution {
    @field:PlanningEntityCollectionProperty
    var ingots: List<Ingot>? = null

    @field:ProblemFactProperty
    var knapsack: Knapsack? = null

    @get:ValueRangeProvider(id = "selected")
    val selected: CountableValueRange<Boolean>
        get() = ValueRangeFactory.createBooleanValueRange()

    @ConstraintConfigurationProvider
    var constraintConfiguration = KnapsackConstraintConfiguration()

    @field:PlanningScore
    var score: HardSoftScore? = null
}