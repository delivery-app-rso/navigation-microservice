package si.fri.rso.navigationmicroservice.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.maps.model.DistanceMatrixElement;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.lib.NavigationDto;
import si.fri.rso.navigationmicroservice.models.converters.NavigationConverter;
import si.fri.rso.navigationmicroservice.models.entities.NavigationEntity;
import si.fri.rso.navigationmicroservice.services.clients.GoogleMapsApi;

@RequestScoped
public class NavigationBean {

    private Logger log = Logger.getLogger(NavigationBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private GoogleMapsApi googleMapsApi;

    public List<Navigation> getNavigations() {

        TypedQuery<NavigationEntity> query = em.createNamedQuery(
                "NavigationEntity.getAll", NavigationEntity.class);

        List<NavigationEntity> resultList = query.getResultList();

        return resultList.stream().map(NavigationConverter::toDto).collect(Collectors.toList());

    }

    public List<Navigation> getNavigationFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, NavigationEntity.class, queryParameters).stream()
                .map(NavigationConverter::toDto).collect(Collectors.toList());
    }

    public Navigation getNavigation(Integer id) {

        NavigationEntity navigationEntity = em.find(NavigationEntity.class, id);

        if (navigationEntity == null) {
            throw new NotFoundException();
        }

        Navigation item = NavigationConverter.toDto(navigationEntity);

        return item;
    }

    public Navigation createNavigation(NavigationDto navigationDto) {
        String[] origins = { navigationDto.getOrigin() };
        String[] destinations = { navigationDto.getDestination() };

        // We only use one origin and destination
        List<DistanceMatrixElement[]> matrixElements = googleMapsApi.getDistanceMatrixData(origins, destinations);
        DistanceMatrixElement[] distanceMatrixElements = matrixElements.get(0);

        NavigationEntity navigationEntity = new NavigationEntity();
        navigationEntity.setDeliveryId(navigationDto.getDeliveryId());
        navigationEntity.setDistance(distanceMatrixElements[0].distance.toString());
        navigationEntity.setTime(distanceMatrixElements[0].duration.toString());

        this.persistNavigation(navigationEntity);

        return NavigationConverter.toDto(navigationEntity);
    }

    public Navigation persistNavigation(NavigationEntity navigationEntity) {
        try {
            beginTx();
            em.persist(navigationEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (navigationEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return NavigationConverter.toDto(navigationEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
