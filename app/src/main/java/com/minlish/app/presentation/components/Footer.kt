package com.minlish.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.minlish.app.presentation.components.AppColors.OnSurfaceVariant

sealed class NavItem(val label: String, val icon: ImageVector, val selectedIcon: ImageVector?= null){
    object Analytics : NavItem("analytics", Icons.Default.BarChart)
    object Library : NavItem("library", Icons.Default.ImportContacts)
    object Learning : NavItem("learning", Icons.Default.School, Icons.Default.School)
    object Practice : NavItem("practice", Icons.Default.FitnessCenter, Icons.Default.FitnessCenter)
    object Profile : NavItem("profile", Icons.Default.Person)
}

@Composable
fun Footer(currentRoute: String, onNavigate: (String) -> Unit, modifier: Modifier= Modifier){
    val items= listOf(
        NavItem.Analytics,
        NavItem.Library,
        NavItem.Learning,
        NavItem.Practice,
        NavItem.Profile,
    )

    Surface(
        modifier=modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp)),

        color=Color.White.copy(alpha = 0.95f),
        shadowElevation = 6.dp
    ){
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            contentColor = OnSurfaceVariant
        ){
            items.forEach{it ->
                val isSelected = currentRoute.lowercase() == it.label
                NavigationBarItem(
                    selected = isSelected,
                    onClick={onNavigate(it.label)},
                    icon={
                        Icon(
                            imageVector = if (isSelected && it.selectedIcon!=null) it.selectedIcon else it.icon,
                            contentDescription = it.label,
                            modifier=Modifier.size(24.dp)
                        )
                    },
                    label={
                        Text(
                            text=it.label.replaceFirstChar { char -> char.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.Blue,
                        indicatorColor = if (isSelected) Color.Blue else  Color.Transparent,
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
            }
        }
    }
}