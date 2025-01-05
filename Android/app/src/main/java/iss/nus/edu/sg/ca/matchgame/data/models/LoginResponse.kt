package iss.nus.edu.sg.ca.matchgame.data.models

data class LoginResponse(
    val status: Boolean,
    val message: String? = null,
    val userId: Int = -1,
    val isPaidUser: Boolean
)