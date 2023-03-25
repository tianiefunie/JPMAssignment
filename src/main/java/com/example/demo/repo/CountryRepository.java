package com.example.demo.repo;
import java.util.List;

import com.example.demo.dto.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository  extends CrudRepository<Country, Long> {
    Country findById(int id);
}
