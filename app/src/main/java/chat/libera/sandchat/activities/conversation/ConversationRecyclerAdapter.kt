package chat.libera.sandchat.activities.main.fragments.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chat.libera.sandchat.R
import chat.libera.sandchat.irc.Message
import chat.libera.sandchat.observable.ObservableChangeType
import chat.libera.sandchat.observable.ObservableLinkedHashMap

class MessageViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
class ConversationRecyclerAdapter(private val items: ObservableLinkedHashMap<String, Message>) : RecyclerView.Adapter<MessageViewHolder>() {

    init {
        this.items.changed.register { change ->
            when (change.type) {
                ObservableChangeType.CHANGE -> this.notifyItemChanged(change.start)
                ObservableChangeType.INSERT -> this.notifyItemRangeInserted(change.start, change.length)
                ObservableChangeType.REMOVE -> this.notifyItemRangeRemoved(change.start, change.length)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = this.items[this.items.keyAt(position)]
        holder.itemView.findViewById<TextView>(R.id.author).text = item.sender.displayName ?: item.sender.nickname
        holder.itemView.findViewById<TextView>(R.id.message).text = item.body
    }

    override fun getItemId(position: Int): Long {
        val id = this.items.keyAt(position)
        return id.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return this.items.size
    }
}
