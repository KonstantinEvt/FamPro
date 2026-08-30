package ru.memman.repository;

import ru.memman.entity.Address;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Qualifier("addressRepo")
@Repository
public interface AddressRepo extends JpaRepository<Address,Long>,InternRepo<Address> {}