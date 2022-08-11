package bf.multi.server.domain.user;


import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "USER")
public class User {

    // UserDetails는 시큐리티가 관리하는 객체
    // ID, NAME, EMAIL, PHOTO_LINK, GENDER, PHONE, AGE, INTRO, START_DATE, MODIFIED_DATE
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 45, nullable = false)
    private String name;

    @Column(name = "EMAIL", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "PHOTO_LINK", nullable = false, columnDefinition = "TEXT")
    private String photoLink;

    @Column(name = "GENDER", length = 1, nullable = false)
    private String gender;

    @Column(name = "PHONE", length = 45, nullable = false)
    private String phone;

    @Column(name = "AGE", nullable = false)
    private Integer age;

    @Column(name = "INTRO", nullable = false, columnDefinition = "TEXT")
    private String intro;

    @Column(name = "START_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp startDate;

    @Column(name = "MODIFIED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp modifiedDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Singular
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String name, String email, String photoLink, String gender, String phone, Integer age, String intro, Timestamp startDate, Timestamp modifiedDate, List<String> roles) {
        this.name = name;
        this.email = email;
        this.photoLink = photoLink;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
        this.intro = intro;
        this.startDate = startDate;
        this.modifiedDate = modifiedDate;

        this.roles = roles;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return name;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}