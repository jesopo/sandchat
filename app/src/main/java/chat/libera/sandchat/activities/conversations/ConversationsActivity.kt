package chat.libera.sandchat.activities.conversations

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import chat.libera.sandchat.R
import chat.libera.sandchat.activities.conversation.ConversationActivity
import chat.libera.sandchat.irc.Conversation
import chat.libera.sandchat.irc.Space
import chat.libera.sandchat.observable.ObservableLinkedHashMap

class ConversationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        var toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar)

        var actionBar = this.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        var drawer = this.findViewById<DrawerLayout>(R.id.drawer)
        var toggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.show_spaces, R.string.hide_spaces)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val conversations = ObservableLinkedHashMap<String, Conversation>()
        conversations.changed.register {
            val hasChats = conversations.size > 0
            this.findViewById<View>(R.id.empty).visibility =
                if (hasChats) View.GONE else View.VISIBLE
            this.findViewById<View>(R.id.conversations).visibility =
                if (hasChats) View.VISIBLE else View.GONE
        }

        val recycler = this.findViewById<RecyclerView>(R.id.conversations)
        val adapter = ConversationsRecyclerAdapter(conversations).apply {
            setHasStableIds(true)
        }
        recycler.adapter = adapter

        adapter.selected.register { position: Int ->
            val intent = Intent(this, ConversationActivity::class.java)
            this.startActivity(intent)
        }

        conversations["test 1"] = Conversation("Jess 1")
        conversations["test 2"] = Conversation("Jess 2")
    }
}