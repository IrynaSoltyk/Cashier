<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setCharacterEncoding("UTF-8"); %>

<c:set var="page" scope="page" value="products" />
<%@ include file="fragments/header.jspf"%>
<div class="container">
	<h2><fmt:message key="products.title"/> <fmt:message key="products.text.${not empty param.action? param.action : 'emptystr'}"/></h2>

	<!--Search Form -->


	<form action="productgetall" method="get">
		<div class="input-group">
			<c:if test="${not empty param.chequeId}">
				<input type="hidden" id="chequeId" name="chequeId"
					value="${param.chequeId}" />
			</c:if>
			<input type="hidden" id="action" name="action" value="search" /> <input
				type="text" name="searchPattern" id="searchPattern"
				class="form-control" required="true"
				placeholder="<fmt:message key='products.label.search.input.placeholder'/>" />

			<div class="input-group-btn">
				<button type="submit" class="btn btn-info">
					<span class="glyphicon glyphicon-search"></span><fmt:message key="products.button.label.search"/>
				</button>
			</div>
			<br></br>
		</div>
	</form>

</div>
</div>
<%@ include file="fragments/messages.jspf"%>
<!-- table -->


<c:choose>
	<c:when test="${not empty products}">
<%@ include file="fragments/limit.jspf"%>
			<table class="table table-striped">
				<thead>
					<tr>
						<td><fmt:message key="products.table.label.id" /></td>
						<td><fmt:message key="products.table.label.name" /></td>
						<td><fmt:message key="products.table.label.price" /></td>
						<td><fmt:message key="products.table.label.amount" /></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</thead>
				<c:forEach var="product" items="${products}">
					<c:set var="classProducts" value="" />
					<c:if test="${id == priduct.id}">
						<c:set var="classSucess" value="info" />
					</c:if>
					<tr class="${classProducts}">
						<td>${product.id}</td>
						<td>${product.name}</td>
						<td>${product.price} <fmt:message key="app.currency"/></td>
						<td>${product.amount}&nbsp;${product.units}</td>
						<td>
							<!-- if user role = cashier --> <c:forEach var="role"
								items="${user.roles}">
								<c:choose>
									<c:when test="${role=='CASHIER' && not empty param.chequeId}">
										<form>
											<div class="input-group input-group-sm">
												<input type="hidden" id="productId" name="productId" value=${product.id}> 
												<input type="hidden" id="chequeId" name="chequeId" value=${param.chequeId}>
												<input type="number" class="form-control" name="amount"	placeholder="<fmt:message key='products.label.addtocheque.placeholder'/>" max="${product.amount}" min="1">
												<div class="input-group-btn">
													<button type="submit" formaction="chequeproductadd"	class="btn btn-primary btn-sm"><fmt:message key="products.button.label.addtocheque"/></button>
												</div>
											</div>
										</form>
									</c:when>
								</c:choose>
							</c:forEach>
						</td>
						<!--  if user role = commodityexp -->
						<c:forEach var="role" items="${user.roles}">
							<c:choose>
								<c:when test="${role=='COMMODITY_EXPERT'}">
									<td>
										<form method="get" id="product" role="form">
										<input type="hidden" id="productId" name="productId" value=${product.id}>
 										<button type="submit" formaction="productgetone" class="btn btn-primary btn-sm"><fmt:message key="products.button.label.edit"/></button></td>
										</form>
									<td>
										<form method="post" id="product" role="form" class=>
										<input type="hidden" id="productId" name="productId" value=${product.id}>
										<button tye="submit" formaction="productdelete" class="btn btn-primary btn-sm"><fmt:message key="products.button.label.delete"/></button>
									</form>
									</td>
								</c:when>
								<c:otherwise>
									<td></td>
									<td></td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
	</c:when>
	<c:otherwise>
		<br>
		</br>
		<div class="alert alert-info"><fmt:message key="products.text.noproductsfound" /></div>
	</c:otherwise>
</c:choose>

<%@ include file="fragments/pagination.jspf"%>

<br>
<br>
<table>
<tr>
<td>
<c:if test="${not empty param.searchPattern}">
<form method="post" action="productgetall">
	<button type="submit" class="btn btn-primary  btn-md"><fmt:message key="products.button.label.gobacktoproductlist"/></button>
</form>
</c:if>
</td>
<td>
<c:forEach var="role" items="${user.roles}">
	<c:choose>
		<c:when test="${role=='COMMODITY_EXPERT'}">
			<div class="container">
				<form method="post" action="productnew">
					<button type="submit" class="btn btn-primary  btn-md"><fmt:message key="products.button.label.addnewproduct"/></button>
				</form>
			</div>
		</c:when>
	</c:choose>
</c:forEach>
<td>
</tr>
</table>
<%@ include file="fragments/footer.jspf"%>