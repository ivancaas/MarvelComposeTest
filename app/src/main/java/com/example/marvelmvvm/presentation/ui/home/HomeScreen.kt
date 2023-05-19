package com.example.marvelmvvm.presentation.ui.home

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Window
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.marvelmvvm.App
import com.example.marvelmvvm.R
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.presentation.ui.common.LoadingAnimated
import com.example.marvelmvvm.presentation.ui.common.LoadingOverlay
import com.example.marvelmvvm.presentation.ui.detail.DetailRoute
import com.example.marvelmvvm.util.noRippleClickable
import com.example.marvelmvvm.util.screenHeight
import com.example.marvelmvvm.util.then
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), uiState: HomeUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            viewModel.searchQuery.value,
            onValueChange = { viewModel.onSearchQueryChanged(it) })
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.weight(1f)) {
                    LoadingOverlay()
                }
            }

            HomeUiState.EmptyList -> {
                Column(Modifier.fillMaxSize()) {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_results),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }

                }
            }

           is HomeUiState.Error -> {
                LaunchedEffect(key1 = null){
                    Toast.makeText(App.instance, uiState.message, Toast.LENGTH_SHORT).show()
                }
                //TODO MAKE TOAST ON ERROR FROM VM
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "ERROR")
                    Button(onClick = { viewModel.onSearchQueryChanged(viewModel.searchQuery.value) }) {
                        Text(
                            text = stringResource(R.string.retry),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            is HomeUiState.Success -> {

                Column {
                   /* SearchBar(
                        viewModel.searchQuery.value,
                        onValueChange = { viewModel.onSearchQueryChanged(it) })*/
                    CharactersList(
                        onItemClick = {
                            viewModel.navigateToRoute(
                                DetailRoute.getRouteWithCharacterId(
                                    it
                                )
                            )
                        },
                        viewModel
                    )
                }
            }
        }

    }


}

@Composable
fun SearchBar(query: String, onValueChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.search)) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.clear_text),
                        tint = Color.Black,
                        modifier = Modifier.background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun ColumnScope.CharactersList(
    onItemClick: (Int) -> Unit,
    viewModel: HomeViewModel
) {
    val characters = viewModel.characters
    var isGrid by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(5.dp))
            .align(Alignment.End)
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ViewAgenda,
            contentDescription = "List View",
            tint = if (!isGrid) Color.Black else Color.Gray,
            modifier = Modifier
                .size(25.dp)
                .clickable { isGrid = false }
        )
        Icon(
            imageVector = Icons.Default.Window,
            contentDescription = "Grid View",
            tint = if (isGrid) Color.Black else Color.Gray,
            modifier = Modifier
                .size(25.dp)
                .clickable { isGrid = true }
        )
    }
    if (isGrid) CharacterGrid(
        characters,
        viewModel,
        onItemClick
    )
    else CharacterList(
        characters,
        viewModel,
        onItemClick
    )

}

@Composable
fun CharacterList(
    characters: SnapshotStateList<CharacterEntity>,
    viewModel: HomeViewModel,
    onItemClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    if (listState.isScrollInProgress) {
        LaunchedEffect(key1 = null) {
            focusManager.clearFocus()
        }
    }
    LazyColumn(state = listState) {
        if (characters.isEmpty()) {

            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LoadingAnimated()
                }
            }
        }
        itemsIndexed(characters) { index, character ->
            if (index == characters.indexOf(characters.last()) - 1 && listState.isScrollInProgress) {
                viewModel.getCharacters()
            }
            CharacterListItem(character, onItemClick, checkFav = viewModel::checkFavorite) {
                viewModel.addOrRemoveFromFavs(it)
            }
            if (index == characters.indexOf(characters.last()) && !viewModel.noMoreResults) {
                LaunchedEffect(key1 = null) {
                    if (listState.isScrollInProgress)
                        viewModel.getCharacters()

                }
                CircularProgressIndicator(color = Color.Red)
            }
        }
    }
}

