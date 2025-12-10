package doan.bai_2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import doan.bai_2.Repositories.RoleRepository;
import doan.bai_2.Repositories.UserRepository;
import doan.bai_2.enums.Roles;
import doan.bai_2.models.RoleEntity;
import doan.bai_2.models.UserEntity;

@Component
public class CreateAdmin implements CommandLineRunner{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder bcrypt;
    
    @Override
    public void run(String... args) throws Exception {
        if(userRepo.count() == 0){
            String password = bcrypt.encode("123123");
            UserEntity admin = new UserEntity("admin", "admin@gmail.com", password);

            // set role
            RoleEntity adminRole = roleRepo.findByName(Roles.ADMIN.toString());
            if(adminRole == null){
                adminRole = new RoleEntity();
                adminRole.setName(Roles.ADMIN.toString());
                adminRole.setDescription("Administrator role with full permissions");
                roleRepo.save(adminRole);
            }
            admin.setRoleId(adminRole);

            // save
            userRepo.save(admin);
        }
    }
    
}
