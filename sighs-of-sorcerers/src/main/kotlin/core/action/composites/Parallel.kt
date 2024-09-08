package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

// 按顺序执行所有子节点，如果全部节点都成功了，返回成功状态，如果有节点的状态为RUNNING，则返回RUNNING状态，否则返回失败状态
class Parallel(vararg actions: Action) : CompositeAction(*actions) {
    override fun run(): ActionStatus {
        var result = ActionStatus.SUCCESS
        for (action in actions) {
            val status = action.tick()
            if (status == ActionStatus.RUNNING || (status == ActionStatus.FAILURE && result != ActionStatus.RUNNING)) {
                result = status
            }
        }
        return result
    }
}
