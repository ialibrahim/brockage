package org.ialibrahim.brokerage.service;

import org.ialibrahim.brokerage.exception.InvalidOperationException;
import org.ialibrahim.brokerage.security.AccessAuthority;
import org.ialibrahim.brokerage.security.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionChecker {

    private PermissionChecker() {

    }

    public static void checkPermission(Long customerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().contains(AccessAuthority.ADMIN)) {
            return;
        }

        if (user.getCustomerId() == null || !user.getCustomerId().equals(customerId)) {
            throw new InvalidOperationException("Customer is not allowed to access this resource");
        }
    }

    public static void onlyAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().contains(AccessAuthority.ADMIN)) {
            return;
        }

        throw new InvalidOperationException("Customer is not allowed to access this resource");
    }
}
