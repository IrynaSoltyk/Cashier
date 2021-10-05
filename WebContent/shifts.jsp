<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="page" scope="page" value="shifts" />
<%@ include file="fragments/header.jspf"%>
<div class="container">
	<h2><fmt:message key="shifts.title"/></h2>
<%@ include file="fragments/messages.jspf"%>
</div>
<div>
	<c:choose>
		<c:when test="${not empty shifts}">
			<%@ include file="fragments/limit.jspf"%>
			<table class="table table-striped">
				<thead>
					<tr>
						<td><fmt:message key="shifts.table.label.shiftid"/></td>
						<td><fmt:message key="shifts.table.label.begindate"/></td>
						<td><fmt:message key="shifts.table.label.enddate"/></td>
						<td></td>
						<td></td>
					</tr>
				</thead>
				<c:forEach var="shift" items="${shifts}">
					<c:set var="classShifts" value="" />
					<c:if test="${shiftId == shift.id}">
						<c:set var="classSucess" value="info" />
					</c:if>
					<tr class="${classShifts}">

						<td>${shift.id}</td>
						<td>${shift.beginDate}</td>
						<c:choose>
							<c:when test="${empty shift.endDate}">
								<td>Current shift</td>
								<form method="get" id="shift" role="form">
									<input type="hidden" id="shiftId" name="shiftId"
										value="${shift.id}"> <input type="hidden" id="type"
										name="type" value="X">
									<td><button type="submit" formaction="shiftclose"
											class="btn btn-primary btn-md"><fmt:message key="shifts.button.label.close"/></button></td>
									<td><button type="submit" formaction="report"
											class="btn btn-primary  btn-md"><fmt:message key="shifts.button.label.xreport"/></button></td>
								</form>
							</c:when>
							<c:otherwise>
								<td>${shift.endDate}</td>
								<form method="get" id="shift" role="form">
									<input type="hidden" id="shiftId" name="shiftId"
										value="${shift.id}"> <input type="hidden" id="type"
										name="type" value="Z">
								<td></td>
								<td><button type="submit" formaction="report"
										class="btn btn-primary btn-sm"><fmt:message key="shifts.button.label.zreport"/></button></td>
								</form>
							</c:otherwise>
						</c:choose>

					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<br>
			<div class="alert alert-info"><fmt:message key="shifts.text.noshifts"/></div>
		</c:otherwise>
	</c:choose>
</div>

<%@ include file="fragments/pagination.jspf"%>

<br>
<br>
<div class="container">

	<c:choose>
		<c:when test="${empty shiftId}">
		<form method="get" id="shift" role="form">
			<button type="submit" formaction="shiftopen"
				class="btn btn-primary  btn-md"><fmt:message key="shifts.button.label.new"/></button>
				</form>
		</c:when>
		<c:otherwise>
			<form method="get" id="shift" role="form">
				<input type="hidden" id="shiftId" name="shiftId" value=${shift.id}>
				<input type="hidden" id="type" name="type" value="X">
				<button type="submit" formaction="shiftclose"
					class="btn btn-primary  btn-md"><fmt:message key="shifts.button.label.close"/></button>
				<button type="submit" formaction="report"
					class="btn btn-primary  btn-md"><fmt:message key="shifts.button.label.xreport"/></button>
			</form>
		</c:otherwise>
	</c:choose>

</div>
<%@ include file="fragments/footer.jspf"%>
