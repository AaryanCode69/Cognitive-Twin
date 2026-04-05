package com.example.cognitivetwin.user.entity;

import com.example.cognitivetwin.common.BaseEntity;
import com.example.cognitivetwin.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_users_email",
                columnNames = "email"
        )
})
public class UserEntity extends BaseEntity {

    @Column(unique = true,nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;
}
