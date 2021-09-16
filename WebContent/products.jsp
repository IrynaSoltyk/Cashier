<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="page" scope="page" value="product" />
<%@ include file="fragments/header.jspf"%>
<div class="container">
	<h2>Products ${param.action}</h2>

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
				placeholder="Type the product name or id" />

			<div class="input-group-btn">
				<button type="submit" class="btn btn-info">
					<span class="glyphicon glyphicon-search"></span> Search
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
		<div class=row">
			<div class="col10">
				<span class="badge">${count}</span> products in total
			</div>

			<div class="col2">
				Show: <a href="${pageContext.request.queryString}">5</a>
				|<a href="${pageContext.request.pathInfo}?${not empty param.page}?'page='+${param.page}:''}&limit=10">10</a>
				|<a href="?limit=15">15</a>

			</div>
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
						<td>${product.price}</td>
						<td>${product.amount}&nbsp;${product.units}</td>
						<td>
							<!-- if user role = cashier --> <c:forEach var="role"
								items="${user.roles}">
								<c:choose>
									<c:when test="${role=='CASHIER' && not empty param.chequeId}">
										<form>
											<div class="input-group input-group-sm">
												<input type="hidden" id="productId" name="productId"
													value=${product.id}> <input type="hidden"
													id="chequeId" name="chequeId" value=${param.chequeId}>
												<input type="number" class="form-control" name="amount"
													placeholder="put in amount" max="${product.amount}" min="1">
												<div class="input-group-btn">
													<button type="submit" formaction="chequeproductadd"
														class="btn btn-primary btn-sm">Add to cheque</button>
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
 										<button type="submit" formaction="productgetone" class="btn btn-primary btn-sm">Edit</button></td>
										</form>
									<td>
										<form method="post" id="product" role="form" class=>
										<input type="hidden" id="productId" name="productId" value=${product.id}>
										<button tye="submit" formaction="productdelete" class="btn btn-primary btn-sm">Delete</button>
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
		<div class="alert alert-info">No products found matching your
			search criteria</div>
	</c:otherwise>
</c:choose>
<!-- PAGINATION -->
<div class="container">
	<table border="0" cellpadding="5" cellspacing="5">
		<tr>
			<c:if test="${pagesN>1}">
				<c:forEach begin="1" end="${pagesN}" var="i">
					<c:choose>
						<c:when test="${pageN eq i}">
							<td>${i}</td>
						</c:when>
						<c:otherwise>
							<td><a href="productgetall?page=${i}&limit=${limit}">${i}</a></td>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
		</tr>
	</table>
</div>

<br>
<br>
<table>
<tr>
<td>
<c:if test="${not empty param.searchPattern}">
<form method="post" action="productgetall">
	<button type="submit" class="btn btn-primary  btn-md">Go back to product list</button>
</form>
</c:if>
</td>
<td>
<c:forEach var="role" items="${user.roles}">
	<c:choose>
		<c:when test="${role=='COMMODITY_EXPERT'}">
			<div class="container">
				<form method="post" action="productnew">
					<button type="submit" class="btn btn-primary  btn-md">Add new product</button>
				</form>
			</div>
		</c:when>
	</c:choose>
</c:forEach>
<td>
</tr>
</table>
<%@ include file="fragments/footer.jspf"%>