package com.app.zhardem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "distance")
    private double distance;

    @Column(name = "about_text")
    private String aboutText;

    @Column(name = "price_of_doctor")
    private int priceOfDoctor;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "doctor")
    private List<Review> reviews;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointments> appointments;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    public double getAverageRating(){
        double sum = 0.0;
        if (reviews != null && !reviews.isEmpty()){
            for(Review review : reviews){
                sum+=review.getRating();
            }
            return sum / reviews.size();
        }
        return 0.0;
    }

}
