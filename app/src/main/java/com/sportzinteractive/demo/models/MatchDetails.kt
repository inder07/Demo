package com.sportzinteractive.demo.models

data class MatchDetails(
    val Batting: Batting,
    val Bowling: Bowling,
    val Iscaptain: Boolean,
    val Iskeeper: Boolean,
    val Name_Full: String,
    val Position: String
)