package com.gmail.jamesgodwillarkoh.lecturersview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.modules.core.PermissionListener
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import java.util.zip.Inflater


class MainActivity : AppCompatActivity(),CourseAdapter.OnItemClickListener {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    private lateinit var recyclerView: RecyclerView

    private lateinit var courseAdapter:CourseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val serverURL: URL
        serverURL = try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)

            // When using JaaS, set the obtained JWT here
            //.setToken("MyJWT")
            // Different features flags can be set
            //.setFeatureFlag("toolbox.enabled", false)
            //.setFeatureFlag("filmstrip.enabled", false)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        registerForBroadcastMessages()

        // Inflate the layout for this fragment
        recyclerView=findViewById(R.id.v_recycleView_home)



        courseAdapter= CourseAdapter(applicationContext)


        courseAdapter.setOnItemClickListener(this)
        recyclerView.adapter=courseAdapter

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun onItemClick(position: Int, intent: Intent) {

        startActivity(intent)
        Toast.makeText(applicationContext,"Lecturers view", Toast.LENGTH_LONG).show()
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }


        private fun onBroadcastReceived(intent: Intent?) {
            if (intent != null) {
                val event = BroadcastEvent(intent)
                when (event.getType()) {
                    BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                    BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
                }
            }
        }

    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
    }



}
