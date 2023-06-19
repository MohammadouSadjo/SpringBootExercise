package springboot.test.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "userproject")
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "project_id")
    private String project_id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "owner")
    private Boolean owner;
}
