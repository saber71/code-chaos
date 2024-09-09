package heraclius.core.action.composites

import heraclius.core.action.Action
import heraclius.core.action.ActionStatus

/**
 * CompositeAction 是一个抽象类，用于复合多个 Action 实例，使其作为一个整体执行。
 * 它提供了默认的运行和状态管理实现，但要求子类实现具体的执行逻辑。
 *
 * @param actions 一个可变参数，表示要复合在一起的 Action 实例列表。
 */
abstract class CompositeAction(vararg actions: Action) : Action() {
    // 将传入的 Action 实例转换并存储为可变列表，以便在需要时可以动态更改 Action 列表。
    val actions = actions.toMutableList()

    /**
     * 在运行结束后执行的操作，用于处理状态变更的逻辑。
     * 此方法重写了父类的 afterRun 方法，以在状态无效或新状态不是正在运行时通知所有子 Action 状态变为无效。
     *
     * @param newStatus 运行后的新状态，用于决定是否需要通知子 Action 状态变更。
     */
    override fun afterRun(newStatus: ActionStatus) {
        // 当当前状态不是无效且新状态不是正在运行时，通知所有子 Action 它们的状态变为无效。
        if (status != ActionStatus.INVALID && newStatus != ActionStatus.RUNNING) {
            for (action in actions) {
                action.afterRun(ActionStatus.INVALID)
            }
        }
        // 调用父类的 afterRun 方法进行进一步处理。
        super.afterRun(newStatus)
    }
}
