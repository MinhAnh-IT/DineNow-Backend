package com.vn.DineNow.repositories;

import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.SignWith;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.provider = :provider")
    boolean existsByEmail(@Param("email") String email, @Param("provider")SignWith provider);
    boolean existsByPhone(String phone);

    User findByEmailAndProvider(@NotNull String email, @NotNull SignWith signWith);
}
