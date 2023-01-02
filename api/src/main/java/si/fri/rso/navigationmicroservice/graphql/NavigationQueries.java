import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.kumuluz.ee.graphql.annotations.GraphQLApplicationClass;
import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.fri.rso.navigationmicroservice.lib.Navigation;
import si.fri.rso.navigationmicroservice.services.beans.NavigationBean;

@ApplicationScoped
@GraphQLClass
public class NavigationQueries {
    @Inject
    private NavigationBean navigationBean;

    @GraphQLQuery
    public PaginationWrapper<Navigation> getAllNavigations(@GraphQLArgument(name = "pagination") Pagination pagination,
            @GraphQLArgument(name = "sort") Sort sort,
            @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(navigationBean.getNavigations(), pagination, sort, filter);
    }

    @GraphQLQuery
    public Navigation getNavigation(@GraphQLArgument(name = "deliveryId") Integer deliveryId) {
        return navigationBean.getNavigation(deliveryId);
    }
}