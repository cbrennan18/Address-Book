<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../header.jsp" />

<c:choose>
<c:when test="${not empty people}">
	<div class="container">
	<h2>People</h2>

<table>
<tr>
	<td>First Name</td>
	<td>Last Name</td>
	<td>Email</td>
	<td>Street Address</td>
	<td>Apartment</td>
	<td>City</td>
	<td>State</td>
	<td>Zip Code</td>
	<td>Date of Birth</td>
	<td>Age</td>
	<td>Image</td>
	<td></td>
</tr>
<c:forEach var="person" items="${people}">
<tr>
	<td>${person.firstName}</td>
	<td>${person.lastName}</td>
	<td>${person.email}</td>
	<td>${person.streetAddress}</td>
	<td>${person.apartment}</td>
	<td>${person.cityAddress}</td>
	<td>${person.stateAddress}</td>
	<td>${person.zipAddress}</td>
	<td><input type="date" value="${person.dateOfBirth}" disabled></td>
	<td>${person.age}</td>
	<td>
		<c:choose>
			<c:when test="${not empty person.imageBytes}"><img src="${person.imageUrl}" height="60" width="60">
			</c:when>
			<c:when test="${empty person.imageBytes}"><img src="../img/profile.png" height="60" width="60">
			</c:when>
		</c:choose>
	</td>
	<td><a href="person/view?id=${person.id}">View</a> | <a href="person/edit?id=${person.id}">Edit</a>
</tr>
</c:forEach>
</table>
		<div><a href="person/new"><input type="button" id="cancel" value="Create Person"></a></div>
	</div>
</c:when>
<c:otherwise>
	<div class="bgimg">
		<div class="middle">
			<h2>People</h2>
			<hr>
			<div><a href="person/new">Create</a></div>
			<p>There are no people yet.</p>
		</div>
	</div></c:otherwise>
</c:choose>

<jsp:include page="../footer.jsp" />