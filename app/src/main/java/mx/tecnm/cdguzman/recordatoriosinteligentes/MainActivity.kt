package mx.tecnm.cdguzman.recordatoriosinteligentes

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import mx.tecnm.cdguzman.recordatoriosinteligentes.data.ReminderManager
import mx.tecnm.cdguzman.recordatoriosinteligentes.model.Recordatorio
import mx.tecnm.cdguzman.recordatoriosinteligentes.ui.theme.RecordatoriosInteligentesTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosInteligentesTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val reminderManager = remember { ReminderManager(context) }
    var listaRecordatorios by remember { mutableStateOf(emptyList<Recordatorio>()) }

    // Refrescar la lista cada vez que la actividad vuelve al primer plano (ON_RESUME)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                listaRecordatorios = reminderManager.getReminders()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ReminderHeader() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, CrearRecordatorioActivity::class.java)
                    context.startActivity(intent)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {

                Text("+", fontSize = 24.sp, color = androidx.compose.ui.graphics.Color.White)
            }
        }
    )
    { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ReminderList(listaRecordatorios)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderHeader() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Recordatorios Inteligentes",
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
fun ReminderList(recordatorios: List<Recordatorio>) {
    if (recordatorios.isEmpty()) {
        Text(
            text = "No hay recordatorios aún.",
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(recordatorios) { recordatorio ->
                ReminderItem(recordatorio)
            }
        }
    }
}

@Composable
fun ReminderItem(recordatorio: Recordatorio) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val fechaFormateada = sdf.format(Date(recordatorio.horaARecordar))

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = recordatorio.titulo,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recordatorio.texto,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Recordar el: $fechaFormateada",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}

@Composable
fun ReminderFooter(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Añadir Nuevo Recordatorio")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    RecordatoriosInteligentesTheme {
        MainScreen()
    }
}

