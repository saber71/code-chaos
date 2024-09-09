package heraclius.core.action

/**
 * 表示一个动作，所有动作都应该继承此类。
 * 动作类提供了一种统一的方式来执行不同的操作，并处理它们的状态。
 */
abstract class Action {
    // 动作的状态，默认初始化为INVALID，表示动作还未开始或已经结束。
    protected var status: ActionStatus = ActionStatus.INVALID

    /**
     * 执行动作的方法，每次动作执行时都会调用。
     * 该方法首先调用beforeRun进行执行前的操作，
     * 然后执行具体的动作逻辑run，
     * 最后调用afterRun处理执行后的逻辑，并返回当前动作的状态。
     *
     * @return 当前动作的状态。
     */
    fun tick(): ActionStatus {
        beforeRun()
        afterRun(run())
        return status
    }

    /**
     * 在执行具体的动作逻辑(run)之前调用的方法。
     * 该方法默认实现为空，子类可以根据需要重写此方法以执行一些初始化或预备操作。
     */
    protected open fun beforeRun() {}

    /**
     * 具体执行动作的逻辑。
     * 该方法需要由子类实现，是每个动作的核心逻辑所在。
     *
     * @return 动作执行后的状态。
     */
    protected abstract fun run(): ActionStatus

    /**
     * 在run方法执行后调用的方法，用于处理动作执行后的逻辑。
     * 默认实现是将动作的状态设置为run方法返回的状态。
     * 子类可以根据需要重写此方法以执行一些额外的处理操作。
     *
     * @param newStatus 动作的新状态，通常是由run方法返回的。
     */
    open fun afterRun(newStatus: ActionStatus) {
        status = newStatus
    }
}
