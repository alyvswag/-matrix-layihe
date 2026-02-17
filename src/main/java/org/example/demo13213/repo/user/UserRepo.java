package org.example.demo13213.repo.user;


import org.example.demo13213.model.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u WHERE u.username = :username AND u.isActive = true ")
    Optional<Users> findUserByUsername(@Param("username") String username);

    long count();

//    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status = 'ACTIVE' ")
//    Optional<User> findUserById(@Param("id") Long id);
//
//    @Query(" SELECT u FROM User u where u.status = 'ACTIVE' ")
//    List<User> findAllUser();
//
//    @Query("SELECT u FROM UserRole u WHERE u.role.roleName = :roleName AND u.isActive = true ")
//    List<UserRole>  getUsersByRole(@Param("roleName") RoleName roleName);
}
