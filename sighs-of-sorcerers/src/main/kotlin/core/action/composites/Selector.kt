package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

/**
 * 选择器复合行为类，用于依次执行内部包含的行为，直到其中一个成功为止。
 * 如果所有行为都失败，则选择器返回失败。
 *
 * @param actions 可变参数，表示要依次尝试执行的行为。
 * @param memory 是否启用记忆功能，若为 true，则在运行期间保存当前执行的行为索引。
 */
class Selector(vararg actions: Action, val memory: Boolean = false) : CompositeAction(*actions) {
    // 行为执行的当前索引，用于循环执行内部行为。
    private var index = 0

    /**
     * 执行选择器逻辑。
     *
     * 根据是否启用记忆功能来重置索引，然后依次尝试执行每个行为，
     * 直到有一个行为返回非失败状态，或者所有行为都尝试过。
     *
     * @return ActionStatus 表示执行结果的状态。
     */
    override fun run(): ActionStatus {
        // 根据记忆功能决定是否重置索引。
        if (!(status == ActionStatus.RUNNING && memory)) {
            index = 0
        }
        // 遍历从当前索引开始到行为列表末尾的行为。
        for (action in actions.slice(index..actions.lastIndex)) {
            // 执行当前行为并获取状态。
            val status = action.tick()
            // 如果行为失败，尝试下一个行为。
            if (status == ActionStatus.FAILURE) {
                index++
            } else {
                // 如果行为成功或进行中，返回该状态。
                return status
            }
        }
        // 如果所有行为都失败，返回失败状态。
        return ActionStatus.FAILURE
    }
}
