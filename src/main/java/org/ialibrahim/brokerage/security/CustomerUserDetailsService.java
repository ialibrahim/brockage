package org.ialibrahim.brokerage.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ialibrahim.brokerage.dao.CustomerRepository;
import org.ialibrahim.brokerage.entity.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("customerUserDetailsService")
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {

            if (username.equals("admin")) {
                return getAdminUser();
            }

            CustomerEntity dbUser = getUserFromDB(username);
            User user = modelMapper.map(dbUser, User.class);
            user.getAuthorities().add(AccessAuthority.CUSTOMER);

            return user;
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }
    }

    private CustomerEntity getUserFromDB(String username) {

        return customerRepository.getByUsername(username);
    }

    private User getAdminUser() {

        User user = new User();
        user.setUsername("admin");
        user.setPassword("df48d34b7285c6d3eda3a3e2a904e2c63afcaae0936ec383eb7cad10b10177664b12de90");
        user.getAuthorities().add(AccessAuthority.ADMIN);

        return user;
    }
}
