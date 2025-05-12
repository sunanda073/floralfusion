package com.floralfusion.floralfusion.repositories;

import com.floralfusion.floralfusion.entities.CompanyOrder;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.enums.DeliveryStatus;
import com.floralfusion.floralfusion.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CompanyOrderRepository extends JpaRepository<CompanyOrder, Long> {

    List<CompanyOrder> findByCompany(Company company);

    List<CompanyOrder> findAllByCompany_CompanyID(Long companyId);

    List<CompanyOrder> findByCompanyOrderByOrderedAtDesc(Company company);

    // 1. Count “assigned today” using assignedAt
    int countByDeliveryPartnerAndAssignedAtBetween(
            DeliveryPartner partner,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay);

    // 2. Count “delivered today”
    int countByDeliveryPartnerAndDeliveredAtBetweenAndDeliveryStatus(
            DeliveryPartner partner,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            DeliveryStatus status);

    // 3. Count current pending
    int countByDeliveryPartnerAndDeliveryStatus(
            DeliveryPartner partner,
            DeliveryStatus status);

    // 4. Lists of pending and delivered
    List<CompanyOrder> findByDeliveryPartnerAndDeliveryStatus(
            DeliveryPartner partner,
            DeliveryStatus status);

    // 5. Average daily delivered (native SQL, grouping by DATE(delivered_at))
    @Query(value = "SELECT AVG(daily_count) " +
            "  FROM ( " +
            "         SELECT COUNT(*) AS daily_count " +
            "           FROM company_orders " +
            "          WHERE delivery_partner_id = :partnerId " +
            "            AND delivery_status = 'DELIVERED' " +
            "          GROUP BY DATE(delivered_at) " +
            "       ) AS daily_counts", nativeQuery = true)
    Double calculateAverageDailyForPartner(@Param("partnerId") Long partnerId);
}
