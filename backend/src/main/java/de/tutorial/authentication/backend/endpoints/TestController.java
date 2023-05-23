package de.tutorial.authentication.backend.endpoints;

import de.tutorial.authentication.backend.user.UserRegisterationDto;
import de.tutorial.authentication.backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody UserRegisterationDto userRegisterationDto) {
        userService.register(userRegisterationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/secured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> secured() {
        return new ResponseEntity<>("{\"msg\":\"Hello secure World\"}", HttpStatus.OK);
    }

    @GetMapping(value = "/unsecured", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> unsecured() {
        return new ResponseEntity<>("{\"msg\":\"Hello unsecure World\"}", HttpStatus.OK);
    }

}
