package heraclius.core.action.decorators

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

// 反转子节点的执行结果，但保留RUNNING和INVALID状态
class Invert(child: Action) : DecorateAction(child) {
    override fun run(): ActionStatus {
        val result = child.tick()
        if (result == ActionStatus.SUCCESS) return ActionStatus.FAILURE
        else if (result == ActionStatus.FAILURE) return ActionStatus.SUCCESS
        return result
    }
}
