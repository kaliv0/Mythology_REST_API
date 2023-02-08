package com.kaliv.myths.entity.users;

import javax.persistence.*;

import java.time.ZonedDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private ZonedDateTime joinDate;
    private ZonedDateTime currentLoginDate;
    private ZonedDateTime lastLoginDate;
    private String role;

    @SuppressWarnings("JpaAttributeTypeInspection")//=> converts to tinyblob in Db
    private String[] authorities;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean isActive;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean isNotLocked;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }
}