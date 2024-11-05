package com.vedasole.ekartecommercebackend.controller;

import com.vedasole.ekartecommercebackend.payload.AuthenticationRequest;
import com.vedasole.ekartecommercebackend.payload.AuthenticationResponse;
import com.vedasole.ekartecommercebackend.service.serviceInterface.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Controller for handling authentication related requests.
 *
 * @author Ved Asole
 * @since 1.0.0
 */
@Validated
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(
        value = {
                "http://localhost:5173",
                "https://ekart.vedasole.cloud",
                "https://ekart-shopping.netlify.app",
                "https://develop--ekart-shopping.netlify.app"
        },
        allowCredentials = "true",
        exposedHeaders = {"Authorization"}
)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticates a user and returns an authentication token.
     *
     * @param authRequest The authentication request containing the user's credentials.
     * @param request The HTTP request.
     * @return A ResponseEntity containing the authentication response and an HTTP status code.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest authRequest,
            HttpServletRequest request
    ) {
        AuthenticationResponse authenticationResponse = this.authenticationService.authenticate(authRequest, request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authenticationResponse.getToken());
        return new ResponseEntity<>(authenticationResponse, headers, HttpStatus.OK);
    }

    /**
     * Checks if a user is authenticated based on the provided request.
     *
     * @param request The HTTP request.
     * @return A ResponseEntity containing a boolean indicating whether the user is authenticated or not, and an HTTP status code.
     */
    @GetMapping("/check-token")
    public ResponseEntity<Boolean> isAuthenticated(HttpServletRequest request) {
        boolean isValid = this.authenticationService.authenticate(request);
        if(isValid) return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
    }

}