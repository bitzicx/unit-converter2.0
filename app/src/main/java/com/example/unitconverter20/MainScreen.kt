package com.example.unitconverter20

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainViewModel : ViewModel() {
    var fromSymbol by mutableStateOf("C")
    var toSymbol by mutableStateOf("F")
    var areaSymbol by mutableStateOf("Temperature")
    var fromValue by mutableStateOf("0")

    fun updateFromSymbol(newSymbol: String) {
        fromSymbol = newSymbol
    }
    fun updateToSymbol(newSymbol: String) {
        toSymbol = newSymbol
    }

    fun updateFromValue(newValue: String) {
        fromValue = newValue
    }

    fun updateAreaSymbol(newSymbol: String) {
        areaSymbol = newSymbol

        // Reset from/to symbols to defaults of selected area
        when (newSymbol) {
            "Temperature" -> {
                fromSymbol = "C"
                toSymbol = "F"
            }
            "Length" -> {
                fromSymbol = "M"
                toSymbol = "KM"
            }
            "Speed" -> {
                fromSymbol = "m/s"
                toSymbol = "km/h"
            }
            "Digital Storage" -> {
                fromSymbol = "b"
                toSymbol = "KB"
            }
        }
    }
}

@Composable
fun MainScreen() {

    val unitMap =
        mapOf(
            "Temperature" to
                    Pair(listOf("Celsius", "Fahrenheit", "Kelvin"), listOf("C", "F", "K")),
            "Length" to
                    Pair(
                        listOf("Meter", "Centi Meter", "Kilo Meter", "Foot", "Inch"),
                        listOf("M", "CM", "KM", "Ft", "In")
                    ),
            "Speed" to
                    Pair(
                        listOf(
                            "Meter per second",
                            "Kilometer per hour",
                            "Miles per hour",
                            "Knot"
                        ),
                        listOf("m/s", "km/h", "mph", "kn")
                    ),
            "Digital Storage" to
                    Pair(
                        listOf(
                            "Bit",
                            "Byte",
                            "Kilobyte",
                            "Megabyte",
                            "Gigabyte",
                            "Terabyte"
                        ),
                        listOf("b", "B", "KB", "MB", "GB", "TB")
                    )
        )

    val viewModel: MainViewModel = viewModel()

    val area = listOf("Temperature", "Digital Storage", "Speed", "Length")
    val areaSymbol = listOf("Temperature", "Digital Storage", "Speed", "Length")

    val selectedArea = viewModel.areaSymbol
    val units = unitMap[selectedArea] ?: Pair(emptyList(), emptyList())
    val unitNames = units.first
    val unitSymbols = units.second
    val fromSymbol = viewModel.fromSymbol
    val toSymbol = viewModel.toSymbol

    Column(modifier = Modifier.fillMaxSize()) {
        Title()

        Spacer(modifier = Modifier.height(10.dp))

        CustomDropDownMenu(area, areaSymbol, selectedArea, 20.sp, viewModel::updateAreaSymbol)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                UnitBox {
                    CustomDropDownMenu(
                        unitNames,
                        unitSymbols,
                        fromSymbol,
                        30.sp,
                        viewModel::updateFromSymbol
                    )
                }
            }
            Box(modifier = Modifier.padding(5.dp).weight(2.3f)) { DigitBox() { NumericEntryBox() } }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(5.dp).weight(1f)) {
                UnitBox() {
                    CustomDropDownMenu(
                        unitNames,
                        unitSymbols,
                        toSymbol,
                        30.sp,
                        viewModel::updateToSymbol
                    )
                }
            }
            Box(modifier = Modifier.padding(5.dp).weight(2.3f)) { DigitBox() { ResultLabel() } }
        }
    }
}

@Composable
fun Title() {
    Text(
        "Unit Converter",
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun UnitBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier =
            Modifier.height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray)
                .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Unit", fontSize = 10.sp, modifier = Modifier.align(Alignment.TopStart))
        content()
    }
}

