package com.example.firebase.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.R
import com.example.firebase.modeldata.Siswa
import com.example.firebase.view.route.DestinasiHome
import com.example.firebase.viewmodel.HomeViewModel
import com.example.firebase.viewmodel.PenyediaViewModel
import com.example.firebase.viewmodel.StatusUiSiswa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (String) -> Unit, // ✅ STRING
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.loadSiswa()
    }

    val statusUiSiswa by viewModel.statusUiSiswa.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiHome.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        HomeBody(
            statusUiSiswa = statusUiSiswa,
            onSiswaClick = navigateToItemUpdate,
            retryAction = viewModel::loadSiswa,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    statusUiSiswa: StatusUiSiswa,
    onSiswaClick: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        when (statusUiSiswa) {
            is StatusUiSiswa.Loading -> LoadingScreen()

            is StatusUiSiswa.Success ->
                DaftarSiswa(
                    itemSiswa = statusUiSiswa.siswa,
                    onSiswaClick = { siswa ->
                        onSiswaClick(siswa.id) // ✅ FIX UTAMA
                    }
                )

            is StatusUiSiswa.Error ->
                ErrorScreen(
                    retryAction = retryAction,
                    modifier = Modifier.fillMaxSize()
                )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.gagal))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun DaftarSiswa(
    itemSiswa: List<Siswa>,
    onSiswaClick: (Siswa) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(itemSiswa, key = { it.id }) { siswa ->
            ItemSiswa(
                siswa = siswa,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onSiswaClick(siswa) }
            )
        }
    }
}

@Composable
fun ItemSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(siswa.nama, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Phone, contentDescription = null)
                Text(siswa.telpon)
            }
            Text(siswa.alamat)
        }
    }
}
