<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../header.jsp" />



<h2>Person</h2>

<ul id="errors">
<c:forEach var="error" items="${person.validationErrors}">
	<li>${error}</li>
</c:forEach>
</ul>
<div class="container">
	<form method="post" action="${action}" enctype="multipart/form-data" >
	<input type="hidden" name="id" value="${person.id}">
	<div class="row">
		<div class="col-25">
			<label for="firstName">First Name</label>
		</div>
		<div class="col-75">
			<input type="text" id="firstName" name="firstName" value="${person.firstName}" placeholder="First name.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="lastName">Last Name</label>
		</div>
		<div class="col-75">
			<input type="text" name="lastName" id="lastName" value="${person.lastName}" placeholder="Last name.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="email">Email</label>
		</div>
		<div class="col-75">
			<input type="email" name="email" id="email" value="${person.email}" placeholder="Email address.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="streetAddress">Street Address</label>
		</div>
		<div class="col-75">
			<input type="text" name="streetAddress" id="streetAddress" value="${person.streetAddress}"  placeholder="Street address.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="apartment">Apartment</label>
		</div>
		<div class="col-75">
			<input type="text" name="apartment" id="apartment" value="${person.apartment}" placeholder="Apartment no.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="cityAddress">City</label>
		</div>
		<div class="col-75">
			<input type="text" name="cityAddress" id="cityAddress" value="${person.cityAddress}"  placeholder="City.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="stateAddress">State</label>
		</div>
		<div class="col-75">
			<input type="text" name="stateAddress" id="stateAddress" value="${person.stateAddress}" placeholder="State.."/>
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<label for="zipAddress">Zip Code</label>
		</div>
		<div class="col-75">
			<input type="text" name="zipAddress" id="zipAddress" value="${person.zipAddress}" placeholder="Zip code.." pattern="^[0-9]{5}(?:-[0-9]{4})?$"/>
		</div>
	</div>
		<%-- todo
		<div>
			<Label>Image</Label>
			<input type="text" name="imageUrl" value="${person.imageUrl}"/>
		</div> --%>
	<div class="row">
		<div class="col-25">
			<label for="image">Image</label>
		</div>
		<div class="col-75">
			<input type="file" name="image" id="image" />
		</div>
	</div>
	<div class="row">
		<div class="col-25">
			<Label for="dateOfBirth">Date of Birth</Label>
		</div>
		<div class="col-75">
			<input type="date" name="dateOfBirth" id="dateOfBirth" value="${person.dateOfBirth}"/>
		</div>
	</div>
	<input type="submit" value="Save"/>
	<input type="button" value="Cancel" id="cancel" onclick="javascript:history.go(-1)"/>
	</form>
</div>

<jsp:include page="../footer.jsp" />