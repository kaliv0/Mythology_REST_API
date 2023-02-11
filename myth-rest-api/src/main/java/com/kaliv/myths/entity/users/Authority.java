package com.kaliv.myths.entity.users;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "authorities")
public class Authority extends BaseEntity {

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();
}
