package com.tuanwei.eyes.repository;

import com.tuanwei.eyes.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * JpaRepository第一个是实体类类型，第二个是主键类型
 */

@Transactional
public interface UsersRepository extends JpaRepository<Users,Integer> {
    @Modifying
    @Query(value = "update users set  password = :password where email = :email ",nativeQuery = true)
    void updateUser(@Param("email") String email,@Param("password") String password);
}
