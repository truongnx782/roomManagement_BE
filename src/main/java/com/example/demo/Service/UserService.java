package com.example.demo.Service;

import com.example.demo.Util.ERole;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repo.RoleRepository;
import com.example.demo.Repo.UserRepository;
import com.example.demo.Request.ChangePasswordRequest;
import com.example.demo.Request.CreateUserRequest;
import com.example.demo.Request.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

//    @Override
    public void register(CreateUserRequest request) {
        // TODO Auto-generated method stub
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

//    @Override
    public User getUserByUsername(String username) {
        // TODO Auto-generated method stub
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Not Found User"));
        return user;
    }

//    @Override
    public User updateUser(UpdateProfileRequest request) {
        // TODO Auto-generated method stub
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException("Not Found User"));
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return user;
    }

//    @Override
    public void changePassword(ChangePasswordRequest request) {
        // TODO Auto-generated method stub
        // User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException("Not Found User"));

        // if(encoder.encode(request.getOldPassword()) != user.getPassword()){
        //   throw new BadRequestException("Old Passrword Not Same");
        // }
        // user.setPassword(encoder.encode(request.getNewPassword()));

        // userRepository.save(user);

    }
}
