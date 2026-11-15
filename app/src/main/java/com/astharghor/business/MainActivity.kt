package com.astharghor.business

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { CalculatorScreen() } }
    }
}

@Composable
fun CalculatorScreen() {
    var sale by remember { mutableStateOf("1250") }
    var costUsd by remember { mutableStateOf("5") }
    var rate by remember { mutableStateOf("120") }
    var expectedProfit by remember { mutableStateOf("300") }
    var delivered by remember { mutableStateOf(true) }

    val saleValue = sale.toDoubleOrNull() ?: 0.0
    val costValue = (costUsd.toDoubleOrNull() ?: 0.0) * (rate.toDoubleOrNull() ?: 0.0)
    val cod = saleValue * 0.01
    val courier = 115.0
    val expected = expectedProfit.toDoubleOrNull() ?: 0.0
    val actualProfit = if (delivered) saleValue - costValue - cod - courier else 0.0
    val lostProfit = if (delivered) 0.0 else courier + expected
    val netProfit = actualProfit - lostProfit
    val fmt = NumberFormat.getNumberInstance(Locale("en", "BD"))

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("আস্থার ঘর বিজনেস", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("অর্ডারের লাভ-লোকসান হিসাব", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(sale, { sale = it }, label = { Text("বিক্রয় মূল্য (টাকা)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(costUsd, { costUsd = it }, label = { Text("পণ্যের ক্রয়মূল্য (USD)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(rate, { rate = it }, label = { Text("ডলারের রেট (টাকা)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(expectedProfit, { expectedProfit = it }, label = { Text("প্রত্যাশিত লাভ (টাকা)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Text("অর্ডারের অবস্থা", fontWeight = FontWeight.SemiBold)
        Row {
            FilterChip(selected = delivered, onClick = { delivered = true }, label = { Text("ডেলিভারি হয়েছে") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = !delivered, onClick = { delivered = false }, label = { Text("ক্যানসেল / রিটার্ন") })
        }
        Spacer(Modifier.height(18.dp))
        ResultRow("প্রকৃত লাভ", fmt.format(actualProfit))
        ResultRow("হারানো লাভ", fmt.format(lostProfit))
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        ResultRow("নিট লাভ / লোকসান", fmt.format(netProfit), true)
        Spacer(Modifier.height(12.dp))
        Text("হিসাব: COD বিক্রয় মূল্যের ১%, কুরিয়ার চার্জ ১১৫ টাকা। ক্যানসেল/রিটার্ন হলে ১১৫ টাকা চার্জের সঙ্গে প্রত্যাশিত লাভও হারানো লাভ হিসেবে ধরা হয়েছে।", style = MaterialTheme.typography.bodySmall)
        Text("এটি প্রাথমিক ক্যালকুলেটর সংস্করণ। অর্ডার সংরক্ষণ, রিপোর্ট ও ব্যাকআপ এখনো যুক্ত হয়নি।", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ResultRow(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text("৳ $value", fontWeight = FontWeight.Bold)
    }
}
