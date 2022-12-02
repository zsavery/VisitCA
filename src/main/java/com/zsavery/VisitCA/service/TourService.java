package com.zsavery.VisitCA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsavery.VisitCA.domain.Difficulty;
import com.zsavery.VisitCA.domain.Region;
import com.zsavery.VisitCA.domain.Tour;
import com.zsavery.VisitCA.domain.TourPackage;
import com.zsavery.VisitCA.repo.TourPackageRepository;
import com.zsavery.VisitCA.repo.TourRepository;

@Service
public class TourService {
    private TourPackageRepository tourPackageRepository;
    private TourRepository tourRepository;

    @Autowired
    public TourService(TourPackageRepository tourPackageRepository, TourRepository tourRepository) {
        this.tourPackageRepository = tourPackageRepository;
        this.tourRepository = tourRepository;
    }

    public Tour createTour(String title, String description, String blurb, Integer price,
                            String duration, String bullets, String keywords,
                            String tourPackageName, Difficulty difficulty, Region region) {
        TourPackage tourPackage = tourPackageRepository.findById(tourPackageName)
        .orElseThrow(() -> new RuntimeException("Tour package does not exist" + tourPackageName));

        return tourRepository.save(new Tour(title, description, blurb, price, duration, bullets, 
                        keywords, tourPackage, difficulty, region));

    }

    public long total(){
        return tourRepository.count();
    }
}
