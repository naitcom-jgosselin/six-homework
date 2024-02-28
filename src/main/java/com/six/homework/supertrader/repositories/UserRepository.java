package com.six.homework.supertrader.repositories;

import com.six.homework.supertrader.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
