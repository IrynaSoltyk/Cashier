<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="page" scope="page" value="cheques"/>
<%@ include file="fragments/header.jspf" %>
<div class="container"> 
  <h2>Cheques</h2>

</div> 
<%@ include file="fragments/messages.jspf"%>
<!-- table -->

  <c:choose>
      <c:when test="${not empty cheques}">
      <div class=row">
          			<div class="col10"> <span class="badge">${count}</span> cheques in total</div>

          			<div class="col2" >
						 Show: <a href="?limit=5">5</a>|<a href="?limit=10">10</a>|<a href="?limit=15">15</a>
 			 
	</div>
          <table  class="table table-striped">
              <thead>
                  <tr>
                      <td>Cheque N</td>
                      <td>Date</td>
                      <td>Cost</td>
                      <td>Status</td>
                      <td>Cashier Name</td> 
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
                      		Open
                     	 </c:when>
                      	<c:when test="${empty cheque.cancelledDate}">
                    		 Closed
                     	 </c:when>
                     	 <c:otherwise>
                      		 Cancelled by ${cheque.cancelledBy.name} <br>
                     		 on ${cheque.cancelledDate}
                     	 </c:otherwise>
                      </c:choose>
                        </td>
                      <td>${cheque.createdBy.name}</td>
                      <td><button type="submit" formaction="chequeedit" class="btn btn-primary btn-sm">${not empty cheque.date? 'Show':'Edit'}</button> </td>
					   </form>					   
					   <td>
					   <c:forEach var="role" items="${user.roles}">
  				  	 	<c:choose>
  				  	 	<c:when test="${role=='MANAGER' && empty cheque.cancelledDate && not empty cheque.date}">
					   <button type="submit" formaction="chequecancel" class="btn btn-primary btn-sm">Cancel cheque</button>
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
              No cheques yet
          </div>
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
                        <td><a href="chequegetall?page=${i}&limit=${limit}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </c:if>
        </tr>
    </table> 
    </div>

<div class="container">
<form action ="chequeedit" method="get">            
    <br></br>
    <input type = "hidden" name="action" value="add">
    <button type="submit" class="btn btn-info  btn-md">Add new cheque</button> 
</form>
</div>
<%@ include file="fragments/footer.jspf"%>