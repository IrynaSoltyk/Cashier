<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="page" scope="page" value="product" />
<%@ include file="fragments/header.jspf"%>
<div class="container">
	<h2>Shifts</h2>
<%@ include file="fragments/messages.jspf"%>
</div>
<div>
	<c:choose>
		<c:when test="${not empty shifts}">
			<div class=row">
				<div class="col10">
					<span class="badge">${count}</span> shifts in total
				</div>

				<div class="col2">
					Show: <a href="?limit=5">5</a>|<a href="?limit=10">10</a>|<a
						href="?limit=15">15</a>

				</div>
			</div>
			<table class="table table-striped">
				<thead>
					<tr>
						<td>shift id</td>
						<td>begin date</td>
						<td>end date</td>
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
											class="btn btn-primary btn-md">Close</button></td>
									<td><button type="submit" formaction="report"
											class="btn btn-primary  btn-md">Form Xreport</button></td>
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
										class="btn btn-primary btn-sm">Show Z report</button></td>
								</form>
							</c:otherwise>
						</c:choose>

					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<br>
			<div class="alert alert-info">No shifts yet</div>
		</c:otherwise>
	</c:choose>
</div>
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
							<td><a href="shiftgetall?page=${i}&limit=${limit}">${i}</a></td>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
		</tr>
	</table>
</div>
<br>
<br>
<div class="container">

	<c:choose>
		<c:when test="${empty shiftId}">
		<form method="get" id="shift" role="form">
			<button type="submit" formaction="shiftopen"
				class="btn btn-primary  btn-md">Start new shift</button>
				</form>
		</c:when>
		<c:otherwise>
			<form method="get" id="shift" role="form">
				<input type="hidden" id="shiftId" name="shiftId" value=${shift.id}>
				<input type="hidden" id="type" name="type" value="X">
				<button type="submit" formaction="shiftclose"
					class="btn btn-primary  btn-md">Close current shift</button>
				<button type="submit" formaction="report"
					class="btn btn-primary  btn-md">Form Xreport</button>
			</form>
		</c:otherwise>
	</c:choose>

</div>
<%@ include file="fragments/footer.jspf"%>
