package app.com.note.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.com.note.R
import app.com.note.db.Note
import kotlinx.android.synthetic.main.note_item_layout.view.*

class NotesAdapter(private val notes: List<Note>): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.tv_title.setText(notes[position].title)
        holder.itemView.tv_desc.setText(notes[position].note)

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddFragment()
            action.note = notes[position]
            Navigation.findNavController(it).navigate(action)
        }
    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}