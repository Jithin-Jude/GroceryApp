package com.jithin.groceryapp.ui.components


/*
 * --------------------------------------------------------------------------
 * File: CategoryTabsView.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.model.DishModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabsView(
    modifier: Modifier = Modifier,
    categories: List<CategoryModel>,
    onIncrement: (dish: DishModel) -> Unit,
    onDecrement: (dish: DishModel) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { categories.size }
    )

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {

        // 🔹 Tabs
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp
        ) {
            categories.forEachIndexed { index, category ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = category.name,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        // 🔹 Swipeable Pager (ViewPager replacement)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            ProductListView(
                modifier = Modifier.fillMaxSize(),
                listOfProducts = categories[pageIndex].dishes,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
            )
        }
    }
}