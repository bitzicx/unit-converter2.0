package com.example.unitconverter20

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class InputViewModel: ViewModel(){
    var fromValue by mutableStateOf("0")

    fun update(newVal : String){
         fromValue = newVal
    }

}

@Composable
fun MainScreen(){

    Column(modifier = Modifier.fillMaxSize()) {

        Title()

        Spacer(modifier = Modifier.height(10.dp))

        Row (modifier = Modifier.padding(10.dp)) {
            UnitBox("C", "Unit", modifier = Modifier.align(Alignment.CenterVertically))
            DigitBox(modifier = Modifier.align(Alignment.CenterVertically)){
                NumericEntrtBox()
            }

        }

        Row(modifier = Modifier.padding(10.dp)) {
            UnitBox("F", "Unit", modifier = Modifier.align(Alignment.CenterVertically))
            DigitBox(modifier = Modifier.align(Alignment.CenterVertically)){
                ResultLabel()
            }

        }
    }

}

@Composable
fun Title(){
    Text("Unit Converter",
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 20.dp),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun UnitBox(unit : String, title: String, modifier: Modifier = Modifier){
    Box(modifier = modifier
        .width(125.dp)
        .height(100.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color.DarkGray)
        .padding(10.dp)
    ){
        Text(text = title, fontSize = 10.sp, modifier = Modifier.align(Alignment.TopStart))
        Text(text = unit, modifier = Modifier.align(Alignment.Center), fontSize = 30.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun DigitBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit = {}){
    Box(modifier = modifier.padding(10.dp)
        .clip(RoundedCornerShape(12.dp))
        .height(100.dp).width(260.dp)
        .background(Color.DarkGray)
    ){
        content()
    }
}

@Composable
fun NumericEntrtBox() {
    val viewModel: InputViewModel = viewModel()
    val fromValue = viewModel.fromValue

    TextField(
        value = fromValue,
        onValueChange = {
            if (valid(it)) {
                viewModel.update(it)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun ResultLabel(){
    val viewModel: InputViewModel = viewModel()
    val toValue = celsiusToFahrenheit(viewModel.fromValue)

    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
        contentAlignment = Alignment.Center
    ) {
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
    return str.length < 8 &&
            str.count { it == '.' } <= 1 &&
            str.all { it.isDigit() || it == '.' }
}

fun celsiusToFahrenheit(celsius: String): String {
    if (celsius.isEmpty()) return ""
    val result = celsius.toDouble() * 9 / 5 + 32
    return String.format("%.3f", result).trimEnd('0').trimEnd('.')
}