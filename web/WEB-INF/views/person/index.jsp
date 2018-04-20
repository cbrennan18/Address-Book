<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page
        import="java.net.URL"
        import="java.util.*"
%>
<%
    URL baseUrl;
    if (request.getServerPort()==80 || request.getServerPort()==443) {
        baseUrl = new URL(request.getScheme(), request.getServerName(), request.getContextPath());
    }
    else {
        baseUrl = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
    }
    pageContext.setAttribute("baseUrl", baseUrl.toString() + "/");

    Object flash = session.getAttribute("flash");
    if (flash != null
            ) {
        session.setAttribute("flash", null);
        request.setAttribute("flash", flash);
    }

%>
<html>
<head>
    <title>CRUD Example</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#317EFB"/>
    <base href="${baseUrl}" />
    <link rel="stylesheet" type="text/css" href="./css/main.css">
    <link rel="shortcut icon" href="./img/favicon.ico" type="image/x-icon" />
    <script src="javascript/jquery-3.3.1.min.js" type="text/javascript"></script>
    <script src="javascript/mustache.js"></script>
    <script src="javascript/application.js" type="text/javascript"></script>
    <script async defer
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDmKfu4-JA9LR7JDlqSO8mG5AHWBScHdU4">
    </script>
    <script src="/javascript/person.js"></script>
    <script src="/javascript/jquery-serializefiles.js"></script>

    <script type="text/javascript">
        var context_root = "${pageContext.request.contextPath}";
        var base_url = "${baseUrl}";
    </script>
</head>
<body>
<div>${flash}</div>
</body>
</html>

