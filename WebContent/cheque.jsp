<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="page" scope="page" value="cheque N ${cheque.id}"/>
<%@ include file="fragments/header.jspf"%>
<body>
	<div class="container">


		<h2><fmt:message key="cheque.title"/> N ${cheque.id}</h2>
		<!-- check -->
		<fmt:message key="cheque.text.shift"/> ${cheque.shiftId} ${cheque.createdBy.name} ${cheque.date}

		<%@ include file="fragments/messages.jspf"%>
	</div>
	<c:if test="${cheque == null}">karaul</c:if>
	<div class="container">
		<c:choose>
			<c:when test="${not empty cheque.products}">
				<table class="table table-striped">
					<thead>
						<tr>
							<td><fmt:message key="products.table.label.id" /></td>
							<td><fmt:message key="products.table.label.name" /></td>
							<td><fmt:message key="products.table.label.price" /></td>
							<td><fmt:message key="products.table.label.amount" /></td>
							<td></td>
							<td></td>
						</tr>
					</thead>
					<c:forEach var="product" items="${cheque.products}">
						<tr>
							<td>${product.productId}</td>
							<td>${product.name}</td>
							<td>${product.price}</td>
							<td>
								<form method="post">
									<input type="number" name="amount" id="amount"
										value="${product.amount}"
										${not empty cheque.date? 'disabled':''}>&nbsp;${product.units}


								
							</td>
							<td><c:choose>
									<c:when test="${empty cheque.date}">

										<input type="hidden" id="chequeId" name="chequeId" value="${cheque.id}">
										<input type="hidden" id="cpId" name="cpId" value="${product.id}">
										<button formaction="editincheque" class="btn btn-primary btn-sm"><fmt:message key="cheque.button.label.edit"/></button>
										<button formaction="removefromcheque"
											class="btn btn-primary btn-sm"><fmt:message key="cheque.button.label.remove"/></button>
										</form>
									</c:when>
									<c:otherwise>
										<c:if test="${role=='MANAGER'}">
											<form method="post">
												<input type="hidden" id="cancel" name="cancel" value="yes">
												<input type="hidden" id="cpId" name="cpId"
													value=${product.id}> <input type="hidden"
													id="chequeId" name="chequeId" value=${cheque.id}>
												<button type="submit" formaction="editincheque"
													class="btn btn-primary btn-sm"><fmt:message key="cheque.button.label.cancel"/></button>
											</form>
										</c:if>
									</c:otherwise>
								</c:choose></td>

						</tr>
					</c:forEach>

				</table>
				<table>
					<tr>
						<td colspan="6"><c:choose>
								<c:when test="${not empty cheque.cancelledDate}">
              <fmt:message key="cheques.text.status.cancelledby"/> ${cheque.cancelledBy.name} <br>
              <fmt:message key="cheques.text.status.on"/> ${cheque.cancelledDate} 
              </c:when>
								<c:otherwise>
									<h3>Total cost: ${cheque.cost} <fmt:message key="app.currency"/></h3>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>

			</c:when>
			<c:otherwise>
				<br>
				<br>
				<div class="alert alert-info"><fmt:message key="cheque.text.noproduct"/></div>
			</c:otherwise>
		</c:choose>
	</div>

	<form method="post">
		<input type="hidden" id="chequeId" name="chequeId" value=${cheque.id}>
		<c:choose>
			<c:when test="${empty cheque.date}">
				<div class="container">
					<button type="submit" formaction="productgetall?action=addtocheque"
						class="btn btn-primary btn-md"><fmt:message key="cheque.button.label.addproduct"/></button>

				</div>
				<br>
				<c:choose>
					<c:when test="${not empty cheque.products}">
						<div class="container">
							<button type="submit" formaction="chequeclose"
								class="btn btn-info btn-md"><fmt:message key="cheque.button.label.closecheque"/></button>
						</div>
						<br>
					</c:when>
					<c:otherwise>
						<div class="container">
							<button type="submit" formaction="chequedelete"
								class="btn btn-info btn-md"><fmt:message key="cheque.button.label.deletecheque"/></button>
						</div>
						<br>
					</c:otherwise>
				</c:choose>

			</c:when>
			<c:otherwise>

				<c:forEach var="role" items="${user.roles}">
					<c:if
						test="${role=='MANAGER' && empty cheque.cancelledDate && not empty cheque.date}">
						<div class="container">
							<button type="submit" formaction="chequecancel"
								class="btn btn-info btn-md"><fmt:message key="cheque.button.label.cancelcheque"/></button>
						</div>
					</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</form>
	</div>

	<%@ include file="fragments/footer.jspf"%>