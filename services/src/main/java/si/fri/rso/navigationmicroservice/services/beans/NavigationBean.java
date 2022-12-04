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

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.models.converters.NavigationConverter;
import si.fri.rso.navigationmicroservice.models.entities.NavigationEntity;


@RequestScoped
public class NavigationBean {//should be ok

    private Logger log = Logger.getLogger(NavigationBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Navigation> getAddresses() {

        TypedQuery<NavigationEntity> query = em.createNamedQuery(
                "NavigationEntity.getAll", NavigationEntity.class);

        List<NavigationEntity> resultList = query.getResultList();

        return resultList.stream().map(NavigationConverter::toDto).collect(Collectors.toList());

    }

    public List<Navigation> getAddressFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, NavigationEntity.class, queryParameters).stream()
                .map(NavigationConverter::toDto).collect(Collectors.toList());
    }

    public Navigation getAddresses(Integer id) {

        NavigationEntity navigationEntity = em.find(NavigationEntity.class, id);

        if (navigationEntity == null) {
            throw new NotFoundException();
        }

        Navigation item = NavigationConverter.toDto(navigationEntity);

        return item;
    }

    public Navigation createItem(Navigation item) {

        NavigationEntity navigationEntity = NavigationConverter.toEntity(item);

        try {
            beginTx();
            em.persist(navigationEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (navigationEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return NavigationConverter.toDto(navigationEntity);
    }

    public Navigation putItem(Integer id, Navigation item) {

        NavigationEntity c = em.find(NavigationEntity.class, id);

        if (c == null) {
            return null;
        }

        NavigationEntity updatedNavigationEntity = NavigationConverter.toEntity(item);

        try {
            beginTx();
            updatedNavigationEntity.setId(c.getId());
            updatedNavigationEntity = em.merge(updatedNavigationEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return NavigationConverter.toDto(updatedNavigationEntity);
    }

    public boolean deleteItem(Integer id) {

        NavigationEntity navigationEntity = em.find(NavigationEntity.class, id);

        if (navigationEntity != null) {
            try {
                beginTx();
                em.remove(navigationEntity);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
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
