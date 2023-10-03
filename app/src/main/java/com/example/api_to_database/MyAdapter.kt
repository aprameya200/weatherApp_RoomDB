package com.example.api_to_database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class MyAdapter(mainActivity: CoroutineScope, private val allForecast: List<WeatherInfo.Main>,private val Days: List<WeatherInfo.WeatherData>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.day.text = getDayOfWeek(Days[position].dt_txt)
        holder.dayMin.text = allForecast[position].temp_min.toString()
        holder.dayMax.text = allForecast[position].temp_max.toString()
        holder.weekday.text = convertToAmPmTime(Days[position].dt_txt)
    }

    fun getDayOfWeek(dateTimeStr: String): String {
        // Define the format of the input date and time string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the input string into a LocalDateTime object
        val dateTime = LocalDateTime.parse(dateTimeStr, formatter)

        // Get the day of the week as a string (e.g., "Monday")
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, java.util.Locale.getDefault())

        return dayOfWeek
    }

    fun convertToAmPmTime(dateTimeString: String): String {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val date = dateFormat.parse(dateTimeString)

            // Create a SimpleDateFormat for AM/PM time format
            val amPmTimeFormat = SimpleDateFormat("h a", Locale.US)
            return amPmTimeFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "" // Return an empty string or handle the error as needed
    }


    override fun getItemCount(): Int {
        return 14
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var day: TextView = itemView.findViewById(R.id.weekDay)
        var dayMin: TextView = itemView.findViewById(R.id.min)
        var dayMax: TextView = itemView.findViewById(R.id.max)
        var weekday: TextView = itemView.findViewById(R.id.time)

    }

}
