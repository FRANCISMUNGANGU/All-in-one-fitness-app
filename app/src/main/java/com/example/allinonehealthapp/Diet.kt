package com.example.allinonehealthapp


import androidx.compose.foundation.layout.* // Covers height, width, size
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.allinonehealthapp.data.FoodItem
import com.example.allinonehealthapp.data.NutritionViewModel

@Composable
fun DietScreen(viewModel: NutritionViewModel) {
    val foodItems by viewModel.foodList.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()
    val isPaginating by viewModel.isPaginating.collectAsState()

    // Using Box to layer the search results and the initial loader
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Your Search Bar and Button remain at the top
            SearchBar(onSearch = {query -> viewModel.searchFood(query)})
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(foodItems) { index, food ->
                    FoodRow(food)

                    // Infinite Scroll Trigger
                    // We only trigger when reaching the end AND we aren't already loading
                    if (index == foodItems.lastIndex && !isPaginating && !isInitialLoading) {
                        LaunchedEffect(Unit) {
                            viewModel.searchFood(viewModel.currentQuery, isNewSearch = false)
                        }
                    }
                }

                // 2. The Pagination Loader (Bottom of list)
                if (isPaginating) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Fetching more...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        // 3. The Initial Search Loader (Full screen overlay)
        if (isInitialLoading) {
            Surface(
                color = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun FoodRow(food: FoodItem) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)) {
            Text(
                text = food.product_name,
                style = MaterialTheme.typography.titleLarge)
            Text(text = "Calories: ${food.calories_100g ?: 0} kcal")
            Text(text = "Carbs: ${food.carbohydrates_100g ?: 0}g")
            Text(text = "Protein: ${food.proteins_100g ?: 0}g")
            Text(text = "Fats: ${food.fat_100g ?: 0}g")
            Text(text = "Fibre: ${food.fiber_100g ?: 0}g")


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search food (e.g. apple)") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Button(
            onClick = { if (text.isNotBlank()) onSearch(text) },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text("Search")
        }
    }
}