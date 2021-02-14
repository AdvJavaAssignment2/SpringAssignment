package com.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "AuthorityEntity")
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long authority_id;
    @Column(name = "authority_name")
    private String authority_name;
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();

    public long getId() {
        return this.authority_id;
    }

    public void setId(long authority_id) {
        this.authority_id = authority_id;
    }

    public String getName() {
        return this.authority_name;
    }

    public void setName(String authority_name) {
        this.authority_name = authority_name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}