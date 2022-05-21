package com.appvaze.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appvaze.tipcalculator.components.InputFields
import com.appvaze.tipcalculator.ui.theme.Purple200
import com.appvaze.tipcalculator.ui.theme.TipCalculatorTheme
import com.appvaze.tipcalculator.widgets.RoundIconButtons

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                MyApp()
            }
        }
    }
}

@Preview
@Composable
fun MyApp() {
    Column {

        MainContent()
    }
}

//@Preview
@Composable
fun TopCard(
    totalPerPerson: Double = 134.0
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp)
            .padding(20.dp)
            .clip(
                CircleShape.copy(
                    all = CornerSize(12.dp)
                )
            ),
        color = Purple200
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    BillForm {
        Log.d("TAG", "MainContent: it")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier.padding(20.dp),
    onValueChange: (String) -> Unit
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipAmountState = remember {
        mutableStateOf(0.0 )
    }
    val tipPercentage = (sliderPositionState.value * 100).toInt()
    val splitByState = remember {
        mutableStateOf(1)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    TopCard(totalPerPersonState.value)
    androidx.compose.material.Surface(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            InputFields(modifier = Modifier.fillMaxWidth(), valueState = totalBillState,
                labelId = "Enter Bill", enabled = true, isSingleLine =true,
                onAction = KeyboardActions{
                    if(!validState){
                        return@KeyboardActions
                    }
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }

            )
            if(validState){
                BillBottomSection(splitByState)
                Row(modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tip"   )
                    Text(text = "$${tipAmountState.value}" )
                }

                Column(verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$tipPercentage%")
                    Spacer(modifier = Modifier.height(22.dp))
                    Slider(
                        value = sliderPositionState.value,
                        steps = 5,
                        onValueChange = {
                            sliderPositionState.value = it
                            tipAmountState.value = calculateTotalTip(totalBillState.value.toString().toDouble() , tipPercentage )
                            Log.d("TAG", "BillForm: $it")
                            totalPerPersonState.value = calculateTotalPerson(totalBillState.value.toDouble(), splitByState.value , tipPercentage)
                        })
                }
            }else{

            }

        }


    }
}

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if(totalBill > 1 && totalBill.toString().isNotEmpty()) (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerson(
    totalBill: Double,
    splitBy: Int,
    tipPercentage:Int
) : Double{
    val bill = calculateTotalTip(totalBill = totalBill , tipPercentage) + totalBill
    return (bill/splitBy)
}

@Composable
fun BillBottomSection(splitByState: MutableState<Int>) {
    androidx.compose.material.Surface() {
        Row(modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Split", modifier = Modifier.align(
                alignment = Alignment.CenterVertically
            ))
            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()) {
                RoundIconButtons(imageVector = Icons.Default.Remove) {
                    splitByState.value = if (splitByState.value >1 ) {
                        splitByState.value - 1
                    } else {
                         1
                    }

                }
                Text(text = "${splitByState.value}" ,
                    Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp))
                RoundIconButtons(imageVector = Icons.Default.Add) {
                    splitByState.value++
                    

                }
            }
        }



    }

}




