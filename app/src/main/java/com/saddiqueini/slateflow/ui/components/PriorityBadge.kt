package com.saddiqueini.slateflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saddiqueini.slateflow.data.model.Priority
import com.saddiqueini.slateflow.ui.theme.Typography

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val accentColor = priority.getComposeColor()

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(accentColor.copy(alpha = 0.12f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.35f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(accentColor)
        )

        if (showLabel) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = priority.displayName,
                style = Typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = accentColor
                )
            )
        }
    }
}
