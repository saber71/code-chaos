package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

class Sequence(vararg actions: Action, val memory: Boolean = false) : CompositeAction(*actions) {
    private var index = 0

    override fun run(): ActionStatus {
        if (!(status == ActionStatus.RUNNING && memory)) {
            index = 0
        }
        for (action in actions.slice(index..actions.lastIndex)) {
            val status = action.tick()
            if (status == ActionStatus.SUCCESS) {
                index++
            } else {
                return status
            }
        }
        return ActionStatus.SUCCESS
    }
}
