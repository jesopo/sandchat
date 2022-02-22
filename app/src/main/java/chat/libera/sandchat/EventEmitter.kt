package chat.libera.sandchat

class EventHook(val remove: () -> Unit)
class EventEmitter<E> {
    private val listeners: MutableSet<(event: E) -> Unit> = mutableSetOf()

    fun register(listener: (event: E) -> Unit): EventHook {
        this.listeners.add(listener)
        return EventHook {
            this.listeners.remove(listener)
        }
    }
    fun unregister(listener: (event: E) -> Unit) {
        this.listeners.remove(listener)
    }

    fun call(value: E) {
        for (listener in this.listeners) {
            listener(value)
        }
    }
}