package com.skillbox.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedRegisterRequest {
    private String username;
    private String email;
    private String password;
    private List<String> roleNames;
}