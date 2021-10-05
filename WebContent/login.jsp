<%@ include file="fragments/header.jspf"%>

<c:set var="page" scope="page" value="login" />

<div class="container">
	<h2>
		<fmt:message key="login.label.title" />
	</h2>
	<form action="login" method="post">
		<c:if test="${not empty loginErrorMsg}">
			<div class="alert alert-error">${loginErrorMsg}</div>
			<c:remove var="errorMsg" scope="session" />
		</c:if>
	<div>
			<label for="login"><fmt:message key="login.label.login" />:</label>
			<input type="text" class="form-control" id="login" name="login"
				required>
		</div>
		<div class="form-group">
			<label for="password"><fmt:message key="login.label.password" />:</label>
			<input type="password" class="form-control" id="password"
				name="password" required>
		</div>
		<button type="submit" class="btn btn-primary">
			<fmt:message key="login.label.submit" />
		</button>
	</form>
</div>
<%@ include file="fragments/footer.jspf"%>