<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../header.jsp" />
<style>
	/* Always set the map height explicitly to define the size of the div
     * element that contains the map. */
	#map {
		height: 400px;
		width: 700px;
	}
	/* Optional: Makes the sample page fill the window. */
	html, body {
		height: 100%;
		margin: 0;
		padding: 0;
	}
	#floating-panel {
		position: absolute;
		top: 10px;
		left: 25%;
		z-index: 5;
		background-color: #fff;
		padding: 5px;
		border: 1px solid #999;
		text-align: center;
		font-family: 'Roboto','sans-serif';
		line-height: 30px;
		padding-left: 10px;
	}
</style>

<h2>Person</h2>
<div class="container">
<table>
<tr>
	<td>First Name</td>
	<td>${person.firstName}</td>
</tr>
<tr>
	<td>Last Name</td>
	<td>${person.lastName}</td>
</tr>
<tr>
	<td>Email</td>
	<td>${person.email}</td>
</tr>
	<tr>
		<td>Street Address</td>
		<td>${person.streetAddress}</td>
	</tr>
	<tr>
		<td>Apartment</td>
		<td>${person.apartment}</td>
	</tr>
	<tr>
		<td>City</td>
		<td>${person.cityAddress}</td>
	</tr>
	<tr>
		<td>State</td>
		<td>${person.stateAddress}</td>
	</tr>
	<tr>
		<td>Zip Code</td>
		<td>${person.zipAddress}</td>
	</tr>
	<tr>
		<td>Date of Birth</td>
		<td><input type="date" value="${person.dateOfBirth}" disabled></td>
	</tr>
	<tr>
		<td>Age</td>
		<td>${person.age}</td>
	</tr>
	<tr>
		<td>Image</td>
		<td>
			<c:choose>
				<c:when test="${not empty person.imageBytes}"><img src="${person.imageUrl}" height="60" width="60">
				</c:when>
				<c:when test="${empty person.imageBytes}"><img src="../img/profile.png" height="60" width="60">
				</c:when>
			</c:choose>
		</td>
	</tr>
</table>
</div>
<input type="submit" name="Edit" onclick="javascript:redirect('person/edit?id=${person.id}')" value="Edit">

<form method="post" action="person/delete">
	<input type="hidden" name="id" value="${person.id}">
	<input type="submit" value="Delete" onclick="if(confirm('Are you sure you want to delete this?')) return true; else return false;">
</form>
	<div>
		<input type="hidden" id="address" type="textbox" value="<c:out value='${person.streetAddress} + " " + ${person.cityAddress}' />">
		<input id="submit" class="btn btn-primary btn-large" type="button" style="display: none;" value="Find Location">
	</div>

	<script type="text/javascript">
        setTimeout(function(){ document.getElementById('submit').click(); }, 1000); // 5 seconds
	</script>
	<br>
	<div id="map"></div>
	<script>
        function initMap() {
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 8,
                center: {lat: 44.263, lng: -36.246}
            });
            var geocoder = new google.maps.Geocoder();

            document.getElementById('submit').addEventListener('click', function() {
                geocodeAddress(geocoder, map);
            });
        }

        function geocodeAddress(geocoder, resultsMap) {
            var address = document.getElementById('address').value;
            geocoder.geocode({'address': address}, function(results, status) {
                if (status === 'OK') {
                    resultsMap.setCenter(results[0].geometry.location);
                    var marker = new google.maps.Marker({
                        map: resultsMap,
                        position: results[0].geometry.location
                    });
                } else {
                    alert('Geocode was not successful for the following reason: ' + status);
                }
            });
        }
	</script>
	<script async defer
			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDmKfu4-JA9LR7JDlqSO8mG5AHWBScHdU4&callback=initMap">
	</script>

<a href="person/">Back to List</a>

<jsp:include page="../footer.jsp" />