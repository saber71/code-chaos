package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

abstract class CompositeAction(vararg actions: Action) : Action() {
    protected val actions = actions.toList()

    override fun afterRun(newStatus: ActionStatus) {
        if (status != ActionStatus.INVALID && newStatus != ActionStatus.RUNNING) {
            for (action in actions) {
                action.afterRun(ActionStatus.INVALID)
            }
        }
        super.afterRun(newStatus)
    }
}
