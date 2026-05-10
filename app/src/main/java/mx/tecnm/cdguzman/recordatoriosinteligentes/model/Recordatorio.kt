package mx.tecnm.cdguzman.recordatoriosinteligentes.model

data class Recordatorio(
    val titulo: String,
    val texto: String,
    val horaCreada: Long = System.currentTimeMillis(),
    val horaARecordar: Long
)
