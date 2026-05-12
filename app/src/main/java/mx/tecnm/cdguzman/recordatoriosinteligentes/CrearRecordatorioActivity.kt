package mx.tecnm.cdguzman.recordatoriosinteligentes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tecnm.cdguzman.recordatoriosinteligentes.data.ReminderManager
import mx.tecnm.cdguzman.recordatoriosinteligentes.model.Recordatorio
import mx.tecnm.cdguzman.recordatoriosinteligentes.ui.theme.RecordatoriosInteligentesTheme
import androidx.compose.ui.platform.LocalContext

class CrearRecordatorioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosInteligentesTheme {
                CrearRecordatorioScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecordatorioScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val reminderManager = remember { ReminderManager(context) }
    
    var titulo by remember { mutableStateOf("") }
    var texto by remember { mutableStateOf("") }
    
    val calendar = remember { Calendar.getInstance() }
    var selectedTimestamp by remember { mutableLongStateOf(0L) }
    var fechaHoraLabel by remember { mutableStateOf("Seleccionar fecha y hora") }

    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun showDateTimePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        
                        selectedTimestamp = calendar.timeInMillis
                        fechaHoraLabel = sdf.format(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Recordatorio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para seleccionar fecha y hora
            Button(
                onClick = { showDateTimePicker() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = fechaHoraLabel)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    if (titulo.isNotBlank() && selectedTimestamp > 0) {
                        val nuevoRecordatorio = Recordatorio(
                            titulo = titulo,
                            texto = texto,
                            horaARecordar = selectedTimestamp
                        )
                        reminderManager.saveReminder(nuevoRecordatorio)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = titulo.isNotBlank() && selectedTimestamp > 0
            ) {
                Text("Guardar Recordatorio")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CrearRecordatorioPreview() {
    RecordatoriosInteligentesTheme {
        CrearRecordatorioScreen(onBack = {})
    }
}
