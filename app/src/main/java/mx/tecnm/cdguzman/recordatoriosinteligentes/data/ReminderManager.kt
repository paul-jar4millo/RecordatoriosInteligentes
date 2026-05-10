package mx.tecnm.cdguzman.recordatoriosinteligentes.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.tecnm.cdguzman.recordatoriosinteligentes.model.Recordatorio

class ReminderManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("recordatorios_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveReminder(recordatorio: Recordatorio) {
        val reminders = getReminders().toMutableList()
        reminders.add(recordatorio)
        val json = gson.toJson(reminders)
        sharedPreferences.edit().putString("lista_recordatorios", json).apply()
    }

    fun getReminders(): List<Recordatorio> {
        val json = sharedPreferences.getString("lista_recordatorios", null) ?: return emptyList()
        val type = object : TypeToken<List<Recordatorio>>() {}.type
        return gson.fromJson(json, type)
    }
}
