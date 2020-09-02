package com.example.quipper_cording_test.extension

fun Int.formatNumOfTopics(): String{
    return "${String.format("%d講義", this)}"
}