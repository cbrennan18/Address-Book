var id;

$(document).ready(function() {

    list();

    function getAge(dateString) {
        var today = new Date();
        var birthDate = new Date(dateString);
        var age = today.getFullYear() - birthDate.getFullYear();
        var m = today.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    }

    function openModal() {
        $('.modal').show();
    }

    function closeModal() {
        $('.modal').remove();
    }

    $('body').on('click', 'span.close, input.close', function(){
        closeModal();
    });

    $('body').on('click', '#save', function(e){
        e.preventDefault();
        var action = $(this).data("action");
        if (action == "edit") {
            updatePerson($('form#edit').serializefiles());
        }
        else if (action == "create") {
            createPerson($('form#create').serializefiles());
        }
        closeModal();
    });

    $('body').on('click', 'input.delete', function(e){
        e.preventDefault();
        var id = $(this).data('personid');
        deletePerson(id);
    });

    $('body').on('click', 'input.create', function() {
        closeModal();
        newPerson();
    });

    $('body').on('click', 'input.view', function(){
        var id = $(this).data('personid');
        closeModal();
        viewPerson(id);
    });

    $('body').on('click', 'input.edit', function(){
        var id = $(this).data('personid');
        closeModal();
        editPerson(id);
    });

    function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 8,
            center: {lat: 44.263, lng: -36.246}
        });
        var geocoder = new google.maps.Geocoder();

        document.getElementById('mapSubmit').addEventListener('click', function() {
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
    function list() {
        $('#list').remove();
        //todo show icon
        $.ajax('/person/list', {
            success: function (response) {
                $.get('templates/list.mst', function(template) {
                    var rendered = Mustache.render(template, {'people' : response});
                    $('body').append(rendered);
                });
                //todo hide icon
            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' with ' + errorMessage);
                //todo hide icon
            }
        });
    }

    function newPerson() {
        showForm('Create Person', 'create', {});
    }

    function createPerson(params) {

        $.ajax({
            url: "/person/create",
            method: "POST",
            cache:false,
            processData:false,
            contentType:false,
            data: params,
            success: function (response) {
                closeModal();
                list();
            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' here ' + errorMessage);
            }

        })
    }

    function updatePerson(params) {
        $.ajax({
            url: "/person/update",
            method: "POST",
            cache:false,
            processData:false,
            contentType:false,
            data: params,
            success: function (response) {
                closeModal();
                list();
            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' here ' + errorMessage);
            }
        })
    }

    function deletePerson(id) {
        $.ajax({
            url: "/person/delete?id=" + id,
            method: "POST",
            cache:false,
            processData:false,
            contentType:false,
            data: id,
            success: function (response) {
                closeModal();
                list();
            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' here ' + errorMessage);
            }
        })
    }

    function viewPerson(id) {
        $.ajax({
            url: "/person/view?id=" + id,
            success: function (response) {
                $.get('templates/view.mst', function(template) {
                    var rendered = Mustache.render(template, response);
                    $('body').append(rendered);
                    openModal();
                    setTimeout(function(){ $('body').find('input[name=mapSubmit]').click(); }, 1000); // 5 seconds
                    initMap();
                });
            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' here ' + errorMessage);
            }
        });
    }

    function editPerson(id) {
        $.ajax({
            url: "/person/edit?id=" + id,
            success: function (response) {

                showForm('Edit Person', 'edit', response);

            },
            error: function (request, errorType, errorMessage) {
                alert('Error: ' + errorType + ' here ' + errorMessage);
            }
        });
    }

    function showForm(title, action, person) {
        $.get('templates/form.mst', function(template) {
            var rendered = Mustache.render(template, {'title' : title, 'action' : action, 'person' : person});
            $('body').append(rendered);
            openModal();
        });
    }

});