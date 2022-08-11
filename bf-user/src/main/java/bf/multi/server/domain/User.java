package bf.multi.server.domain;


import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { // UserDetails는 시큐리티가 관리하는 객체
    // id, name, email, profile_image, gender, phone, age, intro, start_date, modified_date
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_SEQ_ID")
    private Long userSequenceId;

    @Column(name = "NAME", nullable = false, length = 20)
    private String userName;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String userEmail;

    @Column(name = "GENDER", length = 5)
    private String gender;

    @Column(name = "PROFILE_IMAGE", length = 100)
    private String profileImage;

    @Column(name = "PHONE_NUM", length = 15)
    private String phoneNum;

    @Column(name = "AGE")
    private int age;

    @Column(name = "INTRO", length = 100)
    private String intro;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
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
