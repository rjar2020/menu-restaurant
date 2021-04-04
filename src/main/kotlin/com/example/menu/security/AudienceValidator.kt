package com.example.menu.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.util.Assert

internal class AudienceValidator(private var audience: String) : OAuth2TokenValidator<Jwt> {

    init {
        Assert.hasText(audience, "audience is null or empty")
    }

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult =
            when (jwt.audience.contains(audience)) {
                true -> OAuth2TokenValidatorResult.success()
                false -> OAuth2TokenValidatorResult.failure(OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN))
            }
}