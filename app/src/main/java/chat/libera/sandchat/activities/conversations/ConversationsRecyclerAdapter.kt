package chat.libera.sandchat.activities.conversations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chat.libera.sandchat.EventEmitter
import chat.libera.sandchat.R
import chat.libera.sandchat.irc.Conversation
import chat.libera.sandchat.irc.Message
import chat.libera.sandchat.observable.ObservableChangeType
import chat.libera.sandchat.observable.ObservableLinkedHashMap

class ConversationViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var click: (() -> Int)? = null;
}
class ConversationsRecyclerAdapter(private val items: ObservableLinkedHashMap<String, Conversation>) : RecyclerView.Adapter<ConversationViewHolder>() {
    val selected = EventEmitter<Int>()

    init {
        this.items.changed.register { change ->
            when (change.type) {
                ObservableChangeType.CHANGE -> this.notifyItemChanged(change.start)
                ObservableChangeType.INSERT -> this.notifyItemRangeInserted(change.start, change.length)
                ObservableChangeType.REMOVE -> this.notifyItemRangeRemoved(change.start, change.length)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_conversation, parent, false)
        val holder = ConversationViewHolder(view)
        view.setOnClickListener {
            this.selected.call(holder.click?.invoke() ?: -1)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.click = {
            position
        }
        val item = this.items[this.items.keyAt(position)]
        holder.itemView.findViewById<TextView>(R.id.displayName).text = item.name
    }

    override fun getItemId(position: Int): Long {
        val id = this.items.keyAt(position)
        return id.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return this.items.size
    }
}
