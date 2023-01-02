package si.fri.rso.navigationmicroservice.services.clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;

import si.fri.rso.navigationmicroservice.services.config.GoogleMapsProperties;

@ApplicationScoped
public class GoogleMapsApi {
    @Inject
    private GoogleMapsProperties googleMapsProperties;

    private GeoApiContext context;

    @PostConstruct
    public void initApi() {
        context = new GeoApiContext.Builder().apiKey(googleMapsProperties.getApiKey())
                .build();
    }

    public List<DistanceMatrixElement[]> getDistanceMatrixData(String[] origin, String[] destination) {
        List<DistanceMatrixElement[]> matrixElements = new ArrayList<>();

        try {
            DistanceMatrix distanceMatrix = DistanceMatrixApi.getDistanceMatrix(context, origin, destination).await();
            DistanceMatrixRow[] distanceMatrixRows = distanceMatrix.rows;

            for (DistanceMatrixRow distanceMatrixRow : distanceMatrixRows) {
                matrixElements.add(distanceMatrixRow.elements);
            }

        } catch (ApiException | InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return matrixElements;
    }
}
