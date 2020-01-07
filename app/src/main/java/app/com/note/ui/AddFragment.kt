package app.com.note.ui


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import app.com.note.R
import app.com.note.db.Note
import app.com.note.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.launch

class AddFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddFragmentArgs.fromBundle(it).note
            edittextTitle.setText(note?.title)
            edittextNote.setText(note?.note)
        }

        addNoteAction.setOnClickListener{view ->
            val noteTitle = edittextTitle.text.toString().trim()
            val noteBody = edittextNote.text.toString().trim()

            if (noteTitle.isEmpty()){
                edittextTitle.setError("Enter Title here!!")
                edittextTitle.requestFocus()
                return@setOnClickListener
            }
            if (noteBody.isEmpty()){
                edittextNote.setError("Enter Title here!!")
                edittextNote.requestFocus()
                return@setOnClickListener
            }

            launch{
                context?.let {
                    val mNote = Note(noteTitle, noteBody)
                    if (note == null){
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    }else{
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }

                    Navigation.findNavController(view).navigate(AddFragmentDirections.actionAddFragmentToHomeFragment())
                }
            }

        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("If you delete note you can't able to recover note.")
            setPositiveButton("Delete"){_,_ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddFragmentDirections.actionAddFragmentToHomeFragment()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("Cancel"){_,_ ->

            }
            setCancelable(false)
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot delete")
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

//    private fun saveNote(note: Note){
//        class SaveNote: AsyncTask<Void,Void,Void>(){
//            override fun doInBackground(vararg p0: Void?): Void? {
//                NoteDatabase(activity!!).getNoteDao().addNote(note)
//                return null
//            }
//
//            override fun onPostExecute(result: Void?) {
//                super.onPostExecute(result)
//                Toast.makeText(activity,"Note Saved", Toast.LENGTH_LONG).show()
//            }
//        }
//        SaveNote().execute()
//
//    }

}
