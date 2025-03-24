package bankBackend.bank.controller;
import bankBackend.bank.model.UserSpring;
import bankBackend.bank.service.UserSpringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class UserSpringController {
    @Autowired
    private UserSpringService userService;

    @GetMapping
    public List<UserSpring> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSpring> getUserById(@PathVariable Long id) {
        Optional<UserSpring> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserSpring createUser(@RequestBody UserSpring user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSpring> updateUser(@PathVariable Long id, @RequestBody UserSpring userDetails) {
        try {
            UserSpring updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}