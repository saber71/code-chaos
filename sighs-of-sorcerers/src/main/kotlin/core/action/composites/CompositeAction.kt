package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

abstract class CompositeAction(vararg actions: Action) : Action() {
    protected val actions = actions.toList()

    override fun stop(newStatus: ActionStatus) {
        if (status != ActionStatus.INVALID && newStatus != ActionStatus.RUNNING) {
            for (action in actions) {
                action.stop(ActionStatus.INVALID)
            }
        }
        super.stop(newStatus)
    }
}
