package com.jithin.groceryapp.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jithin.groceryapp.R
import com.jithin.groceryapp.ui.theme.Typography


/*
 * --------------------------------------------------------------------------
 * File: OrderSuccessDialog.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

@Composable
fun OrderSuccessDialog() {
    Dialog(
        onDismissRequest = { /* BLOCK dismiss */ }
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_success),
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.order_successfully_placed),
                    style = Typography.titleMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
