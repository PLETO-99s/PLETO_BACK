package gugus.pleco.domain.plee.domain;

import gugus.pleco.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "plee_id")
    private Long id;

    @Column(name = "plee_name")
    private String pleeName;

    @Column(name = "eco_count")
    private Long ecoCount;

    @Column(name = "complete_count")
    private Long completeCount;

    @Enumerated(EnumType.STRING)
    private PleeStatus pleeStatus;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    //이름 설정
    public void setPleeName(String pleeName){
        this.pleeName=pleeName;
    }

    //연관 관계 로직
    public void setUser(User user){
        this.user=user;
        user.addPlee(this);
    }

    public void setGrow(Long completeCount) {
        this.ecoCount = 0L;
        this.completeCount = completeCount;
        this.pleeStatus = PleeStatus.GROWING;
    }

    //생성 로직
    public static Plee createPlee(User user, String pleeName, Long completeCount){
        Plee plee = new Plee();
        plee.setUser(user);
        plee.setPleeName(pleeName);
        plee.setGrow(completeCount);

        return plee;
    }

    // eco 증가 로직
    public void addEcoCount(){


        this.ecoCount += 1;

        if (this.ecoCount >= this.completeCount) {
            complete();
        }
    }

    private void complete() {
        this.pleeStatus = PleeStatus.COMPLETE;
    }

}
