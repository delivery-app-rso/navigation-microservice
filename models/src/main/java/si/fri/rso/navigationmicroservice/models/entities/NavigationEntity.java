package si.fri.rso.navigationmicroservice.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "navigations")
@NamedQueries(value = {
        @NamedQuery(name = "NavigationEntity.getAll", query = "SELECT im FROM NavigationEntity im")
})
public class NavigationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deliveryId")
    private Integer deliveryId;

    @Column(name = "distance")
    private String distance;

    @Column(name = "time")
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}