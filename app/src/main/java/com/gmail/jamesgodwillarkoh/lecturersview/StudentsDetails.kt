package com.gmail.jamesgodwillarkoh.lecturersview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StudentsDetails : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView

    private lateinit var studentsAdapter: StudentsAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                finish();
                startActivity(getIntent());
                true
            }
            R.id.help -> {
                Toast.makeText(this,"Help",Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_students_details)


        val extras = intent.extras

        if (extras != null) {
            recyclerView=findViewById(R.id.v_recycleView_student)
           var value = extras.getString("courseCode").toString()

            value = value.replace("\\s".toRegex(), "")


            studentsAdapter= StudentsAdapter(applicationContext,value)

            

            recyclerView.adapter=studentsAdapter

        }


    }
}