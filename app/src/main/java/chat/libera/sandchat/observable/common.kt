package chat.libera.sandchat.observable;

enum class ObservableChangeType {
    CHANGE,
    INSERT,
    REMOVE
}
open class ObservableChange(public val type: ObservableChangeType, public val start: Int, public val length: Int)
class ObservableChangeChange(index: Int) : ObservableChange(ObservableChangeType.CHANGE, index, 1)
class ObservableChangeInsert(start: Int, length: Int) : ObservableChange(ObservableChangeType.INSERT, start, length)
class ObservableChangeRemove(start: Int, length: Int) : ObservableChange(ObservableChangeType.REMOVE, start, length)
