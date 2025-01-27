package com.ae.todo.components

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    var sharedPreferences: SharedPreferences? = null
    const val TASK_ID = "TaskId"
    const val SAVED_LANG_POS = "SavedLanguagePosition"
    const val SAVED_MODE_POS = "SavedModePosition"
    const val SAVED_LANG = "SavedLanguage"
    const val SAVED_MODE = "SavedMode"
    var myLang: String? = null
    var myTheme: Int? = null

    fun convertToDate(dateByLong: Long, isTime: Boolean): String {
        val formatPattern = if (isTime) "hh:mm a" else "dd/MM/yyyy"
        return SimpleDateFormat(formatPattern, Locale.getDefault()).format(Date(dateByLong))
    }

    fun convertToLong(dateByString: String, isTime: Boolean): Long {
        val formatPattern = if (isTime) "hh:mm a" else "dd/MM/yyyy"
        return SimpleDateFormat(formatPattern, Locale.getDefault()).parse(dateByString)?.time!!
    }

    fun showDatePicker(
        calendar: Calendar, context: Context, onDateSelected: (Long) -> Unit
    ) {
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    fun showTimePicker(
        calendar: Calendar, context: Context, onTimeSelected: (Long) -> Unit
    ) {
        val timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false,
        )
        timePicker.show()
    }

    fun setLanguage(activity: Activity, selectedLanguage: String) {
        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = activity.resources.configuration
        config.setLocale(locale)
        activity.resources.updateConfiguration(
            config, activity.resources.displayMetrics
        )
    }

    fun setMode(selectedTheme: Int) {
        AppCompatDelegate.setDefaultNightMode(selectedTheme)
    }

    fun initApp(activity: Activity) {
        myLang = sharedPreferences?.getString(SAVED_LANG, "en")
        myTheme = sharedPreferences?.getInt(SAVED_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        setLanguage(activity, myLang ?: "en")
        setMode(myTheme ?: AppCompatDelegate.MODE_NIGHT_NO)
    }

}