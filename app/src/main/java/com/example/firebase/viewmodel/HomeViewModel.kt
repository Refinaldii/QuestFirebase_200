package com.example.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.Siswa
import com.example.firebase.repositori.RepositorySiswa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface StatusUiSiswa {
    object Loading : StatusUiSiswa
    data class Success(val siswa: List<Siswa>) : StatusUiSiswa
    object Error : StatusUiSiswa
}

class HomeViewModel(
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    private val _statusUiSiswa =
        MutableStateFlow<StatusUiSiswa>(StatusUiSiswa.Loading)

    val statusUiSiswa: StateFlow<StatusUiSiswa> = _statusUiSiswa

    fun loadSiswa() {
        viewModelScope.launch {
            _statusUiSiswa.value = StatusUiSiswa.Loading
            try {
                val data = repositorySiswa.getDataSiswa()
                println("🔥 DATA FIRESTORE: $data")
                _statusUiSiswa.value = StatusUiSiswa.Success(data)
            } catch (e: Exception) {
                _statusUiSiswa.value = StatusUiSiswa.Error
            }
        }
    }
}
