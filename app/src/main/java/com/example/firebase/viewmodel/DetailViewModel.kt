@file:OptIn(InternalSerializationApi::class)

package com.example.firebase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.Siswa
import com.example.firebase.repositori.RepositorySiswa
import com.example.firebase.view.route.DestinasiDetail
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import java.io.IOException

sealed interface StatusUIDetail {
    data class Success(val satusiswa: Siswa?) : StatusUIDetail
    object Error : StatusUIDetail
    object Loading : StatusUIDetail
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    // 🔧 FIX: ID = String (Firestore documentId)
    private val idSiswa: String =
        savedStateHandle[DestinasiDetail.itemIdArg]
            ?: error("idSiswa tidak ditemukan di SavedStateHandle")

    var statusUIDetail: StatusUIDetail by mutableStateOf(StatusUIDetail.Loading)
        private set

    init {
        getSatuSiswa()
    }

    private fun getSatuSiswa() {
        viewModelScope.launch {
            statusUIDetail = StatusUIDetail.Loading
            statusUIDetail = try {
                StatusUIDetail.Success(
                    satusiswa = repositorySiswa.getSatuSiswa(idSiswa)
                )
            } catch (e: IOException) {
                StatusUIDetail.Error
            } catch (e: Exception) {
                StatusUIDetail.Error
            }
        }
    }

    suspend fun hapusSatuSiswa() {
        try {
            repositorySiswa.hapusSatuSiswa(idSiswa)
            println("Sukses hapus siswa id=$idSiswa")
        } catch (e: Exception) {
            println("Gagal hapus siswa: ${e.message}")
        }
    }
}
