package de.tutorial.authentication.backend.endpoints;

import de.tutorial.authentication.backend.user.UserRegisterationDto;
import de.tutorial.authentication.backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegisterationDto userRegisterationDto) {
        userService.register(userRegisterationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/secured")
    public ResponseEntity<String> secured() {
        return new ResponseEntity<>("Hello secure World", HttpStatus.OK);
    }

    @GetMapping("/unsecured")
    public ResponseEntity<String> unsecured() {
        return new ResponseEntity<>("Hello unsecure World", HttpStatus.OK);
    }

}
