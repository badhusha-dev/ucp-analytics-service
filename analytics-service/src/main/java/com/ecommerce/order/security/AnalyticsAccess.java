package com.ecommerce.order.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/** Authorization from the verified tenant token only; identity headers are never trusted here. */
public final class AnalyticsAccess {
    private AnalyticsAccess() {}

    public static Optional<TenantPrincipal> current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() instanceof TenantPrincipal p ? Optional.of(p) : Optional.empty();
    }

    public static boolean has(TenantPrincipal principal, String permission) {
        return principal.permissions().contains(permission) || principal.permissions().contains("*");
    }

    public static TenantPrincipal require(String permission) {
        TenantPrincipal principal = current()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "A tenant-scoped token is required."));
        if (!has(principal, permission)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission " + permission + " is required.");
        }
        return principal;
    }
}
