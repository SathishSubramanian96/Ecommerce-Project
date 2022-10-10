package com.luv2code.ecommerce.config;

import java.util.ArrayList; 
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.luv2code.ecommerce.entity.Country;
import com.luv2code.ecommerce.entity.Order;
import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import com.luv2code.ecommerce.entity.State;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{
	
	@Value("${allowed.origins}")
	private String[] theAllowedOrigins;
	
	private EntityManager entityManager;
	@Autowired
	public MyDataRestConfig(EntityManager theentityManager) {
		entityManager= theentityManager;
	}
	

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,CorsRegistry cors) {
		
		HttpMethod[] theUnsupportedActions = {HttpMethod.PUT,HttpMethod.DELETE,
				                             HttpMethod.POST,HttpMethod.PATCH};
		
		diasableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
		diasableHttpMethods(Product.class, config, theUnsupportedActions);
		diasableHttpMethods(Country.class, config, theUnsupportedActions);
		diasableHttpMethods(State.class, config, theUnsupportedActions);
		diasableHttpMethods(Order.class, config, theUnsupportedActions);
		
		
		// call an internal helper methods
		exposeIds(config);
		
		//configure cors mapping
		cors.addMapping( config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
		
	}


	private void diasableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration()
	      .forDomainType(theClass)
	      .withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
	      .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
	}


	private void exposeIds(RepositoryRestConfiguration config) {
		//expose entity ids
		//
		
		//get lists of all entity classes from entity Manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		//create an array of the entity type
		List<Class> entityClasses = new ArrayList<>();
		
		//get the entitiy types for the entities
		for(EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());	
		}
		
		// - expose the entityids of the arrayof entity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
		
	}
	
	

	
}
