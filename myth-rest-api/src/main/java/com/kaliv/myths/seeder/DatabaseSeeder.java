package com.kaliv.myths.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.kaliv.myths.entity.users.Authority;
import com.kaliv.myths.entity.users.Role;
import com.kaliv.myths.entity.users.User;
import com.kaliv.myths.persistence.AuthorityRepository;
import com.kaliv.myths.persistence.RoleRepository;
import com.kaliv.myths.persistence.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(AuthorityRepository authorityRepository,
                          RoleRepository roleRepository,
                          UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Authority readAuthority = new Authority();
        readAuthority.setName("READ");
        authorityRepository.save(readAuthority);

        Authority writeAuthority = new Authority();
        writeAuthority.setName("WRITE");
        authorityRepository.save(writeAuthority);

        Authority updateAuthority = new Authority();
        updateAuthority.setName("UPDATE");
        authorityRepository.save(updateAuthority);

        Authority deleteAuthority = new Authority();
        deleteAuthority.setName("DELETE");
        authorityRepository.save(deleteAuthority);

        Role userRole = new Role();
        userRole.setName("USER");
        userRole.getAuthorities().add(readAuthority);
        roleRepository.save(userRole);

        Role staffRole = new Role();
        staffRole.setName("STAFF");
        staffRole.getAuthorities().add(readAuthority);
        staffRole.getAuthorities().add(writeAuthority);
        staffRole.getAuthorities().add(updateAuthority);
        roleRepository.save(staffRole);

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.getAuthorities().add(readAuthority);
        adminRole.getAuthorities().add(writeAuthority);
        adminRole.getAuthorities().add(updateAuthority);
        adminRole.getAuthorities().add(deleteAuthority);
        Role savedAdminRole = roleRepository.save(adminRole);

        User defaultAdmin = new User();
        defaultAdmin.setFirstName("Kaloyan");
        defaultAdmin.setLastName("Ivanov");
        defaultAdmin.setEmail("kaloyan.ivanov88@gmail.com");
        defaultAdmin.setUsername("kaliv0");
        defaultAdmin.setPassword(passwordEncoder.encode("123pass"));
        defaultAdmin.setRole(savedAdminRole);
        defaultAdmin.setActive(true);
        defaultAdmin.setNotLocked(true);
        userRepository.save(defaultAdmin);
    }
}
