package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove Item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter taht our data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        // 1. Let's detect when the user calls on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //Code in here is going to execute when the user clicks on button
//            Log.i("Caren", "User Clicked on Button")
//        }

        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        //Set up the button and input field
        //Get a reference to the button
        //Set an on click listener to it
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. grab the text user has inputted
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()

        }

    }
    //Save the data that the user has inputted
    // Get the file we need
    fun getDataFile(): File{
        return File(filesDir, "data.txt")
    }

    //saving data by writing and reading from a file

    //Load the items by reading every line int he data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
//Save items by writing them into our data file
    fun saveItems(){
    try{
        FileUtils.writeLines(getDataFile(), listOfTasks)

    }catch(ioException: IOException){
        ioException.printStackTrace()
    }

    }
}