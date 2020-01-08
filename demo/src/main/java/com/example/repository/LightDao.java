package com.example.repository;

import com.example.model.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightDao  extends JpaRepository<Light, Integer>, LightDaoCustom {
}
