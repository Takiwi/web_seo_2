package doan.bai_2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import doan.bai_2.Repositories.RoleRepository;
import doan.bai_2.Repositories.UserRepository;
import doan.bai_2.dto.user.requests.RegisterRequest;
import doan.bai_2.dto.user.responses.UserResponse;
import doan.bai_2.enums.Roles;
import doan.bai_2.error.exceptions.DuplicationDataException;
import doan.bai_2.models.RoleEntity;
import doan.bai_2.models.UserEntity;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder bcrypt;

    public UserResponse register(RegisterRequest request){
        // Check username is exist
        boolean isUserExist = userRepo.existsByUsername(request.getUsername());

        if(isUserExist){
            throw new DuplicationDataException("Username is registered!");
        }

        // check email is exist???
        if(userRepo.existsByEmail(request.getEmail())){
            throw new DuplicationDataException("Email is registered!");
        }

        // hash password
        String hashPassword = bcrypt.encode(request.getPassword());
        
        UserEntity user = new UserEntity(
            request.getUsername(), 
            request.getEmail(), 
            hashPassword
        );

        // set role
        RoleEntity defaultRole = roleRepo.findByName(Roles.USER.toString());
        if(defaultRole != null){
            user.setRoleId(defaultRole);
        }else{
            // create default role
            defaultRole = new RoleEntity();
            defaultRole.setName(Roles.USER.toString());
            defaultRole.setDescription("Default role with limited permissions");
            roleRepo.save(defaultRole);

            user.setRoleId(defaultRole);
        }

        // set slug
        user.setSlug(request.getUsername().toLowerCase().replace(" ", "-"));

        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new Error("Create new user failed!", e.getCause());
        }
        
        return new UserResponse(user, "Create new account success!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check username
        Optional<UserEntity> user = userRepo.findByUsername(username);

        if(!user.isPresent()){
            throw new UsernameNotFoundException("Username is not registered!");
        }

        // convert role to string
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.get().getRoleId().getName()); 

        return new User(
            user.get().getUsername(),
            user.get().getPassword(),
            List.of(authority));
    }
}