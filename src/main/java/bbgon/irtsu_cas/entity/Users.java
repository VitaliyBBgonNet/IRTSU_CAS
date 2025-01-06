package bbgon.irtsu_cas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String lastName;
    private String surname;
    private String email;
    private String password;
    private String position;
    private String avtar;
    private String phone;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private List<Details> userDetails;

    private LocalDateTime createdAccount;
}
