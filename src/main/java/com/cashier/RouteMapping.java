package com.cashier;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.cashier.controllers.ChequeAddProductController;
import com.cashier.controllers.ChequeCancelController;
import com.cashier.controllers.ChequeCloseController;
import com.cashier.controllers.ChequeDeleteController;
import com.cashier.controllers.ChequeEditController;
import com.cashier.controllers.ChequeGetAllController;
import com.cashier.controllers.ChequeProductEditController;
import com.cashier.controllers.ChequeProductRemoveController;
import com.cashier.controllers.LoginController;
import com.cashier.controllers.LogoutController;
import com.cashier.controllers.NotFoundController;
import com.cashier.controllers.ProductAddController;
import com.cashier.controllers.ProductDeleteController;
import com.cashier.controllers.ProductEditController;
import com.cashier.controllers.ProductUpdateController;
import com.cashier.controllers.ProductsGetAllController;
import com.cashier.controllers.ReportController;
import com.cashier.controllers.RoutingEntity;
import com.cashier.controllers.ShiftCloseController;
import com.cashier.controllers.ShiftGetAllController;
import com.cashier.controllers.ShiftOpenController;
import com.cashier.models.Role;


public class RouteMapping {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private static final List<RoutingEntity> routingEntities = new ArrayList<>();
	static {
		RoutingEntity routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("productgetone");
		routingEntity.setControllerClass(ProductEditController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequeproductadd");
		routingEntity.setControllerClass(ChequeAddProductController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("productnew");
		routingEntity.setControllerClass(ProductEditController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT, Role.CASHIER);
		routingEntity.setUrlPattern("productgetall");
		routingEntity.setControllerClass(ProductsGetAllController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("productedit");
		routingEntity.setControllerClass(ProductUpdateController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("productadd");
		routingEntity.setControllerClass(ProductAddController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("productdelete");
		routingEntity.setControllerClass(ProductDeleteController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequegetall");
		routingEntity.setControllerClass(ChequeGetAllController.class);
		routingEntities.add(routingEntity);

		//routingEntity = new RoutingEntity();
		//routingEntity.setRequiredRoles(Role.CASHIER);
		//routingEntity.setUrlPattern("chequeadd");
		//routingEntity.setControllerClass(ChequeAddController.class);
		//routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequeedit");
		routingEntity.setControllerClass(ChequeEditController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequedelete");
		routingEntity.setControllerClass(ChequeDeleteController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("editincheque");
		routingEntity.setControllerClass(ChequeProductEditController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("removefromcheque");
		routingEntity.setControllerClass(ChequeProductRemoveController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequecancel");
		routingEntity.setControllerClass(ChequeCancelController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER);
		routingEntity.setUrlPattern("chequeclose");
		routingEntity.setControllerClass(ChequeCloseController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.MANAGER);
		routingEntity.setUrlPattern("report");
		routingEntity.setControllerClass(ReportController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.MANAGER);
		routingEntity.setUrlPattern("shiftopen");
		routingEntity.setControllerClass(ShiftOpenController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.MANAGER);
		routingEntity.setUrlPattern("shiftclose");
		routingEntity.setControllerClass(ShiftCloseController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.MANAGER);
		routingEntity.setUrlPattern("shiftgetall");
		routingEntity.setControllerClass(ShiftGetAllController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.MANAGER);
		routingEntity.setUrlPattern("shiftget");
		routingEntity.setControllerClass(ShiftGetAllController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER, Role.CASHIER, Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("login");
		routingEntity.setControllerClass(LoginController.class);
		routingEntities.add(routingEntity);

		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER, Role.CASHIER, Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("logout");
		routingEntity.setControllerClass(LogoutController.class);
		routingEntities.add(routingEntity);
		
		routingEntity = new RoutingEntity();
		routingEntity.setRequiredRoles(Role.CASHIER, Role.CASHIER, Role.COMMODITY_EXPERT);
		routingEntity.setUrlPattern("");
		routingEntity.setControllerClass(NotFoundController.class);
		routingEntities.add(routingEntity);

	}
	
	private RouteMapping() {
		
	}
	
	public static RoutingEntity getRoute(HttpServletRequest request) {
		String url = request.getRequestURI();
		for (RoutingEntity entity : routingEntities) {
			if (url.contains(entity.getUrlPattern())) {
				return entity;
			}	
		}
		return null;
	}
	
	
}
