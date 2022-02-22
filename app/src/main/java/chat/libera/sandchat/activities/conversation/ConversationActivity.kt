package chat.libera.sandchat.activities.conversation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chat.libera.sandchat.R
import chat.libera.sandchat.activities.main.fragments.conversation.ConversationRecyclerAdapter
import chat.libera.sandchat.irc.*
import chat.libera.sandchat.observable.ObservableLinkedHashMap
import java.util.*

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)

        val actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        val messages = ObservableLinkedHashMap<String, Message>();
        val recycler = this.findViewById<RecyclerView>(R.id.messages)
        recycler.adapter = ConversationRecyclerAdapter(messages).apply {
            setHasStableIds(false)
        }

        var pinMostRecent = true
        recycler.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisible = (recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                pinMostRecent = lastVisible == (messages.size-1)
            }
        })
        messages.changed.register {
            if (pinMostRecent) {
                recycler.scrollToPosition(messages.size-1)
            }
        }

        this.findViewById<EditText>(R.id.input).setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    if (v.text.isNotEmpty()) {
                        val sender = Sender("Jess Test", "jess", "~jess", "testhostname.")
                        messages[UUID.randomUUID().toString()] = Message(
                            sender,
                            MessageType.MESSAGE,
                            v.text.toString(),
                            Date()
                        )
                        v.text = ""
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}