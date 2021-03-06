package com.gmail.jamesgodwillarkoh.lecturersview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.parse.FindCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import java.util.*

class CourseAdapter(val context: Context?): RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {



   private lateinit var mListener: OnItemClickListener






    interface  OnItemClickListener{
        fun onItemClick( position: Int,intent: Intent)
    }


    fun setOnItemClickListener(listener: OnItemClickListener){
mListener=listener
    }


    class CourseViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {





        val query: ParseQuery<ParseObject> =ParseQuery.getQuery<ParseObject>("Course")






       val courseLecturer:TextView=itemView.findViewById(R.id.v_couse_lecturer)

        val courseCode:TextView=itemView.findViewById(R.id.v_coursecode)

        val shim:ShimmerFrameLayout=itemView.findViewById(R.id.v_shimmer_load2)

        val videoCall:ImageView=itemView.findViewById(R.id.v_start_video)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {


        val v=LayoutInflater.from(context).inflate(R.layout.course_item,parent,false)





        return CourseViewHolder(v)
    }





    override fun getItemCount(): Int {
        val size=ParseQuery.getQuery<ParseObject>("Course")


        return size.count()
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
//       holder.courseCode.text= arrayCourse.getJSONArray(position).toString()
//        holder.courseLecturer.text= arrayLecturers.getJSONArray(position).toString()

      //  val courseCode=holder.courseCode.text.trim().toString()

    holder.query.findInBackground { objects, e ->
        if (e==null)
        {
            holder.shim.hideShimmer()
            val name=objects[position].getString("lecturer")
            val code=objects[position].getString("code")
            holder.courseCode.text= code
            holder.courseLecturer.text=name
        }

        else{
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            holder.shim.hideShimmer()



        }

    }

        holder.videoCall.setOnClickListener {
            Toast.makeText(context,"Video Started",Toast.LENGTH_LONG).show()
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom("csm")
                // Settings for audio and video
                //.setAudioMuted(true)
                //.setVideoMuted(true)
                .build()
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(context, options)
        }



        holder.itemView.setOnClickListener {
            val position2=holder.adapterPosition

           val value=holder.courseCode.text.trim().toString()

            val intent=Intent(context, StudentsDetails::class.java)

            intent.putExtra("courseCode",value)




            mListener.onItemClick(position2,intent)
        }
//



    }
}