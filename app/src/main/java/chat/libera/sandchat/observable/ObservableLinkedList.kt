package chat.libera.sandchat.observable

import chat.libera.sandchat.EventEmitter
import java.util.*
import java.util.function.Predicate

class ObservableLinkedList<E> : LinkedList<E>()  {
    val changed: EventEmitter<ObservableChange> = EventEmitter()

    override fun add(element: E): Boolean {
        val ret = super.add(element)
        this.changed.call(ObservableChangeInsert(super.size-1, 1));
        return ret
    }

    override fun add(index: Int, element: E) {
        val ret = super.add(index, element)
        this.changed.call(ObservableChangeInsert(index, 1));
        return ret
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val ret = super.addAll(elements)
        this.changed.call(ObservableChangeInsert(super.size-elements.size, elements.size));
        return ret
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val ret = super.addAll(index, elements)
        this.changed.call(ObservableChangeInsert(index, elements.size));
        return ret
    }

    override fun remove(element: E): Boolean {
        val ret = super.remove(element)
        this.changed.call(ObservableChangeRemove(super.size-1, 1));
        return ret
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var found = false
        for (item in elements) {
            val index = super.indexOf(item)
            if (index != -1) {
                super.removeAt(index)
                this.changed.call(ObservableChangeRemove(index, 1))
                found = true
            }
        }
        return found
    }

    override fun removeAt(index: Int): E {
        val ret = super.removeAt(index)
        this.changed.call(ObservableChangeRemove(index, 1))
        return ret
    }

    override fun removeIf(filter: Predicate<in E>): Boolean {
        var found = false
        for (item in this) {
            val index = super.indexOf(item)
            if (filter.test(item)) {
                super.removeAt(index)
                this.changed.call(ObservableChangeRemove(index, 1))
                found = true
            }
        }
        return found
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
        this.changed.call(ObservableChangeRemove(fromIndex, toIndex-fromIndex))
    }
}