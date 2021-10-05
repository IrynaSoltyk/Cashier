<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="page" scope="page" value="report"/>

<%@ include file="fragments/header.jspf"%>
	<script>
		function printDiv(divName){
			var printContents = document.getElementById(divName).innerHTML;
			var originalContents = document.body.innerHTML;

			document.body.innerHTML = printContents;
			window.print();
			document.body.innerHTML = originalContents;

		}
	</script>
<div  id="toprint">
<div class="container">
	<h2>${param.type}&nbsp;<fmt:message key="report.title"/></h2>

	<%@ include file="fragments/messages.jspf"%>
</div>

<div class="container"  id="toprint">
	<fmt:message key="report.text.shift"/> # ${shiftId}
	${user.name}
	<table class="table">
		<tr class="active">
			<td colspan="2"><h5><fmt:message key="report.text.sell"/></h5></td>
		</tr>
		<tr>
			<td><fmt:message key="report.text.overall"/> <fmt:message key="report.text.cheques"/></td>
			<td>${report.closed}&nbsp;<fmt:message key="report.text.cheques"/></td>
		</tr>
		<tr>
			<td><fmt:message key="report.text.overall"/> <fmt:message key="report.text.sum"/></td>
			<td>${report.closedCost}&nbsp; <fmt:message key="app.currency"/></td>
		</tr>
		<tr  class="active" >
			<td colspan="2"><h5><fmt:message key="report.text.return"/></h5></td>
		</tr>
		<tr>
			<td><fmt:message key="report.text.overall"/> <fmt:message key="report.text.cancelled"/></td>
			<td>${report.cancelled}&nbsp;<fmt:message key="report.text.cheques"/></td>
		</tr>
		<tr>
			<td><fmt:message key="report.text.overall"/> <fmt:message key="report.text.sum"/></td>
			<td>${report.cancelledCost}&nbsp;<fmt:message key="app.currency"/></td>
		</tr>
	</table>
</div>
</div>
<div class="container">
<button onclick="printDiv('toprint');" class="btn btn-info"><fmt:message key="repotr.button.label.print"/></button>
</div>

<%@ include file="fragments/footer.jspf"%>