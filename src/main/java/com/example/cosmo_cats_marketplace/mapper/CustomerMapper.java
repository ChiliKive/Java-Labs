package com.example.cosmo_cats_marketplace.mapper;

import com.example.cosmo_cats_marketplace.domain.Customer;
import com.example.cosmo_cats_marketplace.dto.customer.CustomerDto;
import com.example.cosmo_cats_marketplace.dto.customer.CustomerEntry;
import com.example.cosmo_cats_marketplace.dto.customer.CustomerListDto;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface CustomerMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "address", source = "address")
  @Mapping(target = "phone", source = "phone")
  @Mapping(target = "email", source = "email")
  CustomerDto toCustomerDto(Customer customer);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "address", source = "address")
  @Mapping(target = "phone", source = "phone")
  @Mapping(target = "email", source = "email")
  CustomerEntry toCustomerEntry(Customer customer);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "address", source = "address")
  @Mapping(target = "phone", source = "phone")
  @Mapping(target = "email", source = "email")
  Customer toCustomer(CustomerDto customerDto);

  default CustomerListDto toCustomerListDto(List<Customer> customers) {
    return CustomerListDto.builder().customers(toCustomerEntries(customers)).build();
  }

  List<CustomerEntry> toCustomerEntries(List<Customer> customers);
}
