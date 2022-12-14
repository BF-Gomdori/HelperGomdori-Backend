package bf.multi.server.domain.user;

import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helper.Helper;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"helper", "helpee"})
//@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 45, nullable = false)
    private String username;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false, unique = true)
    private String password;

    @Column(name = "PHOTO_LINK", nullable = false, columnDefinition = "TEXT")
    private String photoLink;

    @Column(name = "GENDER", length = 10, nullable = false)
    private String gender;

    @Column(name = "PHONE", length = 45, nullable = false)
    private String phone;

    @Column(name = "AGE", nullable = false)
    private String age;

    @Column(name = "START_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp startDate;

    @Column(name = "MODIFIED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp modifiedDate;

    @Column(name = "FCM_TOKEN", nullable = false, columnDefinition = "TEXT")
    private String FCMToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Helper helper;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Helpee helpee;

    @PrePersist
    public void prePersist(){
        this.role = this.role == null ? UserRole.ROLE_USER : this.role;
    }

    @Builder
    public User(String name, String email, String password,
                String photoLink, String gender, String phone,
                String age, Timestamp startDate, Timestamp modifiedDate,
                String FCMToken, UserRole roleUser) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.photoLink = photoLink;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
        this.startDate = startDate;
        this.modifiedDate = modifiedDate;
        this.FCMToken = FCMToken;
        this.role = roleUser;
    }

}
