package org.ialibrahim.brokerage.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Data
@NoArgsConstructor
public class Customer implements UserDetails {
    private Long customerId;
    private String				username;
    private String				password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
