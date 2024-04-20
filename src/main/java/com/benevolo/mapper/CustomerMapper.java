package com.benevolo.mapper;

import com.benevolo.dto.CustomerDTO;
import com.benevolo.entity.CustomerEntity;

public class CustomerMapper {

    private CustomerMapper() {
        // empty constructor
    }

    public static CustomerDTO map(CustomerEntity customer) {
        return new CustomerDTO(customer.getId(), customer.getEmail());
    }

}
