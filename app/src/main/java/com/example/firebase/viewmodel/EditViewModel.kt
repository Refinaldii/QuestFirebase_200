package com.example.firebase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.DetailSiswa
import com.example.firebase.modeldata.UIStateSiswa
import com.example.firebase.modeldata.toDataSiswa
import com.example.firebase.modeldata.toUiStateSiswa
import com.example.firebase.repositori.RepositorySiswa
import com.example.firebase.view.route.DestinasiDetail
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    // 🔧 FIX: id sebagai String (Firestore documentId)
    private val idSiswa: String =
        savedStateHandle[DestinasiDetail.itemIdArg]
            ?: error("idSiswa tidak ditemukan di SavedStateHandle")

    init {
        viewModelScope.launch {
            repositorySiswa.getSatuSiswa(idSiswa)?.let { siswa ->
                uiStateSiswa = siswa.toUiStateSiswa(isEntryValid = true)
            }
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa = UIStateSiswa(
            detailSiswa = detailSiswa,
            isEntryValid = validasiInput(detailSiswa)
        )
    }

    private fun validasiInput(
        detail: DetailSiswa = uiStateSiswa.detailSiswa
    ): Boolean =
        detail.nama.isNotBlank() &&
                detail.alamat.isNotBlank() &&
                detail.telpon.isNotBlank()

    suspend fun editSatuSiswa() {
        if (!uiStateSiswa.isEntryValid) return

        try {
            repositorySiswa.editSatuSiswa(
                idSiswa,
                uiStateSiswa.detailSiswa.toDataSiswa()
            )
            println("Update sukses: id=$idSiswa")
        } catch (e: Exception) {
            println("Update gagal: ${e.message}")
        }
    }
}