@Composable
fun CharacterGrid(
    characters: SnapshotStateList<CharacterEntity>,
    viewModel: HomeViewModel,
    onItemClick: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val gridState = rememberLazyGridState()
    if (characters.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoadingAnimated()
        }
    }
    if (gridState.isScrollInProgress) {
        LaunchedEffect(key1 = null) {
            focusManager.clearFocus()
        }
    }
    LazyVerticalGrid(state = gridState, columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        content = {

            itemsIndexed(characters) { index, character ->

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CharacterGridItem(
                        character,
                        onItemClick
                    ) {
                        viewModel.addOrRemoveFromFavs(it)
                    }
                    if (index == characters.indexOf(characters.last()) - 1 && !viewModel.noMoreResults) {
                        LaunchedEffect(key1 = null) {
                            if (gridState.isScrollInProgress)
                                viewModel.getCharacters()

                        }
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
            }
        })
}


@Composable
fun CharacterGridItem(
    character: CharacterEntity,
    onItemClick: (Int) -> Unit,
    onFavClick: (CharacterEntity) -> Unit
) {
    val liked = rememberSaveable(key = character.id.toString()) {
        mutableStateOf(character.liked)
    }
    var isScaled by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isScaled) 1.80f else 1f,
        animationSpec = TweenSpec(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    val rotation by animateFloatAsState(
        targetValue = if (isScaled) 45f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    val coroutineScope = rememberCoroutineScope()
    Box(
        Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onItemClick(character.id) }
    ) {

        SubcomposeAsyncImage(
            model = character.thumbnail,
            loading = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.placeholder(
                        visible = true,
                        color = Color.DarkGray,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .background(Color.Gray)
                    )
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()

        )
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.33f),
                            Color.Transparent,
                        )
                    )
                )
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.33f),
                        )
                    )
                )
        )
        Text(
            character.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart)
        )
        Button(
            onClick = {
                onFavClick(character)
                coroutineScope.launch {
                    if (character.liked) {
                        isScaled = !isScaled
                        delay(500)
                        isScaled = !isScaled
                    }
                }

                character.liked = !character.liked
                liked.value = character.liked
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                Icons.Outlined.Favorite,
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .rotate(rotation)
                    .animateContentSize(),
                tint = if (liked.value) Color.Red else MaterialTheme.colorScheme.onSurface,
                contentDescription = "Favorite"
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterListItem(
    character: CharacterEntity,
    onItemClick: (Int) -> Unit,
    checkFav: suspend (Int) -> Boolean,
    onFavClick: (CharacterEntity) -> Unit,
) {
    val liked = rememberSaveable(key = character.id.toString()) {
        mutableStateOf(character.liked)
    }
    //Everytime the screen is shown we check for the like, to maintain data integrity
    LaunchedEffect(key1 = null){
        liked.value = checkFav(character.id)
        character.liked = liked.value
        //chjeck vm if its liked
    }
    var isScaled by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isScaled) 1.80f else 1f,
        animationSpec = TweenSpec(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    val rotation by animateFloatAsState(
        targetValue = if (isScaled) 45f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(screenHeight() * 0.10f)
        .clickable { onItemClick(character.id) }, verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.fillMaxWidth(0.2f), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = character.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
            Icon(
                Icons.Outlined.Favorite,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(5.dp)
                    .size(20.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .rotate(rotation)
                    .animateContentSize()
                    .noRippleClickable {
                        onFavClick(character)
                        coroutineScope.launch {
                            if (character.liked) {
                                isScaled = !isScaled
                                delay(500)
                                isScaled = !isScaled
                            }
                        }
                        character.liked = !character.liked
                        liked.value = character.liked
                    },
                tint = (liked.value).then(Color.Red) ?: Color.Black,
                contentDescription = "Favorite"
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
        ) {
            Text(
                character.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White, maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp, vertical = 2.5.dp)
                    .basicMarquee(
                        animationMode = MarqueeAnimationMode.Immediately,
                        iterations = Int.MAX_VALUE
                    )
            )
            Text(
                character.description, maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.more_info),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 2.5.dp)
                )
                Icon(Icons.Default.ArrowForward, tint = Color.White, contentDescription = null)
            }
        }
    }

}


