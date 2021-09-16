<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="page" scope="page" value="cheques" />
<%@ include file="fragments/header.jspf"%>
<div class="container">
	<h2>${param.type}&nbsp;Report</h2>
	<%@ include file="fragments/messages.jspf"%>
</div>

<div class="container">
	Shift # ${shiftId}
	${user.name}
	<table class="table">
		<tr class="active">
			<td colspan="2"><h5>Sell</h5></td>
		</tr>
		<tr>
			<td>Overall cheques</td>
			<td>${report.closed}&nbsp;cheques</td>
		</tr>
		<tr>
			<td>Overall sum</td>
			<td>${report.closedCost}&nbsp; grn.</td>
		</tr>
		<tr  class="active" >
			<td colspan="2"><h5>Return</h5></td>
		</tr>
		<tr>
			<td>Overall cancelled</td>
			<td>${report.cancelled}&nbsp;cheques</td>
		</tr>
		<tr>
			<td>Overall sum</td>
			<td>${report.cancelledCost}&nbsp;grn.</td>
		</tr>
	</table>
</div>
<div class="container">
<button onclick="window.print();" class="btn btn-info">Print report</button>
</div>

<%@ include file="fragments/footer.jspf"%>