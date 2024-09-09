package heraclius.core.action

abstract class Action {
    protected var status: ActionStatus = ActionStatus.INVALID

    fun tick(): ActionStatus {
        beforeRun()
        afterRun(run())
        return status
    }

    protected open fun beforeRun() {}

    protected abstract fun run(): ActionStatus

    open fun afterRun(newStatus: ActionStatus) {
        status = newStatus
    }
}
