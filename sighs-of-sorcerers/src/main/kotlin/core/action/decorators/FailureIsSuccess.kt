package heraclius.core.action.decorators

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

// 如果子节点的执行结果是FAILURE，则返回SUCCESS，其他结果照常返回
class FailureIsSuccess(child: Action) : DecorateAction(child) {
    override fun run(): ActionStatus {
        val result = child.tick()
        if (result == ActionStatus.FAILURE) return ActionStatus.SUCCESS
        return result
    }
}
