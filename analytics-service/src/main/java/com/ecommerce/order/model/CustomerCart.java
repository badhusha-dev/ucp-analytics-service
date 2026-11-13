package com.ecommerce.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "customer_cart")
@Data
@Schema(description = "Server-side synchronized customer cart for multi-device persistence and recovery")
public class CustomerCart {

    /** Owning tenant, copied from order-service; carts (tenant-less upstream) belong to the legacy tenant. */
    @jakarta.persistence.Column(name = "tenant_id", nullable = false)
    private java.util.UUID tenantId = java.util.UUID.fromString("00000000-0000-4000-8000-000000000001");

    public java.util.UUID getTenantId() { return tenantId; }
    public void setTenantId(java.util.UUID tenantId) { this.tenantId = tenantId == null ? java.util.UUID.fromString("00000000-0000-4000-8000-000000000001") : tenantId; }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Customer user ID", example = "2")
    private Long userId;

    @Column(name = "items_json", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Serialized cart items JSON array")
    private String itemsJson;

    @Column(nullable = false, length = 32)
    @Schema(description = "Cart status: ACTIVE, ABANDONED, CONVERTED", example = "ACTIVE")
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
