package org.example.demo13213.repo.order;

import org.example.demo13213.model.dao.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo  extends CrudRepository<Orders, Integer> {

}
