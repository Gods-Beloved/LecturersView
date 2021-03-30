package com.gmail.jamesgodwillarkoh.lecturersview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.parse.GetCallback
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlin.math.*

class StudentsAdapter(val context: Context?,val ParseClassName:String) : RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>(){




    private lateinit var mListener: OnItemClickListener




    interface  OnItemClickListener{
        fun onItemClick( position: Int,intent: Intent)
    } 


    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }





    class StudentsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val student: TextView =itemView.findViewById(R.id.v_student)

        val studentName: TextView =itemView.findViewById(R.id.v_student_name)

        val classAttended: TextView =itemView.findViewById(R.id.v_class_attended)

        val time: TextView =itemView.findViewById(R.id.v_time)

        val lastUpdate: TextView =itemView.findViewById(R.id.v_time_lastupadate)

        val loc: TextView =itemView.findViewById(R.id.v_loc)

        val location: TextView =itemView.findViewById(R.id.v_location)

        val sessions: TextView =itemView.findViewById(R.id.v_couse_sessions)









    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val v= LayoutInflater.from(context).inflate(R.layout.student_item,parent,false)





        return StudentsViewHolder(v)
    }

    override fun getItemCount(): Int {


        val query: ParseQuery<ParseObject> = ParseQuery.getQuery<ParseObject>(ParseClassName)






       return query.count()
    }
  //  6.6731847 -1.5667415

    private fun distance(lat1:Double,lng1:Double,lat2:Double=6.6732,lng2:Double=-1.5674):Double{
        val earthRadius = 3958.75 // in miles, change to 6371 for kilometer output

        val dLat = Math.toRadians(lat2-lat1)
        val dLng = Math.toRadians(lng2-lng1)

      val sindLat = sin(dLat / 2)
        val sindLng = sin(dLng / 2)

        val a = sindLat.pow(2.0) + sindLng.pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))

        val c = 2 * atan2(sqrt(a), sqrt(1-a))

        return earthRadius * c
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {

        val query: ParseQuery<ParseObject> = ParseQuery.getQuery<ParseObject>(ParseClassName)

        val newPosition=holder.adapterPosition
        query.findInBackground { objects, e ->
            if (e==null)
            {

                val studentLatitude=objects[position].getDouble("latitude")
                val studentsLongitude=objects[position].getDouble("longitude")


                if (distance(studentLatitude,studentsLongitude)>0.1){
                    holder.location.text="Outside College"
                }

                if (distance(studentLatitude,studentsLongitude)<0.1){
                    holder.location.text=context?.getString(R.string.college)

                    val name=objects[position].getString("name")
                    val session=objects[position].get("classAttended")
                    holder.classAttended.text=context?.getString(R.string.classes)
                    holder.loc.text="Location"
                    holder.studentName.text="Student Name"
                    holder.time.text="Time"
                    holder.lastUpdate.text=objects[position].updatedAt.toString()


                    holder.student.text= name
                    holder.sessions.text=session.toString()
                }

            }else{


                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
            }



        }



    }


}



