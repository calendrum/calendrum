package io.github.calendrum.examples.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val feed = assets.open("sample.ics").use { it.readBytes() }
        val items = Schedule.from(
            feed,
            Instant.parse("2026-09-01T00:00:00Z"),
            Instant.parse("2026-12-01T00:00:00Z"),
            TimeZone.of("Europe/Berlin"),
        )
        setContent {
            MaterialTheme {
                Scaffold { padding -> ScheduleList(items, Modifier.padding(padding)) }
            }
        }
    }
}

@Composable
fun ScheduleList(items: List<ScheduleItem>, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize()) {
        items(items) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = {
                    Column {
                        Text("${item.day} ${item.time}")
                        if (item.location.isNotEmpty()) Text(item.location)
                    }
                },
            )
        }
    }
}
