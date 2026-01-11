package com.jithin.groceryapp.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jithin.groceryapp.core.ui.theme.Typography


/*
 * --------------------------------------------------------------------------
 * File: RoundedCornerButton.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun RoundedCornerButton(
    text: String,
    iconRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 70.dp,
    cornerRadius: Dp = 50.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            // START ICON (no tint applied)
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(32.dp),
                tint = Color.Unspecified
            )

            // CENTERED TEXT
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                style = Typography.titleLarge
            )
        }
    }
}
