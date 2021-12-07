package com.watcherr.backend.auth

import com.watcherr.backend.dtos.AuthClaims
import com.watcherr.backend.entities.User
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebArgumentResolver.UNRESOLVED
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthTokenWebResolver : HandlerMethodArgumentResolver {

    @Autowired
    lateinit var authTokenHandler: AuthTokenHandler

    // to register Auth annotation
    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return methodParameter.getParameterAnnotation(Auth::class.java) != null
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val logger = KotlinLogging.logger {}
        val headernames: MutableIterator<String> = webRequest.headerNames
        return headernames
//        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")) {
//            val authToken = authTokenHeader.substring(7)
//            logger.info { "token $authToken" }
//            val claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(authToken).body
//            return authToken;
//        }
//        if (parameter.parameterType == User::class.java) {
//          // looking for the auth token in the headers
//            var authToken = webRequest.getHeader("Bearer")
//            logger.info("Token:".plus(authToken))
//            if (authToken == null) {
//                throw AuthException("No token found")
//            }
//
//            return authTokenHandler.getUserFromToken(authToken)
//        }
//
//        return UNRESOLVED
        return AuthClaims(99999)
    }
}