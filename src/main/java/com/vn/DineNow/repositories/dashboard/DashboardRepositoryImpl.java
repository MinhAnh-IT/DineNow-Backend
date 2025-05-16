package com.vn.DineNow.repositories.dashboard;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.payload.response.statistical.dashboard.AdminDashboardResponseDTO;
import com.vn.DineNow.payload.response.statistical.dashboard.OwnerDashboardResponseDTO;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardRepositoryImpl implements DashboardRepository {
    EntityManager entityManager;

    @Override
    public OwnerDashboardResponseDTO getOwnerDashboard(long ownerId, Set<OrderStatus> statuses, OrderStatus statusCompleted) {
        String inStatuses = statuses.stream()
                .map(s -> "'" + s.name() + "'")
                .collect(Collectors.joining(", "));

        String sql = """
            SELECT
                (SELECT COUNT(*) FROM restaurants r WHERE r.owner_id = :ownerId),
                (SELECT COUNT(*)
                 FROM orders o
                 JOIN reservations res ON o.reservation_id = res.id
                 JOIN restaurants r ON res.restaurant_id = r.id
                 WHERE r.owner_id = :ownerId AND o.status = :statusCompleted),
                (SELECT COALESCE(SUM(o.total_price), 0)
                 FROM orders o
                 JOIN reservations res ON o.reservation_id = res.id
                 JOIN restaurants r ON res.restaurant_id = r.id
                 WHERE r.owner_id = :ownerId AND o.status IN (%s)),
                (SELECT COUNT(DISTINCT fc.id)
                 FROM food_categories fc
                 JOIN restaurants r ON fc.restaurant_id = r.id
                 WHERE r.owner_id = :ownerId),
                (SELECT COUNT(*)
                 FROM reviews rv
                 JOIN restaurants r ON rv.restaurant_id = r.id
                 WHERE r.owner_id = :ownerId),
                (SELECT COUNT(*)
                 FROM menu_item_reviews mir
                 JOIN menu_items mi ON mir.menu_item_id = mi.id
                 JOIN restaurants r ON mi.restaurant_id = r.id
                 WHERE r.owner_id = :ownerId)
        """.formatted(inStatuses);

        Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("ownerId", ownerId)
                .setParameter("statusCompleted", statusCompleted.name())
                .getSingleResult();

        return OwnerDashboardResponseDTO.builder()
                .totalRestaurant(((Number) result[0]).intValue())
                .totalOrderCompleted(((Number) result[1]).intValue())
                .totalRevenue((BigDecimal) result[2])
                .totalFoodCategory(((Number) result[3]).intValue())
                .totalRestaurantReview(((Number) result[4]).intValue())
                .totalMenuItemReview(((Number) result[5]).intValue())
                .build();
    }

    @Override
    public AdminDashboardResponseDTO getAdminDashboard(Set<OrderStatus> statuses, OrderStatus statusCompleted) {
        String inStatuses = statuses.stream()
                .map(s -> "'" + s.name() + "'")
                .collect(Collectors.joining(", "));

        String sql = """
            SELECT
                (SELECT COUNT(*) FROM restaurants),
                (SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER'),
                (SELECT COUNT(*) FROM users WHERE role = 'OWNER'),
                (SELECT COALESCE(SUM(total_price), 0) FROM orders WHERE status IN (%s)),
                (SELECT COALESCE(SUM(rt.fee_per_guest * rsv.number_of_people), 0)
                 FROM orders o
                 JOIN reservations rsv ON o.reservation_id = rsv.id
                 JOIN restaurants res ON rsv.restaurant_id = res.id
                 JOIN restaurant_tiers rt ON res.tier_id = rt.id
                 WHERE o.status IN ('COMPLETED', 'PAID')),
                (SELECT COUNT(*)
                 FROM orders o
                 JOIN reservations rsv ON o.reservation_id = rsv.id
                 JOIN restaurants res ON rsv.restaurant_id = res.id
                 WHERE o.status = :statusCompleted)
        """.formatted(inStatuses);

        Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("statusCompleted", statusCompleted.name())
                .getSingleResult();

        return AdminDashboardResponseDTO.builder()
                .totalRestaurant(((Number) result[0]).intValue())
                .totalUser(((Number) result[1]).intValue())
                .totalOwner(((Number) result[2]).intValue())
                .totalRevenue((BigDecimal) result[3])
                .totalProfit((BigDecimal) result[4])
                .totalOrderCompleted(((Number) result[5]).intValue())
                .build();
    }
}