@Composable
fun CustomDropDownMenu(
    options: List<String>,
    symbols: List<String>,
    selectedOption: String,
    textSize: TextUnit,
    updateValue: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box {
            Text(
                text = selectedOption,
                modifier = Modifier.clickable { expanded = true },
                fontSize = textSize,
                fontWeight = FontWeight.Bold
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.indices.forEach { index ->
                    DropdownMenuItem(
                        text = { Text(options[index]) },
                        onClick = {
                            expanded = false
                            updateValue(symbols[index])
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DigitBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}) {
    Box(
        modifier =
            modifier.padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
                .height(100.dp)
                .width(260.dp)
                .background(Color.DarkGray)
    ) { content() }
}

@Composable
fun NumericEntryBox() {
    val viewModel: MainViewModel = viewModel()
    val fromValue = viewModel.fromValue

    TextField(
        value = fromValue,
        onValueChange = {
            if (valid(it)) {
                viewModel.updateFromValue(it)
            }
        },
        modifier = Modifier.fillMaxSize().background(Color.Transparent),
        singleLine = true,
        textStyle =
            TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            ),
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun ResultLabel() {
    val viewModel: MainViewModel = viewModel()
    val fromValue = viewModel.fromValue
    val fromSymbol = viewModel.fromSymbol
    val toSymbol = viewModel.toSymbol

    val toValue =
        when (Pair(fromSymbol, toSymbol)) {
            // Temperature conversions
            Pair("C", "C") -> celsiusToCelsius(fromValue)
            Pair("F", "F") -> fahrenheitToFahrenheit(fromValue)
            Pair("K", "K") -> kelvinToKelvin(fromValue)
            Pair("F", "K") -> fahrenheitToKelvin(fromValue)
            Pair("F", "C") -> fahrenheitToCelsius(fromValue)
            Pair("C", "F") -> celsiusToFahrenheit(fromValue)
            Pair("C", "K") -> celsiusToKelvin(fromValue)
            Pair("K", "C") -> kelvinToCelsius(fromValue)
            Pair("K", "F") -> kelvinToFahrenheit(fromValue)

            // Length conversions
            Pair("M", "M") -> meterToMeter(fromValue)
            Pair("M", "CM") -> meterToCentimeter(fromValue)
            Pair("M", "KM") -> meterToKilometer(fromValue)
            Pair("M", "Ft") -> meterToFoot(fromValue)
            Pair("M", "In") -> meterToInch(fromValue)
            Pair("CM", "M") -> centimeterToMeter(fromValue)
            Pair("CM", "CM") -> centimeterToCentimeter(fromValue)
            Pair("CM", "KM") -> centimeterToKilometer(fromValue)
            Pair("CM", "Ft") -> centimeterToFoot(fromValue)
            Pair("CM", "In") -> centimeterToInch(fromValue)
            Pair("KM", "M") -> kilometerToMeter(fromValue)
            Pair("KM", "CM") -> kilometerToCentimeter(fromValue)
            Pair("KM", "KM") -> kilometerToKilometer(fromValue)
            Pair("KM", "Ft") -> kilometerToFoot(fromValue)
            Pair("KM", "In") -> kilometerToInch(fromValue)
            Pair("Ft", "M") -> footToMeter(fromValue)
            Pair("Ft", "CM") -> footToCentimeter(fromValue)
            Pair("Ft", "KM") -> footToKilometer(fromValue)
            Pair("Ft", "Ft") -> footToFoot(fromValue)
            Pair("Ft", "In") -> footToInch(fromValue)
            Pair("In", "M") -> inchToMeter(fromValue)
            Pair("In", "CM") -> inchToCentimeter(fromValue)
            Pair("In", "KM") -> inchToKilometer(fromValue)
            Pair("In", "Ft") -> inchToFoot(fromValue)
            Pair("In", "In") -> inchToInch(fromValue)

            // Speed conversions
            Pair("m/s", "m/s") -> meterPerSecondToMeterPerSecond(fromValue)
            Pair("m/s", "km/h") -> meterPerSecondToKilometerPerHour(fromValue)
            Pair("m/s", "mph") -> meterPerSecondToMilesPerHour(fromValue)
            Pair("m/s", "kn") -> meterPerSecondToKnot(fromValue)
            Pair("km/h", "m/s") -> kilometerPerHourToMeterPerSecond(fromValue)
            Pair("km/h", "km/h") -> kilometerPerHourToKilometerPerHour(fromValue)
            Pair("km/h", "mph") -> kilometerPerHourToMilesPerHour(fromValue)
            Pair("km/h", "kn") -> kilometerPerHourToKnot(fromValue)
            Pair("mph", "m/s") -> milesPerHourToMeterPerSecond(fromValue)
            Pair("mph", "km/h") -> milesPerHourToKilometerPerHour(fromValue)
            Pair("mph", "mph") -> milesPerHourToMilesPerHour(fromValue)
            Pair("mph", "kn") -> milesPerHourToKnot(fromValue)
            Pair("kn", "m/s") -> knotToMeterPerSecond(fromValue)
            Pair("kn", "km/h") -> knotToKilometerPerHour(fromValue)
            Pair("kn", "mph") -> knotToMilesPerHour(fromValue)
            Pair("kn", "kn") -> knotToKnot(fromValue)

            // Digital Storage conversions
            Pair("b", "b") -> bitToBit(fromValue)
            Pair("b", "B") -> bitToByte(fromValue)
            Pair("b", "KB") -> bitToKilobyte(fromValue)
            Pair("b", "MB") -> bitToMegabyte(fromValue)
            Pair("b", "GB") -> bitToGigabyte(fromValue)
            Pair("b", "TB") -> bitToTerabyte(fromValue)
            Pair("B", "b") -> byteToBit(fromValue)
            Pair("B", "B") -> byteToByte(fromValue)
            Pair("B", "KB") -> byteToKilobyte(fromValue)
            Pair("B", "MB") -> byteToMegabyte(fromValue)
            Pair("B", "GB") -> byteToGigabyte(fromValue)
            Pair("B", "TB") -> byteToTerabyte(fromValue)
            Pair("KB", "b") -> kilobyteToBit(fromValue)
            Pair("KB", "B") -> kilobyteToByte(fromValue)
            Pair("KB", "KB") -> kilobyteToKilobyte(fromValue)
            Pair("KB", "MB") -> kilobyteToMegabyte(fromValue)
            Pair("KB", "GB") -> kilobyteToGigabyte(fromValue)
            Pair("KB", "TB") -> kilobyteToTerabyte(fromValue)
            Pair("MB", "b") -> megabyteToBit(fromValue)
            Pair("MB", "B") -> megabyteToByte(fromValue)
            Pair("MB", "KB") -> megabyteToKilobyte(fromValue)
            Pair("MB", "MB") -> megabyteToMegabyte(fromValue)
            Pair("MB", "GB") -> megabyteToGigabyte(fromValue)
            Pair("MB", "TB") -> megabyteToTerabyte(fromValue)
            Pair("GB", "b") -> gigabyteToBit(fromValue)
            Pair("GB", "B") -> gigabyteToByte(fromValue)
            Pair("GB", "KB") -> gigabyteToKilobyte(fromValue)
            Pair("GB", "MB") -> gigabyteToMegabyte(fromValue)
            Pair("GB", "GB") -> gigabyteToGigabyte(fromValue)
            Pair("GB", "TB") -> gigabyteToTerabyte(fromValue)
            Pair("TB", "b") -> terabyteToBit(fromValue)
            Pair("TB", "B") -> terabyteToByte(fromValue)
            Pair("TB", "KB") -> terabyteToKilobyte(fromValue)
            Pair("TB", "MB") -> terabyteToMegabyte(fromValue)
            Pair("TB", "GB") -> terabyteToGigabyte(fromValue)
            Pair("TB", "TB") -> terabyteToTerabyte(fromValue)
            else -> ""
        }

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp), contentAlignment = Center) {
        Text(
            text = toValue,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


fun valid(str: String): Boolean {
    return str.length < 8 && str.count { it == '.' } <= 1 && str.all { it.isDigit() || it == '.' }
}

@SuppressLint("DefaultLocale")
fun formatResult(value: Double): String {
    return String.format("%.3f", value).trimEnd('0').trimEnd('.')
}

fun celsiusToFahrenheit(celsius: String): String {
    if (celsius.isEmpty()) return ""
    val result = celsius.toDouble() * 9 / 5 + 32
    return formatResult(result)
}

fun celsiusToCelsius(celsius: String): String = celsius

fun celsiusToKelvin(celsius: String): String {
    if (celsius.isEmpty()) return ""
    val kelvin = celsius.toDouble() + 273.15
    return formatResult(kelvin)
}

fun fahrenheitToCelsius(fahrenheit: String): String {
    if (fahrenheit.isEmpty()) return ""
    val celsius = (fahrenheit.toDouble() - 32) * 5 / 9
    return formatResult(celsius)
}

fun fahrenheitToFahrenheit(fahrenheit: String): String = fahrenheit

fun kelvinToKelvin(kelvin: String): String = kelvin

fun fahrenheitToKelvin(fahrenheit: String): String {
    val celsius = fahrenheitToCelsius(fahrenheit)
    return if (celsius.isEmpty()) "" else celsiusToKelvin(celsius)
}

fun kelvinToCelsius(kelvin: String): String {
    if (kelvin.isEmpty()) return ""
    val celsius = kelvin.toDouble() - 273.15
    return formatResult(celsius)
}

fun kelvinToFahrenheit(kelvin: String): String {
    if (kelvin.isEmpty()) return ""
    val fahrenheit = (kelvin.toDouble() - 273.15) * 9 / 5 + 32
    return formatResult(fahrenheit)
}

// Meter conversions
fun meterToMeter(meter: String): String = meter

fun meterToCentimeter(meter: String): String {
    if (meter.isEmpty()) return ""
    val result = meter.toDouble() * 100
    return formatResult(result)
}

fun meterToKilometer(meter: String): String {
    if (meter.isEmpty()) return ""
    val result = meter.toDouble() / 1000
    return formatResult(result)
}

fun meterToFoot(meter: String): String {
    if (meter.isEmpty()) return ""
    val result = meter.toDouble() * 3.28084
    return formatResult(result)
}

fun meterToInch(meter: String): String {
    if (meter.isEmpty()) return ""
    val result = meter.toDouble() * 39.3701
    return formatResult(result)
}

// Centimeter conversions
fun centimeterToMeter(centimeter: String): String {
    if (centimeter.isEmpty()) return ""
    val result = centimeter.toDouble() / 100
    return formatResult(result)
}

fun centimeterToCentimeter(centimeter: String): String = centimeter

fun centimeterToKilometer(centimeter: String): String {
    if (centimeter.isEmpty()) return ""
    val result = centimeter.toDouble() / 100000
    return formatResult(result)
}

fun centimeterToFoot(centimeter: String): String {
    if (centimeter.isEmpty()) return ""
    val result = centimeter.toDouble() * 0.0328084
    return formatResult(result)
}

fun centimeterToInch(centimeter: String): String {
    if (centimeter.isEmpty()) return ""
    val result = centimeter.toDouble() * 0.393701
    return formatResult(result)
}

// Kilometer conversions
fun kilometerToMeter(kilometer: String): String {
    if (kilometer.isEmpty()) return ""
    val result = kilometer.toDouble() * 1000
    return formatResult(result)
}

fun kilometerToCentimeter(kilometer: String): String {
    if (kilometer.isEmpty()) return ""
    val result = kilometer.toDouble() * 100000
    return formatResult(result)
}

fun kilometerToKilometer(kilometer: String): String = kilometer

fun kilometerToFoot(kilometer: String): String {
    if (kilometer.isEmpty()) return ""
    val result = kilometer.toDouble() * 3280.84
    return formatResult(result)
}

fun kilometerToInch(kilometer: String): String {
    if (kilometer.isEmpty()) return ""
    val result = kilometer.toDouble() * 39370.1
    return formatResult(result)
}

// Foot conversions
fun footToMeter(foot: String): String {
    if (foot.isEmpty()) return ""
    val result = foot.toDouble() / 3.28084
    return formatResult(result)
}

fun footToCentimeter(foot: String): String {
    if (foot.isEmpty()) return ""
    val result = foot.toDouble() / 0.0328084
    return formatResult(result)
}

fun footToKilometer(foot: String): String {
    if (foot.isEmpty()) return ""
    val result = foot.toDouble() / 3280.84
    return formatResult(result)
}

fun footToFoot(foot: String): String = foot

fun footToInch(foot: String): String {
    if (foot.isEmpty()) return ""
    val result = foot.toDouble() * 12
    return formatResult(result)
}

// Inch conversions
fun inchToMeter(inch: String): String {
    if (inch.isEmpty()) return ""
    val result = inch.toDouble() / 39.3701
    return formatResult(result)
}

fun inchToCentimeter(inch: String): String {
    if (inch.isEmpty()) return ""
    val result = inch.toDouble() / 0.393701
    return formatResult(result)
}

fun inchToKilometer(inch: String): String {
    if (inch.isEmpty()) return ""
    val result = inch.toDouble() / 39370.1
    return formatResult(result)
}

fun inchToFoot(inch: String): String {
    if (inch.isEmpty()) return ""
    val result = inch.toDouble() / 12
    return formatResult(result)
}

fun inchToInch(inch: String): String = inch

// Meter per second conversions
fun meterPerSecondToMeterPerSecond(mps: String): String = mps

fun meterPerSecondToKilometerPerHour(mps: String): String {
    if (mps.isEmpty()) return ""
    val result = mps.toDouble() * 3.6
    return formatResult(result)
}

fun meterPerSecondToMilesPerHour(mps: String): String {
    if (mps.isEmpty()) return ""
    val result = mps.toDouble() * 2.23694
    return formatResult(result)
}

fun meterPerSecondToKnot(mps: String): String {
    if (mps.isEmpty()) return ""
    val result = mps.toDouble() * 1.94384
    return formatResult(result)
}

// Kilometer per hour conversions
fun kilometerPerHourToMeterPerSecond(kph: String): String {
    if (kph.isEmpty()) return ""
    val result = kph.toDouble() / 3.6
    return formatResult(result)
}

fun kilometerPerHourToKilometerPerHour(kph: String): String = kph

fun kilometerPerHourToMilesPerHour(kph: String): String {
    if (kph.isEmpty()) return ""
    val result = kph.toDouble() / 1.60934
    return formatResult(result)
}

fun kilometerPerHourToKnot(kph: String): String {
    if (kph.isEmpty()) return ""
    val result = kph.toDouble() / 1.852
    return formatResult(result)
}

// Miles per hour conversions
fun milesPerHourToMeterPerSecond(mph: String): String {
    if (mph.isEmpty()) return ""
    val result = mph.toDouble() / 2.23694
    return formatResult(result)
}

fun milesPerHourToKilometerPerHour(mph: String): String {
    if (mph.isEmpty()) return ""
    val result = mph.toDouble() * 1.60934
    return formatResult(result)
}

fun milesPerHourToMilesPerHour(mph: String): String = mph

fun milesPerHourToKnot(mph: String): String {
    if (mph.isEmpty()) return ""
    val result = mph.toDouble() / 1.15078
    return formatResult(result)
}

// Knot conversions
fun knotToMeterPerSecond(knot: String): String {
    if (knot.isEmpty()) return ""
    val result = knot.toDouble() / 1.94384
    return formatResult(result)
}

fun knotToKilometerPerHour(knot: String): String {
    if (knot.isEmpty()) return ""
    val result = knot.toDouble() * 1.852
    return formatResult(result)
}

fun knotToMilesPerHour(knot: String): String {
    if (knot.isEmpty()) return ""
    val result = knot.toDouble() * 1.15078
    return formatResult(result)
}

fun knotToKnot(knot: String): String = knot

// Bit conversions
fun bitToBit(bit: String): String = bit

fun bitToByte(bit: String): String {
    if (bit.isEmpty()) return ""
    val result = bit.toDouble() / 8
    return formatResult(result)
}

fun bitToKilobyte(bit: String): String {
    if (bit.isEmpty()) return ""
    val result = bit.toDouble() / (8 * 1024)
    return formatResult(result)
}

fun bitToMegabyte(bit: String): String {
    if (bit.isEmpty()) return ""
    val result = bit.toDouble() / (8 * 1024 * 1024)
    return formatResult(result)
}

fun bitToGigabyte(bit: String): String {
    if (bit.isEmpty()) return ""
    val result = bit.toDouble() / (8 * 1024 * 1024 * 1024)
    return formatResult(result)
}

fun bitToTerabyte(bit: String): String {
    if (bit.isEmpty()) return ""
    val result = bit.toDouble() / (8.0 * 1024 * 1024 * 1024 * 1024)
    return formatResult(result)
}

// Byte conversions
fun byteToBit(byte: String): String {
    if (byte.isEmpty()) return ""
    val result = byte.toDouble() * 8
    return formatResult(result)
}

fun byteToByte(byte: String): String = byte

fun byteToKilobyte(byte: String): String {
    if (byte.isEmpty()) return ""
    val result = byte.toDouble() / 1024
    return formatResult(result)
}

fun byteToMegabyte(byte: String): String {
    if (byte.isEmpty()) return ""
    val result = byte.toDouble() / (1024 * 1024)
    return formatResult(result)
}

fun byteToGigabyte(byte: String): String {
    if (byte.isEmpty()) return ""
    val result = byte.toDouble() / (1024 * 1024 * 1024)
    return formatResult(result)
}

fun byteToTerabyte(byte: String): String {
    if (byte.isEmpty()) return ""
    val result = byte.toDouble() / (1024.0 * 1024 * 1024 * 1024)
    return formatResult(result)
}

// Kilobyte conversions
fun kilobyteToBit(kilobyte: String): String {
    if (kilobyte.isEmpty()) return ""
    val result = kilobyte.toDouble() * 8 * 1024
    return formatResult(result)
}

fun kilobyteToByte(kilobyte: String): String {
    if (kilobyte.isEmpty()) return ""
    val result = kilobyte.toDouble() * 1024
    return formatResult(result)
}

fun kilobyteToKilobyte(kilobyte: String): String = kilobyte

fun kilobyteToMegabyte(kilobyte: String): String {
    if (kilobyte.isEmpty()) return ""
    val result = kilobyte.toDouble() / 1024
    return formatResult(result)
}

fun kilobyteToGigabyte(kilobyte: String): String {
    if (kilobyte.isEmpty()) return ""
    val result = kilobyte.toDouble() / (1024 * 1024)
    return formatResult(result)
}

fun kilobyteToTerabyte(kilobyte: String): String {
    if (kilobyte.isEmpty()) return ""
    val result = kilobyte.toDouble() / (1024.0 * 1024 * 1024)
    return formatResult(result)
}

// Megabyte conversions
fun megabyteToBit(megabyte: String): String {
    if (megabyte.isEmpty()) return ""
    val result = megabyte.toDouble() * 8 * 1024 * 1024
    return formatResult(result)
}

fun megabyteToByte(megabyte: String): String {
    if (megabyte.isEmpty()) return ""
    val result = megabyte.toDouble() * 1024 * 1024
    return formatResult(result)
}

fun megabyteToKilobyte(megabyte: String): String {
    if (megabyte.isEmpty()) return ""
    val result = megabyte.toDouble() * 1024
    return formatResult(result)
}

fun megabyteToMegabyte(megabyte: String): String = megabyte

fun megabyteToGigabyte(megabyte: String): String {
    if (megabyte.isEmpty()) return ""
    val result = megabyte.toDouble() / 1024
    return formatResult(result)
}

fun megabyteToTerabyte(megabyte: String): String {
    if (megabyte.isEmpty()) return ""
    val result = megabyte.toDouble() / (1024 * 1024)
    return formatResult(result)
}

// Gigabyte conversions
fun gigabyteToBit(gigabyte: String): String {
    if (gigabyte.isEmpty()) return ""
    val result = gigabyte.toDouble() * 8 * 1024 * 1024 * 1024
    return formatResult(result)
}

fun gigabyteToByte(gigabyte: String): String {
    if (gigabyte.isEmpty()) return ""
    val result = gigabyte.toDouble() * 1024 * 1024 * 1024
    return formatResult(result)
}

fun gigabyteToKilobyte(gigabyte: String): String {
    if (gigabyte.isEmpty()) return ""
    val result = gigabyte.toDouble() * 1024 * 1024
    return formatResult(result)
}

fun gigabyteToMegabyte(gigabyte: String): String {
    if (gigabyte.isEmpty()) return ""
    val result = gigabyte.toDouble() * 1024
    return formatResult(result)
}

fun gigabyteToGigabyte(gigabyte: String): String = gigabyte

fun gigabyteToTerabyte(gigabyte: String): String {
    if (gigabyte.isEmpty()) return ""
    val result = gigabyte.toDouble() / 1024
    return formatResult(result)
}

// Terabyte conversions
fun terabyteToBit(terabyte: String): String {
    if (terabyte.isEmpty()) return ""
    val result = terabyte.toDouble() * 8 * 1024 * 1024 * 1024 * 1024
    return formatResult(result)
}

fun terabyteToByte(terabyte: String): String {
    if (terabyte.isEmpty()) return ""
    val result = terabyte.toDouble() * 1024 * 1024 * 1024 * 1024
    return formatResult(result)
}

fun terabyteToKilobyte(terabyte: String): String {
    if (terabyte.isEmpty()) return ""
    val result = terabyte.toDouble() * 1024 * 1024 * 1024
    return formatResult(result)
}

fun terabyteToMegabyte(terabyte: String): String {
    if (terabyte.isEmpty()) return ""
    val result = terabyte.toDouble() * 1024 * 1024
    return formatResult(result)
}

fun terabyteToGigabyte(terabyte: String): String {
    if (terabyte.isEmpty()) return ""
    val result = terabyte.toDouble() * 1024
    return formatResult(result)
}

fun terabyteToTerabyte(terabyte: String): String = terabyte
