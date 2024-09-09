package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

/**
 * 并行执行多个动作的类。Parallel类继承自CompositeAction，用于同时执行多个动作。
 * 动作的执行顺序和数量由构造函数中的vararg参数决定。
 *
 * @param actions 一个或多个要并行执行的动作。
 */
class Parallel(vararg actions: Action) : CompositeAction(*actions) {
    /**
     * 执行并行动作。此方法遍历并执行每个动作，根据动作的执行结果更新整体状态。
     * 如果所有动作都成功完成，返回ActionStatus.SUCCESS；
     * 如果任一动作正在运行中，返回ActionStatus.RUNNING；
     * 如果任一动作失败，返回ActionStatus.FAILURE
     *
     * @return ActionStatus 动作执行的最终状态。
     */
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
