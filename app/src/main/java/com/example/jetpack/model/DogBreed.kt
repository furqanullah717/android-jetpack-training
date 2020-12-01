package  com.example.jetpack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DogBreed(
    val bred_for: String?,
    val breed_group: String?,
    val life_span: String?,
    val name: String?,
    val origin: String?,
    val temperament: String?,
    val url: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}