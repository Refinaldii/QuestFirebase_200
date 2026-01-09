package com.example.firebase.repositori
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.firebase.modeldata.Siswa

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)

    suspend fun getSatuSiswa(id: String): Siswa?
    suspend fun editSatuSiswa(id: String, siswa: Siswa)
    suspend fun hapusSatuSiswa(id: String)
}


class FirebaseRepositorySiswa : RepositorySiswa {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.id, // ✅ PAKAI DOCUMENT ID
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        collection.document().set(
            mapOf(
                "nama" to siswa.nama,
                "alamat" to siswa.alamat,
                "telpon" to siswa.telpon
            )
        ).await()
    }

    override suspend fun getSatuSiswa(id: String): Siswa? {
        return try {
            val doc = collection.document(id).get().await()
            if (!doc.exists()) return null

            Siswa(
                id = doc.id,
                nama = doc.getString("nama") ?: "",
                alamat = doc.getString("alamat") ?: "",
                telpon = doc.getString("telpon") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun editSatuSiswa(id: String, siswa: Siswa) {
        collection.document(id).set(
            mapOf(
                "nama" to siswa.nama,
                "alamat" to siswa.alamat,
                "telpon" to siswa.telpon
            )
        ).await()
    }

    override suspend fun hapusSatuSiswa(id: String) {
        collection.document(id).delete().await()
    }
}
