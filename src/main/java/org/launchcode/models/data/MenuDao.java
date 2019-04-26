package org.launchcode.models.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import javax.transaction.Transactional;

@Repository
@Transactional

public interface MenuDao extends CrudRepository<Model, Integer> {
}
