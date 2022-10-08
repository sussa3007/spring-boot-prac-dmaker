package com.pracex.programming.dmaker.repository;

import com.pracex.programming.dmaker.entity.Developer;
import com.pracex.programming.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetiredDeveloperRepository
        extends JpaRepository<RetiredDeveloper, Long> {


}
