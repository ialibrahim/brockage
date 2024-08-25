package org.ialibrahim.brokerage.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    private Long customerId;
    private String username;
    private String password;
    private List<BrokerageAuthority> authorities;
    private boolean enabled = true;

    @Override
    public Collection<BrokerageAuthority> getAuthorities() {

        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        return authorities;
    }

    @JsonSetter("authorities")
    public void setAuthorities(String[] auth) {

        this.authorities = new ArrayList<>();
        Arrays.stream(auth).map(AccessAuthority::fromValue).forEach(a -> authorities.add(a));
    }
}
