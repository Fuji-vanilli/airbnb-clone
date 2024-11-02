package com.fuji.airbnb_clone_backend.listing.domain;

import com.fuji.airbnb_clone_backend.shared.kernel.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "listing")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Listing extends AbstractAuditingEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listingSequenceGenerator")
    @SequenceGenerator(name = "listingSequenceGenerator", sequenceName = "listing_generator", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "guests")
    private int guests;

    @Column(name = "bedrooms")
    private int bedrooms;

    @Column(name = "beds")
    private int beds;

    @Column(name = "bathrooms")
    private int bathrooms;

    @Column(name = "public_id")
    private UUID publicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_category")
    private BookingCategory bookingCategory;

    @Column(name = "location")
    private String location;

    @Column(name = "landlord_public_id")
    private UUID landLordPublicId;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.REMOVE)
    private Set<ListingPicture> pictures= new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }
}
