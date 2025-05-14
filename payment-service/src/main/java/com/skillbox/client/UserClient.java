package com.skillbox.client;

import com.skillbox.client.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "userClient",
        url = "${catalog-service.url}/users"
)
public interface UserClient {

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable("userId") String id);

    @GetMapping("/name/{username}")
    UserDto getUserByName(@PathVariable("username") String username);
}
