package com.tsunamihandia.repository;

import com.tsunamihandia.dto.UsersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UsersDTO, Long> {

    UsersDTO findByName(String name);
}
