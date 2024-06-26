package com.denisyordanp.pokemonapp.schema.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MyPokemonEntity.TABLE_NAME)
data class MyPokemonEntity(
    @PrimaryKey
    @ColumnInfo(ID_COLUMN)
    val id: String,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val nickname: String,

    @ColumnInfo(UPDATED_TIME_COLUMN)
    val updatedTime: Long
) {
    companion object {
        const val TABLE_NAME = "my_pokemon"
        const val ID_COLUMN = "id"
        const val UPDATED_TIME_COLUMN = "updated_time"
    }
}
