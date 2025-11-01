package org.example.demo13213.model.dao;


import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Cascade(CascadeType.ALL)//siliencek
    Long id;

    @Column(name = "username" , unique = true)
    String username;
    @Column(name = "full_name")
    String fullName;
    @Column(name = "password")
    String password;
    @Column(name = "is_active")
    Boolean isActive;
    @CreationTimestamp
    @Column(name = "created_at")
    Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;

}
