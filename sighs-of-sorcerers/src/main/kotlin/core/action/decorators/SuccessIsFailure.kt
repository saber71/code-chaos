package heraclius.core.action.decorators

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

// 如果子节点的执行结果是SUCCESS，则返回FAILURE，其他状态照常返回
class SuccessIsFailure(child: Action) : DecorateAction(child) {
    override fun run(): ActionStatus {
        val result = child.tick()
        if (result == ActionStatus.SUCCESS) return ActionStatus.FAILURE
        return result
    }
}
