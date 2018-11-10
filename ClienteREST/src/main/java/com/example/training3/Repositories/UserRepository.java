package com.example.training3.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.training3.Models.User;

public interface UserRepository extends CrudRepository<User, String>{
	List<User> findAll();
	User findByNombre(String nombre);
}
