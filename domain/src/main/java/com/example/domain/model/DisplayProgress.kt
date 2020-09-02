package com.example.domain.model

enum class DisplayProgress(val status: String) {
    NOT_STARTED("受講前"),
    IN_PROGRESS("受講中"),
    MASTERED("受講済み");

    companion object{
        fun fromProgress(progress: Int): DisplayProgress{
            return when(progress){
                in(1..99) -> IN_PROGRESS
                100 -> MASTERED
                else -> NOT_STARTED
            }
        }
    }
}