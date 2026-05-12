package mx.tecnm.cdguzman.recordatoriosinteligentes.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.tecnm.cdguzman.recordatoriosinteligentes.model.Recordatorio
import mx.tecnm.cdguzman.recordatoriosinteligentes.receiver.ReminderReceiver

class ReminderManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("recordatorios_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveReminder(recordatorio: Recordatorio) {
        val reminders = getReminders().toMutableList()
        reminders.add(recordatorio)
        val json = gson.toJson(reminders)
        sharedPreferences.edit().putString("lista_recordatorios", json).apply()
        
        scheduleReminder(recordatorio)
    }

    private fun scheduleReminder(recordatorio: Recordatorio) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("titulo", recordatorio.titulo)
            putExtra("texto", recordatorio.texto)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            recordatorio.horaCreada.toInt(), // ID único para el PendingIntent
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    recordatorio.horaARecordar,
                    pendingIntent
                )
            } else {
                // Si no puede programar alarmas exactas, usamos una inexacta como respaldo
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    recordatorio.horaARecordar,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                recordatorio.horaARecordar,
                pendingIntent
            )
        }
    }

    fun getReminders(): List<Recordatorio> {
        val json = sharedPreferences.getString("lista_recordatorios", null) ?: return emptyList()
        val type = object : TypeToken<List<Recordatorio>>() {}.type
        return gson.fromJson(json, type)
    }
}
