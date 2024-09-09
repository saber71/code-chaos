package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

/**
 * Sequence类是一个复合动作类，它按顺序执行一系列动作。
 * 一旦一个动作失败，整个Sequence将停止执行并返回当前动作的状态。
 * 如果所有动作都成功，Sequence将返回成功状态。
 *
 * @param actions 一个或多个要按顺序执行的动作。
 * @param memory 是否保留Sequence的状态记忆，默认为false。如果为true，在重复执行Sequence时，它将从上次中断的地方继续执行。
 */
class Sequence(vararg actions: Action, val memory: Boolean = false) : CompositeAction(*actions) {
    // 用于跟踪当前执行到的动作索引。
    private var index = 0

    /**
     * 开始执行Sequence中的动作。
     *
     * 此函数首先检查是否需要重置动作索引，然后遍历动作列表，
     * 执行每个动作并根据动作的返回状态决定是否继续执行下一个动作。
     *
     * @return ActionStatus 返回Sequence的执行状态，可能是RUNNING、FAILURE或SUCCESS。
     */
    override fun run(): ActionStatus {
        // 根据memory标志决定是否重置动作索引。
        if (!(status == ActionStatus.RUNNING && memory)) {
            index = 0
        }

        // 遍历并执行每个动作，直到某个动作失败或所有动作成功。
        for (action in actions.slice(index..actions.lastIndex)) {
            val status = action.tick()
            // 如果动作成功，移动到下一个动作。
            if (status == ActionStatus.SUCCESS) {
                index++
            } else {
                // 如果动作失败或运行中，返回当前状态。
                return status
            }
        }
        // 所有动作成功执行，返回成功状态。
        return ActionStatus.SUCCESS
    }
}
