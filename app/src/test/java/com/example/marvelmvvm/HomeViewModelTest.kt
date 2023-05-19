
import com.example.marvelmvvm.data.repository.CharacterRepository
import com.example.marvelmvvm.mockCharacterEntities
import com.example.marvelmvvm.nav.RouteNavigator
import com.example.marvelmvvm.presentation.ui.home.HomeUiState
import com.example.marvelmvvm.presentation.ui.home.HomeViewModel
import com.example.marvelmvvm.util.Resource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private val characterRepository = mock<CharacterRepository>()
    private val routeNavigator = mock<RouteNavigator>()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = HomeViewModel(routeNavigator, characterRepository)
    }

    @Test
    fun `Loading state works`() = runTest {
        whenever(characterRepository.getCharacters("", 0)).thenReturn(
            Resource.Success(listOf())
        )

        assertEquals(HomeUiState.Loading, viewModel.uiState.value)

    }

    @Test
    fun `EmptyList state works`() = runTest {
        whenever(characterRepository.getCharacters("", 0)).thenReturn(Resource.Success(listOf()))

        viewModel.getCharacters()

        assertEquals(HomeUiState.EmptyList, viewModel.uiState.value)

    }

    @Test
    fun `Success state works and characters is filled`() = runTest {
        whenever(characterRepository.getCharacters("", 0)).thenReturn(Resource.Success(mockCharacterEntities))

        viewModel.getCharacters()
        val realResult =viewModel.characters.toList()
        assertEquals(HomeUiState.Success, viewModel.uiState.value)
        assertEquals(mockCharacterEntities, realResult)


    }
}
