package com.floralfusion.floralfusion.repositories;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.entities.User;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {

    List<DeliveryPartner> findByAdmin_AdminID(Long adminID);
    List<DeliveryPartner> findByAreaAndCurrentDeliveriesLessThan(String area, int maxDeliveries);
    DeliveryPartner findByUser(User user);
}
