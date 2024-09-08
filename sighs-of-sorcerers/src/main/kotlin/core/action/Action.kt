package heraclius.core.action

abstract class Action {
    protected var status: ActionStatus = ActionStatus.INVALID

    fun tick(): ActionStatus {
        beforeRun()
        stop(run())
        afterRun()
        return status
    }

    protected open fun beforeRun() {}

    protected open fun afterRun() {}

    protected abstract fun run(): ActionStatus

    open fun stop(newStatus: ActionStatus) {
        status = newStatus
    }
}
