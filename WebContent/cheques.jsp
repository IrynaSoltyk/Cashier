<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="page" scope="page" value="cheques"/>
<%@ include file="fragments/header.jspf" %>
<div class="container"> 
  <h2><fmt:message key="cheques.title"/></h2>
</div> 
<%@ include file="fragments/messages.jspf"%>
<!-- table -->

  <c:choose>
      <c:when test="${not empty cheques}">
      <%@ include file="fragments/limit.jspf"%>
          <table  class="table table-striped">
              <thead>
                  <tr>
                      <td><fmt:message key="cheques.table.label.id"/></td>
                      <td><fmt:message key="cheques.table.label.date"/></td>
                      <td><fmt:message key="cheques.table.label.cost"/></td>
                      <td><fmt:message key="cheques.table.label.status"/></td>
                      <td><fmt:message key="cheques.table.label.cashiername"/></td> 
                      <td></td>
                      <td></td>
                  </tr>
              </thead>
              <c:forEach var="cheque" items="${cheques}">
                 
                  <tr>
                  <form  method="get" id="cheque" role="form" >    
                  <input type="hidden" id="action" name="action" value="edit">
  				  <input type="hidden" id="chequeId" name="chequeId" value=${cheque.id}>
                      <td>${cheque.id}</td>
                      <td>${cheque.date}</td>
                      <td>${cheque.cost} grn.</td>
                      <td>
                      <c:choose>
                     	 <c:when test="${empty cheque.date}">
                      		<fmt:message key="cheques.text.status.open"/>
                     	 </c:when>
                      	<c:when test="${empty cheque.cancelledDate}">
                    		<fmt:message key="cheques.text.status.closed"/>
                     	 </c:when>
                     	 <c:otherwise>
                      		 <fmt:message key="cheques.text.status.cancelledby"/> ${cheque.cancelledBy.name} <br>
                     		 <fmt:message key="cheques.text.status.on"/> ${cheque.cancelledDate}
                     	 </c:otherwise>
                      </c:choose>
                        </td>
                      <td>${cheque.createdBy.name}</td>
                      <td><button type="submit" formaction="chequeedit" class="btn btn-primary btn-sm"><fmt:message key="cheques.button.label.${not empty cheque.date? 'show':'edit'}"/></button> </td>
					   </form>					   
					   <td>
					   <c:forEach var="role" items="${user.roles}">
  				  	 	<c:choose>
  				  	 	<c:when test="${role=='MANAGER' && empty cheque.cancelledDate && not empty cheque.date}">
					   <form  method="get" >    
  				  		<input type="hidden" id="chequeId" name="chequeId" value=${cheque.id}>
					   <button type="submit" formaction="chequecancel" class="btn btn-primary btn-sm"><fmt:message key="cheques.button.label.cancelcheque"/></button>
					   </form>
					   </c:when>
					   </c:choose>
					   </c:forEach>
					   </td>
                  </tr>
              </c:forEach>               
          </table>  
      </c:when>                    
      <c:otherwise>
      <br>  </br>           
          <div class="alert alert-info">
             <fmt:message key="products.text.nocheques"/>
          </div>
      </c:otherwise>
  </c:choose>                        
     
     <%@ include file="fragments/pagination.jspf"%>

<div class="container">
<form action ="chequeedit" method="get">            
    <br></br>
    <input type = "hidden" name="action" value="add">
    <button type="submit" class="btn btn-info  btn-md"><fmt:message key="cheques.button.label.addnewcheque"/></button> 
</form>
</div>
<%@ include file="fragments/footer.jspf"%>