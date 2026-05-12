package mx.tecnm.cdguzman.recordatoriosinteligentes.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mx.tecnm.cdguzman.recordatoriosinteligentes.service.ReminderService

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "Recordatorio"
        val texto = intent.getStringExtra("texto") ?: ""

        val serviceIntent = Intent(context, ReminderService::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("texto", texto)
        }
        
        // Iniciamos el servicio para manejar la notificación
        context.startService(serviceIntent)
    }
}
