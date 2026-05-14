<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html>
<head>

	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Software Expired</title>

	<!-- CSS -->
	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css' />">
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.2/css/all.min.css' />">
	
	<!-- JS -->
	<script src="<c:url value='/webjars/jquery/3.6.0/jquery.min.js' />"></script>
	<script src="<c:url value='/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js' />"></script>
	<script src="<c:url value='/resources/javascript/index.js' />"></script>

</head>
<body>
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">${error_message}</h3>
           </div>
       </div>
    </div>
  </div>
</div>
</div>
</body>
</html>