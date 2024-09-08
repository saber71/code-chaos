package heraclius.core.action.decorators

import heraclius.core.action.Action

abstract class DecorateAction(protected val child: Action) : Action() {
}
