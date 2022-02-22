package chat.libera.sandchat.observable

import chat.libera.sandchat.EventEmitter
import java.util.*
import kotlin.NoSuchElementException

class ObservableLinkedHashMap<K, V> {
    val changed: EventEmitter<ObservableChange> = EventEmitter()
    private val _map = hashMapOf<K, V>()
    private val _order = mutableListOf<K>()

    val size: Int get() = this._order.size
    val keys: Set<K> get() = this._map.keys

    operator fun get(key: K): V {
        val value = this._map[key]
        if (value != null) {
            return value
        } else {
            throw NoSuchElementException()
        }
    }

    operator fun set(key: K, value: V)  {
        this._map[key] = value
        this._order.add(key)
        this.changed.call(ObservableChangeInsert(this._order.size-1, 1))
    }

    fun remove(key: K): V? {
        return this._map.remove(key)
    }
    fun remoteAt(index: Int): V? {
        val key = this._order[index]
        return this._map.remove(key)
    }

    fun rename(key1: K, key2: K): Boolean {
        val value = this._map[key1]
        return if (value != null) {
            val index = this._order.indexOf(key1)

            this._map.remove(key1)
            this._order.removeAt(index)

            this._map[key2] = value
            this._order.add(index, key2)

            this.changed.call(ObservableChangeChange(index))

            true
        } else {
            false
        }
    }

    fun keyAt(index: Int): K {
        return this._order[index]
    }
}