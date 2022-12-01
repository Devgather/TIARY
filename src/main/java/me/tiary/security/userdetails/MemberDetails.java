package me.tiary.security.userdetails;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {
    private final String profileUuid;

    private final Collection<? extends GrantedAuthority> authorities;

    @Builder
    public MemberDetails(final String profileUuid, final String... authorities) {
        this(profileUuid, (authorities == null) ? (null) : (AuthorityUtils.createAuthorityList(authorities)));
    }

    public String getProfileUuid() {
        return profileUuid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}