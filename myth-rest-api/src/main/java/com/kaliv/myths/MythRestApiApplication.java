package com.kaliv.myths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kaliv.myths.entity.users.Authority;
import com.kaliv.myths.entity.users.Role;
import com.kaliv.myths.persistence.AuthorityRepository;
import com.kaliv.myths.persistence.RoleRepository;

@SpringBootApplication
public class MythRestApiApplication implements CommandLineRunner {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public MythRestApiApplication(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MythRestApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var authority = new Authority();
        authority.setName("user:read");
        authorityRepository.save(authority);

        var role = new Role();
        role.setName("USER");
        role.getAuthorities().add(authority);
        roleRepository.save(role);
    }
}
