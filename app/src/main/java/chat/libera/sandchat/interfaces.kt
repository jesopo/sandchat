package chat.libera.sandchat

import chat.libera.sandchat.observable.ObservableLinkedHashMap
import java.util.*
import kotlin.collections.HashMap

enum class MessageType {
    MESSAGE,
    NOTICE,
    ACTION
}

class Sender(val displayName: String?, val nickname: String, val username: String, val hostname: String)

class Message(val sender: Sender, val type: MessageType, val body: String, val timestamp: Date, var received: Boolean = false)

// a PM or a channel
class Conversation(var name: String) {
    val messages = ObservableLinkedHashMap<String, Message>()
}

// a server
class Space(val id: Int, var name: String?) {
    val conversations = HashMap<String, Conversation>()
}
