package com.skillbox.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EnrollRequest extends EnrollManuallyRequest {
    private String method;
    @JsonIgnore
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonIgnore
    @Override
    public String getEmail() {
        return super.getEmail();
    }
}
