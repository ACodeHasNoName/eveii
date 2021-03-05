package opta

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore


@ConstraintConfiguration(constraintPackage = "opta")
class KnapsackConstraintConfiguration {
    @ConstraintWeight("Max Weight")
    var weight = HardSoftScore.ofHard(1)

    @ConstraintWeight("Max Value")
    var maxValue = HardSoftScore.ofSoft(1)
}