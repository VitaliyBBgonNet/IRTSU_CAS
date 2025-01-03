package bbgon.irtsu_cas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String details;
    private String documentation;
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
