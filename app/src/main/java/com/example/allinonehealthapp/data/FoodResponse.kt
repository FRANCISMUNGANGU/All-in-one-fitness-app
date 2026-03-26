package com.example.allinonehealthapp.data
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.5.18.207:8000/"

    // Remove the naming policy if your data class uses underscores!
    private val gson = GsonBuilder().create()

    val instance: NutritionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NutritionApi::class.java)
    }
}

data class FoodResponse(
    val count: Int,
    val data: List<FoodItem>,
)

data class FoodItem(
    val code: String,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("energy_100g") val energy_100g: Double?,
    @SerializedName("calories_100g") val calories_100g: Double?,
    @SerializedName("fat_100g") val fat_100g: Double?,
    @SerializedName("saturated_fat_100g") val saturated_fat_100g: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates_100g: Double?,
    @SerializedName("sugars_100g") val sugars_100g: Double?,
    @SerializedName("fiber_100g") val fiber_100g: Double?,
    @SerializedName("proteins_100g") val proteins_100g: Double?,
    @SerializedName("") val salt_100g: Double?,

    @SerializedName("vitamin_a_100g") val vitamin_a_100g: Double?,
    @SerializedName("vitamin_c_100g") val vitamin_c_100g: Double?,
    @SerializedName("vitamin_d_100g") val vitamin_d_100g: Double?,
    @SerializedName("vitamin_e_100g") val vitamin_e_100g: Double?,
    @SerializedName("vitamin_k_100g") val vitamin_k_100g: Double?,
    @SerializedName("vitamin_b1_100g") val vitamin_b1_100g: Double?,
    @SerializedName("vitamin_b2_100g") val vitamin_b2_100g: Double?,
    @SerializedName("vitamin_b6_100g") val vitamin_b6_100g: Double?,
    @SerializedName("vitamin_b9_100g") val vitamin_b9_100g: Double?,
    @SerializedName("vitamin_ b12_100g") val vitamin_b12_100g: Double?,

    @SerializedName("calcium_100g") val calcium_100g: Double?,
    @SerializedName("iron_100g") val iron_100g: Double?,
    @SerializedName("magnesium_100g") val magnesium_100g: Double?,
    @SerializedName("potassium_100g") val potassium_100g: Double?,
    @SerializedName("zinc_100g") val zinc_100g: Double?,
    @SerializedName("sodium_100g") val sodium_100g: Double?,
    @SerializedName("iodine_100g") val iodine_100g: Double?,
)

