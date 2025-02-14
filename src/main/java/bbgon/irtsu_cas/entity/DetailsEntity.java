package bbgon.irtsu_cas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class DetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String image;

    private String name;
    private String description;
    private String documentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UsersEntity owner;

    private String status;

    private LocalDateTime createdDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "rent_id")
    private RentEntity rent;

    private String tenant;
}

