package com.appvaze.tipcalculator.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

 val iconButtonSizeModifire = Modifier.size(40.dp)
@Composable
fun RoundIconButtons(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {

    Card(modifier = modifier
        .padding(4.dp)
        .clickable { onClick.invoke() }
        .then(iconButtonSizeModifire),
        shape = CircleShape,
        elevation = 4.dp
    ) {
        Icon(imageVector = imageVector, contentDescription = null )
    }

}