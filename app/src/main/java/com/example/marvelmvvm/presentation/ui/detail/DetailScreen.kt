package com.example.marvelmvvm.presentation.ui.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.marvelmvvm.R
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.mapThumbnail
import com.example.marvelmvvm.data.remote.responses.ComicsResponse
import com.example.marvelmvvm.data.remote.responses.EventsResponse
import com.example.marvelmvvm.data.remote.responses.SeriesResponse
import com.example.marvelmvvm.presentation.ui.common.LoadingOverlay
import com.example.marvelmvvm.util.screenHeight
import com.example.marvelmvvm.util.screenWidth
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel(), uiState: DetailUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is DetailUiState.Loading -> {
                Box {
                    LoadingOverlay()
                }
            }

            is DetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "ERROR")
                    Button(onClick = { viewModel.getCharacterData() }) {
                        Text(
                            text = stringResource(R.string.retry),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            is DetailUiState.Success -> DetailsView(character = uiState.characterData)
        }

    }


}

@Composable
fun DetailsView(viewModel: DetailViewModel = hiltViewModel(), character: CharacterEntity) {
    val comics by viewModel.comicUiState.collectAsState()
    val events by viewModel.eventUiState.collectAsState()
    val series by viewModel.serieUiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CharacterDetails(character) {
                viewModel.addOrRemoveFromFavs(it)
            }
            ComicsList(comics, viewModel::getCharacterComic, viewModel.noMoreComics)
            EventsList(events, viewModel::getCharacterEvents, viewModel.noMoreEvents)
            SerieList(series, viewModel::getCharacterSeries, viewModel.noMoreSeries)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterDetails(character: CharacterEntity, onFavClick: (CharacterEntity) -> Unit) {
    // Display character details
    val coroutineScope = rememberCoroutineScope()
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
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(
            character.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier
                .basicMarquee(
                    animationMode = MarqueeAnimationMode.Immediately,
                    iterations = Int.MAX_VALUE
                )
                .padding(vertical = 10.dp, horizontal = 10.dp), maxLines = 1, color = Color.White
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
                .padding(10.dp),
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

    SubcomposeAsyncImage(
        model = character.thumbnail,
        loading = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(screenHeight() / 3)
                    .padding(horizontal = 5.dp)
                    .placeholder(
                        visible = true,
                        color = Color.DarkGray,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            ) {
                Box(
                    modifier = Modifier
                        .height(screenHeight() / 3)
                        .background(Color.Gray)
                )
            }
        },
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(screenHeight() / 3)


    )
    if (character.description.isNotEmpty()) {
        Text(
            "Description", style = MaterialTheme.typography.headlineSmall, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp), color = Color.White
        )
        Text(
            character.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White, modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun ComicsList(
    comicsState: ComicUiState,
    getComics: () -> Unit,
    noMoreComics: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.comics),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 5.dp),
            color = Color.White
        )
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
    when (comicsState) {
        is ComicUiState.Loading -> {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(screenHeight() / 3)
                    .width(screenWidth() / 3)
                    .padding(horizontal = 5.dp)
                    .placeholder(
                        visible = true,
                        color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                    .padding(vertical = 5.dp)
            ) {}
        }

        is ComicUiState.Success -> {
            val listState = rememberLazyListState()
            if (comicsState.comics.isEmpty())
                Text(
                    text = stringResource(R.string.no_comics), modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),color=Color.White, textAlign = TextAlign.Center
                )
            LazyRow(state = listState, contentPadding = PaddingValues(horizontal = 10.dp)) {
                itemsIndexed(comicsState.comics) { index, comic ->
                    ComicItem(comic)
                    if (index == comicsState.comics.indexOf(comicsState.comics.last()) && !noMoreComics) {
                        LaunchedEffect(key1 = null) {
                            if (listState.isScrollInProgress)
                                getComics()
                        }
                    }
                }
            }

        }

        is ComicUiState.Error -> {
            Text(text = stringResource(R.string.error_retrieving_comics))
        }
    }


}

