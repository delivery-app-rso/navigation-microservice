package si.fri.rso.navigationmicroservice.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "addresses")
@NamedQueries(value = {
        @NamedQuery(name = "NavigationEntity.getAll", query = "SELECT im FROM NavigationEntity im")
})
public class NavigationEntity {//should be ok

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "sentOn")
    private String sentOn;

    @Column(name = "deliveredOn")
    private String deliveredOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSentOn() {
        return sentOn;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = sentOn;
    }

    public String getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(String deliveredOn) {
        this.deliveredOn = deliveredOn;
    }
}