package com.tsunamihandia.Rest;

import com.tsunamihandia.Exception.CustomErrorType;
import com.tsunamihandia.dto.UsersDTO;
import com.tsunamihandia.repository.UserJpaRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {
    public static final Logger logger =
            LoggerFactory.getLogger(UserRegistrationRestController.class);

    private UserJpaRepository userJpaRepository;

    @Autowired
    public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    // This annotation is equivalent to the next
//    @RequestMapping(value = "/", method = RequestMethod.GET)
    @GetMapping("/")
    public ResponseEntity<List<UsersDTO>> listAllUsers() {
        List<UsersDTO> users = userJpaRepository.findAll();

        if (users.isEmpty())
            return new ResponseEntity<List<UsersDTO>>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<List<UsersDTO>>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable("id") final Long id) {
        Optional<UsersDTO> user = userJpaRepository.findById(id);

        if (user.isEmpty())
            return new ResponseEntity<UsersDTO>(
                    new CustomErrorType(String.format("User with id %s not found", id)),
                    HttpStatus.NOT_FOUND);

        return new ResponseEntity<UsersDTO>(user.get(), HttpStatus.OK);
    }

    // This annotation is equivalent to the next
//    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersDTO> createUser(@Valid @RequestBody final UsersDTO user) {
        logger.info("Creating User: {}", user);

        if (userJpaRepository.findByName(user.getName()) != null) {
            logger.error("Unable to create. A user with name {} already exist", user.getName());
            return new ResponseEntity<UsersDTO>(
                    new CustomErrorType(String.format("Unable to create new user. A user with name %s already exist.", user.getName())),
                    HttpStatus.CONFLICT
            );
        }

        userJpaRepository.save(user);
        return new ResponseEntity<UsersDTO>(user, HttpStatus.CREATED);
    }

    // This annotation is equivalent to the next
//    @RequestMapping(method = RequestMethod.PUT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersDTO> updateUser(
            @PathVariable("id") final Long id, @RequestBody UsersDTO user) {
        // fecth user based on id an set to currentUser object of type UserDTO
        Optional<UsersDTO> currentUser = userJpaRepository.findById(id);

        if (currentUser.isEmpty())
            return new ResponseEntity<UsersDTO>(
                    new CustomErrorType(String.format("Unable to update. User with id %s not found.", id)),
                    HttpStatus.NOT_FOUND);

        // update currentUser object data with user object data
        currentUser.get().setName(user.getName());
        currentUser.get().setAddress(user.getAddress());
        currentUser.get().setEmail(user.getEmail());

        // save currentUser object
        userJpaRepository.saveAndFlush(currentUser.get());

        // return ResponseEntity object
        return new ResponseEntity<UsersDTO>(currentUser.get(), HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<UsersDTO> deleteUser(@PathVariable("id") final Long id) {
        Optional<UsersDTO> currentUser = userJpaRepository.findById(id);

        if(currentUser.isEmpty())
            return new ResponseEntity<UsersDTO>(
                    new CustomErrorType(String.format("Unable to delete. User with id %s not found.")),
                    HttpStatus.NOT_FOUND);

        userJpaRepository.delete(currentUser.get());
        return new ResponseEntity<UsersDTO>(
                new CustomErrorType(String.format("Deleted User with id %s.", id)),
                HttpStatus.NO_CONTENT);

    }
}
