<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="fragments/header.jspf"%>
<body>
	<div class="container">
		<c:if test="${empty action}">
			<c:set var="action" value="add" />
		</c:if>
		<form action=${action=="edit"?'productedit':'productadd'}
			method="post" role="form" data-toggle="validator">

			<input type="hidden" id="action" name="action" value="${action}" /> <input
				type="hidden" id="id" name="id" value="${product.id}" />
			<h2>Product &nbsp;&nbsp;${product.id}</h2>
			
			<%@ include file="fragments/messages.jspf"%>

			<div class="form-group col-xs-4">
				<label for="name" class="control-label col-xs-4"><fmt:message
						key="product.label.name" />:</label> <input type="text" name="name"
					id="name" class="form-control" value="${product.name}"
					required="true" /> <label for="price"
					class="control-label col-xs-4"><fmt:message
						key="product.label.price" />:</label> <input type="text" name="price"
					id="price" class="form-control" value="${product.price}"
					required="true" /> <label for="amount"
					class="control-label col-xs-4"><fmt:message
						key="product.label.amount" />:</label> <input type="text" name="amount"
					id="amount" class="form-control" value="${product.amount}"
					required="true" /> <label for="units"
					class="control-label col-xs-4"><fmt:message
						key="product.label.units" />:</label> <select name="units" id="units"
					class="form-control" required="true">
					<option value="KG"
						${product.units=='KG'? 'selected="selected"' : ''}>kg</option>
					<option value="LITRE"
						${product.units=='LITRE'? 'selected="selected"' : ''}>
						litre</option>
					<option value="PIECE"
						${product.units == 'PIECE'? 'selected="selected"' : ''}>
						piece</option>
				</select> <br></br>
				<button type="submit" class="btn btn-primary  btn-md">${action=="edit"?'Accept':'Add'}</button>
			</div>
		</form>
	</div>
	<%@ include file="fragments/footer.jspf"%>