@Composable
fun EventsList(
    eventUiState: EventUiState,
    getEvents: () -> Unit,
    noMoreEvents: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.events),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 5.dp),
            color = Color.White
        )
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
    when (eventUiState) {
        is EventUiState.Loading -> {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(screenHeight() / 3)
                    .width(screenWidth() / 3)
                    .padding(horizontal = 5.dp)
                    .placeholder(
                        visible = true,
                        color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                    .padding(vertical = 5.dp)
            ) {}
        }

        is EventUiState.Success -> {
            val listState = rememberLazyListState()
            if (eventUiState.events.isEmpty())
                Text(
                    text = stringResource(R.string.no_events), modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),color=Color.White, textAlign = TextAlign.Center
                )
            LazyRow(state = listState, contentPadding = PaddingValues(horizontal = 10.dp)) {
                itemsIndexed(eventUiState.events) { index, event ->
                    EventItem(event)
                    if (index == eventUiState.events.indexOf(eventUiState.events.last()) && !noMoreEvents) {
                        LaunchedEffect(key1 = null) {
                            if (listState.isScrollInProgress)
                                getEvents()
                        }
                    }
                }
            }
        }

        is EventUiState.Error -> {
            Text(text = stringResource(R.string.error_retrieving_events))
        }
    }


}

@Composable
fun ComicItem(comicData: ComicsResponse.ComicResponse.ComicData) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .width(screenWidth() / 3)
            .aspectRatio(9 / 16f)
            .padding(horizontal = 5.dp)
    ) {
        SubcomposeAsyncImage(
            model = mapThumbnail(comicData.thumbnail),
            loading = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(screenHeight() / 3)
                        .fillMaxWidth()
                        .placeholder(
                            visible = true,
                            color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(screenHeight() / 3)
                            .fillMaxWidth()
                            .background(Color.Gray)

                    )
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .clip(RoundedCornerShape(5.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
        )
        Text(text = comicData.title, color = Color.White)
        Text(
            text = comicData.dates.first().date.substring(0, 10),
            modifier = Modifier.align(Alignment.BottomEnd),
            color = Color.White
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventItem(eventData: EventsResponse.EventResponse.EventData) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .width(screenWidth() / 3)
            .aspectRatio(9 / 16f)
            .padding(horizontal = 5.dp)
    ) {
        SubcomposeAsyncImage(
            model = mapThumbnail(eventData.thumbnail),
            loading = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(screenHeight() / 3)
                        .fillMaxWidth()
                        .placeholder(
                            visible = true,
                            color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(screenHeight() / 3)
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .clip(RoundedCornerShape(5.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
        )
        Text(text = eventData.title, color = Color.White)
    }
}

@Composable
fun SerieItem(serieData: SeriesResponse.SerieResponse.SerieData) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .width(screenWidth() / 3)
            .aspectRatio(9 / 16f)
            .padding(horizontal = 5.dp)
    ) {
        SubcomposeAsyncImage(
            model = mapThumbnail(serieData.thumbnail),
            loading = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(screenHeight() / 3)
                        .fillMaxWidth()
                        .placeholder(
                            visible = true,
                            color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                        .padding(horizontal = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(screenHeight() / 3)
                            .fillMaxWidth()
                            .background(Color.Gray)

                    )
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .clip(RoundedCornerShape(5.dp))

        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))

        )
        Text(text = serieData.title, color = Color.White)
    }
}

@Composable
fun SerieList(
    serieUiState: SeriesUiState,
    getSerie: () -> Unit,
    noMoreSerie: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.series),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 5.dp),
            color = Color.White
        )
        Icon(Icons.Default.ArrowForward, contentDescription = null)
    }
    when (serieUiState) {
        is SeriesUiState.Loading -> {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(screenHeight() / 3)
                    .width(screenWidth() / 3)
                    .padding(horizontal = 5.dp)
                    .placeholder(
                        visible = true,
                        color = Color.DarkGray, shape = RoundedCornerShape(5.dp),
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                    .padding(vertical = 5.dp)
            ) {}
        }

        is SeriesUiState.Success -> {
            val listState = rememberLazyListState()
            if (serieUiState.series.isEmpty())
                Text(
                    text = stringResource(R.string.no_series), modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),color=Color.White, textAlign = TextAlign.Center
                )
            LazyRow(state = listState, contentPadding = PaddingValues(horizontal = 10.dp)) {
                itemsIndexed(serieUiState.series) { index, event ->
                    SerieItem(event)
                    if (index == serieUiState.series.indexOf(serieUiState.series.last()) && !noMoreSerie) {
                        LaunchedEffect(key1 = null) {
                            if (listState.isScrollInProgress)
                                getSerie()
                        }
                    }
                }
            }
        }

        is SeriesUiState.Error -> {
            Text(text = stringResource(R.string.error_retrieving_series))
        }
    }


}



