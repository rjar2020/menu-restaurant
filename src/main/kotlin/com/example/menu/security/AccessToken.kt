package com.example.menu.security

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessToken(
        @JsonProperty("access_token") val token: String,
        @JsonProperty("expires_in") val expiresIn: String,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("scope") val scope: String
)
