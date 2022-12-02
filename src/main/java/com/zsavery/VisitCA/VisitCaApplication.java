package com.zsavery.VisitCA;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zsavery.VisitCA.domain.Difficulty;
import com.zsavery.VisitCA.domain.Region;
import com.zsavery.VisitCA.service.TourPackageService;
import com.zsavery.VisitCA.service.TourService;

@SpringBootApplication
public class VisitCaApplication implements CommandLineRunner{

	@Autowired
	private TourPackageService tourPackageService;

	@Autowired
	private TourService tourService;


	public static void main(String[] args) {
		SpringApplication.run(VisitCaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Create tour packages
		createTourPackages();
		long numberOfPackages = tourPackageService.total();

		//Load the tours from json file
		createTours("ExploreCalifornia.json");
		long numberOfTours = tourService.total();
	}

	/**
     * Initialize all the known tour packages
     */
	private void createTourPackages(){
        tourPackageService.createTourPackage("BC", "Backpack Cal");
        tourPackageService.createTourPackage("CC", "California Calm");
        tourPackageService.createTourPackage("CH", "California Hot springs");
        tourPackageService.createTourPackage("CY", "Cycle California");
        tourPackageService.createTourPackage("DS", "From Desert to Sea");
        tourPackageService.createTourPackage("KC", "Kids California");
        tourPackageService.createTourPackage("NW", "Nature Watch");
        tourPackageService.createTourPackage("SC", "Snowboard Cali");
        tourPackageService.createTourPackage("TC", "Taste of California");
    }

    /**
     * Create tour entities from an external file
     */
    private void createTours(String fileToImport) throws IOException {
        TourFromFile.read(fileToImport).forEach(importedTour ->
            tourService.createTour(importedTour.getTitle(),
                    importedTour.getDescription(),
                    importedTour.getBlurb(),
                    importedTour.getPrice(),
                    importedTour.getLength(),
                    importedTour.getBullets(),
                    importedTour.getKeywords(),
                    importedTour.getPackageType(),
                    importedTour.getDifficulty(),
                    importedTour.getRegion()));
    }

    /**
     * Helper class to import ExploreCalifornia.json
     */
    private static class TourFromFile {
        //fields
        private String packageType, title, description, blurb, price, length,
                bullets, keywords, difficulty, region;
        //reader
        static List<TourFromFile> read(String fileToImport) throws IOException {
            return new ObjectMapper().setVisibility(FIELD, ANY).
                    readValue(new FileInputStream(fileToImport), new TypeReference<List<TourFromFile>>() {});
        }
        protected TourFromFile(){}

        String getPackageType() { return packageType; }

        String getTitle() { return title; }

        String getDescription() { return description; }

        String getBlurb() { return blurb; }

        Integer getPrice() { return Integer.parseInt(price); }

        String getLength() { return length; }

        String getBullets() { return bullets; }

        String getKeywords() { return keywords; }

        Difficulty getDifficulty() { return Difficulty.valueOf(difficulty); }

        Region getRegion() { return Region.findByLabel(region); }
    }

	
}
