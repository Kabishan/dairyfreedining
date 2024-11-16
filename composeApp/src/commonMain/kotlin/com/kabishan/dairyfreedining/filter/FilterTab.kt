package com.kabishan.dairyfreedining.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.dp
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.filter_bar_name
import dairyfreedining.composeapp.generated.resources.filter_list
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterTab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filtersTabName = stringResource(Res.string.filter_bar_name)

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .clickable { onClick() }
            .clearAndSetSemantics {
                contentDescription = filtersTabName
                role = Role.Button
            }
            .padding(8.dp)
    ) {
        Text(text = filtersTabName.uppercase())
        Spacer(modifier = Modifier.width(4.dp))
        Icon(painterResource(Res.drawable.filter_list), contentDescription = String())
    }
}