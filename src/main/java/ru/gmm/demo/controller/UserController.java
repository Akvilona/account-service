/**
 * Создал Андрей Антонов 10/17/2023 11:26 AM
 **/

package ru.gmm.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gmm.demo.model.UserRegistrationRq;
import ru.gmm.demo.model.UserRs;
import ru.gmm.demo.model.UserUpdateRq;
import ru.gmm.demo.service.UserService;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("api/users/{id}")
    public UserRs getUser(
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String serName
    ) {
        System.out.printf("id %s name %s surName %s".formatted(id, name, serName));
        return userService.getUser(id);
    }

    @GetMapping("api/users")
    public List<UserRs> getUsers() {
        return userService.getUsers();
    }


    @PostMapping("api/users")
    public UserRs saveUser(@RequestBody UserRegistrationRq userRegistrationRq) {
        return userService.registration(userRegistrationRq);
    }

    @PutMapping("api/users")
    public UserRs putUser(@RequestBody UserUpdateRq userUpdateRq) {
        return userService.update(userUpdateRq);
    }

    @DeleteMapping("api/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
